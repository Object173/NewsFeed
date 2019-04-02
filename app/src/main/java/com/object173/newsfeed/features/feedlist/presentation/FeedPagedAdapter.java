package com.object173.newsfeed.features.feedlist.presentation;

import android.arch.paging.PagedListAdapter;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.object173.newsfeed.R;
import com.object173.newsfeed.databinding.ItemFeedListBinding;
import com.object173.newsfeed.features.feedlist.domain.model.Feed;

public class FeedPagedAdapter extends PagedListAdapter<Feed, FeedPagedAdapter.FeedViewHolder> {

    FeedPagedAdapter() {
        super(new FeedDiffUtilCallback());
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        final ItemFeedListBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.item_feed_list, viewGroup, false);
        return new FeedViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final FeedViewHolder feedViewHolder, final int position) {
        feedViewHolder.bindFeed(getItem(position));
    }

    static class FeedViewHolder extends RecyclerView.ViewHolder {

        private ItemFeedListBinding mBinding;

        FeedViewHolder(final ItemFeedListBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.setFeed(new FeedItemViewModel());
        }

        void bindFeed(final Feed feed) {
            mBinding.getFeed().setFeed(feed);
            mBinding.executePendingBindings();
        }
    }

    private static class FeedDiffUtilCallback extends DiffUtil.ItemCallback<Feed> {

        @Override
        public boolean areItemsTheSame(@NonNull final Feed oldItem, @NonNull final Feed newItem) {
            return oldItem.getLink().equals(newItem.getLink());
        }

        @Override
        public boolean areContentsTheSame(@NonNull final Feed oldItem, @NonNull final Feed newItem) {
            return oldItem.getTitle().equals(newItem.getTitle());
        }
    }
}
