package com.object173.newsfeed.features.feedlist.domain;

import com.object173.newsfeed.features.feedlist.domain.model.Category;

import java.util.List;

import androidx.lifecycle.LiveData;

public interface CategoryInteractor {
    LiveData<List<Category>> getCategories();
}
