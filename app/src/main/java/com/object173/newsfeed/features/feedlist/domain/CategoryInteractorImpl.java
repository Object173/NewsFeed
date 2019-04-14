package com.object173.newsfeed.features.feedlist.domain;

import com.object173.newsfeed.features.feedlist.data.LocalDataSource;
import com.object173.newsfeed.features.feedlist.domain.model.Category;

import java.util.List;

import androidx.lifecycle.LiveData;

public class CategoryInteractorImpl implements CategoryInteractor {

    private final FeedRepository mRepository;

    public CategoryInteractorImpl(FeedRepository repository) {
        mRepository = repository;
    }

    @Override
    public LiveData<List<Category>> getCategories() {
        return mRepository.getCategories();
    }
}
