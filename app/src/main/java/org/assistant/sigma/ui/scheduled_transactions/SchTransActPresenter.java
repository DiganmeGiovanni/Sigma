package org.assistant.sigma.ui.scheduled_transactions;

import org.assistant.sigma.model.repositories.AccountsRepository;

/**
 * Created by giovanni on 19/12/17.
 * Handles business logic for {@link ScheduledTransactionsActivity}
 */
public class SchTransActPresenter {
    private AccountsRepository accountsRepository;

    public SchTransActPresenter() {
        this.accountsRepository = new AccountsRepository();
    }

    public void onDestroy() {
        this.accountsRepository.destroy();
    }

    public boolean haveActiveAccounts() {
        return accountsRepository.haveActive();
    }
}
