package org.assistant.sigma;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import org.assistant.sigma.accounts.AccountsActivity;
import org.assistant.sigma.model.entities.User;
import org.assistant.sigma.ui.overview.ActOverview;
import org.assistant.sigma.ui.scheduled_transactions.ScheduledTransactionsActivity;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 *
 * Created by giovanni on 31/07/17.
 */
public class DrawerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setupToolbar(@NonNull Toolbar toolbar, @NonNull DrawerLayout mDrawerLayout,
                             @NonNull User user, @IdRes int activeItem) {
        IconDrawable iconMenu = new IconDrawable(this, MaterialIcons.md_menu)
                .colorRes(R.color.gray_light)
                .actionBarSize();
        toolbar.setNavigationIcon(iconMenu);
        setSupportActionBar(toolbar);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }

        setupDrawerLayout(toolbar, mDrawerLayout, user, activeItem);
    }

    private void setupDrawerLayout(Toolbar toolbar, DrawerLayout mDrawerLayout, User user,
                                   int activeItem) {
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
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

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        setupDrawerMenuItems(mDrawerLayout, activeItem);
        setupDrawerUserData(user);
    }

    private void setupDrawerMenuItems(final DrawerLayout mDrawerLayout, int activeItem) {

        //
        // Transactions menu entry
        IconDrawable iconMoney = new IconDrawable(this, MaterialIcons.md_attach_money)
                .colorRes(R.color.textColorSecondary)
                .sizeDp(24);
        TextView tvTransactions = (TextView) findViewById(R.id.tv_transactions);
        tvTransactions.setCompoundDrawablesWithIntrinsicBounds(iconMoney, null, null, null);
        if (activeItem == R.id.tv_transactions) {
            tvTransactions.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_background));
        } else {
            tvTransactions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DrawerActivity.this.finish();
                }
            });
        }

        //
        // Transactions menu entry
        IconDrawable iconTrans = new IconDrawable(this, MaterialIcons.md_attach_money);
        iconTrans.colorRes(R.color.textColorSecondary);
        iconTrans.sizeDp(24);
        TextView tvTransPAccount = findViewById(R.id.tv_transactions_per_account);
        tvTransPAccount.setCompoundDrawablesWithIntrinsicBounds(iconTrans, null, null, null);
        if (activeItem == R.id.tv_transactions_per_account) {
            tvTransPAccount.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_background));
        } else {
            tvTransPAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDrawerLayout.closeDrawer(Gravity.START);

                    Intent i = new Intent(DrawerActivity.this, ActOverview.class);
                    startActivity(i);
                }
            });
        }

        //
        // Accounts menu entry
        IconDrawable iconAccounts = new IconDrawable(this, MaterialIcons.md_account_balance)
                .colorRes(R.color.textColorSecondary)
                .sizeDp(24);
        TextView tvAccounts = (TextView) findViewById(R.id.tv_accounts);
        tvAccounts.setCompoundDrawablesWithIntrinsicBounds(iconAccounts, null, null, null);
        if (activeItem == R.id.tv_accounts) {
            tvAccounts.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_background));
        } else {
            tvAccounts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDrawerLayout.closeDrawer(Gravity.START);

                    Intent intent = new Intent(DrawerActivity.this, AccountsActivity.class);
                    startActivity(intent);
                }
            });
        }

        //
        // Scheduled transactions menu entry
        IconDrawable iconClock = new IconDrawable(this, MaterialIcons.md_timer)
                .colorRes(R.color.textColorSecondary)
                .sizeDp(24);
        TextView tvSchedule = (TextView) findViewById(R.id.tv_scheduled_transactions);
        tvSchedule.setCompoundDrawablesWithIntrinsicBounds(iconClock, null, null, null);
        if (activeItem == R.id.tv_scheduled_transactions) {
            tvSchedule.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_background));
        } else {
            tvSchedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDrawerLayout.closeDrawer(Gravity.START);

                    Intent intent = new Intent(DrawerActivity.this, ScheduledTransactionsActivity.class);
                    startActivity(intent);
                }
            });
        }

        //
        // Charts menu entry
        IconDrawable iconChart = new IconDrawable(this, MaterialIcons.md_insert_chart)
                .colorRes(R.color.textColorSecondary)
                .sizeDp(24);
        TextView tvCharts = (TextView) findViewById(R.id.tv_charts);
        tvCharts.setCompoundDrawablesWithIntrinsicBounds(iconChart, null, null, null);

        //
        // Preferences menu entry
        IconDrawable iconSettings = new IconDrawable(this, MaterialIcons.md_settings)
                .colorRes(R.color.textColorSecondary)
                .sizeDp(24);
        TextView tvPreferences = (TextView) findViewById(R.id.tv_preferences);
        tvPreferences.setCompoundDrawablesWithIntrinsicBounds(iconSettings, null, null, null);
        tvPreferences.setOnClickListener(new View.OnClickListener() {
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

                mDrawerLayout.closeDrawer(Gravity.START);
            }
        });
    }

    public void setupDrawerUserData(User user) {
        TextView tvUserName = (TextView) findViewById(R.id.tv_user_name);
        tvUserName.setText(user.fullName());

        TextView tvUserEmail = (TextView) findViewById(R.id.tv_user_email);
        tvUserEmail.setText(user.getEmail());

        CircleImageView civProfile = (CircleImageView) findViewById(R.id.civ_user_profile_image);
        Glide.with(this).load(user.getUrlPicture()).into(civProfile);
    }
}
