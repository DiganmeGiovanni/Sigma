package org.assistant.sigma.accounts;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import org.assistant.sigma.DrawerActivity;
import org.assistant.sigma.R;
import org.assistant.sigma.accounts.list.AccountsListFragment;
import org.assistant.sigma.accounts.list.AccountsListPresenter;
import org.assistant.sigma.databinding.ActDrawerBinding;
import org.assistant.sigma.model.repositories.UsersRepository;
import org.assistant.sigma.utils.ActivityUtils;

/**
 *
 * Created by giovanni on 5/05/17.
 */
public class AccountsActivity extends DrawerActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UsersRepository usersRepository = new UsersRepository();

        // View binding
        ActDrawerBinding viewBinding = DataBindingUtil.setContentView(this, R.layout.act_drawer);

        // Setup toolbar
        setupToolbar(
                viewBinding.toolbar,
                viewBinding.drawerLayout,
                usersRepository.activeUser(),
                R.id.tv_accounts
        );

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
