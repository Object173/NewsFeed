package com.object173.newsfeed.features.news.item.presentation;

import android.content.Context;
import android.content.Intent;

import com.object173.newsfeed.features.base.presentation.SingleFragmentActivity;

import androidx.fragment.app.Fragment;

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
