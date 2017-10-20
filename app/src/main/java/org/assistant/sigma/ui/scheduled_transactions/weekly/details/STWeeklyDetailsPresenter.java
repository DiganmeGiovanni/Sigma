package org.assistant.sigma.ui.scheduled_transactions.weekly.details;

import org.assistant.sigma.model.entities.ScheduledTransactionWeekly;
import org.assistant.sigma.model.repositories.ScheduledTransactionsRepository;

/**
 * Created by giovanni on 19/10/17.
 *
 */
public class STWeeklyDetailsPresenter implements STWeeklyDetailsContract.Presenter {
    private STWeeklyDetailsContract.View mView;

    private ScheduledTransactionsRepository sTransRepository;

    public STWeeklyDetailsPresenter(STWeeklyDetailsContract.View mView) {
        this.mView = mView;
        this.mView.setPresenter(this);

        sTransRepository = new ScheduledTransactionsRepository();
    }

    @Override
    public void onDestroy() {
        sTransRepository.destroy();
    }

    @Override
    public void deleteSTWeekly(String stWeeklyId) {
        sTransRepository.deleteWeekly(stWeeklyId);
    }

    @Override
    public void loadSTWeekly(String stWeeklyId) {
        ScheduledTransactionWeekly sTransWeekly = sTransRepository.findWeekly(stWeeklyId);
        this.mView.renderSTWeekly(sTransWeekly);
    }
}
