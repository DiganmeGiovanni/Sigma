package org.assistant.sigma.transactions.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.assistant.sigma.R;
import org.assistant.sigma.adapters.TransactionsAdapter;
import org.assistant.sigma.databinding.FragTransactionsBinding;
import org.assistant.sigma.model.entities.Transaction;

import io.realm.RealmResults;

/**
 *
 * Created by giovanni on 21/05/17.
 */
public class TransactionsListFragment extends Fragment implements TransactionsListContract.View {

    private FragTransactionsBinding viewBinding;
    private TransactionsListContract.Presenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_transactions, container, false);
        viewBinding = FragTransactionsBinding.bind(rootView);

        mPresenter.loadLastTransactions();
        return rootView;
    }

    @Override
    public void setPresenter(TransactionsListContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public void toggleLoadingAnimation(boolean isLoading) {

    }

    @Override
    public void renderTransactions(RealmResults<Transaction> transactions) {
        if (transactions.size() > 0) {
            TransactionsAdapter adapter = new TransactionsAdapter(getContext(), transactions);
            viewBinding.lvTransactions.setAdapter(adapter);

            viewBinding.tvWithoutTransactions.setVisibility(View.GONE);
            viewBinding.lvTransactions.setVisibility(View.VISIBLE);
        } else {
            viewBinding.lvTransactions.setVisibility(View.GONE);
            viewBinding.tvWithoutTransactions.setVisibility(View.VISIBLE);
        }

    }
}
