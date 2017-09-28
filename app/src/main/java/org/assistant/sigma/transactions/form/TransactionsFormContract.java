package org.assistant.sigma.transactions.form;

import org.assistant.sigma.BasePresenter;
import org.assistant.sigma.BaseView;
import org.assistant.sigma.model.entities.Account;
import org.assistant.sigma.model.entities.Transaction;
import org.assistant.sigma.utils.callbacks.CBGeneric;

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

        void updateTransaction(Transaction transaction, CBGeneric<Boolean> callback);

        void loadTransaction(String transactionId);
    }

    interface View extends BaseView<Presenter> {

        void updateAccountsSpinner(RealmResults<Account> accounts);

        void setupCategoriesPicker();

        void setupForm();

        void onSaveBtnClicked();

        boolean validateForm();

        void preloadTransaction(Transaction transaction);
    }
}
