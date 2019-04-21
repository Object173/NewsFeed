package com.object173.newsfeed.features.settings.domain;

import android.util.Pair;

import com.object173.newsfeed.features.base.model.local.News;
import com.object173.newsfeed.features.base.model.network.RequestResult;

import java.util.List;

public interface UpdateRepository {
    List<String> getAutoUpdateList();
    Pair<RequestResult, Integer> updateFeedNews(String feedLink);
}
