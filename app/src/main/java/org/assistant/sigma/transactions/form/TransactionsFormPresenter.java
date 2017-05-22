package org.assistant.sigma.transactions.form;

import android.content.Context;

import org.assistant.sigma.model.entities.Transaction;
import org.assistant.sigma.model.repositories.AccountsRepository;
import org.assistant.sigma.model.repositories.TransactionCategoriesRepository;
import org.assistant.sigma.model.repositories.TransactionsRepository;

/**
 *
 * Created by giovanni on 6/05/17.
 */
public class TransactionsFormPresenter implements TransactionsFormContract.Presenter {

    private TransactionsFormContract.View mTransactionsFormView;

    private AccountsRepository accountsRepository;
    private TransactionsRepository transactionsRepository;
    private TransactionCategoriesRepository categoriesRepository;

    public TransactionsFormPresenter(TransactionsFormContract.View mTransactionsFormView) {
        this.mTransactionsFormView = mTransactionsFormView;
        mTransactionsFormView.setPresenter(this);

        accountsRepository = new AccountsRepository();
        transactionsRepository = new TransactionsRepository();
        categoriesRepository = new TransactionCategoriesRepository();
    }

    @Override
    public void start() { }

    @Override
    public void loadAccounts() {
        mTransactionsFormView.updateAccountsSpinner(accountsRepository.allActive());
    }

    @Override
    public void loadSpentCategories(Context mContext) {
        mTransactionsFormView
                .updateCategoriesSpinner(categoriesRepository.allSpentCategories(mContext));
    }

    @Override
    public void loadIncomeCategories() {
        mTransactionsFormView.updateCategoriesSpinner(categoriesRepository.allIncomeCategories());
    }

    @Override
    public void saveTransaction(Transaction transaction) {
        double balance = accountsRepository.currentBalance(transaction.getAccount());
        transaction.setCurrentAccountBalance(balance + transaction.getQuantity());

        transactionsRepository.insert(transaction);
    }
}
