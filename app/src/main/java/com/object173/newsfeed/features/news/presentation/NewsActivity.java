package com.object173.newsfeed.features.news.presentation;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.object173.newsfeed.features.SingleFragmentActivity;

public class NewsActivity extends SingleFragmentActivity {

    private static final String KEY_NEWS_ID = "news_id";

    public static Intent getIntent(Context context, Long newsId) {
        Intent result = new Intent(context, NewsActivity.class);
        result.putExtra(KEY_NEWS_ID, newsId);
        return result;
    }

    @Override
    protected Fragment createFragment() {
        return NewsFragment.newInstance(getIntent().getExtras().getLong(KEY_NEWS_ID));
    }
}
