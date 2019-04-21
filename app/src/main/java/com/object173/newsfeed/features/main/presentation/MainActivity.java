package com.object173.newsfeed.features.main.presentation;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.object173.newsfeed.R;
import com.object173.newsfeed.databinding.ActivityMainBinding;
import com.object173.newsfeed.features.base.presentation.BaseListFragment;
import com.object173.newsfeed.features.feed.presentation.FeedActivity;
import com.object173.newsfeed.features.feedlist.presentation.FeedListFragment;
import com.object173.newsfeed.features.newslist.category.presentation.NewsCategoryFragment;
import com.object173.newsfeed.features.settings.presentation.SettingsActivity;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_CATEGORY = "category";

    private static final String TAG_FEED = "feed";
    private static final String TAG_NEWS = "news";

    private MainActivityViewModel mViewModel;
    private ActivityMainBinding mBinding;

    private String mCurrentCategory;
    private String[] mCategoriesTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = ViewModelProviders.of(this, new MainViewModelFactory(getApplication()))
                .get(MainActivityViewModel.class);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        if(savedInstanceState != null) {
            mCurrentCategory = savedInstanceState.getString(KEY_CATEGORY);
        }
        mCategoriesTitle = new String[] {getString(R.string.non_category_title)};

        mBinding.tabs.setup(this, getSupportFragmentManager(), R.id.tabContent);

        mBinding.tabs.addTab(mBinding.tabs.newTabSpec(TAG_FEED).setIndicator(getString(R.string.tab_feed_title)),
                FeedListFragment.class, BaseListFragment.getBundle(mCurrentCategory));
        mBinding.tabs.addTab(mBinding.tabs.newTabSpec(TAG_NEWS).setIndicator(getString(R.string.tab_news_title)),
                NewsCategoryFragment.class, BaseListFragment.getBundle(mCurrentCategory));

        mViewModel.getCategories().observe(this, strings -> {
            mCategoriesTitle = new String[strings.size() + 1];
            mCategoriesTitle[0] = getString(R.string.non_category_title);
            for(int i = 1; i < strings.size() + 1; i++) {
                mCategoriesTitle[i] = strings.get(i - 1);
            }
            invalidateOptionsMenu();
        });
    }

    private void setCurrentCategory(String category) {
        mCurrentCategory = category;

        FragmentManager manager = getSupportFragmentManager();

        Fragment feedFragment = manager.findFragmentByTag(TAG_FEED);
        Fragment newsFragment = manager.findFragmentByTag(TAG_NEWS);

        if(feedFragment != null) {
            ((BaseListFragment)feedFragment).setParam(category);
        }
        if(newsFragment != null) {
            ((BaseListFragment)newsFragment).setParam(category);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CATEGORY, mCurrentCategory);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fragment_feed_list, menu);

        if(mCategoriesTitle == null) {
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
                    if(position == 0) {
                        setCurrentCategory(null);
                    }
                    else {
                        setCurrentCategory((String) spinner.getAdapter().getItem(position));
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
