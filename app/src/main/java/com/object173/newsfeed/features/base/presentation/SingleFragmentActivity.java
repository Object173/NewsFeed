package com.object173.newsfeed.features.base.presentation;

import android.os.Bundle;

import com.object173.newsfeed.R;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public abstract class SingleFragmentActivity extends BaseActivity {

    protected Fragment mFragment;
    protected abstract Fragment createFragment();

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    protected Fragment getFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        final FragmentManager fragmentManager = getSupportFragmentManager();
        mFragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if(mFragment == null) {
            mFragment = createFragment();
            fragmentManager.beginTransaction().add(R.id.fragment_container, mFragment).commit();
        }
    }
}
