package com.object173.newsfeed.features.newslist.presentation;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.object173.newsfeed.R;
import com.object173.newsfeed.databinding.FragmentListBinding;
import com.object173.newsfeed.features.newslist.domain.model.RequestResult;
import com.object173.newsfeed.libs.log.ILogger;
import com.object173.newsfeed.libs.log.LoggerFactory;

public class NewsListFragment extends Fragment {

    private static final ILogger LOGGER = LoggerFactory.get(NewsListFragment.class);

    private NewsListViewModel mViewModel;
    private FragmentListBinding mBinding;
    private NewsPagedAdapter mPagedAdapter;

    private static final String ATTR_FEED_LINK = "feed_link";

    public static NewsListFragment newInstance(final String feedLink) {
        final NewsListFragment result = new NewsListFragment();
        Bundle args = new Bundle();
        args.putString(ATTR_FEED_LINK, feedLink);
        result.setArguments(args);
        return result;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String feedLink = getArguments().getString(ATTR_FEED_LINK);
        mViewModel = ViewModelProviders.of(this, new NewsListViewModelFactory(
                getActivity().getApplication(), feedLink)).get(NewsListViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);

        mBinding.swipeRefreshLayout.setOnRefreshListener(() ->
                mViewModel.updateFeed().observe(this, this::onUpdateStatus));

        if(mViewModel.getRefreshStatus() != null) {
            mViewModel.updateFeed().observe(this, this::onUpdateStatus);
        }

        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPagedAdapter = new NewsPagedAdapter();
        mBinding.recyclerView.setAdapter(mPagedAdapter);

        mViewModel.getNewsData().observe(this, news -> mPagedAdapter.submitList(news));

        return mBinding.getRoot();
    }

    private void onUpdateStatus(RequestResult requestResult) {
        boolean isRefreshed = requestResult == RequestResult.RUNNING;
        mBinding.swipeRefreshLayout.setRefreshing(isRefreshed);

        if(!isRefreshed) {
            Snackbar.make(mBinding.getRoot(), requestResult.toString(), Snackbar.LENGTH_SHORT).show();
            mViewModel.getRefreshStatus().removeObservers(this);
            mViewModel.cancelRefresh();
        }
    }
}
