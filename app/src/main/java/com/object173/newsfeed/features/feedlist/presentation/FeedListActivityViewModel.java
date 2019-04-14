package com.object173.newsfeed.features.feedlist.presentation;

import com.object173.newsfeed.features.feedlist.domain.CategoryInteractor;
import com.object173.newsfeed.features.feedlist.domain.model.Category;

import java.util.LinkedList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class FeedListActivityViewModel extends ViewModel {

    private final CategoryInteractor mInteractor;
    private LiveData<List<String>> mCategories;

    FeedListActivityViewModel(CategoryInteractor interactor) {
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
