package com.object173.newsfeed.features.category.presentation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedListAdapter;

import com.object173.newsfeed.R;
import com.object173.newsfeed.features.base.model.local.Category;
import com.object173.newsfeed.features.base.presentation.BaseListFragment;
import com.object173.newsfeed.features.base.presentation.BaseListFragmentViewModel;

public class CategoryListFragment extends BaseListFragment<Category, CategoryPagedAdapter.CategoryViewHolder> {

    static CategoryListFragment newInstance() {
        return new CategoryListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected BaseListFragmentViewModel<Category> getViewModel() {
        return ViewModelProviders.of(this, new CategoryListViewModelFactory(getActivity().getApplication()))
                .get(CategoryListViewModel.class);
    }

    @Override
    protected PagedListAdapter<Category, CategoryPagedAdapter.CategoryViewHolder> getPagedAdapter() {
        return new CategoryPagedAdapter(this);
    }

    @Override
    protected int getRemoveMessage() {
        return R.string.remove_category_message;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_category, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull final MenuItem item) {
        if (item.getItemId() == R.id.add_feed) {
            if(getActivity() == null) {
                return true;
            }
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            AddCategoryDialog dialog = AddCategoryDialog.newInstance();
            dialog.setTargetFragment(this, AddCategoryDialog.REQUEST_CODE);
            dialog.show(fragmentManager, AddCategoryDialog.TAG);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == AddCategoryDialog.REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK && data != null) {
                mViewModel.addData(new Category(AddCategoryDialog.getCategoryTitle(data)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
