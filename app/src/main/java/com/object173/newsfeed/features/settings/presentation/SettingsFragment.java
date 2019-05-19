package com.object173.newsfeed.features.settings.presentation;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.snackbar.Snackbar;
import com.object173.newsfeed.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    private SettingsViewModel mViewModel;
    private boolean isUpdateConfigChanged = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, new SettingsViewModelFactory(getActivity().getApplication()))
                .get(SettingsViewModel.class);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.fragment_settings);

        findPreference(getString(R.string.pref_key_auto_refresh_enabled))
                .setOnPreferenceChangeListener((preference, newValue) -> isUpdateConfigChanged = true);
        findPreference(getString(R.string.pref_key_refresh_period))
                .setOnPreferenceChangeListener((preference, newValue) -> isUpdateConfigChanged = true);
        findPreference(getString(R.string.pref_key_wifi_only))
                .setOnPreferenceChangeListener((preference, newValue) -> isUpdateConfigChanged = true);

        findPreference(getString(R.string.pref_key_current_theme))
                .setOnPreferenceChangeListener((preference, newValue) -> {
                    if(getView() != null) {
                        Snackbar.make(getView(), R.string.change_theme_message, Snackbar.LENGTH_LONG).show();
                    }
                    return true;
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        if(isUpdateConfigChanged) {
            mViewModel.changeAutoUpdateConfig(getContext());
        }
    }
}
