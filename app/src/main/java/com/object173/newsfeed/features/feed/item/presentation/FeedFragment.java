package com.object173.newsfeed.features.feed.item.presentation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.object173.newsfeed.R;
import com.object173.newsfeed.databinding.FragmentFeedBinding;
import com.object173.newsfeed.features.base.model.network.RequestResult;
import com.object173.newsfeed.features.category.presentation.CategoryListActivity;

public class FeedFragment extends Fragment {

    private FeedViewModel mViewModel;
    private FragmentFeedBinding mBinding;

    private String mFeedLink;
    private FeedBinding mFeedBinding;

    private static final String ATTR_FEED_LINK = "feed_link";
    private static final int SELECT_CATEGORY_REQUEST_CODE = 173;

    private static final String KEY_FEED = "feed";

    static FeedFragment newInstance(final String feedLink) {
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

        mViewModel = ViewModelProviders.of(this, new FeedViewModelFactory(
                getActivity().getApplication())).get(FeedViewModel.class);

        if(getArguments() != null) {
            mFeedLink = getArguments().getString(ATTR_FEED_LINK);
        }
        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_FEED)) {
            mFeedBinding = (FeedBinding) savedInstanceState.getSerializable(KEY_FEED);
        }
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_feed, container, false);

        if(mFeedBinding != null) {
            mBinding.setFeed(mFeedBinding);
        }
        else {
            mViewModel.getFeed(mFeedLink).observe(this, feed -> {
                mFeedBinding = new FeedBinding(feed, mFeedLink == null);
                mBinding.setFeed(mFeedBinding);

                if(getActivity() != null) {
                    getActivity().invalidateOptionsMenu();
                }
            });
        }

        if(mViewModel.getIsRefreshed() != null) {
            mViewModel.getIsRefreshed().observe(this, this::setLoadingStatus);
        }

        mBinding.categoryButton.setOnClickListener(view -> startActivityForResult(CategoryListActivity.getIntent(getActivity()), SELECT_CATEGORY_REQUEST_CODE));

        return mBinding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if(mBinding != null) {
            outState.putSerializable(KEY_FEED, mBinding.getFeed());
        }
    }

    private void setLoadingStatus(final RequestResult loadingStatus) {
        boolean isLoading = loadingStatus == RequestResult.RUNNING;

        mBinding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        mBinding.content.setVisibility(isLoading ? View.GONE : View.VISIBLE);

        if(loadingStatus == RequestResult.SUCCESS && getActivity() != null) {
            getActivity().finish();
        }
        if(!isLoading) {
            showErrorMessage(loadingStatus);
            mViewModel.cancelLoadFeed(this);
        }
    }

    private void showErrorMessage(RequestResult result) {
        if(result == RequestResult.SUCCESS) {
            return;
        }
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
            case EXISTS:
                showMessage(R.string.feed_exists_message);
                break;
            case NOT_EXIST:
                showMessage(R.string.feed_not_exists_message);
                break;
            default:
                showMessage(R.string.unknown_error_message);
                break;
        }
    }

    private void showMessage(int messageId) {
        Snackbar.make(mBinding.getRoot(), messageId, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_feed, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_feed) {
            if (mFeedLink == null) {
                mViewModel.loadFeed(mFeedBinding.getFeed()).observe(this, this::setLoadingStatus);
            } else {
                mViewModel.updateFeed(mFeedBinding.getFeed()).observe(this, this::setLoadingStatus);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == SELECT_CATEGORY_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK && data != null) {
                mBinding.getFeed().setCategory(CategoryListActivity.getCategory(data));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
