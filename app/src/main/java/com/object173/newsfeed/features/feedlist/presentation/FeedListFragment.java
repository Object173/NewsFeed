package com.object173.newsfeed.features.feedlist.presentation;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.object173.newsfeed.R;
import com.object173.newsfeed.databinding.FragmentListBinding;
import com.object173.newsfeed.features.feedloader.presentation.AddFeedActivity;

public class FeedListFragment extends Fragment {

    private FeedListViewModel mViewModel;
    private FragmentListBinding mBinding;
    private FeedPagedAdapter mPagedAdapter;

    public static FeedListFragment newInstance() {
        return new FeedListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mViewModel = ViewModelProviders.of(this, new FeedListViewModelFactory
                (getActivity().getApplication())).get(FeedListViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);

        mBinding.swipeRefreshLayout.setEnabled(false);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPagedAdapter = new FeedPagedAdapter();
        mBinding.recyclerView.setAdapter(mPagedAdapter);

        mViewModel.getFeedData().observe(this, feedList -> mPagedAdapter.submitList(feedList));

        return mBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_feed_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_feed:
                startActivity(AddFeedActivity.getIntent(getContext()));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
