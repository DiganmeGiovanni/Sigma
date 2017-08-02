package org.assistant.sigma.transactions.list;

import org.assistant.sigma.BasePresenter;
import org.assistant.sigma.BaseView;
import org.assistant.sigma.model.entities.Transaction;

import io.realm.RealmResults;

/**
 *
 * Created by giovanni on 21/05/17.
 */
public interface TransactionsListContract {

    interface Presenter extends BasePresenter {

        void loadLastTransactions();
    }

    interface View extends BaseView<Presenter> {

        void toggleLoadingAnimation(boolean isLoading);

        void renderTransactions(RealmResults<Transaction> transactions);
    }
}
