package com.object173.newsfeed.features.base.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.object173.newsfeed.R;
import com.object173.newsfeed.databinding.FragmentListBinding;
import com.object173.newsfeed.features.base.model.network.RequestResult;

import static com.object173.newsfeed.features.base.model.network.RequestResult.*;

public abstract class BaseListFragment<V, VH extends RecyclerView.ViewHolder> extends Fragment
    implements OnItemClickListener<V> {

    private static final String ARG_PARAM = "param";
    private static final String ARG_CURRENT_POSITION = "current_position";

    protected BaseListFragmentViewModel<V> mViewModel;
    private FragmentListBinding mBinding;
    private PagedListAdapter<V, VH> mPagedAdapter;

    private MutableLiveData<Boolean> mIsListEmpty = new MutableLiveData<>();
    private final MutableLiveData<ItemClickEvent<V>> mClickedItem = new MutableLiveData<>();

    private int mCurrentPosition = 0;

    public static void putParam(Bundle bundle, String param) {
        bundle.putString(ARG_PARAM, param);
    }

    protected abstract BaseListFragmentViewModel<V> getViewModel();
    protected abstract PagedListAdapter<V, VH> getPagedAdapter();

    public LiveData<ItemClickEvent<V>> getClickedItem() {
        return mClickedItem;
    }

    @Override
    public void onItemClick(V item) {
        mClickedItem.setValue(new ItemClickEvent<>(ItemClickEvent.Type.CLICK, item));
    }

    @Override
    public void onItemLongClick(V item) {
        mClickedItem.setValue(new ItemClickEvent<>(ItemClickEvent.Type.LONG_CLICK, item));
    }

    @StringRes
    protected abstract int getRemoveMessage();

    @StringRes
    private int getEmptyListMessage() {
        return R.string.list_is_empty;
    }

    public void setParam(String param) {
        mIsListEmpty.setValue(null);

        mViewModel.getListData(param, this).observe(this, feedList -> {
                mPagedAdapter.submitList(feedList);
                mIsListEmpty.setValue(feedList.isEmpty());
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = getViewModel();
        mPagedAdapter = getPagedAdapter();

        setParam((getArguments() != null && getArguments().containsKey(ARG_PARAM)) ?
                getArguments().getString(ARG_PARAM) : null);

        if(savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ARG_CURRENT_POSITION);
        }
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);

        mIsListEmpty.observe(this, this::setFrame);

        initRecyclerView();
        initRefreshLayout();
        initTouchHelper();

        mBinding.emptyTextView.setText(getEmptyListMessage());

        return mBinding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if(mBinding != null) {
            mCurrentPosition =  mBinding.recyclerView.getVerticalScrollbarPosition();
        }
        outState.putInt(ARG_CURRENT_POSITION, mCurrentPosition);
    }

    @Override
    public void onPause() {
        super.onPause();

        mViewModel.checkReviewedItemsCount(mPagedAdapter.getItemCount());
    }

    private void initRecyclerView() {
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.recyclerView.setAdapter(mPagedAdapter);

        mBinding.recyclerView.scrollToPosition(mCurrentPosition);
    }

    private void initRefreshLayout() {
        if(mViewModel.isRefreshEnabled()) {
            mBinding.swipeRefreshLayout.setEnabled(true);

            mBinding.swipeRefreshLayout.setOnRefreshListener(() ->
                    mViewModel.updateData().observe(this, this::onUpdateStatus));
            if(mViewModel.getRefreshStatus() != null) {
                mViewModel.updateData().observe(this, this::onUpdateStatus);
            }
        }
        else
        {
            mBinding.swipeRefreshLayout.setEnabled(false);
        }
    }

    private void initTouchHelper() {
        final ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(this::removeData);
        final ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mBinding.recyclerView);
    }

    private void removeData(final int position) {
        mViewModel.removeData(position).observe(this, result -> {
            if(result != null && result) {
                Snackbar.make(mBinding.getRoot(), getRemoveMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void showErrorMessage(RequestResult result) {
        switch (result) {
            case HTTP_FAIL:
                showMessage(R.string.http_fail_message);
                break;
            case INCORRECT_LINK:
                showMessage(R.string.incorrect_link_message);
                break;
            case INCORRECT_RESPONSE:
                showMessage(R.string.incorrect_response_message);
                break;
            case NO_INTERNET:
                showMessage(R.string.no_internet_message);
                break;
            default:
                showMessage(R.string.unknown_error_message);
                break;
        }
    }

    private void showMessage(int messageId) {
        Snackbar.make(mBinding.getRoot(), messageId, Snackbar.LENGTH_SHORT).show();
    }

    protected void showMessage(String message) {
        Snackbar.make(mBinding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    private void onUpdateStatus(RequestResult requestResult) {
        boolean isRefreshed = requestResult == RUNNING;
        mBinding.swipeRefreshLayout.setRefreshing(isRefreshed);

        if(isRefreshed) {
            return;
        }

        if(requestResult != SUCCESS) {
            showErrorMessage(requestResult);
        }
        mViewModel.getRefreshStatus().removeObservers(this);
        mViewModel.cancelRefresh();
    }

    private void setFrame(Boolean isEmpty) {
        if(isEmpty == null) {
            mBinding.progressBar.setVisibility(View.VISIBLE);
            mBinding.recyclerView.setVisibility(View.GONE);
            mBinding.emptyTextView.setVisibility(View.GONE);
        }
        else {
            mBinding.progressBar.setVisibility(View.GONE);
            mBinding.recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
            mBinding.emptyTextView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        }
    }
}
