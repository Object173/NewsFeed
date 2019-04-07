package com.object173.newsfeed.features.feed.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.object173.newsfeed.R;
import com.object173.newsfeed.databinding.FragmentFeedBinding;
import com.object173.newsfeed.features.feed.domain.model.RequestResult;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class FeedFragment extends Fragment {

    private FeedViewModel mViewModel;
    private FragmentFeedBinding mBinding;

    private static final String ATTR_FEED_LINK = "feed_link";

    private boolean mIsNewFeed;

    public static FeedFragment newInstance(final String feedLink) {
        final FeedFragment result = new FeedFragment();
        Bundle args = new Bundle();
        args.putString(ATTR_FEED_LINK, feedLink);
        result.setArguments(args);
        return result;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        final String feedLink = getArguments().getString(ATTR_FEED_LINK);
        mIsNewFeed = feedLink == null;
        mViewModel = ViewModelProviders.of(this, new FeedViewModelFactory(
                getActivity().getApplication(), feedLink)).get(FeedViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_feed, container, false);

        mViewModel.getFeed().observe(this, feed -> {
            mBinding.setFeed(new FeedBinding(feed, mIsNewFeed));
            getActivity().invalidateOptionsMenu();
        });

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
            mViewModel.cancelLoadFeed(this);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_feed, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_feed:
                if(mIsNewFeed) {
                    mViewModel.loadFeed().observe(this, this::setLoadingStatus);
                }
                else {
                    mViewModel.updateFeed().observe(this, this::setLoadingStatus);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
