package org.assistant.sigma.model.catalogs;

import org.assistant.sigma.model.entities.TransactionCategory;

/**
 * Created by giovanni on 28/09/17.
 *
 */
public class DefaultTransactionCategories {

    public static final int ID_PROVISIONS = 1;
    public static final int ID_BAR = 2;
    public static final int ID_HOME = 3;
    public static final int ID_RESTAURANT = 4;
    public static final int ID_CLOTHES = 5;
    public static final int ID_TRANSPORT = 6;
    public static final int ID_OTHER_SPENT = 7;

    public static final int ID_SALARY = 8;
    public static final int ID_OTHER_INCOME = 9;

    public static boolean isDefault(TransactionCategory category) {
        return category.getId() >= 1 && category.getId() <= 9;
    }
}
