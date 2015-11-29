package com.hackathon.congente.views;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackathon.congente.R;
import com.hackathon.congente.callbacks.IdeiaCallback;
import com.hackathon.congente.database.DataBase;
import com.hackathon.congente.datatype.Ideia;
import com.hackathon.congente.network.calls.CadastrarIdeiaAsyncTask;
import com.hackathon.congente.network.calls.LoginAsyncTask;
import com.hackathon.congente.thirdparty.CoverFlow;
import com.hackathon.congente.thirdparty.ImageAdapter;

public class AddIdeiaFragment extends Fragment implements IdeiaCallback {
    private ProgressDialog _progressBar;
    private CadastrarIdeiaAsyncTask _ideiaTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.add_ideia, container, false);
        _progressBar = new ProgressDialog(getActivity());

        setButtons(view);

        return view;
    }

    private void setButtons(View view) {
        ImageView cancelButton = (ImageView) view.findViewById(R.id.addideia_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });

        Button saveButton = (Button) view.findViewById(R.id.addideia_salvar);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ideia ideia = new Ideia();
                ideia.nome = ((EditText) getView().findViewById(R.id.addideia_nome)).getText().toString();
                ideia.problema = ((EditText) getView().findViewById(R.id.addideia_problema)).getText().toString();
                if (!ideia.isValid()) {
                    showError(getString(R.string.error_emptyideia));
                } else {
                    //Fazendo login
                    showLoading();
                    _ideiaTask = new CadastrarIdeiaAsyncTask(ideia, AddIdeiaFragment.this);
                    _ideiaTask.CadastrarIdeia();
                }
            }
        });
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
    public void CadastroResult(boolean result, Ideia user) {
        cancelLoading();
        if(result) {
            getFragmentManager().popBackStackImmediate();
        }
        else {
            showError(getString(R.string.login_error));
        }
    }
}
