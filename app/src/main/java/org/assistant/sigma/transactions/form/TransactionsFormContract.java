package org.assistant.sigma.transactions.form;

import org.assistant.sigma.BasePresenter;
import org.assistant.sigma.BaseView;
import org.assistant.sigma.model.entities.Account;
import org.assistant.sigma.model.entities.Transaction;

import io.realm.RealmResults;

/**
 *
 * Created by giovanni on 6/05/17.
 */
interface TransactionsFormContract {

    interface Presenter extends BasePresenter {

        void onDestroy();

        void loadAccounts();

        void saveTransaction(Transaction transaction);
    }

    interface View extends BaseView<Presenter> {

        void updateAccountsSpinner(RealmResults<Account> accounts);

        void setupCategoriesPicker();

        void setupForm();

        void onSaveBtnClicked();

        boolean validateForm();
    }
}
