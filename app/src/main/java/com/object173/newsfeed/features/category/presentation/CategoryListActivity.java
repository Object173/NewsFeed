package com.object173.newsfeed.features.category.presentation;

import android.content.Context;
import android.content.Intent;

import com.object173.newsfeed.R;
import com.object173.newsfeed.features.base.presentation.SingleFragmentActivity;

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
