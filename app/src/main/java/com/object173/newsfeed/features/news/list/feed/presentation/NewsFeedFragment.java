package com.object173.newsfeed.features.news.list.feed.presentation;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;

import com.object173.newsfeed.features.base.model.local.News;
import com.object173.newsfeed.features.base.presentation.BaseListFragment;
import com.object173.newsfeed.features.base.presentation.BaseListFragmentViewModel;
import com.object173.newsfeed.features.news.list.NewsListFragment;
import com.object173.newsfeed.features.news.list.NewsListViewModelFactory;

public class NewsFeedFragment extends NewsListFragment {

    static NewsFeedFragment newInstanceByFeed(final String feedLink) {
        final NewsFeedFragment result = new NewsFeedFragment();
        Bundle args = new Bundle();
        BaseListFragment.putParam(args, feedLink);
        result.setArguments(args);
        return result;
    }

    @Override
    protected BaseListFragmentViewModel<News> getViewModel() {
        return ViewModelProviders.of(this, new NewsListViewModelFactory(getActivity().getApplication()))
                .get(NewsFeedViewModel.class);
    }
}
