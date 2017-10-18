package org.assistant.sigma.ui.scheduled_transactions.weekly.form;

import org.assistant.sigma.model.entities.ScheduledTransactionWeekly;
import org.assistant.sigma.model.entities.Transaction;
import org.assistant.sigma.model.repositories.AccountsRepository;
import org.assistant.sigma.model.repositories.ScheduledTransactionsRepository;
import org.assistant.sigma.model.repositories.TransactionsRepository;

/**
 * Created by giovanni on 17/10/17.
 *
 */
public class STWeeklyFormPresenter implements STWeeklyFormContract.Presenter {
    private STWeeklyFormContract.View mView;

    private AccountsRepository accountsRepository;
    private TransactionsRepository transactionsRepository;
    private ScheduledTransactionsRepository stRepository;

    STWeeklyFormPresenter(STWeeklyFormContract.View mView) {
        this.mView = mView;
        this.mView.setPresenter(this);

        accountsRepository = new AccountsRepository();
        transactionsRepository = new TransactionsRepository();
        stRepository = new ScheduledTransactionsRepository();
    }

    @Override
    public void onDestroy() {
        transactionsRepository.destroy();
        accountsRepository.destroy();
    }

    @Override
    public void loadAccounts() {
        mView.renderAccounts(accountsRepository.allActive());
    }

    @Override
    public void saveTransaction(ScheduledTransactionWeekly sTWeekly) {
        stRepository.upsert(sTWeekly);
    }

    @Override
    public void loadTransaction(String stWeeklyId) {
        ScheduledTransactionWeekly stWeekly = stRepository.find(stWeeklyId);
        if (stWeekly != null) {
            mView.preloadTransaction(stWeekly);
        }
    }

    @Override
    public Transaction lastTransaction() {
        return transactionsRepository.lastTransaction();
    }
}
