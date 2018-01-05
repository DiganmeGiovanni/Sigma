package org.assistant.sigma.ui.overview;

import org.assistant.sigma.BasePresenter;
import org.assistant.sigma.model.entities.Settings;
import org.assistant.sigma.model.entities.TransactionCategory;
import org.assistant.sigma.model.repositories.TransactionCategoriesRepository;
import org.assistant.sigma.model.repositories.TransactionsRepository;
import org.assistant.sigma.model.repositories.UsersRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.realm.RealmResults;

/**
 * Created by giovanni on 25/12/17.
 *
 */
public class OverviewPresenter implements BasePresenter {
    private UsersRepository usersRepository;
    private TransactionCategoriesRepository transCatRepository;
    private TransactionsRepository transactionsRepository;

    OverviewPresenter() {
        this.usersRepository = new UsersRepository();
        this.transCatRepository = new TransactionCategoriesRepository();
        this.transactionsRepository = new TransactionsRepository();
    }

    @Override
    public void onDestroy() {
        usersRepository.onDestroy();
        transactionsRepository.destroy();
        transCatRepository.onDestroy();
    }

    void calcIncomeSince(Date start, FragOverview view) {
        double income = transactionsRepository.incomeSince(start, false);
        view.renderIncome(income);
    }

    void calcSpentSince(Date start, FragOverview view) {
        double spent = transactionsRepository.spentSince(start, false);
        view.renderSpent(spent);
    }

    void calcSpentByCategorySince(Date start, FragOverview view) {
        double totalSpent = transactionsRepository.spentSince(start, false);

        // All categories with spent = 0 will be appended after categories with spent > 0
        Map<TransactionCategory, Double> zeroSpent = new HashMap<>();
        Map<TransactionCategory, Double> nonZeroSpent = new HashMap<>();

        // Calculates spent for each category and render categories with spent > 0
        RealmResults<TransactionCategory> spentCats = transCatRepository.allSpent();
        for (final TransactionCategory category : spentCats) {
            double spent = transactionsRepository.spentSince(
                    start,
                    false,
                    category.getId()
            );

            if (spent <= 0) {
                zeroSpent.put(category, spent);
            } else {
                nonZeroSpent.put(category, spent);
            }
        }

        // Show empty message if there is not categories with spent
        if (nonZeroSpent.size() == 0) {
            view.showEmptyState();
        } else {
            // Render categories with spent > 0
            for (Map.Entry<TransactionCategory, Double> entry : nonZeroSpent.entrySet()) {
                TransactionCategory category = entry.getKey();
                Double spent = entry.getValue();
                float percent = (float) ((spent/totalSpent) * 100);
                view.renderSpentForCategory(category, spent, percent);
            }

            // Render categories with spent = 0
            for (Map.Entry<TransactionCategory, Double> entry : zeroSpent.entrySet()) {
                TransactionCategory category = entry.getKey();
                Double spent = entry.getValue();
                float percent = (float) ((spent/totalSpent) * 100);
                view.renderSpentForCategory(category, spent, percent);
            }
        }

    }

    Settings getCurrentUserSettings() {
        return usersRepository.activeUser().getSettings();
    }
}
