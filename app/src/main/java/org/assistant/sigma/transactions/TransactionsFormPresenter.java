package org.assistant.sigma.transactions;

import org.assistant.sigma.model.entities.Transaction;
import org.assistant.sigma.model.repositories.AccountsRepository;
import org.assistant.sigma.model.repositories.TransactionCategoriesRepository;

/**
 *
 * Created by giovanni on 6/05/17.
 */
public class TransactionsFormPresenter implements TransactionsFormContract.Presenter {

    private TransactionsFormContract.View mTransactionsFormView;

    private AccountsRepository accountsRepository;
    private TransactionCategoriesRepository categoriesRepository;

    public TransactionsFormPresenter(TransactionsFormContract.View mTransactionsFormView) {
        this.mTransactionsFormView = mTransactionsFormView;
        mTransactionsFormView.setPresenter(this);

        accountsRepository = new AccountsRepository();
        categoriesRepository = new TransactionCategoriesRepository();
    }

    @Override
    public void start() { }

    @Override
    public void loadAccounts() {
        mTransactionsFormView.updateAccountsSpinner(accountsRepository.allActive());
    }

    @Override
    public void loadSpentCategories() {
        mTransactionsFormView.updateCategoriesSpinner(categoriesRepository.allSpentCategories());
    }

    @Override
    public void loadIncomeCategories() {
        mTransactionsFormView.updateCategoriesSpinner(categoriesRepository.allIncomeCategories());
    }

    @Override
    public void saveTransaction(Transaction transaction) {

    }
}
