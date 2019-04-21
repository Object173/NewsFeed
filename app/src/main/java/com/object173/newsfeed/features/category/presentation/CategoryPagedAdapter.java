package com.object173.newsfeed.features.category.presentation;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.object173.newsfeed.R;
import com.object173.newsfeed.databinding.ItemCategoryListBinding;
import com.object173.newsfeed.features.base.model.local.Category;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryPagedAdapter extends PagedListAdapter<Category, CategoryPagedAdapter.CategoryViewHolder> {

    private final OnCategoryClickListener mCategoryClickListener;

    public interface OnCategoryClickListener {
        void onClick(Category category);
    }

    CategoryPagedAdapter(OnCategoryClickListener categoryClickListener) {
        super(new CategoryDiffUtilCallback());
        this.mCategoryClickListener = categoryClickListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        final ItemCategoryListBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.item_category_list, viewGroup, false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryViewHolder categoryViewHolder, final int position) {
        categoryViewHolder.bindCategory(getItem(position));
        categoryViewHolder.itemView.setOnClickListener(v -> mCategoryClickListener.onClick(getItem(position)));
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {

        private ItemCategoryListBinding mBinding;

        CategoryViewHolder(final ItemCategoryListBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bindCategory(final Category category) {
            mBinding.setCategory(category);
            mBinding.executePendingBindings();
        }
    }

    private static class CategoryDiffUtilCallback extends DiffUtil.ItemCallback<Category> {

        @Override
        public boolean areItemsTheSame(@NonNull final Category oldItem, @NonNull final Category newItem) {
            return oldItem.getTitle().equals(newItem.getTitle());
        }

        @Override
        public boolean areContentsTheSame(@NonNull final Category oldItem, @NonNull final Category newItem) {
            return oldItem.getTitle().equals(newItem.getTitle());
        }
    }
}
