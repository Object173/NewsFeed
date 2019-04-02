package com.object173.newsfeed.features.feedlist.presentation;

import android.support.v4.app.Fragment;

import com.object173.newsfeed.features.SingleFragmentActivity;

public class FeedListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return FeedListFragment.newInstance();
    }
}
