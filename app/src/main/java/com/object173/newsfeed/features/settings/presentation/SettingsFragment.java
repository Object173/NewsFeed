package com.object173.newsfeed.features.settings.presentation;

import android.os.Bundle;

import com.object173.newsfeed.R;

import androidx.lifecycle.ViewModelProviders;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SeekBarPreference;

public class SettingsFragment extends PreferenceFragmentCompat {

    private SettingsViewModel mViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, new SettingsViewModelFactory()).get(SettingsViewModel.class);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.fragment_settings);
    }

    @Override
    public void onStop() {
        super.onStop();
        mViewModel.changeAutoUpdateConfig(getContext());
        mViewModel.changeNotificationConfig(getContext());
    }
}
