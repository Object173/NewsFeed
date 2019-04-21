package com.object173.newsfeed.features.base.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.object173.newsfeed.features.base.model.network.RequestResult;
import com.object173.newsfeed.libs.log.LoggerFactory;

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

    LiveData<PagedList<T>> getListData(String param) {
        if(param.equals(mParam) && mListData != null) {
            return mListData;
        }
        mParam = param;
        LoggerFactory.get(this.getClass()).info(mParam);
        return getListData(loadFactoryData(param));
    }

    LiveData<PagedList<T>> getListData() {
        if(mParam == null && mListData != null) {
            return mListData;
        }
        mParam = null;
        LoggerFactory.get(this.getClass()).info("null");
        return getListData(loadFactoryData());
    }

    LiveData<Boolean> removeData(int position) {
        return removeData(mListData.getValue().get(position));
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
}
