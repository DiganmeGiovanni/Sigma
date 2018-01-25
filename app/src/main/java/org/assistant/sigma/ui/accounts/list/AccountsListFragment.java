package org.assistant.sigma.ui.accounts.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import org.assistant.sigma.R;
import org.assistant.sigma.databinding.FragAccountsListBinding;
import org.assistant.sigma.model.entities.Account;
import org.assistant.sigma.ui.accounts.detail.ActAccountDetail;
import org.assistant.sigma.ui.accounts.form.AccountsFormFragment;
import org.assistant.sigma.ui.accounts.form.AccountsFormPresenter;

import io.realm.RealmList;

/**
 * Created by giovanni on 5/05/17.
 *
 */
public class AccountsListFragment extends Fragment implements AccountsListContract.View {
    private FragAccountsListBinding viewBinding;
    private AccountsListPresenter mPresenter;
    private AccountsAdapter accountsAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mPresenter = new AccountsListPresenter(this);
        mPresenter.onCreate();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_accounts_list, container, false);
        viewBinding = FragAccountsListBinding.bind(rootView);

        setupAddAccountBtn();
        mPresenter.loadAccounts();

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void goToAccountDetails(String accountId) {
        Intent i = new Intent(getActivity(), ActAccountDetail.class);
        i.putExtra(ActAccountDetail.ACCOUNT_ID, accountId);
        startActivity(i);
    }

    @Override
    public void goToNewAccount() {
        AccountsFormFragment accountsFormFragment = new AccountsFormFragment();
        new AccountsFormPresenter(accountsFormFragment);

        accountsFormFragment.show(getFragmentManager(), "accountsForm");
    }

    @Override
    public void notifyAccountsListChanged() {
        if (accountsAdapter != null) {
            accountsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void updateAccountsList(final RealmList<Account> accounts) {
        accountsAdapter = new AccountsAdapter(getContext(), accounts, mPresenter);
        viewBinding.lvAccounts.setAdapter(accountsAdapter);
        viewBinding.lvAccounts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Account account = accounts.get(i);
                goToAccountDetails(account.getId());
            }
        });
    }

    private void setupAddAccountBtn() {
        IconDrawable iconAdd = new IconDrawable(getContext(), MaterialIcons.md_add)
                .colorRes(R.color.gray_light)
                .sizeDp(24);
        viewBinding.btnAddAccount.setImageDrawable(iconAdd);
        viewBinding.btnAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccountsListFragment.this.goToNewAccount();
            }
        });
    }
}
