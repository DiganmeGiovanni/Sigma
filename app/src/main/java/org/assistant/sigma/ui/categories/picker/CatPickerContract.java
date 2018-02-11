package org.assistant.sigma.ui.categories.picker;

import org.assistant.sigma.model.entities.TransactionCategory;

import java.util.List;

/**
 * Created by giovanni on 10/02/18.
 *
 */
public interface CatPickerContract {

    interface View {
        void toggleLoadingCategories(boolean isLoading);

        void renderCategories(List<TransactionCategory> categories);
    }
}
