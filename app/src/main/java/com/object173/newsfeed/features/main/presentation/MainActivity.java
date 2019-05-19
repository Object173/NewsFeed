package com.object173.newsfeed.features.main.presentation;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.object173.newsfeed.R;
import com.object173.newsfeed.databinding.ActivityMainBinding;
import com.object173.newsfeed.features.base.presentation.BaseActivity;
import com.object173.newsfeed.features.base.presentation.ItemClickEvent;
import com.object173.newsfeed.features.feed.item.presentation.FeedActivity;
import com.object173.newsfeed.features.feed.list.presentation.FeedListFragment;
import com.object173.newsfeed.features.news.item.presentation.NewsActivity;
import com.object173.newsfeed.features.news.list.NewsListFragment;
import com.object173.newsfeed.features.news.list.category.presentation.NewsCategoryFragment;
import com.object173.newsfeed.features.news.list.feed.presentation.NewsFeedActivity;
import com.object173.newsfeed.features.news.list.feed.presentation.NewsFeedFragment;
import com.object173.newsfeed.features.settings.presentation.SettingsActivity;

import java.util.Arrays;

public class MainActivity extends BaseActivity {

    private static final String KEY_CATEGORY = "category";
    private static final String KEY_PAGE = "current_page";

    private static final String TAG_FEED = "feed";
    private static final String TAG_NEWS = "news";

    private FeedListFragment mFeedListFragment;
    private NewsListFragment mNewsListFragment;

    private MainActivityViewModel mViewModel;
    private ActivityMainBinding mBinding;

    private String mCurrentCategory;
    private String[] mCategoriesTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setHomeButtonEnabled(false);
        super.onCreate(savedInstanceState);

        mViewModel = ViewModelProviders.of(this, new MainViewModelFactory(getApplication()))
                .get(MainActivityViewModel.class);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        if(savedInstanceState != null) {
            mCurrentCategory = savedInstanceState.getString(KEY_CATEGORY);
        }
        mCategoriesTitle = new String[] {getString(R.string.non_category_title)};

        initFragments(savedInstanceState);

        mViewModel.getCategories().observe(this, strings -> {
            mCategoriesTitle = new String[strings.size() + 1];
            mCategoriesTitle[0] = getString(R.string.non_category_title);
            for(int i = 1; i < strings.size() + 1; i++) {
                mCategoriesTitle[i] = strings.get(i - 1);
            }
            invalidateOptionsMenu();
        });
    }

    private void initFragments(Bundle savedInstanceState) {
        boolean isTwoFragmentActivity = mBinding.feedFrame == null;

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        if(isTwoFragmentActivity) {

            mFeedListFragment = (FeedListFragment) manager.findFragmentByTag(TAG_FEED);
            if(mFeedListFragment == null) {
                mFeedListFragment = new FeedListFragment();
                transaction.add(mBinding.frameLayout.getId(), mFeedListFragment, TAG_FEED);
            }

            mNewsListFragment = (NewsListFragment) manager.findFragmentByTag(TAG_NEWS);
            if(mNewsListFragment == null) {
                mNewsListFragment = new NewsCategoryFragment();
                transaction.add(mBinding.frameLayout.getId(), mNewsListFragment, TAG_NEWS);
                transaction.hide(mNewsListFragment);
            }

            mFeedListFragment.getClickedItem().observe(this, event -> {
                if(event.mType == ItemClickEvent.Type.CLICK) {
                    startActivity(NewsFeedActivity.getIntent(this, event.mItem.getLink()));
                }
                else {
                    startActivity(FeedActivity.getUpdateIntent(this, event.mItem.getLink()));
                }
            });

            transaction.commit();

            mBinding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
                        FragmentTransaction newTransaction = getSupportFragmentManager().beginTransaction();
                        switch (item.getItemId()) {
                            case R.id.feed_page:
                                newTransaction.show(mFeedListFragment);
                                newTransaction.hide(mNewsListFragment);
                                newTransaction.commit();
                                return true;
                            case R.id.add_page:
                                startActivity(FeedActivity.getLoadIntent(this));
                                return false;
                            case R.id.news_page:
                                newTransaction.hide(mFeedListFragment);
                                newTransaction.show(mNewsListFragment);
                                newTransaction.commit();
                                return true;
                            default:
                                return super.onOptionsItemSelected(item);
                        }
                    });

            if(savedInstanceState != null) {
                String tag = savedInstanceState.getString(KEY_PAGE);
                mBinding.bottomNavigation.setSelectedItemId(tag != null && tag.equals(TAG_NEWS) ? 1 : 0);
            }
        }
        else {
            mFeedListFragment = (FeedListFragment) manager.findFragmentByTag(TAG_FEED);
            if(mFeedListFragment == null) {
                mFeedListFragment = new FeedListFragment();
                transaction.add(mBinding.feedFrame.getId(), mFeedListFragment, TAG_FEED);
            }

            mNewsListFragment = (NewsListFragment) manager.findFragmentByTag(TAG_NEWS);
            if(mNewsListFragment == null) {
                mNewsListFragment = new NewsFeedFragment();
                transaction.add(mBinding.newsFrame.getId(), mNewsListFragment, TAG_NEWS);
            }

            mFeedListFragment.getClickedItem().observe(this, event -> {
                if(event.mType == ItemClickEvent.Type.CLICK) {
                    mNewsListFragment.setParam(event.mItem.getLink());
                }
                else {
                    startActivity(FeedActivity.getUpdateIntent(this, event.mItem.getLink()));
                }
            });

            transaction.commitNow();
        }

        mNewsListFragment.getClickedItem().observe(this, event ->
                startActivity(NewsActivity.getIntent(this, event.mItem.getId())));
    }

    private void setCurrentCategory(String category) {
        mCurrentCategory = category;

        mFeedListFragment.setParam(category);

        if(mNewsListFragment instanceof NewsCategoryFragment) {
            mNewsListFragment.setParam(category);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CATEGORY, mCurrentCategory);

        if(mBinding != null && mBinding.bottomNavigation != null) {
            outState.putString(KEY_PAGE, mBinding.bottomNavigation.getSelectedItemId() == 0 ?
                    TAG_FEED : TAG_NEWS);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);

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
