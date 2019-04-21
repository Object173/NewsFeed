package com.object173.newsfeed.features.settings.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.object173.newsfeed.features.settings.device.AutoUpdateWorker;
import com.object173.newsfeed.features.settings.domain.SettingsInteractor;
import com.object173.newsfeed.features.settings.domain.SettingsInteractorImpl;

public class SettingsViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private final Application mApplication;

    public SettingsViewModelFactory(@NonNull Application application) {
        super(application);
        mApplication = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == SettingsViewModel.class) {

            final SettingsInteractor interactor = new SettingsInteractorImpl(new AutoUpdateWorker.AutoUpdateServiceImpl());

            return (T) new SettingsViewModel(interactor);
        }
        return super.create(modelClass);
    }
}
