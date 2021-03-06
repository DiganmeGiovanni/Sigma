package org.assistant.sigma.ui.accounts.detail;

import org.assistant.sigma.model.entities.Account;

/**
 * Created by giovanni on 24/01/18.
 *
 */
public interface AccountDetailContract {

    interface View {
        void renderAccount(Account account);

        void renderCurrentBalance(Double balance);

        void renderCurrShortPeriodLabel(String label);

        void renderCurrShortPeriodBalance(Double balance);

        void renderCurrLargePeriodLabel(String label);

        void renderCurrLargePeriodBalance(Double balance);

        void toggleLoading(boolean isLoading);
    }
}
