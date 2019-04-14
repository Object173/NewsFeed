package com.object173.newsfeed.features.category.presentation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.object173.newsfeed.R;
import com.object173.newsfeed.databinding.AddCategoryDialogBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

public class AddCategoryDialog extends DialogFragment {

    static final String TAG = "AddCategoryDialog";
    static final int REQUEST_CODE = 173;

    private static final String EXTRA_CATEGORY = "category_title";
    private AddCategoryDialogBinding mBinding;

    public static AddCategoryDialog newInstance() {
        return new AddCategoryDialog();
    }

    static String getCategoryTitle(final Intent intent) {
        return intent.getStringExtra(EXTRA_CATEGORY);
    }

    private void sendResult(final int resultCode, final String title) {
        final Intent intent = new Intent();
        intent.putExtra(EXTRA_CATEGORY, title);

        getTargetFragment().onActivityResult(REQUEST_CODE, resultCode, intent);
        this.dismiss();
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.add_category_dialog, container, false);

        getDialog().setTitle(getString(R.string.dialog_add_category_title));
        mBinding.addButton.setOnClickListener(v -> sendResult(Activity.RESULT_OK, mBinding.titleET.getText().toString()));

        return mBinding.getRoot();
    }
}
