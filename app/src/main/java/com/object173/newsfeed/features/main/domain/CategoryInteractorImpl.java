package com.object173.newsfeed.features.main.domain;

import java.util.List;

import androidx.lifecycle.LiveData;

import com.object173.newsfeed.features.category.domain.CategoryRepository;
import com.object173.newsfeed.features.base.model.local.Category;

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
