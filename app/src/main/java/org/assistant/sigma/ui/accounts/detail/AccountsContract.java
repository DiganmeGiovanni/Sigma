package org.assistant.sigma.ui.accounts.detail;

import org.assistant.sigma.model.entities.Account;

import io.realm.RealmResults;

/**
 * Created by giovanni on 13/02/18.
 *
 */
public interface AccountsContract {
    interface View {
        void renderAccounts(RealmResults<Account> accounts);

        void toggleLoading(boolean isLoading);
    }
}
