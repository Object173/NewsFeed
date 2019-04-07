package com.object173.newsfeed.features.news.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.object173.newsfeed.R;
import com.object173.newsfeed.databinding.FragmentWebBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class NewsFragment extends Fragment {

    private NewsViewModel mViewModel;
    private FragmentWebBinding mBinding;

    private static final String ATTR_NEWS_ID = "news_id";

    public static NewsFragment newInstance(final Long newsId) {
        final NewsFragment result = new NewsFragment();
        Bundle args = new Bundle();
        args.putLong(ATTR_NEWS_ID, newsId);
        result.setArguments(args);
        return result;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        final Long newsId = getArguments().getLong(ATTR_NEWS_ID);
        mViewModel = ViewModelProviders.of(this, new NewsViewModelFactory(
                getActivity().getApplication(), newsId)).get(NewsViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_web, container, false);

        mBinding.webView.getSettings().setJavaScriptEnabled(false);

        mBinding.progressBar.setMax(100);
        mBinding.webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(final WebView view, final int newProgress) {
                if(newProgress == 100) {
                    mBinding.progressBar.setVisibility(View.GONE);
                }
                else {
                    mBinding.progressBar.setVisibility(View.VISIBLE);
                    mBinding.progressBar.setProgress(newProgress);
                }
            }
        });

        mViewModel.getNews().observe(this, news -> {
            if(news != null) {
                mBinding.webView.loadData(news.getDescription(),
                        "text/html; charset=utf-8", "UTF-8");

                final AppCompatActivity activity = (AppCompatActivity)getActivity();
                if(activity != null && activity.getSupportActionBar() != null) {
                    activity.getSupportActionBar().setTitle(news.getTitle());
                }
            }
            getActivity().invalidateOptionsMenu();
        });

        return mBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.news_fragment, menu);

        final MenuItem searchItem = menu.findItem(R.id.open_link_menu);
        searchItem.setVisible(mViewModel.isSourceLinkEnabled());
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_menu:
                mViewModel.shareNews(getActivity());
                return true;
            case R.id.open_link_menu:
                mViewModel.openSourceLink(getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
