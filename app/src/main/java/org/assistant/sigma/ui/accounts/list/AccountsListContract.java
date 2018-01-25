package org.assistant.sigma.ui.accounts.list;

import org.assistant.sigma.model.entities.Account;

import io.realm.RealmList;

/**
 *
 * Created by giovanni on 5/05/17.
 */
interface AccountsListContract {

    interface View {
        void goToAccountDetails(String accountId);

        void goToNewAccount();

        void notifyAccountsListChanged();

        void updateAccountsList(RealmList<Account> accounts);
    }
}
