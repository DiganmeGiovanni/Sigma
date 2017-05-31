package org.assistant.sigma.accounts;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.assistant.sigma.R;
import org.assistant.sigma.accounts.list.AccountsListFragment;
import org.assistant.sigma.accounts.list.AccountsListPresenter;
import org.assistant.sigma.databinding.ActAccountsBinding;
import org.assistant.sigma.databinding.SideBarMenuBinding;
import org.assistant.sigma.model.repositories.UsersRepository;
import org.assistant.sigma.utils.ActivityUtils;
import org.assistant.sigma.utils.DrawableUtils;

/**
 *
 * Created by giovanni on 5/05/17.
 */
public class AccountsActivity extends AppCompatActivity {

    private UsersRepository usersRepository;
    private ActAccountsBinding viewBinding;
    private SideBarMenuBinding sideBarBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usersRepository = new UsersRepository();

        // View binding
        viewBinding = DataBindingUtil.setContentView(this, R.layout.act_accounts);
        sideBarBinding = viewBinding.sideBar;

        // Setup toolbar
        setupToolbar();

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

    private void setupToolbar() {
        viewBinding.toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(viewBinding.toolbar);

        setupDrawerLayout();
    }

    private void setupDrawerLayout() {
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this,
                viewBinding.drawerLayout,
                viewBinding.toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        viewBinding.drawerLayout.addDrawerListener(mDrawerToggle);
        setupDrawerMenuItems();
        setupDrawerUserData();
    }

    private void setupDrawerMenuItems() {

        // Transactions menu entry
        Drawable moneyDrawable = DrawableUtils.setColorFilter(
                this,
                R.drawable.ic_money_1x,
                R.color.textColorSecondary
        );
        TextView itemTrans = sideBarBinding.tvTransactions;
        itemTrans.setCompoundDrawablesWithIntrinsicBounds(moneyDrawable, null, null, null);
        itemTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccountsActivity.this.finish();
            }
        });


        // Accounts menu entry
        Drawable accountsDrawable = DrawableUtils.setColorFilter(
                this,
                R.drawable.ic_accounts_1x,
                R.color.textColorSecondary
        );
        TextView tvAccounts = sideBarBinding.tvAccounts;
        tvAccounts.setCompoundDrawablesWithIntrinsicBounds(accountsDrawable, null, null, null);
        tvAccounts.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_background));


        // Scheduled transactions menu entry
        Drawable scheduleDrawable = DrawableUtils.setColorFilter(
                this,
                R.drawable.ic_schedule_1x,
                R.color.textColorSecondary
        );
        sideBarBinding.tvScheduledTransactions.setCompoundDrawablesWithIntrinsicBounds(
                scheduleDrawable,
                null,
                null,
                null
        );

        // Charts menu entry
        Drawable chartDrawable = DrawableUtils.setColorFilter(
                this,
                R.drawable.ic_chart_1x,
                R.color.textColorSecondary
        );
        sideBarBinding.tvCharts.setCompoundDrawablesWithIntrinsicBounds(chartDrawable, null, null, null);

        // Preferences menu entry
        Drawable settingsDrawable = DrawableUtils.setColorFilter(
                this,
                R.drawable.ic_settings_1x,
                R.color.textColorSecondary
        );
        sideBarBinding.tvPreferences.setCompoundDrawablesWithIntrinsicBounds(settingsDrawable, null, null, null);
        sideBarBinding.tvPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SettingsFragment fragment = new SettingsFragment();
//                new SettingsPresenter(fragment);
//
//                ActivityUtils.replaceFragmentInActivity(
//                        getSupportFragmentManager(),
//                        fragment,
//                        R.id.content,
//                        "settings"
//                );

                viewBinding.drawerLayout.closeDrawer(Gravity.START);
            }
        });
    }

    private void setupDrawerUserData() {
        sideBarBinding.tvUserName.setText(usersRepository.activeUser().fullName());
        sideBarBinding.tvUserEmail.setText(usersRepository.activeUser().getEmail());
        Glide.with(this)
                .load(usersRepository.activeUser().getUrlPicture())
                .into(sideBarBinding.civUserProfileImage);
    }
}
