package org.assistant.sigma.ui.overview;

import org.assistant.sigma.BasePresenter;
import org.assistant.sigma.model.entities.TransactionCategory;
import org.assistant.sigma.model.entities.User;
import org.assistant.sigma.model.repositories.TransactionCategoriesRepository;
import org.assistant.sigma.model.repositories.TransactionsRepository;
import org.assistant.sigma.model.repositories.UsersRepository;
import org.assistant.sigma.utils.TimeUtils;

import java.util.Date;

import io.realm.RealmResults;

/**
 * Created by giovanni on 25/12/17.
 *
 */
public class OverviewPresenter implements BasePresenter {
    private ActOverview view;
    private UsersRepository usersRepository;
    private TransactionCategoriesRepository transCatRepository;
    private TransactionsRepository transactionsRepository;

    OverviewPresenter(ActOverview view) {
        this.view = view;
        this.usersRepository = new UsersRepository();
        this.transCatRepository = new TransactionCategoriesRepository();
        this.transactionsRepository = new TransactionsRepository();
    }

    @Override
    public void onDestroy() {
        usersRepository.onDestroy();
        transactionsRepository.destroy();
    }

    void computeCurrentLargePeriodSpent() {
        User user = usersRepository.activeUser();
        Date start = TimeUtils.getCurrentLargePeriodStart(user.getSettings());
        double totalSpent = transactionsRepository.spentSince(start, false);

        RealmResults<TransactionCategory> spentCats = transCatRepository.allSpent();
        for (final TransactionCategory category : spentCats) {
            double spent = transactionsRepository.spentSince(
                    start,
                    false,
                    category.getId()
            );

            final float percent = (float) ((spent/totalSpent) * 100);
            if (!view.isDestroyed() && percent > 0) {
                view.addSpent(category, percent, spent);
            }
        }
    }
}
