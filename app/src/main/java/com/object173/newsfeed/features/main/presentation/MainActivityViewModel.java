package com.object173.newsfeed.features.main.presentation;

import com.object173.newsfeed.features.base.model.local.Category;
import com.object173.newsfeed.features.main.domain.CategoryInteractor;

import java.util.LinkedList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {

    private final CategoryInteractor mInteractor;
    private LiveData<List<String>> mCategories;

    MainActivityViewModel(CategoryInteractor interactor) {
        mInteractor = interactor;

        mCategories = Transformations.map(mInteractor.getCategories(), input -> {
            List<String> result = new LinkedList<>();
            for(Category category : input) {
                result.add(category.getTitle());
            }
            return result;
        });
    }

    LiveData<List<String>> getCategories() {
        return mCategories;
    }
}
