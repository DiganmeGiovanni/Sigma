package org.assistant.sigma.ui.accounts;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.joanzapata.iconify.fonts.MaterialIcons;

import org.assistant.sigma.DrawerActivity;
import org.assistant.sigma.R;
import org.assistant.sigma.databinding.ActAccountsBinding;
import org.assistant.sigma.model.entities.Account;
import org.assistant.sigma.ui.accounts.form.AccountsFormFragment;
import org.assistant.sigma.ui.accounts.form.AccountsFormPresenter;
import org.assistant.sigma.ui.accounts.list.AccountsPagerAdapter;
import org.assistant.sigma.ui.accounts.list.AccountsPresenter;
import org.assistant.sigma.ui.util.ButtonsUtils;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 *
 * Created by giovanni on 5/05/17.
 */
public class ActAccounts extends DrawerActivity {
    private ActAccountsBinding vBind;
    private AccountsPresenter accountsPresenter;

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

        accountsPresenter = new AccountsPresenter();
        accountsPresenter.onCreate();

        makeTabs();
        setupFab();
    }

    private void makeTabs() {
        RealmResults<Account> accounts = accountsPresenter.getAccounts();

        // Render accounts if any is available

        final AccountsPagerAdapter accountsAdapter = new AccountsPagerAdapter(
                getSupportFragmentManager(),
                accounts
        );

        accounts.addChangeListener(new RealmChangeListener<RealmResults<Account>>() {
            @Override
            public void onChange(RealmResults<Account> updatedAccounts) {
                AccountsPagerAdapter accountsPagerAdapter = new AccountsPagerAdapter(
                        getSupportFragmentManager(),
                        updatedAccounts
                );
                vBind.viewpager.setAdapter(accountsPagerAdapter);
            }
        });

        vBind.viewpager.setAdapter(accountsAdapter);
        vBind.tabLayout.setupWithViewPager(vBind.viewpager);
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
