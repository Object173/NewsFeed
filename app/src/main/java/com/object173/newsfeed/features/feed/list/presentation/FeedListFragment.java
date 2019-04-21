package com.object173.newsfeed.features.feed.list.presentation;

import android.content.Intent;

import com.object173.newsfeed.R;
import com.object173.newsfeed.features.base.model.local.Feed;
import com.object173.newsfeed.features.base.presentation.BaseListFragment;
import com.object173.newsfeed.features.base.presentation.BaseListFragmentViewModel;
import com.object173.newsfeed.features.feed.item.presentation.FeedActivity;
import com.object173.newsfeed.features.news.list.feed.presentation.NewsFeedActivity;

import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedListAdapter;

public class FeedListFragment extends BaseListFragment<Feed, FeedPagedAdapter.FeedViewHolder> {

    public static FeedListFragment newInstance(String category) {
        FeedListFragment fragment = new FeedListFragment();
        fragment.setArguments(BaseListFragment.getBundle(category));
        return fragment;
    }

    @Override
    protected BaseListFragmentViewModel<Feed> getViewModel() {
        return ViewModelProviders.of(this, new FeedListViewModelFactory(getActivity().getApplication()))
                .get(FeedListFragmentViewModel.class);
    }

    @Override
    protected PagedListAdapter<Feed, FeedPagedAdapter.FeedViewHolder> getPagedAdapter() {
        return new FeedPagedAdapter(new FeedPagedAdapter.OnFeedClickListener() {
            @Override
            public void onClick(Feed feed) {
                final Intent intent = NewsFeedActivity.getIntent(getActivity(), feed.getLink());
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
    protected int getRemoveMessage() {
        return R.string.feed_removed_message;
    }
}
