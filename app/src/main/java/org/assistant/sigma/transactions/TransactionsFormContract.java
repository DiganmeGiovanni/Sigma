package org.assistant.sigma.transactions;

import android.content.Context;

import org.assistant.sigma.BasePresenter;
import org.assistant.sigma.BaseView;
import org.assistant.sigma.model.entities.Account;
import org.assistant.sigma.model.entities.Transaction;
import org.assistant.sigma.model.entities.TransactionCategory;

import java.util.List;

import io.realm.RealmResults;

/**
 *
 * Created by giovanni on 6/05/17.
 */
interface TransactionsFormContract {

    interface Presenter extends BasePresenter {

        void loadAccounts();

        void loadSpentCategories(Context mContext);

        void loadIncomeCategories();

        void saveTransaction(Transaction transaction);
    }

    interface View extends BaseView<Presenter> {

        void updateAccountsSpinner(RealmResults<Account> accounts);

        void updateCategoriesSpinner(List<TransactionCategory> categories);

        void setupForm();

        void setupSaveBtn();

        boolean validateForm();
    }
}
