package org.assistant.sigma.model.repositories;

import org.assistant.sigma.model.entities.TransactionCategory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 *
 * Created by giovanni on 6/05/17.
 */
public class TransactionCategoriesRepository {
    private HashMap<String, String> categories = new HashMap<>();
    private Realm realm = Realm.getDefaultInstance();

    public RealmResults<TransactionCategory> allSpentCategories() {
        return realm.where(TransactionCategory.class).equalTo("incomeCategory", false).findAll();
    }

    public RealmResults<TransactionCategory> allIncomeCategories() {
        return realm.where(TransactionCategory.class).equalTo("incomeCategory", true).findAll();
    }

    /**
     * Check that all given categories exists on database, the categories
     * that does not exists in database will be created
     * @param rawSpentCategories All spent categories, entry key is name and value is
     *                           the description
     * @param rawIncomesCategories All incomes categories, entry key is name and value is
     *                             the description
     */
    public void ensureCategoriesExistence(Map<String, String> rawSpentCategories,
                                          Map<String, String> rawIncomesCategories) {
        Map<String, String> notFoundCategories = new HashMap<>();

        //
        // Verify spent categories
        for (Map.Entry<String, String> entry : rawSpentCategories.entrySet()) {
            TransactionCategory transactionCategory = realm
                    .where(TransactionCategory.class)
                    .equalTo("name", entry.getKey())
                    .equalTo("incomeCategory", false)
                    .findFirst();
            if (transactionCategory == null) {
                notFoundCategories.put(entry.getKey(), entry.getValue());
            }
        }
        if (notFoundCategories.size() > 0) {
            realm.beginTransaction();
            for (Map.Entry<String, String> entry : notFoundCategories.entrySet()) {
                TransactionCategory category = realm.createObject(
                        TransactionCategory.class,
                        UUID.randomUUID().toString()
                );
                category.setIncomeCategory(false);
                category.setName(entry.getKey());
                category.setDescription(entry.getValue());
            }
            realm.commitTransaction();
        }

        //
        // Verify incomes categories
        notFoundCategories = new HashMap<>();
        for (Map.Entry<String, String> entry : rawIncomesCategories.entrySet()) {
            TransactionCategory transactionCategory = realm
                    .where(TransactionCategory.class)
                    .equalTo("name", entry.getKey())
                    .equalTo("incomeCategory", true)
                    .findFirst();
            if (transactionCategory == null) {
                notFoundCategories.put(entry.getKey(), entry.getValue());
            }
        }
        if (notFoundCategories.size() > 0) {
            realm.beginTransaction();
            for (Map.Entry<String, String> entry : notFoundCategories.entrySet()) {
                TransactionCategory category = realm.createObject(
                        TransactionCategory.class,
                        UUID.randomUUID().toString()
                );
                category.setIncomeCategory(true);
                category.setName(entry.getKey());
                category.setDescription(entry.getValue());
            }
            realm.commitTransaction();
        }
    }
}
