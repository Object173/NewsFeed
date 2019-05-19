package com.object173.newsfeed.features.news.list.category.presentation;

import androidx.lifecycle.ViewModelProviders;

import com.object173.newsfeed.features.base.model.local.News;
import com.object173.newsfeed.features.base.presentation.BaseListFragmentViewModel;
import com.object173.newsfeed.features.news.list.NewsListFragment;
import com.object173.newsfeed.features.news.list.NewsListViewModelFactory;

public class NewsCategoryFragment extends NewsListFragment {

    @Override
    protected BaseListFragmentViewModel<News> getViewModel() {
        return ViewModelProviders.of(this, new NewsListViewModelFactory(getActivity().getApplication()))
                .get(NewsCategoryViewModel.class);
    }
}
