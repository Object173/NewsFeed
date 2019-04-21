package com.object173.newsfeed.features.newslist.feed.presentation;

import android.content.Context;
import android.content.Intent;
import com.object173.newsfeed.features.base.presentation.SingleFragmentActivity;

import androidx.fragment.app.Fragment;

public class NewsFeedActivity extends SingleFragmentActivity {

    private static final String KEY_FEED_LINK = "feed_link";

    public static Intent getIntent(Context context, String link) {
        Intent result = new Intent(context, NewsFeedActivity.class);
        result.putExtra(KEY_FEED_LINK, link);
        return result;
    }

    @Override
    protected Fragment createFragment() {
        return NewsFeedFragment.newInstanceByFeed(getIntent().getExtras().getString(KEY_FEED_LINK));
    }
}
