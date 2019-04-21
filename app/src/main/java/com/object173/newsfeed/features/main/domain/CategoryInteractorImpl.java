package com.object173.newsfeed.features.main.domain;

import java.util.List;

import androidx.lifecycle.LiveData;

import com.object173.newsfeed.features.base.domain.CategoryRepository;
import com.object173.newsfeed.features.base.domain.model.local.Category;

public class CategoryInteractorImpl implements CategoryInteractor {

    private final CategoryRepository mRepository;

    public CategoryInteractorImpl(CategoryRepository repository) {
        mRepository = repository;
    }

    @Override
    public LiveData<List<Category>> getCategories() {
        return mRepository.getCategories();
    }
}
