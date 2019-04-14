package com.object173.newsfeed.features.category.presentation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.object173.newsfeed.R;
import com.object173.newsfeed.databinding.FragmentListBinding;
import com.object173.newsfeed.features.ItemTouchHelperCallback;
import com.object173.newsfeed.features.category.domain.model.Category;
import com.object173.newsfeed.features.settings.device.AutoUpdateWorker;
import com.object173.newsfeed.features.settings.device.model.AutoUpdateConfig;
import com.object173.newsfeed.libs.log.LoggerFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

public class CategoryListFragment extends Fragment {

    private CategoryListViewModel mViewModel;
    private FragmentListBinding mBinding;
    private CategoryPagedAdapter mPagedAdapter;

    private static final String EXTRA_CATEGORY = "category_title";

    public static CategoryListFragment newInstance() {
        return new CategoryListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setupActionBar();

        mViewModel = ViewModelProviders.of(this, new CategoryListViewModelFactory
                (getActivity().getApplication())).get(CategoryListViewModel.class);

        mPagedAdapter = new CategoryPagedAdapter(this::returnResult);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPagedAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);

        mBinding.swipeRefreshLayout.setEnabled(false);

        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.recyclerView.setAdapter(mPagedAdapter);
        mViewModel.getFeedData().observe(this, feedList -> mPagedAdapter.submitList(feedList));

        final ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(position ->  mViewModel.removeFeed(position));
        final ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mBinding.recyclerView);

        return mBinding.getRoot();
    }

    public static String getCategory(Intent intent) {
        return intent.getStringExtra(EXTRA_CATEGORY);
    }

    private void returnResult(Category category) {
        final Intent intent = new Intent();
        intent.putExtra(EXTRA_CATEGORY, category.getTitle());

        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_category, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setupActionBar() {
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_feed:
                AddCategoryDialog dialog = AddCategoryDialog.newInstance();
                dialog.setTargetFragment(this, AddCategoryDialog.REQUEST_CODE);
                dialog.show(getFragmentManager(), AddCategoryDialog.TAG);
                return true;
            case android.R.id.home:
                returnResult(new Category(null));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == AddCategoryDialog.REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                mViewModel.addCategory(new Category(AddCategoryDialog.getCategoryTitle(data)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
