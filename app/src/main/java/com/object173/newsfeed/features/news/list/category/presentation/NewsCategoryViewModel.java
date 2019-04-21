package com.object173.newsfeed.features.news.list.category.presentation;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import com.object173.newsfeed.features.base.model.local.News;
import com.object173.newsfeed.features.base.model.network.RequestResult;
import com.object173.newsfeed.features.base.presentation.BaseListFragmentViewModel;
import com.object173.newsfeed.features.news.list.category.domain.NewsCategoryInteractor;

public class NewsCategoryViewModel extends BaseListFragmentViewModel<News> {

    private final NewsCategoryInteractor mNewsInteractor;

    public NewsCategoryViewModel(final NewsCategoryInteractor newsInteractor) {
        mNewsInteractor = newsInteractor;
    }

    @Override
    protected DataSource.Factory<Integer, News> loadFactoryData() {
        return mNewsInteractor.getAllNews();
    }

    @Override
    protected DataSource.Factory<Integer, News> loadFactoryData(String param) {
        return mNewsInteractor.getNewsByCategory(param);
    }

    @Override
    protected LiveData<Boolean> removeData(News obj) {
        return mNewsInteractor.hideNews(obj.getId());
    }

    @Override
    protected LiveData<RequestResult> refreshData() {
        return mNewsInteractor.updateCategory(mParam);
    }

    @Override
    protected boolean isRefreshEnabled() {
        return true;
    }
}
