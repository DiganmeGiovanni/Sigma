package org.assistant.sigma.transactions.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.assistant.sigma.R;
import org.assistant.sigma.databinding.FragTransactionDetailsBinding;
import org.assistant.sigma.model.entities.Transaction;
import org.assistant.sigma.transactions.form.TransactionsFormActivity;
import org.assistant.sigma.utils.TextUtils;
import org.assistant.sigma.utils.callbacks.CBGeneric;

/**
 * Created by giovanni on 27/09/17.
 *
 */
public class TransactionDetailsFragment extends Fragment implements TransactionDetailsContract.View {
    private TransactionDetailsContract.Presenter mPresenter;

    private String transactionId;
    private FragTransactionDetailsBinding viewBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_transaction_details, container, false);
        viewBinding = FragTransactionDetailsBinding.bind(rootView);

        // Load transaction details
        transactionId = getArguments().getString(TransactionDetailsActivity.TRANSACTION_ID);
        mPresenter.loadTransaction(transactionId);

        // Setup fab
        viewBinding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTransaction(transactionId);
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_btn_delete, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btn_delete) {
            onDeleteBtnClicked();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void setPresenter(TransactionDetailsContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public void loadTransaction(Transaction transaction) {
        viewBinding.tvAccount.setText(transaction.getAccount().getName());
        if (transaction.getQuantity() < 0) {
            viewBinding.tvQuantity.setText(TextUtils.asMoney(transaction.getQuantity() * -1));
        } else {
            viewBinding.tvQuantity.setText(TextUtils.asMoney(transaction.getQuantity()));
        }

        viewBinding.tvCategory.setText(transaction.getTransactionCategory().getName());
        viewBinding.tvDateTime.setText(TextUtils.forHumans(transaction.getCreatedAt()));
        viewBinding.tvDescription.setText(transaction.getDescription());
    }

    @Override
    public void editTransaction(String transactionId) {
        Intent intent = new Intent(getContext(), TransactionsFormActivity.class);
        intent.putExtra(
                TransactionsFormActivity.TRANSACTION_ID,
                transactionId
        );

        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onDeleteBtnClicked() {
        // TODO Show progress dialog
        mPresenter.deleteTransaction(transactionId, new CBGeneric<Boolean>() {
            @Override
            public void onResponse(Boolean response) {
                // TODO Hide progress dialog
                getActivity().finish();
            }
        });
    }
}
