package org.assistant.sigma.model.repositories;

import android.support.annotation.Nullable;

import org.assistant.sigma.model.entities.TransactionCategory;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 *
 * Created by giovanni on 6/05/17.
 */
public class TransactionCategoriesRepository extends RealmRepository {

    /**
     * Returns all categories of given type sorted by name
     *
     * @param income If true, only categories of 'income' type will be returned
     * @param firsts Categories in this list will be placed as first results
     * @param lasts Categories in this list will be placed as last results
     * @return Spend categories
     */
    public List<TransactionCategory> all(boolean income, @Nullable List<String> firsts,
                                         @Nullable List<String> lasts) {

        // Find first categories
        List<TransactionCategory> firstCategories = new ArrayList<>();
        if (firsts != null) {
            for (String first : firsts) {
                TransactionCategory category = realm.where(TransactionCategory.class)
                        .equalTo("incomeCategory", income)
                        .equalTo("name", first)
                        .findFirst();
                if (category != null) {
                    firstCategories.add(category);
                }
            }
        }

        // Find last categories
        List<TransactionCategory> lastCategories = new ArrayList<>();
        if (lasts != null) {
            for (String last : lasts) {
                TransactionCategory category = realm.where(TransactionCategory.class)
                        .equalTo("incomeCategory", income)
                        .equalTo("name", last)
                        .findFirst();
                if (category != null) {
                    lastCategories.add(category);
                }
            }
        }

        // Find categories
        String[] firstsLasts = new String[0];
        if (firsts != null) {
            if (lasts != null) {
                firsts.addAll(lasts);
            }

            firstsLasts = firsts.toArray(new String[0]);
        } else if (lasts != null) {
            firstsLasts = lasts.toArray(new String[0]);
        }
        RealmQuery<TransactionCategory> query = realm.where(TransactionCategory.class);
        if (firstsLasts.length > 0) {
            query.not().in("name", firstsLasts);
        }
        RealmResults<TransactionCategory> categories = query
                .equalTo("incomeCategory", income)
                .findAll()
                .sort("name", Sort.ASCENDING);


        // Sorted categories
        List<TransactionCategory> sortedCategories = new ArrayList<>();
        sortedCategories.addAll(firstCategories);
        for (TransactionCategory category : categories) {
            sortedCategories.add(category);
        }
        sortedCategories.addAll(lastCategories);

        return sortedCategories;
    }

    public void upsert(List<TransactionCategory> categories) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(categories);
        realm.commitTransaction();
    }

    public RealmResults<TransactionCategory> allSpent() {
        return realm.where(TransactionCategory.class)
                .equalTo("incomeCategory", false)
                .findAllSorted("id");
    }
}
