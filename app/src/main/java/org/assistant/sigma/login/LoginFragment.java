package org.assistant.sigma.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.assistant.sigma.R;
import org.assistant.sigma.dashboard.DashboardActivity;
import org.assistant.sigma.databinding.FragLoginBinding;
import org.json.JSONObject;

import java.util.Arrays;

/**
 *
 * Created by giovanni on 20/04/17.
 */
public class LoginFragment extends Fragment implements LoginContract.View {

    private LoginContract.Presenter mPresenter;
    private FragLoginBinding viewBinding;
    private CallbackManager callbackManager;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();
        setupFBLoginCallback(callbackManager);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_login, container, false);
        viewBinding = FragLoginBinding.bind(rootView);

        setupLoginFBButton();

        if (mPresenter != null) {
            mPresenter.attemptAutoLogin();
        }

        return rootView;
    }

    @Override
    public void setPresenter(LoginContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public void goToDashboard() {
        Log.i(getClass().getName(), "Going to dashboard");
        Intent intent = new Intent(getActivity(), DashboardActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void setLoadingIndicator(boolean isLoading) {
        if (isLoading) {
            viewBinding.btnLoginFb.setVisibility(View.GONE);
            viewBinding.pbLogin.setVisibility(View.VISIBLE);
        } else {
            viewBinding.pbLogin.setVisibility(View.GONE);
            viewBinding.btnLoginFb.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void startFBLogin() {
        LoginManager.getInstance().logInWithReadPermissions(
                LoginFragment.this,
                Arrays.asList("public_profile", "email")
        );
    }

    private void setupFBLoginCallback(CallbackManager callbackManager) {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                //
                // Retrieve user profile from facebook
                //

                GraphRequest graphRequest = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                mPresenter.onFBLoginSuccess(object);
                            }
                        }
                );

                Bundle bundle = new Bundle();
                bundle.putString("fields", "first_name, last_name, email, picture.width(150).height(150)");
                graphRequest.setParameters(bundle);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                mPresenter.onFBLoginError();
            }

            @Override
            public void onError(FacebookException error) {
                mPresenter.onFBLoginError();
            }
        });
    }

    private void setupLoginFBButton() {
        viewBinding.btnLoginFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.attemptFBLogin();
            }
        });
    }
}
