package com.object173.newsfeed.features.feedlist.presentation;

import android.view.Menu;
import android.view.MenuItem;

import com.object173.newsfeed.R;
import com.object173.newsfeed.features.SingleFragmentActivity;
import com.object173.newsfeed.features.feed.presentation.FeedActivity;
import com.object173.newsfeed.features.settings.presentation.SettingsActivity;

import androidx.fragment.app.Fragment;

public class FeedListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return FeedListFragment.newInstance();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fragment_feed_list, menu);
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
