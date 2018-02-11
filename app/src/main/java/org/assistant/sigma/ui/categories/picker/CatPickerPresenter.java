package org.assistant.sigma.ui.categories.picker;

import android.support.annotation.Nullable;

import org.assistant.sigma.AbstractPresenter;
import org.assistant.sigma.model.dao.CategoriesDao;
import org.assistant.sigma.model.entities.TransactionCategory;

import java.util.List;

/**
 * Created by giovanni on 10/02/18.
 *
 */
public class CatPickerPresenter implements AbstractPresenter {
    private CatPickerContract.View view;
    private CategoriesDao categoriesDao;

    public CatPickerPresenter(CatPickerContract.View view) {
        this.view = view;
        categoriesDao = new CategoriesDao();
    }

    @Override
    public void onCreate() {
        categoriesDao.onCreate();
    }

    @Override
    public void onDestroy() {
        categoriesDao.onDestroy();
    }

    public void loadCategories(boolean income, @Nullable List<String> firsts,
                               @Nullable List<String> lasts) {
        view.toggleLoadingCategories(true);
        List<TransactionCategory> categories = categoriesDao.all(
                income,
                firsts,
                lasts
        );

        view.renderCategories(categories);
        view.toggleLoadingCategories(false);
    }
}
