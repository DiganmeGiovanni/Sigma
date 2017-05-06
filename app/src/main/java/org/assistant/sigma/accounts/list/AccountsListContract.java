package org.assistant.sigma.accounts.list;

import org.assistant.sigma.BasePresenter;
import org.assistant.sigma.BaseView;
import org.assistant.sigma.model.entities.Account;

import io.realm.RealmList;

/**
 *
 * Created by giovanni on 5/05/17.
 */
interface AccountsListContract {

    interface Presenter extends BasePresenter {

        void loadAccounts();
    }

    interface View extends BaseView<Presenter> {
        void goToAccountDetails();

        void goToNewAccount();

        void updateAccountsList(RealmList<Account> accounts);
    }
}
