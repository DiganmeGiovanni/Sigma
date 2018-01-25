package org.assistant.sigma.ui.accounts.form;

import org.assistant.sigma.BasePresenter;
import org.assistant.sigma.BaseView;
import org.assistant.sigma.model.entities.Account;

/**
 *
 * Created by giovanni on 5/05/17.
 */
interface AccountsFormContract {

    interface Presenter extends BasePresenter {

        void saveAccount(Account account);
    }

    interface View extends BaseView<Presenter> {
        boolean validateFields();

        void setupSaveBtn();
    }
}
