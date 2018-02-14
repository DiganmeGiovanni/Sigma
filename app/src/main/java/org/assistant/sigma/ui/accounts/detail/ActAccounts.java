package org.assistant.sigma.ui.accounts.detail;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.joanzapata.iconify.fonts.MaterialIcons;

import org.assistant.sigma.DrawerActivity;
import org.assistant.sigma.R;
import org.assistant.sigma.databinding.ActAccountsBinding;
import org.assistant.sigma.model.entities.Account;
import org.assistant.sigma.ui.accounts.form.AccountsFormFragment;
import org.assistant.sigma.ui.accounts.form.AccountsFormPresenter;
import org.assistant.sigma.ui.accounts.list.AccountsPagerAdapter;
import org.assistant.sigma.ui.util.AlertPresenter;
import org.assistant.sigma.ui.util.ButtonsUtils;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 *
 * Created by giovanni on 5/05/17.
 */
public class ActAccounts extends DrawerActivity implements AccountsContract.View {
    private ActAccountsBinding vBind;
    private AccountsPresenter mPresenter;

    private RealmResults<Account> listedAccounts;
    private Account selectedAccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vBind = DataBindingUtil.setContentView(
                this,
                R.layout.act_accounts
        );
        super.setupToolbar(
                vBind.toolbar,
                vBind.drawerLayout,
                R.id.tv_accounts
        );

        mPresenter = new AccountsPresenter(this);
        mPresenter.onCreate();
        mPresenter.loadAccounts();

        setupFab();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.account_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_recalculate_balance) {
            mPresenter.recalculateBalance();
            return true;
        }
        if (item.getItemId() == R.id.item_delete_account) {
            deleteCurrentAccount();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void renderAccounts(RealmResults<Account> accounts) {
        listedAccounts = accounts;
        final AccountsPagerAdapter accountsAdapter = new AccountsPagerAdapter(
                getSupportFragmentManager(),
                listedAccounts
        );
        if (accounts.size() > 0) {
            selectedAccount = accounts.get(0);
        }

        accounts.addChangeListener(new RealmChangeListener<RealmResults<Account>>() {
            @Override
            public void onChange(RealmResults<Account> updatedAccounts) {
                listedAccounts = updatedAccounts;
                AccountsPagerAdapter accountsPagerAdapter = new AccountsPagerAdapter(
                        getSupportFragmentManager(),
                        listedAccounts
                );
                vBind.viewpager.setAdapter(accountsPagerAdapter);
            }
        });

        vBind.viewpager.setAdapter(accountsAdapter);
        vBind.tabLayout.setupWithViewPager(vBind.viewpager);
        vBind.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedAccount = listedAccounts.get(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    @Override
    public void toggleLoading(boolean isLoading) {
        if (isLoading) {
            vBind.viewpager.setVisibility(View.GONE);
            vBind.pbLoading.setVisibility(View.VISIBLE);
        } else {
            vBind.pbLoading.setVisibility(View.GONE);
            vBind.viewpager.setVisibility(View.VISIBLE);
        }
    }

    private void deleteCurrentAccount() {
        if (selectedAccount != null) {
            String title = getString(R.string.irreversible_action);
            String message = getString(R.string.the_account) +
                    " \"" + selectedAccount.getName() + "\" " +
                    getString(R.string.and_all_its_trans_will_be_deleted_u_sure);
            AlertPresenter.confirm(
                    this,
                    title,
                    message,
                    getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mPresenter.deleteAccount(selectedAccount.getId());
                        }
                    }
            );
        }
    }

    private void setupFab() {
        ButtonsUtils.setupFAB(
                vBind.btnAdd,
                MaterialIcons.md_add,
                R.color.gray_light,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AccountsFormFragment accountsFormFragment = new AccountsFormFragment();
                        new AccountsFormPresenter(accountsFormFragment);

                        accountsFormFragment.show(getSupportFragmentManager(), "accountsForm");
                    }
                }
        );
    }
}
