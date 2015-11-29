package com.hackathon.congente.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hackathon.congente.R;

public class FacaFragment extends Fragment implements View.OnClickListener{
    private static int RESOLVA   = 0;
    private static int DISCUTA   = 1;
    private static int FACA      = 2;
    private static int INSPIRE   = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.principal, container, false);

//        CoverFlow coverFlow = (CoverFlow) view.findViewById(R.id.principal_coverflow);
//        coverFlow.setAdapter(new ImageAdapter(getActivity()));
//
//        ImageAdapter coverImageAdapter =  new ImageAdapter(getActivity());
//
//        coverFlow.setAdapter(coverImageAdapter);
//
//        //coverFlow.setSpacing(-25);
//        coverFlow.setSelection(4, true);
//        coverFlow.setAnimationDuration(1000);

        setButtons(view);

        return view;
    }

    private void setButtons(View view) {
        TextView resolva = (TextView) view.findViewById(R.id.principal_resolva);
        resolva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        if(RESOLVA == v.getTag()) {
            fragment = new FacaFragment();
        }
        else if(DISCUTA == v.getTag()) {
            fragment = new DiscutaFragment();
        }
        else if(FACA == v.getTag()) {
            fragment = new FacaFragment();
        }
        else if(INSPIRE == v.getTag()) {
            fragment = new InspireFragment();
        }

//        Bundle args = new Bundle();
//        args.putSerializable("PlayersID", playersID);
//        fragment.setArguments(args);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
