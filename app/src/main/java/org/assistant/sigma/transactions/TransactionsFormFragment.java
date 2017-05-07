package org.assistant.sigma.transactions;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import org.assistant.sigma.R;
import org.assistant.sigma.adapters.SPAccountsAdapter;
import org.assistant.sigma.adapters.SPTransactionCategoriesAdapter;
import org.assistant.sigma.databinding.FragTransactionsFormBinding;
import org.assistant.sigma.model.entities.Account;
import org.assistant.sigma.model.entities.TransactionCategory;

import io.realm.RealmResults;

/**
 *
 * Created by giovanni on 6/05/17.
 */
public class TransactionsFormFragment extends Fragment implements TransactionsFormContract.View {

    private FragTransactionsFormBinding viewBinding;
    private TransactionsFormContract.Presenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_transactions_form, container, false);
        viewBinding = FragTransactionsFormBinding.bind(rootView);

        setupForm();
        setupSaveBtn();
        return rootView;
    }

    @Override
    public void setPresenter(TransactionsFormContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public void updateAccountsSpinner(RealmResults<Account> accounts) {
        SPAccountsAdapter adapter = new SPAccountsAdapter(getContext(), accounts);
        viewBinding.spAccount.setAdapter(adapter);
    }

    @Override
    public void updateCategoriesSpinner(RealmResults<TransactionCategory> categories) {
        SPTransactionCategoriesAdapter adapter =
                new SPTransactionCategoriesAdapter(getContext(), categories);
        viewBinding.spCategory.setAdapter(adapter);
    }

    @Override
    public void setupForm() {
        mPresenter.loadAccounts();
        mPresenter.loadSpentCategories();
        viewBinding.swIsIncome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    mPresenter.loadIncomeCategories();
                } else {
                    mPresenter.loadSpentCategories();
                }
            }
        });
    }

    @Override
    public void setupSaveBtn() {
        viewBinding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {
                    // TODO
                }
            }
        });
    }

    @Override
    public boolean validateForm() {
        return false;
    }
}
