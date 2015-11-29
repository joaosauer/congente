package com.hackathon.congente.views;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.hackathon.congente.R;
import com.hackathon.congente.callbacks.LoginCallback;
import com.hackathon.congente.database.DataBase;
import com.hackathon.congente.datatype.User;
import com.hackathon.congente.network.calls.LoginAsyncTask;

public class LoginFragment extends Fragment implements LoginCallback{
    private LoginAsyncTask _login;
    private ProgressDialog _progressBar;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private User _user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity());
        callbackManager = CallbackManager.Factory.create();

        DataBase database = DataBase.getDataBase(getContext());
        _user = database.getUserData();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                _user.fb_token = currentAccessToken.getToken();
                _user.usuario = currentAccessToken.getUserId();
                goToIdeias(false);
            }
        };
        // If the access token is available already assign it.
        if(AccessToken.getCurrentAccessToken() != null) {
            _user.fb_token = AccessToken.getCurrentAccessToken().getToken();
            _user.usuario = AccessToken.getCurrentAccessToken().getUserId();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.login, container, false);
        _progressBar = new ProgressDialog(getActivity());
        if(_user == null || !_user.isValid()) {
            //Showing the welcome
            view.findViewById(R.id.welcome_screen).setVisibility(View.VISIBLE);
            Button okButton = (Button) view.findViewById(R.id.welcome_button);
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLogin(getView());
                }
            });
        }
        else {
            goToIdeias(true);
        }
        return view;
    }

    private void showLogin(View view) {
        view.findViewById(R.id.welcome_screen).setVisibility(View.GONE);
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile", "user_friends");
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                _user.usuario = loginResult.getAccessToken().getUserId();
                _user.fb_token = loginResult.getAccessToken().getToken();
                showLogin(getView());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                showError(getString(R.string.error_facebook));
            }
        });
        Button newButton = (Button) view.findViewById(R.id.login_new_user);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getView() != null) {
                    _user.usuario = ((EditText) getView().findViewById(R.id.login_usuario)).getText().toString();
                    _user.senha = ((EditText) getView().findViewById(R.id.login_senha)).getText().toString();
                    if (!_user.isValid()) {
                        showError(getString(R.string.error_emptylogin));
                    } else {
                        //Fazendo login
                        showLoading();
                        _login = new LoginAsyncTask(_user, LoginFragment.this);
                        _login.createLogin();
                    }
                }
            }
        });
        Button entrarButton = (Button) view.findViewById(R.id.login_entrar);
        entrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getView() != null) {
                    _user.usuario = ((EditText) getView().findViewById(R.id.login_usuario)).getText().toString();
                    _user.senha = ((EditText) getView().findViewById(R.id.login_senha)).getText().toString();
                    if (!_user.isValid()) {
                        showError(getString(R.string.error_emptylogin));
                    } else {
                        //Fazendo login
                        showLoading();
                        _login = new LoginAsyncTask(_user, LoginFragment.this);
                        _login.doLogin();
                    }
                }
            }
        });
    }

    private void goToIdeias(boolean checkAccount) {
        if(checkAccount) {
            showLoading();
            _login = new LoginAsyncTask(DataBase.getDataBase(getActivity()).getUserData(), LoginFragment.this);
            _login.doLogin();
        }
        else {
            cancelLoading();
            //starting the main fragment
            Fragment fragment =  new IdeiasFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main, fragment);
            fragmentTransaction.commit();
        }
    }

    private void showError(String error) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(getString(R.string.error));
        alertDialog.setMessage(error);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    public void LoginResult(boolean result, User user) {
        cancelLoading();
        if(result) {
            if(!DataBase.getDataBase(getActivity()).getUserData().isValid()) {
                //Salvando, uma vez que não tem nada no DB
                DataBase.getDataBase(getActivity()).saveUserData(user);
            }
            goToIdeias(false);
        }
        else {
            showError(getString(R.string.login_error));
            showLogin(getView());
        }
    }

    private void showLoading(){
        _progressBar.setTitle(getString(R.string.loading_title));
        _progressBar.setMessage(getString(R.string.loading_message));
        _progressBar.setCancelable(false);
        _progressBar.show();

    }

    private void cancelLoading(){
        _progressBar.dismiss();
    }
}
