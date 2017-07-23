package org.assistant.sigma.categories.picker;

import android.support.annotation.Nullable;

import org.assistant.sigma.BasePresenter;
import org.assistant.sigma.BaseView;
import org.assistant.sigma.model.entities.TransactionCategory;

import java.util.List;

/**
 *
 * Created by giovanni on 22/07/17.
 */
interface CategoryPickerContract {

    interface CPickerPresenter extends BasePresenter {

        void onDestroy();

        void loadCategories(boolean income, @Nullable List<String> firsts,
                            @Nullable List<String> lasts);
    }

    interface CPickerView extends BaseView<CPickerPresenter> {

        void setOnCategorySelectedListener(OnCategorySelectedListener listener);

        void setLoadingAnimation(boolean isLoading);

        void showCategories(List<TransactionCategory> categories);
    }
}
