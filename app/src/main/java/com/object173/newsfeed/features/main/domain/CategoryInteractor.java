package com.object173.newsfeed.features.main.domain;

import com.object173.newsfeed.features.base.model.local.Category;

import java.util.List;

import androidx.lifecycle.LiveData;

public interface CategoryInteractor {
    LiveData<List<Category>> getCategories();
}
