package org.assistant.sigma.ui.categories.picker;

import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import org.assistant.sigma.R;
import org.assistant.sigma.model.entities.TransactionCategory;
import org.assistant.sigma.ui.util.CategoryStylesProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by giovanni on 10/02/18.
 *
 */
public class CatPickerView implements CatPickerContract.View {
    private CatPickerPresenter mPresenter;
    private View rootView;
    private OnCategorySelectedListener listener;
    private Map<Integer, TextView> catPickerItems = new HashMap<>();

    private FlexboxLayout fblCategories;
    private ProgressBar pbLoadingCats;
    private SwitchCompat swPickupIncome;

    public CatPickerView(View rootView, OnCategorySelectedListener listener) {
        this.rootView = rootView;
        this.listener = listener;
    }

    @Override
    public void toggleLoadingCategories(boolean isLoading) {
        if (isLoading) {
            this.fblCategories.setVisibility(View.GONE);
            this.pbLoadingCats.setVisibility(View.VISIBLE);
        } else {
            this.pbLoadingCats.setVisibility(View.GONE);
            this.fblCategories.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void renderCategories(List<TransactionCategory> categories) {
        fblCategories.removeAllViews();
        catPickerItems.clear();

        int dp16 = (int) rootView.getContext().getResources().getDimension(R.dimen.dp16);
        FlexboxLayout.LayoutParams lParams = new FlexboxLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        lParams.setMargins(0, 0, dp16, dp16);

        for (final TransactionCategory category : categories) {
            final TextView textView = CategoryStylesProvider.makeCatPickerItem(
                    rootView.getContext(),
                    category,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onCategorySelected(category);
                        }
                    }
            );
            textView.setLayoutParams(lParams);

            fblCategories.addView(textView);
            catPickerItems.put(category.getId(), textView);
        }
    }

    public void setPresenter(CatPickerPresenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    public void init() {
        bindView();

        // Listen for switch changes
        this.swPickupIncome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    loadIncomeCategories();
                } else {
                    loadSpendCategories();
                }
            }
        });

        loadSpendCategories();
    }

    private void bindView() {
        fblCategories = rootView.findViewById(R.id.fbl_categories);
        pbLoadingCats = rootView.findViewById(R.id.pb_loading_categories);
        swPickupIncome = rootView.findViewById(R.id.sw_pickup_income);
    }

    private void loadIncomeCategories() {
        List<String> lasts = new ArrayList<>();
        lasts.add(rootView.getContext().getString(R.string.category_name_other));

        mPresenter.loadCategories(true, null, lasts);
    }

    private void loadSpendCategories() {
        List<String> firsts = new ArrayList<>();
        firsts.add(rootView.getContext().getString(R.string.category_name_provisions));

        List<String> lasts = new ArrayList<>();
        lasts.add(rootView.getContext().getString(R.string.category_name_other));

        mPresenter.loadCategories(false, firsts, lasts);
    }

    private void onCategorySelected(TransactionCategory category) {
        for (Map.Entry<Integer, TextView> entry : catPickerItems.entrySet()) {
            if (entry.getKey() != category.getId()) {
                CategoryStylesProvider.unSelectCatPickerItem(entry.getValue());
            }
        }

        listener.onCategorySelected(category);
    }

    public void selectCategory(TransactionCategory category) {
        for (Map.Entry<Integer, TextView> entry : catPickerItems.entrySet()) {
            if (entry.getKey() != category.getId()) {
                CategoryStylesProvider.unSelectCatPickerItem(entry.getValue());
            } else {
                CategoryStylesProvider.selectCatPickerItem(category, entry.getValue());
            }
        }

        listener.onCategorySelected(category);
    }
}
