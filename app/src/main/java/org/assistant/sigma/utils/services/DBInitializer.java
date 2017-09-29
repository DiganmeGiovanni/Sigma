package org.assistant.sigma.utils.services;

import android.content.Context;

import org.assistant.sigma.R;
import org.assistant.sigma.model.catalogs.DefaultTransactionCategories;
import org.assistant.sigma.model.entities.TransactionCategory;
import org.assistant.sigma.model.repositories.TransactionCategoriesRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giovanni on 28/09/17.
 *
 */
public class DBInitializer {

    public static void init(Context mContext) {
        upsertTransactionCategories(mContext);
    }

    private static void upsertTransactionCategories(Context mContext) {
        List<TransactionCategory> categories = new ArrayList<>();

        TransactionCategory provisions = new TransactionCategory();
        provisions.setId(DefaultTransactionCategories.ID_PROVISIONS);
        provisions.setIncomeCategory(false);
        provisions.setName(mContext.getString(R.string.category_name_provisions));
        provisions.setDescription(mContext.getString(R.string.category_description_provisions));
        categories.add(provisions);

        TransactionCategory transport = new TransactionCategory();
        transport.setId(DefaultTransactionCategories.ID_TRANSPORT);
        transport.setIncomeCategory(false);
        transport.setName(mContext.getString(R.string.category_name_transport));
        transport.setDescription(mContext.getString(R.string.category_description_transport));
        categories.add(transport);

        TransactionCategory clothes = new TransactionCategory();
        clothes.setId(DefaultTransactionCategories.ID_CLOTHES);
        clothes.setIncomeCategory(false);
        clothes.setName(mContext.getString(R.string.category_name_clothes_shoes));
        clothes.setDescription(mContext.getString(R.string.category_description_clother_shoes));
        categories.add(clothes);

        TransactionCategory home = new TransactionCategory();
        home.setId(DefaultTransactionCategories.ID_HOME);
        home.setIncomeCategory(false);
        home.setName(mContext.getString(R.string.category_name_home));
        home.setDescription(mContext.getString(R.string.category_description_home));
        categories.add(home);

        TransactionCategory restaurants = new TransactionCategory();
        restaurants.setId(DefaultTransactionCategories.ID_RESTAURANT);
        restaurants.setIncomeCategory(false);
        restaurants.setName(mContext.getString(R.string.category_name_restaurants));
        restaurants.setDescription(mContext.getString(R.string.category_description_restaurants));
        categories.add(restaurants);

        TransactionCategory bar = new TransactionCategory();
        bar.setId(DefaultTransactionCategories.ID_BAR);
        bar.setIncomeCategory(false);
        bar.setName(mContext.getString(R.string.category_name_bar));
        bar.setDescription(mContext.getString(R.string.category_description_bar));
        categories.add(bar);

        TransactionCategory otherSpend = new TransactionCategory();
        otherSpend.setId(DefaultTransactionCategories.ID_OTHER_SPENT);
        otherSpend.setIncomeCategory(false);
        otherSpend.setName(mContext.getString(R.string.category_name_other));
        otherSpend.setDescription(mContext.getString(R.string.category_description_other));
        categories.add(otherSpend);


        TransactionCategory salary = new TransactionCategory();
        salary.setId(DefaultTransactionCategories.ID_SALARY);
        salary.setIncomeCategory(true);
        salary.setName(mContext.getString(R.string.category_name_salary));
        salary.setDescription(mContext.getString(R.string.category_description_salary));

        TransactionCategory otherIncome = new TransactionCategory();
        otherIncome.setId(DefaultTransactionCategories.ID_OTHER_INCOME);
        otherIncome.setIncomeCategory(true);
        otherIncome.setName(mContext.getString(R.string.category_name_other));
        otherIncome.setDescription(mContext.getString(R.string.category_description_other));

        // Upsert categories
        TransactionCategoriesRepository categoriesRepository = new TransactionCategoriesRepository();
        categoriesRepository.upsert(categories);
        categoriesRepository.onDestroy();
    }
}
