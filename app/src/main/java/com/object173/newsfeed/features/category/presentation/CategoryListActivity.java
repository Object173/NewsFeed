package com.object173.newsfeed.features.category.presentation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.object173.newsfeed.R;
import com.object173.newsfeed.features.base.model.local.Category;
import com.object173.newsfeed.features.base.presentation.OnItemClickListener;
import com.object173.newsfeed.features.base.presentation.SingleFragmentActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class CategoryListActivity extends SingleFragmentActivity {

    private static final String EXTRA_CATEGORY = "category";

    @Override
    protected Fragment createFragment() {
        return CategoryListFragment.newInstance();
    }

    private void returnResult(Category category) {
        final Intent intent = new Intent();
        intent.putExtra(EXTRA_CATEGORY, category.getTitle());

        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setHomeIconId(android.R.drawable.ic_menu_close_clear_cancel);
        super.onCreate(savedInstanceState);

        ((CategoryListFragment)mFragment).getClickedItem().observe(this, event ->
                returnResult(event.mItem));
    }

    public static String getCategory(Intent intent) {
        return intent.getStringExtra(EXTRA_CATEGORY);
    }

    public static Intent getIntent(final Context context) {
        return new Intent(context, CategoryListActivity.class);
    }

    @Override
    protected void onHomeButtonClick() {
        setResult(Activity.RESULT_OK, new Intent());
        finish();
    }
}
