package com.object173.newsfeed.features.feed.list.presentation;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.object173.newsfeed.R;
import com.object173.newsfeed.databinding.ItemFeedListBinding;
import com.object173.newsfeed.features.base.model.local.Feed;
import com.object173.newsfeed.features.base.presentation.OnItemClickListener;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class FeedPagedAdapter extends PagedListAdapter<Feed, FeedPagedAdapter.FeedViewHolder> {

    private final OnItemClickListener<Feed> mFeedClickListener;

    FeedPagedAdapter(final OnItemClickListener<Feed> feedClickListener) {
        super(new FeedDiffUtilCallback());
        mFeedClickListener = feedClickListener;
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
        Feed feed = getItem(position);
        feedViewHolder.bindFeed(feed);

        feedViewHolder.itemView.setOnClickListener(view -> mFeedClickListener.onItemClick(feed));
        feedViewHolder.itemView.setOnLongClickListener(view -> {
            mFeedClickListener.onItemLongClick(feed);
            return true;
        });
    }

    static class FeedViewHolder extends RecyclerView.ViewHolder {

        private ItemFeedListBinding mBinding;

        FeedViewHolder(final ItemFeedListBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bindFeed(final Feed feed) {
            mBinding.setFeed(feed);
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
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getUpdated().getTime() == newItem.getUpdated().getTime() &&
                    oldItem.getNotReviewedCount() == newItem.getNotReviewedCount();
        }
    }
}
