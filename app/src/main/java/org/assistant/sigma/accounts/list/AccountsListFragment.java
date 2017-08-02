package org.assistant.sigma.accounts.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.assistant.sigma.R;
import org.assistant.sigma.accounts.form.AccountsFormFragment;
import org.assistant.sigma.accounts.form.AccountsFormPresenter;
import org.assistant.sigma.adapters.AccountsAdapter;
import org.assistant.sigma.databinding.FragAccountsListBinding;
import org.assistant.sigma.model.entities.Account;
import org.assistant.sigma.utils.ActivityUtils;

import io.realm.RealmList;

/**
 *
 * Created by giovanni on 5/05/17.
 */
public class AccountsListFragment extends Fragment implements AccountsListContract.View {

    private FragAccountsListBinding viewBinding;
    private AccountsListContract.Presenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_accounts_list, container, false);
        viewBinding = FragAccountsListBinding.bind(rootView);

        setupAddAccountBtn();

        if (mPresenter != null) {
            mPresenter.loadAccounts();
        }

        return rootView;
    }

    @Override
    public void setPresenter(AccountsListContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public void goToAccountDetails() {

    }

    @Override
    public void goToNewAccount() {
        AccountsFormFragment accountsFormFragment = new AccountsFormFragment();

        ActivityUtils.replaceFragmentInActivity(
                getFragmentManager(),
                accountsFormFragment,
                R.id.content,
                "accountsForm"
        );

        // Initialize presenter
        new AccountsFormPresenter(accountsFormFragment);
    }

    @Override
    public void updateAccountsList(RealmList<Account> accounts) {
        AccountsAdapter accountsAdapter = new AccountsAdapter(getContext(), accounts);
        viewBinding.lvAccounts.setAdapter(accountsAdapter);
    }

    private void setupAddAccountBtn() {
        viewBinding.btnAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccountsListFragment.this.goToNewAccount();
            }
        });
    }
}
