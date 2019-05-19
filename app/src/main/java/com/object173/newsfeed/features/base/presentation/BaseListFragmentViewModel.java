package com.object173.newsfeed.features.base.presentation;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.object173.newsfeed.features.base.model.network.RequestResult;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

public abstract class BaseListFragmentViewModel<T> extends ViewModel {

    private static final int LOAD_BLOCK_SIZE = 10;
    private static final int PREFETCH_DISTANCE = 5;

    private LiveData<PagedList<T>> mListData;
    private LiveData<RequestResult> mRequestResult;
    protected String mParam;

    private LiveData<PagedList<T>> getListData(DataSource.Factory<Integer, T> factory) {
        final PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(LOAD_BLOCK_SIZE)
                .setPrefetchDistance(PREFETCH_DISTANCE)
                .build();

        mListData = new LivePagedListBuilder<>(
                factory, config)
                .setFetchExecutor(Executors.newSingleThreadExecutor())
                .build();

        return mListData;
    }

    LiveData<PagedList<T>> getListData(String param, LifecycleOwner owner) {
        if(mListData != null) {
            mListData.removeObservers(owner);
        }

        if(mListData != null && Objects.equals(mParam, param)) {
            return mListData;
        }
        mParam = param;

        if(mParam != null) {
            return getListData(loadFactoryData(param));
        }
        return getListData(loadFactoryData());
    }

    LiveData<Boolean> removeData(int position) {
        if(mListData != null && mListData.getValue() != null) {
            return removeData(mListData.getValue().get(position));
        }
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        result.setValue(false);
        return result;
    }

    LiveData<RequestResult> getRefreshStatus() {
        return mRequestResult;
    }
    LiveData<RequestResult> updateData() {
        return mRequestResult = refreshData();
    }
    void cancelRefresh() {
        mRequestResult = null;
    }

    protected DataSource.Factory<Integer, T> loadFactoryData(String param) {
        return loadFactoryData();
    }
    protected abstract DataSource.Factory<Integer, T> loadFactoryData();
    protected abstract LiveData<Boolean> removeData(T obj);
    public void addData(T obj) {}

    protected LiveData<RequestResult> refreshData() {
        return null;
    }
    protected boolean isRefreshEnabled() {
        return false;
    }

    void checkReviewedItemsCount(int count) {
        if(mListData == null || mListData.getValue() == null) {
            return;
        }

        List<T> data = mListData.getValue().snapshot().subList(0, count);
        checkReviewed(data);
    }

    protected void checkReviewed(List<T> items) {}
}
