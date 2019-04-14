package com.object173.newsfeed.features.newslist.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.object173.newsfeed.R;
import com.object173.newsfeed.databinding.FragmentListBinding;
import com.object173.newsfeed.features.ItemTouchHelperCallback;
import com.object173.newsfeed.features.news.presentation.NewsActivity;
import com.object173.newsfeed.features.newslist.domain.model.News;
import com.object173.newsfeed.features.newslist.domain.model.RequestResult;
import com.object173.newsfeed.libs.log.ILogger;
import com.object173.newsfeed.libs.log.LoggerFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

public class NewsListFragment extends Fragment {

    private static final ILogger LOGGER = LoggerFactory.get(NewsListFragment.class);

    private NewsListViewModel mViewModel;
    private FragmentListBinding mBinding;
    private NewsPagedAdapter mPagedAdapter;

    private static final String ATTR_FEED_CATEGORY = "category";
    private static final String ATTR_FEED_LINK = "feed_link";

    public static NewsListFragment newInstanceByFeed(final String feedLink) {
        final NewsListFragment result = new NewsListFragment();
        Bundle args = new Bundle();
        args.putString(ATTR_FEED_LINK, feedLink);
        result.setArguments(args);
        return result;
    }

    public static NewsListFragment newInstanceByCategory(final String category) {
        final NewsListFragment result = new NewsListFragment();
        Bundle args = new Bundle();
        args.putString(ATTR_FEED_CATEGORY, category);
        result.setArguments(args);
        return result;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String feedLink = getArguments().getString(ATTR_FEED_LINK);
        String category = getArguments().getString(ATTR_FEED_CATEGORY);

        NewsListViewModelFactory factory = category != null ?
                NewsListViewModelFactory.getByCategory(getActivity().getApplication(), category) :
                NewsListViewModelFactory.getByFeed(getActivity().getApplication(), feedLink);

        mViewModel = ViewModelProviders.of(this, factory).get(NewsListViewModel.class);
    }

    public void setCategory(String category) {
        if(mPagedAdapter.getCurrentList() != null) {
            mPagedAdapter.submitList(null);
        }
        mViewModel.setCategory(category).observe(this, feedList -> {
            mPagedAdapter.submitList(feedList);
        });
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
        mPagedAdapter = new NewsPagedAdapter(new NewsPagedAdapter.OnNewsItemListener() {
            @Override
            public void onClick(News news) {
                startActivity(NewsActivity.getIntent(getActivity(), news.getId()));
            }

            @Override
            public void onReviewed(News news) {
                mViewModel.onNewsReviewed(news);
            }
        });
        mBinding.recyclerView.setAdapter(mPagedAdapter);

        final ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(this::hideNews);
        final ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mBinding.recyclerView);

        mViewModel.getNewsData().observe(this, news -> mPagedAdapter.submitList(news));

        return mBinding.getRoot();
    }

    @Override
    public void onPause() {
        super.onPause();
        mViewModel.setReviewedNews();
    }

    private void hideNews(final int position) {
        mViewModel.hideNews(position).observe(this, result -> {
            if(result == Boolean.TRUE) {
                Snackbar.make(mBinding.getRoot(), R.string.news_hidden_message, Snackbar.LENGTH_SHORT).show();
            }
        });
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
