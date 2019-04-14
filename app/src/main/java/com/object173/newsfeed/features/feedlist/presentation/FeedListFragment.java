package com.object173.newsfeed.features.feedlist.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.object173.newsfeed.R;
import com.object173.newsfeed.databinding.FragmentListBinding;
import com.object173.newsfeed.features.ItemTouchHelperCallback;
import com.object173.newsfeed.features.feed.presentation.FeedActivity;
import com.object173.newsfeed.features.feedlist.domain.model.Feed;
import com.object173.newsfeed.features.newslist.presentation.NewsListActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

public class FeedListFragment extends Fragment {

    private static final String KEY_CATEGORY = "category";

    private FeedListFragmentViewModel mViewModel;
    private FragmentListBinding mBinding;
    private FeedPagedAdapter mPagedAdapter;

    public static FeedListFragment newInstance(String category) {
        FeedListFragment fragment = new FeedListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_CATEGORY, category);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setCategory(String category) {
        if(mPagedAdapter.getCurrentList() != null) {
            mPagedAdapter.submitList(null);
        }
        if(category != null) {
            mViewModel.getFeedData(category).observe(this, feedList -> mPagedAdapter.submitList(feedList));
        }
        else {
            mViewModel.getFeedData().observe(this, feedList -> mPagedAdapter.submitList(feedList));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = ViewModelProviders.of(this, new FeedListViewModelFactory
                (getActivity().getApplication())).get(FeedListFragmentViewModel.class);

        mPagedAdapter = new FeedPagedAdapter(new FeedPagedAdapter.OnFeedClickListener() {
            @Override
            public void onClick(Feed feed) {
                final Intent intent = NewsListActivity.getIntent(getActivity(), feed.getLink());
                startActivity(intent);
            }

            @Override
            public boolean onLongClick(Feed feed) {
                final Intent intent = FeedActivity.getUpdateIntent(getActivity(), feed.getLink());
                startActivity(intent);
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mPagedAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);

        mBinding.swipeRefreshLayout.setEnabled(false);

        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.recyclerView.setAdapter(mPagedAdapter);

        String category = getArguments().getString(KEY_CATEGORY);
        setCategory(category);

        final ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(this::removeFeed);
        final ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mBinding.recyclerView);

        return mBinding.getRoot();
    }

    private void removeFeed(final int position) {
        mViewModel.removeFeed(position).observe(this, result -> {
            if(result != null && result) {
                Snackbar.make(mBinding.getRoot(), R.string.feed_removed_message, Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
