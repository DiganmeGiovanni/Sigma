package org.assistant.sigma.ui.transactions;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.assistant.sigma.R;
import org.assistant.sigma.adapters.TransactionsAdapter;
import org.assistant.sigma.databinding.FragTransactionsBinding;
import org.assistant.sigma.model.entities.Transaction;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by giovanni on 24/12/17.
 *
 */
public class FragTransactionsList extends Fragment {
    public static final String ACCOUNT_ID = "ACCOUNT_ID";

    private FragTransactionsBinding vBind;
    private TransactionsPresenter transPresenter;
    private String accountId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.frag_transactions,
                container,
                false
        );
        vBind = FragTransactionsBinding.bind(rootView);

        if (getArguments() != null && getArguments().containsKey(ACCOUNT_ID)) {
            accountId = getArguments().getString(ACCOUNT_ID);

            setupRecyclerView();
            loadTransactions();
        }

        return rootView;
    }

    private void loadTransactions() {
        vBind.pbLoading.setVisibility(View.VISIBLE);

        RealmResults<Transaction> transactions = transPresenter.getTransactions(accountId);
        transactions.addChangeListener(new RealmChangeListener<RealmResults<Transaction>>() {
            @Override
            public void onChange(RealmResults<Transaction> updatedTransactions) {
                renderTransactions(updatedTransactions);
            }
        });
        renderTransactions(transactions);
    }

    private void renderTransactions(RealmResults<Transaction> transactions) {
        TransactionsAdapter adapter = new TransactionsAdapter(getContext(), transactions);
        vBind.rvTransactions.setAdapter(adapter);

        vBind.pbLoading.setVisibility(View.GONE);
        vBind.rvTransactions.setVisibility(View.VISIBLE);
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        vBind.rvTransactions.setLayoutManager(layoutManager);
        vBind.rvTransactions.setItemAnimator(new DefaultItemAnimator());
    }

    public void setTransPresenter(TransactionsPresenter transPresenter) {
        this.transPresenter = transPresenter;
    }
}
