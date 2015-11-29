package com.hackathon.congente.network.calls;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hackathon.congente.callbacks.LoginCallback;
import com.hackathon.congente.datatype.BooleanResult;
import com.hackathon.congente.datatype.User;
import com.hackathon.congente.network.NetworkAccess;

public class LoginAsyncTask extends NetworkAccess {
    private String URL_LOGIN = "http://congente.jedevmobile.com/api/Login.php";
    private String URL_CREATE = "http://congente.jedevmobile.com/api/CreateUser.php";
    private User _login = null;
    private LoginCallback _callback;
    private boolean _isNewLogin;

    public LoginAsyncTask(User login, LoginCallback callback){
        _login = login;
        _callback = callback;
    }

    public void doLogin() {
        _isNewLogin = false;
        execute();
    }

    public void createLogin() {
        _isNewLogin = true;
        execute();
    }

    @Override
    protected boolean isPOST() {
        return true;
    }

    @Override
    protected String getPostData() {
        Gson gson = new Gson();
        return gson.toJson(_login);
    }

    @Override
    protected void dataReceived(String json) {
        if(json != null && !json.isEmpty()) {
            Gson gson = new GsonBuilder().create();
            BooleanResult result = gson.fromJson(json, BooleanResult.class);
            _callback.LoginResult(result.result,_login);
        }
        else {
            _callback.LoginResult(false,_login);
        }
    }

    @Override
    protected String getURL() {
        if(_isNewLogin)
            return URL_CREATE;
        return URL_LOGIN;
    }
}
