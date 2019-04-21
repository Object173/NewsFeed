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
import com.object173.newsfeed.features.base.domain.model.local.Category;
import com.object173.newsfeed.features.base.presentation.BaseListFragment;
import com.object173.newsfeed.features.base.presentation.BaseListFragmentViewModel;
import com.object173.newsfeed.features.base.presentation.ItemTouchHelperCallback;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryListFragment extends BaseListFragment<Category, CategoryPagedAdapter.CategoryViewHolder> {

    private static final String EXTRA_CATEGORY = "category";

    public static CategoryListFragment newInstance() {
        return new CategoryListFragment();
    }

    @Override
    protected BaseListFragmentViewModel<Category> getViewModel() {
        return ViewModelProviders.of(this, new CategoryListViewModelFactory(getActivity().getApplication()))
                .get(CategoryListViewModel.class);
    }

    @Override
    protected PagedListAdapter<Category, CategoryPagedAdapter.CategoryViewHolder> getPagedAdapter() {
        return new CategoryPagedAdapter(this::returnResult);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setupActionBar();
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
                mViewModel.addData(new Category(AddCategoryDialog.getCategoryTitle(data)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
