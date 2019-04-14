package com.object173.newsfeed.features.category.presentation;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.object173.newsfeed.R;
import com.object173.newsfeed.features.SingleFragmentActivity;
import com.object173.newsfeed.features.feed.presentation.FeedActivity;
import com.object173.newsfeed.features.settings.presentation.SettingsActivity;

import androidx.fragment.app.Fragment;

public class CategoryListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return CategoryListFragment.newInstance();
    }

    public static String getCategory(Intent intent) {
        return CategoryListFragment.getCategory(intent);
    }

    public static Intent getIntent(final Context context) {
        return new Intent(context, CategoryListActivity.class);
    }
}
