package com.object173.newsfeed.features.base.presentation;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.object173.newsfeed.App;
import com.object173.newsfeed.features.base.data.pref.PreferenceDataSource;

public abstract class BaseActivity extends AppCompatActivity {

    private Integer mHomeIconId = null;
    private boolean isHomeButtonEnabled = true;

    protected void setHomeIconId(int homeIconId) {
        mHomeIconId = homeIconId;
    }

    protected void setHomeButtonEnabled(boolean enabled) {
        isHomeButtonEnabled = enabled;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        PreferenceDataSource dataSource = App.getPreferenceDataSource(getApplicationContext());
        if(isTaskRoot()) {
            dataSource.updateCurrentThemeId();
        }
        setTheme(dataSource.getCurrentThemeId());

        super.onCreate(savedInstanceState);

        if(isHomeButtonEnabled) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                if (mHomeIconId != null) {
                    actionBar.setHomeAsUpIndicator(mHomeIconId);
                }
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    protected void onHomeButtonClick() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onHomeButtonClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
