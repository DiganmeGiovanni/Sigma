package org.assistant.sigma;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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

import org.assistant.sigma.accounts.AccountsActivity;
import org.assistant.sigma.model.entities.User;
import org.assistant.sigma.utils.DrawableUtils;

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
        toolbar.setNavigationIcon(R.drawable.ic_menu);
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
        Drawable moneyDrawable = DrawableUtils.setColorFilter(
                this,
                R.drawable.ic_money_1x,
                R.color.textColorSecondary
        );
        TextView tvTransactions = (TextView) findViewById(R.id.tv_transactions);
        tvTransactions.setCompoundDrawablesWithIntrinsicBounds(moneyDrawable, null, null, null);
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
        // Accounts menu entry
        Drawable accountsDrawable = DrawableUtils.setColorFilter(
                this,
                R.drawable.ic_accounts_1x,
                R.color.textColorSecondary
        );
        TextView tvAccounts = (TextView) findViewById(R.id.tv_accounts);
        tvAccounts.setCompoundDrawablesWithIntrinsicBounds(accountsDrawable, null, null, null);
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
        Drawable scheduleDrawable = DrawableUtils.setColorFilter(
                this,
                R.drawable.ic_schedule_1x,
                R.color.textColorSecondary
        );
        TextView tvSchedule = (TextView) findViewById(R.id.tv_scheduled_transactions);
        tvSchedule.setCompoundDrawablesWithIntrinsicBounds(scheduleDrawable, null, null, null);

        //
        // Charts menu entry
        Drawable chartDrawable = DrawableUtils.setColorFilter(
                this,
                R.drawable.ic_chart_1x,
                R.color.textColorSecondary
        );
        TextView tvCharts = (TextView) findViewById(R.id.tv_charts);
        tvCharts.setCompoundDrawablesWithIntrinsicBounds(chartDrawable, null, null, null);

        //
        // Preferences menu entry
        Drawable settingsDrawable = DrawableUtils.setColorFilter(
                this,
                R.drawable.ic_settings_1x,
                R.color.textColorSecondary
        );
        TextView tvPreferences = (TextView) findViewById(R.id.tv_preferences);
        tvPreferences.setCompoundDrawablesWithIntrinsicBounds(settingsDrawable, null, null, null);
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
