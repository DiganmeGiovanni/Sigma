package org.assistant.sigma.ui.scheduled_transactions.weekly.form;

import org.assistant.sigma.BasePresenter;
import org.assistant.sigma.BaseView;
import org.assistant.sigma.model.entities.Account;
import org.assistant.sigma.model.entities.ScheduledTransactionWeekly;
import org.assistant.sigma.model.entities.Transaction;

import io.realm.RealmResults;

/**
 * Created by giovanni on 17/10/17.
 *
 */
interface STWeeklyFormContract {

    interface Presenter extends BasePresenter {
        void loadAccounts();

        void saveTransaction(ScheduledTransactionWeekly transaction);

        void loadTransaction(String stWeeklyId);

        Transaction lastTransaction();
    }

    interface View extends BaseView<Presenter> {
        void renderAccounts(RealmResults<Account> accounts);

        void setupCategoriesPicker();

        void setupForm();

        void onSaveBtnClicked();

        boolean validateForm();

        void preloadTransaction(ScheduledTransactionWeekly stWeekly);
    }
}
