package com.object173.newsfeed.features.news.presentation;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.object173.newsfeed.R;
import com.object173.newsfeed.databinding.FragmentWebBinding;

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
            }
        });

        return mBinding.getRoot();
    }
}
