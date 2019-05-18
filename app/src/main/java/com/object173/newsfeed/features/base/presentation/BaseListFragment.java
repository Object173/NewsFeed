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
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.object173.newsfeed.R;
import com.object173.newsfeed.databinding.FragmentListBinding;
import com.object173.newsfeed.features.base.model.network.RequestResult;
import com.object173.newsfeed.libs.log.LoggerFactory;

public abstract class BaseListFragment<V, VH extends RecyclerView.ViewHolder> extends Fragment {

    private static final String ARG_PARAM = "param";
    private static final String ARG_CURRENT_POSITION = "current_position";

    protected BaseListFragmentViewModel<V> mViewModel;
    private FragmentListBinding mBinding;
    private PagedListAdapter<V, VH> mPagedAdapter;

    private String mCurrentParam;
    private MutableLiveData<Boolean> mIsListEmpty = new MutableLiveData<>();

    private int mCurrentPosition = 0;

    public static Bundle getBundle(String param) {
        Bundle bundle = new Bundle();
        if(param != null) {
            bundle.putString(ARG_PARAM, param);
        }
        return bundle;
    }

    public static void putParam(Bundle bundle, String param) {
        bundle.putString(ARG_PARAM, param);
    }

    protected abstract BaseListFragmentViewModel<V> getViewModel();
    protected abstract PagedListAdapter<V, VH> getPagedAdapter();

    @StringRes
    protected int getRemoveMessage() {
        return 0;
    }

    @StringRes
    protected int getEmptyListMessage() {
        return R.string.list_is_empty;
    }

    public void setParam(String param) {
        mCurrentParam = param;
        mIsListEmpty.setValue(null);

        mViewModel.getListData(param, this).observe(this, feedList -> {
            mPagedAdapter.submitList(feedList);
            LoggerFactory.get(getClass()).info("notifyDataSetChanged");
            mIsListEmpty.setValue(feedList.isEmpty());
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = getViewModel();
        mPagedAdapter = getPagedAdapter();

        initParam(savedInstanceState);
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
        outState.putString(ARG_PARAM, mCurrentParam);

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

    private void initParam(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            setParam(savedInstanceState.getString(ARG_PARAM));
            mCurrentPosition = savedInstanceState.getInt(ARG_CURRENT_POSITION);
        }
        else {
            if(getArguments() != null) {
                setParam(getArguments().getString(ARG_PARAM));
            }
            else {
                setParam(null);
            }
        }
    }

    private void removeData(final int position) {
        mViewModel.removeData(position).observe(this, result -> {
            if(result != null && result) {
                Snackbar.make(mBinding.getRoot(), getRemoveMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void onUpdateStatus(RequestResult requestResult) {
        boolean isRefreshed = requestResult == RequestResult.RUNNING;
        mBinding.swipeRefreshLayout.setRefreshing(isRefreshed);

        if(!isRefreshed) {
            Snackbar.make(mBinding.getRoot(), requestResult.toString(), Snackbar.LENGTH_SHORT).show();
            mViewModel.getRefreshStatus().removeObservers(this);
            mViewModel.cancelRefresh();
        }
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
