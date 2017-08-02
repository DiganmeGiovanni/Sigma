package org.assistant.sigma.accounts.form;

import org.assistant.sigma.model.entities.Account;
import org.assistant.sigma.model.entities.User;
import org.assistant.sigma.model.repositories.AccountsRepository;
import org.assistant.sigma.model.repositories.UsersRepository;

/**
 *
 * Created by giovanni on 5/05/17.
 */
public class AccountsFormPresenter implements AccountsFormContract.Presenter {

    private AccountsRepository accountsRepository;
    private UsersRepository usersRepository;

    public AccountsFormPresenter(AccountsFormContract.View mAccountsFormView) {
        mAccountsFormView.setPresenter(this);

        accountsRepository = new AccountsRepository();
        usersRepository = new UsersRepository();
    }

    @Override
    public void onDestroy() {
        accountsRepository.destroy();
        usersRepository.onDestroy();
    }

    @Override
    public void saveAccount(Account account) {
        User user = usersRepository.activeUser();
        accountsRepository.save(account, user);
    }
}
