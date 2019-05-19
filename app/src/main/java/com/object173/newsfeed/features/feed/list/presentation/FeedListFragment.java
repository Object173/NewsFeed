package com.object173.newsfeed.features.feed.list.presentation;

import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedListAdapter;

import com.object173.newsfeed.R;
import com.object173.newsfeed.features.base.model.local.Feed;
import com.object173.newsfeed.features.base.presentation.BaseListFragment;
import com.object173.newsfeed.features.base.presentation.BaseListFragmentViewModel;

public class FeedListFragment extends BaseListFragment<Feed, FeedPagedAdapter.FeedViewHolder> {

    @Override
    protected BaseListFragmentViewModel<Feed> getViewModel() {
        return ViewModelProviders.of(this, new FeedListViewModelFactory(getActivity().getApplication()))
                .get(FeedListFragmentViewModel.class);
    }

    @Override
    protected PagedListAdapter<Feed, FeedPagedAdapter.FeedViewHolder> getPagedAdapter() {
        return new FeedPagedAdapter(this);
    }

    @Override
    protected int getRemoveMessage() {
        return R.string.feed_removed_message;
    }
}
