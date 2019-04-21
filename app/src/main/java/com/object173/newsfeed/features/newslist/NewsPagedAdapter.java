package com.object173.newsfeed.features.newslist;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.object173.newsfeed.R;
import com.object173.newsfeed.databinding.ItemNewsListBinding;
import com.object173.newsfeed.features.base.domain.model.local.News;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class NewsPagedAdapter extends PagedListAdapter<News, NewsPagedAdapter.NewsViewHolder> {

    public interface OnNewsItemListener {
        void onClick(News news);
        void onReviewed(News news);
    }

    private final OnNewsItemListener mNewsItemListener;

    public NewsPagedAdapter(OnNewsItemListener newsItemListener) {
        super(new NewsDiffUtilCallback());
        mNewsItemListener = newsItemListener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        final ItemNewsListBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.item_news_list, viewGroup, false);
        return new NewsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsViewHolder newsViewHolder, final int position) {
        final News news = getItem(position);
        newsViewHolder.bindNews(news);

        newsViewHolder.itemView.setOnClickListener(view -> mNewsItemListener.onClick(getItem(position)));
        mNewsItemListener.onReviewed(news);
    }

    public static class NewsDiffUtilCallback extends DiffUtil.ItemCallback<News> {

        @Override
        public boolean areItemsTheSame(@NonNull final News oldItem, @NonNull final News newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull final News oldItem, @NonNull final News newItem) {
            return oldItem.getTitle().equals(newItem.getTitle());
        }
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {

        private ItemNewsListBinding mBinding;

        NewsViewHolder(final ItemNewsListBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bindNews(final News news) {
            mBinding.setNews(news);
            mBinding.executePendingBindings();
        }
    }
}
