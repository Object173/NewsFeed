package com.object173.newsfeed.features.main.presentation;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.object173.newsfeed.R;
import com.object173.newsfeed.features.base.presentation.BaseListFragment;
import com.object173.newsfeed.features.feed.list.presentation.FeedListFragment;
import com.object173.newsfeed.features.news.list.feed.presentation.NewsFeedFragment;

public class MainPagerAdapter extends FragmentPagerAdapter {

    private final static int COUNT = 2;
    private final Context mContext;

    public MainPagerAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }

    public void setCategory(String category) {
        for(int i = 0; i < COUNT; i++) {
            ((BaseListFragment)getItem(i)).setParam(category);
        }
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            return new FeedListFragment();
        }
        return new NewsFeedFragment();
    }

    @Override public CharSequence getPageTitle(int position) {
        if(position == 0) {
            return mContext.getString(R.string.tab_feed_title);
        }
        return mContext.getString(R.string.tab_news_title);
    }

    @Override
    public int getCount() {
        return COUNT;
    }
}
