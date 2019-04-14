package com.object173.newsfeed.features.feedlist.presentation;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.object173.newsfeed.R;
import com.object173.newsfeed.features.SingleFragmentActivity;
import com.object173.newsfeed.features.feed.presentation.FeedActivity;
import com.object173.newsfeed.features.settings.presentation.SettingsActivity;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class FeedListActivity extends SingleFragmentActivity {

    private static final String KEY_CATEGORY = "category";

    private FeedListActivityViewModel mViewModel;

    private String mCurrentCategory;
    private String[] mCategoriesTitle;

    @Override
    protected Fragment createFragment() {
        return FeedListFragment.newInstance(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            mCurrentCategory = savedInstanceState.getString(KEY_CATEGORY);
        }
        mCategoriesTitle = new String[] {getString(R.string.non_category_title)};

        mViewModel = ViewModelProviders.of(this, new FeedListViewModelFactory(getApplication()))
                .get(FeedListActivityViewModel.class);

        mViewModel.getCategories().observe(this, strings -> {
            mCategoriesTitle = new String[strings.size() + 1];
            mCategoriesTitle[0] = getString(R.string.non_category_title);
            for(int i = 1; i < strings.size() + 1; i++) {
                mCategoriesTitle[i] = strings.get(i - 1);
            }

            invalidateOptionsMenu();
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CATEGORY, mCurrentCategory);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fragment_feed_list, menu);

        if(mFragment == null || mCategoriesTitle == null) {
            return true;
        }

        MenuItem menuItem = menu.findItem(R.id.categorySpinner);
        View view = menuItem.getActionView();
        if (view instanceof Spinner)
        {
            final Spinner spinner = (Spinner) view;
            spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                    mCategoriesTitle));

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long id) {
                    mCurrentCategory = mCategoriesTitle[position];
                    if(position == 0) {
                        ((FeedListFragment)mFragment).setCategory(null);
                    }
                    else {
                        ((FeedListFragment)mFragment).setCategory((String) spinner.getAdapter().getItem(position));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    spinner.setSelection(0);
                }
            });

            int position = mCurrentCategory != null ? Arrays.binarySearch(mCategoriesTitle, mCurrentCategory) : 0;
            spinner.setSelection(position > 0 ? position : 0);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_feed:
                startActivity(FeedActivity.getLoadIntent(this));
                return true;
            case R.id.settings:
                startActivity(SettingsActivity.getIntent(this));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
