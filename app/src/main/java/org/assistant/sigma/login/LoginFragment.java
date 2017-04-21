package org.assistant.sigma.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.assistant.sigma.R;
import org.assistant.sigma.databinding.FragLoginBinding;

/**
 *
 * Created by giovanni on 20/04/17.
 */
public class LoginFragment extends Fragment {

    private FragLoginBinding viewBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_login, container, false);
        viewBinding = FragLoginBinding.bind(rootView);

        setupLoginFBButton();
        return rootView;
    }

    private void setupLoginFBButton() {
        viewBinding.btnLoginFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Init facebook login
            }
        });
    }
}
