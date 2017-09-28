package org.assistant.sigma.transactions.details;

import org.assistant.sigma.BasePresenter;
import org.assistant.sigma.BaseView;
import org.assistant.sigma.model.entities.Transaction;
import org.assistant.sigma.utils.callbacks.CBGeneric;

/**
 * Created by giovanni on 27/09/17.
 *
 */
interface TransactionDetailsContract {

    interface Presenter extends BasePresenter {

        void deleteTransaction(String transactionId, CBGeneric<Boolean> callback);

        void loadTransaction(String transactionId);
    }

    interface View extends BaseView<Presenter> {

        void loadTransaction(Transaction transaction);

        void editTransaction(String transactionId);

        void onDeleteBtnClicked();
    }
}
