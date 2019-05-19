package com.object173.newsfeed.features.feed.item.presentation;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.object173.newsfeed.features.base.presentation.SingleFragmentActivity;

public class FeedActivity extends SingleFragmentActivity {

    private static final String KEY_FEED_LINK = "feed_link";

    public static Intent getUpdateIntent(Context context, String feedLink) {
        Intent result = new Intent(context, FeedActivity.class);
        result.putExtra(KEY_FEED_LINK, feedLink);
        return result;
    }

    public static Intent getLoadIntent(Context context) {
        return new Intent(context, FeedActivity.class);
    }

    @Override
    protected Fragment createFragment() {
        String feedLink = getIntent().getExtras() != null ?
                getIntent().getExtras().getString(KEY_FEED_LINK) : null;
        if(feedLink == null) {
            feedLink = getIntent().getDataString();
        }
        return FeedFragment.newInstance(feedLink);
    }
}
