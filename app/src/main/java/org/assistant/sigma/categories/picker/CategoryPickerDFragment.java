package org.assistant.sigma.categories.picker;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import org.assistant.sigma.R;
import org.assistant.sigma.databinding.FragCategoryPickerBinding;
import org.assistant.sigma.model.entities.TransactionCategory;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by giovanni on 22/07/17.
 */
public class CategoryPickerDFragment extends DialogFragment
        implements CategoryPickerContract.CPickerView {

    private CategoryPickerPresenter mPresenter;
    private FragCategoryPickerBinding viewBinding;

    private OnCategorySelectedListener onCategorySelectedListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_category_picker, container, false);
        viewBinding = FragCategoryPickerBinding.bind(rootView);

        // Load default categories
        loadSpendCategories();

        // Enable switch listener
        viewBinding
                .swPickupIncome
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (isChecked) {
                            List<String> lasts = new ArrayList<>();
                            lasts.add(getString(R.string.category_name_other));

                            mPresenter.loadCategories(true, null, lasts);
                        } else {
                            loadSpendCategories();
                        }
                    }
                });

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        if (window != null) {
            window.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
            );
        }
    }

    @Override
    public void setPresenter(CategoryPickerContract.CPickerPresenter mPresenter) {
        this.mPresenter = (CategoryPickerPresenter) mPresenter;
    }

    @Override
    public void setOnCategorySelectedListener(OnCategorySelectedListener listener) {
        this.onCategorySelectedListener = listener;
    }

    @Override
    public void setLoadingAnimation(boolean isLoading) {
        if (isLoading) {
            viewBinding.fblCategories.setVisibility(View.GONE);
            viewBinding.pbLoadingCategories.setVisibility(View.GONE);
        } else {
            viewBinding.pbLoadingCategories.setVisibility(View.GONE);
            viewBinding.fblCategories.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showCategories(List<TransactionCategory> categories) {
        viewBinding.fblCategories.removeAllViews();

        int dp10 = (int) getResources().getDimension(R.dimen.dp10);
        int dp16 = (int) getResources().getDimension(R.dimen.dp16);
        int blueDark = ContextCompat.getColor(getContext(), R.color.blue_dark);
        FlexboxLayout.LayoutParams lParams = new FlexboxLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        lParams.setMargins(0, 0, dp16, dp16);

        for (final TransactionCategory category : categories) {
            final TextView textView = new TextView(getContext());
            textView.setBackgroundResource(R.drawable.background_category);
            textView.setLayoutParams(lParams);
            textView.setPadding(dp10, dp10, dp10, dp10);
            textView.setTextColor(blueDark);
            textView.setText(category.getName());
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCategorySelected(category, textView);
                }
            });

            viewBinding.fblCategories.addView(textView);
        }
    }

    private void loadSpendCategories() {
        List<String> firsts = new ArrayList<>();
        firsts.add(getString(R.string.category_name_provisions));

        List<String> lasts = new ArrayList<>();
        lasts.add(getString(R.string.category_name_other));

        mPresenter.loadCategories(false, firsts, lasts);
    }

    private void onCategorySelected(TransactionCategory category, TextView textView) {
        int grayMedium = ContextCompat.getColor(getContext(), R.color.gray_medium);
        Drawable background = ContextCompat.getDrawable(
                getContext(),
                R.drawable.background_category
        );

        textView.setTextColor(grayMedium);
        textView.setBackground(background);

        onCategorySelectedListener.onCategorySelected(category);
        dismiss();
    }
}
