package org.assistant.sigma.ui.scheduled_transactions.monthly.list;

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
import org.assistant.sigma.model.entities.ScheduledTransactionMonthly;

import io.realm.RealmResults;

/**
 * Created by giovanni on 12/10/17.
 *
 */
public class STMonthlyListFragment extends Fragment implements STMonthlyListContract.View {
    private FragScheduledTransactionsBinding viewBinding;
    private STMonthlyListContract.Presenter mPresenter;

    private STMonthlyAdapter stMonthlyAdapter;

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
    public void setPresenter(STMonthlyListContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public void renderTransactions(RealmResults<ScheduledTransactionMonthly> transactions) {
        if (transactions.size() > 0) {
            stMonthlyAdapter = new STMonthlyAdapter(
                    getContext(),
                    transactions,
                    new STMonthlyAdapter.OnSTMonthlyClickListener() {
                        @Override
                        public void onSTMonthlyClicked(ScheduledTransactionMonthly sTWeekly) {

                        }
                    }
            );
            viewBinding.rvTransactions.setAdapter(stMonthlyAdapter);

            viewBinding.tvWithoutTransactions.setVisibility(View.GONE);
            viewBinding.rvTransactions.setVisibility(View.VISIBLE);
        } else {
            viewBinding.rvTransactions.setVisibility(View.GONE);
            viewBinding.tvWithoutTransactions.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void notifyTransactionsChanged() {
        if (stMonthlyAdapter != null) {
            stMonthlyAdapter.notifyDataSetChanged();
        }
    }

    private void setupRVTransactions() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        viewBinding.rvTransactions.setLayoutManager(layoutManager);
        viewBinding.rvTransactions.setItemAnimator(new DefaultItemAnimator());
    }
}
