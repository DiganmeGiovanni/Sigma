package org.assistant.sigma.ui.scheduled_transactions.weekly.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.assistant.sigma.R;
import org.assistant.sigma.databinding.FragScheduledTransactionsBinding;
import org.assistant.sigma.model.entities.ScheduledTransactionWeekly;
import org.assistant.sigma.ui.scheduled_transactions.weekly.details.STWeeklyDetailsActivity;

import io.realm.RealmResults;

/**
 * Created by giovanni on 12/10/17.
 *
 */
public class STWeeklyListFragment extends Fragment implements STWeeklyListContract.View {
    private FragScheduledTransactionsBinding viewBinding;
    private STWeeklyListContract.Presenter mPresenter;

    private STWeeklyAdapter stWeeklyAdapter;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_scheduled_transactions, container, false);
        viewBinding = FragScheduledTransactionsBinding.bind(rootView);

        setupRVTransactions();
        mPresenter.loadTransactions();
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void setPresenter(STWeeklyListContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public void renderTransactions(RealmResults<ScheduledTransactionWeekly> transactions) {
        if (transactions.size() > 0) {
            stWeeklyAdapter = new STWeeklyAdapter(
                    getContext(),
                    transactions,
                    new STWeeklyAdapter.OnSTWeeklyClickListener() {
                        @Override
                        public void onSTWeeklyClicked(ScheduledTransactionWeekly sTWeekly) {
                            Intent intent = new Intent(getContext(), STWeeklyDetailsActivity.class);
                            intent.putExtra(
                                    STWeeklyDetailsActivity.ST_WEEKLY_ID,
                                    sTWeekly.getId()
                            );

                            startActivity(intent);
                        }
                    }
            );
            viewBinding.rvTransactions.setAdapter(stWeeklyAdapter);

            viewBinding.tvWithoutTransactions.setVisibility(View.GONE);
            viewBinding.rvTransactions.setVisibility(View.VISIBLE);
        } else {
            viewBinding.rvTransactions.setVisibility(View.GONE);
            viewBinding.tvWithoutTransactions.setVisibility(View.VISIBLE);
        }
    }

    private void setupRVTransactions() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        viewBinding.rvTransactions.setLayoutManager(layoutManager);
        viewBinding.rvTransactions.setItemAnimator(new DefaultItemAnimator());
    }
}
