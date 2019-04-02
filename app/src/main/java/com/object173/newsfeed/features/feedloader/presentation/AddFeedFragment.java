package com.object173.newsfeed.features.feedloader.presentation;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.object173.newsfeed.R;
import com.object173.newsfeed.databinding.FragmentAddFeedBinding;
import com.object173.newsfeed.features.feedloader.domain.model.RequestResult;

public class AddFeedFragment extends Fragment {

    private AddFeedViewModel mViewModel;
    private FragmentAddFeedBinding mBinding;

    private boolean mIsLoading = false;

    public static AddFeedFragment newInstance() {
        return new AddFeedFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mViewModel = ViewModelProviders.of(this, new AddListViewModelFactory())
                .get(AddFeedViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_feed, container, false);

        mBinding.addFeedButton.setOnClickListener(v ->
                mViewModel.loadFeed(mBinding.feedLinkEditText.getText().toString())
                .observe(this, this::setLoadingStatus));

        if(mViewModel.getIsRefreshed() != null) {
            mViewModel.getIsRefreshed().observe(this, this::setLoadingStatus);
        }

        return mBinding.getRoot();
    }

    private void setLoadingStatus(final RequestResult loadingStatus) {
        boolean isLoading = loadingStatus == RequestResult.RUNNING;

        mBinding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        mBinding.content.setVisibility(isLoading ? View.GONE : View.VISIBLE);

        if(loadingStatus == RequestResult.SUCCESS) {
            getActivity().finish();
        }
        if(!isLoading) {
            Snackbar.make(mBinding.getRoot(), loadingStatus.toString(), Snackbar.LENGTH_SHORT).show();
            mViewModel.getIsRefreshed().removeObservers(this);
            mViewModel.cancelLoadFeed();
        }
    }
}
