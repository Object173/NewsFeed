package com.object173.newsfeed.features.newslist.feed.presentation;

import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedListAdapter;

import com.object173.newsfeed.R;
import com.object173.newsfeed.features.base.domain.model.local.News;
import com.object173.newsfeed.features.base.presentation.BaseListFragment;
import com.object173.newsfeed.features.base.presentation.BaseListFragmentViewModel;
import com.object173.newsfeed.features.news.presentation.NewsActivity;
import com.object173.newsfeed.features.newslist.NewsListViewModelFactory;
import com.object173.newsfeed.features.newslist.NewsPagedAdapter;

public class NewsFeedFragment extends BaseListFragment<News, NewsPagedAdapter.NewsViewHolder> {

    public static NewsFeedFragment newInstanceByFeed(final String feedLink) {
        final NewsFeedFragment result = new NewsFeedFragment();
        result.setArguments(BaseListFragment.getBundle(feedLink));
        return result;
    }

    @Override
    protected BaseListFragmentViewModel<News> getViewModel() {
        return ViewModelProviders.of(this, new NewsListViewModelFactory(getActivity().getApplication()))
                .get(NewsFeedViewModel.class);
    }

    @Override
    protected PagedListAdapter<News, NewsPagedAdapter.NewsViewHolder> getPagedAdapter() {
        return new NewsPagedAdapter(new NewsPagedAdapter.OnNewsItemListener() {
            @Override
            public void onClick(News news) {
                startActivity(NewsActivity.getIntent(getContext(), news.getId()));
            }

            @Override
            public void onReviewed(News news) {
            }
        });
    }

    @Override
    protected int getRemoveMessage() {
        return R.string.news_hidden_message;
    }
}