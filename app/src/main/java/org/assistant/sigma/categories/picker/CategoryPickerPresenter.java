package org.assistant.sigma.categories.picker;

import android.support.annotation.Nullable;

import org.assistant.sigma.model.entities.TransactionCategory;
import org.assistant.sigma.model.repositories.TransactionCategoriesRepository;

import java.util.List;

/**
 *
 * Created by giovanni on 22/07/17.
 */
public class CategoryPickerPresenter implements CategoryPickerContract.CPickerPresenter {

    private CategoryPickerContract.CPickerView mView;
    private TransactionCategoriesRepository transactionCategoriesRepository;

    public CategoryPickerPresenter(CategoryPickerContract.CPickerView mView) {
        this.mView = mView;
        this.mView.setPresenter(this);

        transactionCategoriesRepository = new TransactionCategoriesRepository();
    }

    @Override
    public void start() { }

    @Override
    public void onDestroy() {
        transactionCategoriesRepository.onDestroy();
    }

    @Override
    public void loadCategories(boolean income, @Nullable List<String> firsts,
                               @Nullable List<String> lasts) {
        mView.setLoadingAnimation(true);
        List<TransactionCategory> categories = transactionCategoriesRepository.all(
                income,
                firsts,
                lasts
        );

        mView.showCategories(categories);
        mView.setLoadingAnimation(false);
    }
}
