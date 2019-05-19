package com.object173.newsfeed.features.news.list;

import androidx.paging.PagedListAdapter;

import com.object173.newsfeed.R;
import com.object173.newsfeed.features.base.model.local.News;
import com.object173.newsfeed.features.base.presentation.BaseListFragment;

public abstract class NewsListFragment extends BaseListFragment<News, NewsPagedAdapter.NewsViewHolder> {

    @Override
    protected PagedListAdapter<News, NewsPagedAdapter.NewsViewHolder> getPagedAdapter() {
        return new NewsPagedAdapter(this);
    }

    @Override
    protected int getRemoveMessage() {
        return R.string.news_hidden_message;
    }
}
