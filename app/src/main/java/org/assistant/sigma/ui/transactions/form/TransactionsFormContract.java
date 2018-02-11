package org.assistant.sigma.ui.transactions.form;

import org.assistant.sigma.model.entities.Account;
import org.assistant.sigma.model.entities.Transaction;

import io.realm.RealmResults;

/**
 * Created by giovanni on 10/02/18.
 *
 */
public interface TransactionsFormContract {

    interface View {
        void preloadTransaction(Transaction transaction);

        void renderAccounts(RealmResults<Account> accounts);

        void toggleLoading(boolean isLoading);
    }
}
