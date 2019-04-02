package com.object173.newsfeed.features.newslist.presentation;

import android.arch.paging.PagedListAdapter;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.object173.newsfeed.R;
import com.object173.newsfeed.databinding.ItemNewsListBinding;
import com.object173.newsfeed.features.newslist.domain.model.News;

public class NewsPagedAdapter extends PagedListAdapter<News, NewsPagedAdapter.NewsViewHolder> {

    NewsPagedAdapter() {
        super(new NewsDiffUtilCallback());
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
        newsViewHolder.bindNews(getItem(position));
    }

    static class NewsDiffUtilCallback extends DiffUtil.ItemCallback<News> {

        @Override
        public boolean areItemsTheSame(@NonNull final News oldItem, @NonNull final News newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull final News oldItem, @NonNull final News newItem) {
            return oldItem.getTitle().equals(newItem.getTitle());
        }
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {

        private ItemNewsListBinding mBinding;

        NewsViewHolder(final ItemNewsListBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.setNews(new NewsItemViewModel());
        }

        void bindNews(final News news) {
            mBinding.getNews().setNews(news);
            mBinding.executePendingBindings();
        }
    }
}
