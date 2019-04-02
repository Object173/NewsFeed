package com.object173.newsfeed.features.feedloader.presentation;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.object173.newsfeed.features.feedloader.data.LoaderRepositoryImpl;
import com.object173.newsfeed.features.feedloader.domain.LoaderInteractor;
import com.object173.newsfeed.features.feedloader.domain.LoaderInteractorImpl;
import com.object173.newsfeed.features.feedloader.domain.LoaderRepository;

public class AddListViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == AddFeedViewModel.class) {

            final LoaderRepository loaderRepository = new LoaderRepositoryImpl();
            final LoaderInteractor loaderInteractor = new LoaderInteractorImpl(loaderRepository);

            return (T) new AddFeedViewModel(loaderInteractor);
        }
        return super.create(modelClass);
    }
}
