package com.object173.newsfeed.features.settings.presentation;

import com.object173.newsfeed.features.settings.data.SettingsRepositoryImpl;
import com.object173.newsfeed.features.settings.domain.SettingsInteractor;
import com.object173.newsfeed.features.settings.domain.SettingsInteractorImpl;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SettingsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass == SettingsViewModel.class) {
            final SettingsInteractor interactor = new SettingsInteractorImpl(new SettingsRepositoryImpl());

            return (T)new SettingsViewModel(interactor);
        }
        return super.create(modelClass);
    }
}
