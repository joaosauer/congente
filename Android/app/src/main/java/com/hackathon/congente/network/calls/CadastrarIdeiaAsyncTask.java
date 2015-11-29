package com.hackathon.congente.network.calls;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hackathon.congente.callbacks.IdeiaCallback;
import com.hackathon.congente.callbacks.LoginCallback;
import com.hackathon.congente.datatype.BooleanResult;
import com.hackathon.congente.datatype.Ideia;
import com.hackathon.congente.datatype.User;
import com.hackathon.congente.network.NetworkAccess;

public class CadastrarIdeiaAsyncTask extends NetworkAccess {
    private String URL_CADASTRAR_IDEIA = "http://congente.jedevmobile.com/api/CadastrarIdeia.php";
    //private String URL_CREATE = "http://congente.jedevmobile.com/api/CreateUser.php";
    private Ideia _ideia = null;
    private IdeiaCallback _callback;

    public CadastrarIdeiaAsyncTask(Ideia ideia, IdeiaCallback callback){
        _ideia = ideia;
        _callback = callback;
    }

    public void CadastrarIdeia() {
        execute();
    }

    public void createLogin() {
        execute();
    }

    @Override
    protected boolean isPOST() {
        return true;
    }

    @Override
    protected String getPostData() {
        Gson gson = new Gson();
        return gson.toJson(_ideia);
    }

    @Override
    protected void dataReceived(String json) {
        if(json != null && !json.isEmpty()) {
            Gson gson = new GsonBuilder().create();
            BooleanResult result = gson.fromJson(json, BooleanResult.class);
            _callback.CadastroResult(result.result, _ideia);
        }
        else {
            _callback.CadastroResult(false, _ideia);
        }
    }

    @Override
    protected String getURL() {
        return URL_CADASTRAR_IDEIA;
    }
}
