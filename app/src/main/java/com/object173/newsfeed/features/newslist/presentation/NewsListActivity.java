package com.object173.newsfeed.features.newslist.presentation;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import com.object173.newsfeed.features.SingleFragmentActivity;

public class NewsListActivity extends SingleFragmentActivity {

    private static final String KEY_FEED_LINK = "feed_link";

    public static Intent getIntent(Context context, String link) {
        Intent result = new Intent(context, NewsListActivity.class);
        result.putExtra(KEY_FEED_LINK, link);
        return result;
    }

    @Override
    protected Fragment createFragment() {
        return NewsListFragment.newInstance(getIntent().getExtras().getString(KEY_FEED_LINK));
    }
}
