package org.assistant.sigma.ui.transactions;

import org.assistant.sigma.model.entities.Account;
import org.assistant.sigma.model.entities.Transaction;
import org.assistant.sigma.model.repositories.AccountsRepository;
import org.assistant.sigma.model.repositories.TransactionsRepository;

import io.realm.RealmResults;

/**
 * Created by giovanni on 24/12/17.
 *
 */
public class TransactionsPresenter {
    private AccountsRepository accountsRepository;
    private TransactionsRepository transactionsRepository;

    TransactionsPresenter() {
        accountsRepository = new AccountsRepository();
        transactionsRepository = new TransactionsRepository();
    }

    public void onDestroy() {
        accountsRepository.destroy();
    }

    public RealmResults<Account> getAccounts() {
        return accountsRepository.allActive();
    }

    public RealmResults<Transaction> getTransactions(String accountId) {
        return transactionsRepository.findAccountTransactions(accountId);
    }
}
