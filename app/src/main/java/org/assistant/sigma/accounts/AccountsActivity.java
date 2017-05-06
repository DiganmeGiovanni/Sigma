package org.assistant.sigma.accounts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.assistant.sigma.R;
import org.assistant.sigma.accounts.list.AccountsListFragment;
import org.assistant.sigma.accounts.list.AccountsListPresenter;
import org.assistant.sigma.utils.ActivityUtils;

/**
 *
 * Created by giovanni on 5/05/17.
 */
public class AccountsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_accounts);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AccountsListFragment accountsListFragment = (AccountsListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content);
        if (accountsListFragment == null) {
            accountsListFragment = new AccountsListFragment();

            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    accountsListFragment,
                    R.id.content
            );
        }

        // Initialize presenter
        new AccountsListPresenter(accountsListFragment);
    }
}
