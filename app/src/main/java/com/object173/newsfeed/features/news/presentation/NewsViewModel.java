package com.object173.newsfeed.features.news.presentation;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.object173.newsfeed.R;
import com.object173.newsfeed.features.base.domain.model.local.News;
import com.object173.newsfeed.features.news.domain.NewsInteractor;

import androidx.core.app.ShareCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

class NewsViewModel extends ViewModel {

    private final NewsInteractor mNewsInteractor;

    private final Long mNewId;
    private final LiveData<News> mNews;

    NewsViewModel(final NewsInteractor newsInteractor, final Long newsId) {
        mNewsInteractor = newsInteractor;
        mNewId = newsId;

        mNews = mNewsInteractor.getNews(mNewId);
    }

    LiveData<News> getNews() {
        return mNews;
    }

    boolean isSourceLinkEnabled() {
        return mNews.getValue() != null && mNews.getValue().getSourceLink() != null;
    }

    void openSourceLink(final Activity activity) {
        if(!isSourceLinkEnabled()) {
            return;
        }
        final Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mNews.getValue().getSourceLink()));
        activity.startActivity(Intent.createChooser(browserIntent, activity.getString(R.string.open_link_chooser)));
    }

    void shareNews(final Activity activity)
    {
        final Intent sendIntent =
                ShareCompat.IntentBuilder.from(activity)
                        .setChooserTitle(R.string.share_news_chooser_title)
                        .setType("text/plain")
                        .setText(activity.getString(R.string.news_share_format,
                                mNews.getValue().getTitle(), mNews.getValue().getSourceLink()))
                        .createChooserIntent();
        activity.startActivity(sendIntent);
    }
}
