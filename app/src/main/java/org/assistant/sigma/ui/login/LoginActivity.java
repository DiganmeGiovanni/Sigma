package org.assistant.sigma.ui.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.assistant.sigma.R;
import org.assistant.sigma.utils.ActivityUtils;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_no_toolbar);

        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content);
        if (loginFragment == null) {
            loginFragment = new LoginFragment();

            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    loginFragment,
                    R.id.content
            );
        }

        // Initialize presenter
        new LoginPresenter(loginFragment);
    }
}
