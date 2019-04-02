package com.object173.newsfeed.features.feedloader.presentation;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.object173.newsfeed.features.SingleFragmentActivity;

public class AddFeedActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return AddFeedFragment.newInstance();
    }

    public static Intent getIntent(final Context context) {
        return new Intent(context, AddFeedActivity.class);
    }
}
