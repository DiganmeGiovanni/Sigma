package org.assistant.sigma.accounts.list;

import org.assistant.sigma.model.entities.Account;
import org.assistant.sigma.model.entities.User;
import org.assistant.sigma.model.repositories.AccountsRepository;
import org.assistant.sigma.model.repositories.UsersRepository;

import io.realm.RealmChangeListener;
import io.realm.RealmList;

/**
 *
 * Created by giovanni on 5/05/17.
 */
public class AccountsListPresenter implements AccountsListContract.Presenter {

    private final AccountsListContract.View mAccountsListView;
    private AccountsRepository accountsRepository;
    private UsersRepository usersRepository;

    public AccountsListPresenter(AccountsListContract.View mAccountsListView) {
        this.mAccountsListView = mAccountsListView;
        mAccountsListView.setPresenter(this);

        accountsRepository = new AccountsRepository();
        usersRepository = new UsersRepository();
    }

    @Override
    public void onDestroy() {
        accountsRepository.destroy();
        usersRepository.onDestroy();
    }

    @Override
    public void loadAccounts() {
        User user = usersRepository.activeUser();
        RealmList<Account> accounts = accountsRepository.userAccounts(user);
        accounts.addChangeListener(new RealmChangeListener<RealmList<Account>>() {
            @Override
            public void onChange(RealmList<Account> sameAccounts) {
                mAccountsListView.notifyAccountsListChanged();
            }
        });

        mAccountsListView.updateAccountsList(accounts);
    }
}
