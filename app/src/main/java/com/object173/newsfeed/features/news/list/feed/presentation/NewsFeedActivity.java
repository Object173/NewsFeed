package com.object173.newsfeed.features.news.list.feed.presentation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.object173.newsfeed.features.base.presentation.SingleFragmentActivity;
import com.object173.newsfeed.features.news.item.presentation.NewsActivity;

public class NewsFeedActivity extends SingleFragmentActivity {

    private static final String KEY_FEED_LINK = "feed_link";

    public static Intent getIntent(Context context, String link) {
        Intent result = new Intent(context, NewsFeedActivity.class);
        result.putExtra(KEY_FEED_LINK, link);
        return result;
    }

    @Override
    protected Fragment createFragment() {
        Bundle extras = getIntent().getExtras();
        return NewsFeedFragment.newInstanceByFeed(extras != null ? extras.getString(KEY_FEED_LINK) : null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((NewsFeedFragment)mFragment).getClickedItem().observe(this, event ->
                startActivity(NewsActivity.getIntent(this, event.mItem.getId())));
    }
}
