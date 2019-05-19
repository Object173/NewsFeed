package com.object173.newsfeed.features.news.list;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.object173.newsfeed.R;
import com.object173.newsfeed.databinding.ItemNewsListBinding;
import com.object173.newsfeed.features.base.model.local.News;
import com.object173.newsfeed.features.base.presentation.OnItemClickListener;

public class NewsPagedAdapter extends PagedListAdapter<News, NewsPagedAdapter.NewsViewHolder> {

    private final OnItemClickListener<News> mNewsItemListener;

    NewsPagedAdapter(OnItemClickListener<News> newsItemListener) {
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
        newsViewHolder.itemView.setOnClickListener(view -> mNewsItemListener.onItemClick(news));
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

    static class NewsViewHolder extends RecyclerView.ViewHolder {

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
