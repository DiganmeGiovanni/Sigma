package org.assistant.sigma.ui.transactions.listing;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.View;

import com.joanzapata.iconify.fonts.MaterialIcons;

import org.assistant.sigma.DrawerActivity;
import org.assistant.sigma.R;
import org.assistant.sigma.databinding.ActTransactionsBinding;
import org.assistant.sigma.model.entities.Account;
import org.assistant.sigma.transactions.form.TransactionsFormActivity;
import org.assistant.sigma.ui.accounts.AccountsActivity;
import org.assistant.sigma.ui.util.ButtonsUtils;

import io.realm.RealmResults;

/**
 * Created by giovanni on 24/12/17.
 *
 */
public class ActTransactions extends DrawerActivity {
    private ActTransactionsBinding vBind;
    private TransactionsPresenter transPresenter;
    private int selectedTabPos = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vBind = DataBindingUtil.setContentView(
                this,
                R.layout.act_transactions
        );
        super.setupToolbar(
                vBind.toolbar,
                vBind.drawerLayout,
                R.id.tv_transactions_per_account
        );

        transPresenter = new TransactionsPresenter();
        transPresenter.onCreate();
        makeTabs();
        setupAddButton();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        transPresenter.onDestroy();
    }

    private void makeTabs() {
        RealmResults<Account> accounts = transPresenter.getAccounts();
        TransactionsPagerAdapter adapter = new TransactionsPagerAdapter(
                getSupportFragmentManager(),
                accounts
        );

        if (accounts.size() > 3) {
            vBind.tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
        vBind.viewpager.setAdapter(adapter);
        vBind.tabLayout.setupWithViewPager(vBind.viewpager);
        vBind.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedTabPos = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        if (accounts.size() > 0) {
            selectedTabPos = 0;
        }
    }

    private void setupAddButton() {
        ButtonsUtils.setupFAB(
                vBind.btnAdd,
                MaterialIcons.md_add,
                R.color.gray_light,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (transPresenter.getAccounts().size() > 0) {
                            // Go to add transactions
                            Intent intent = new Intent(
                                    ActTransactions.this,
                                    TransactionsFormActivity.class
                            );
                            startActivity(intent);
                        } else {
                            // Go to accounts
                            Intent intent = new Intent(
                                    ActTransactions.this,
                                    AccountsActivity.class
                            );
                            startActivity(intent);
                        }
                    }
                }
        );
    }

    @NonNull
    protected TransactionsPresenter getPresenter() {
        if (transPresenter == null) {
            transPresenter = new TransactionsPresenter();
            transPresenter.onCreate();
        }
        return transPresenter;
    }
}
