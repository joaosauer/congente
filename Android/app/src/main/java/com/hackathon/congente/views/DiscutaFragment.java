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
import com.hackathon.congente.thirdparty.CoverFlow;
import com.hackathon.congente.thirdparty.ImageAdapter;

public class DiscutaFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.principal, container, false);

        //setButtons(view);

        return view;
    }

//    private void setButtons(View view) {
//        TextView resolva = (TextView) view.findViewById(R.id.principal_resolva);
//        resolva.setOnClickListener(this);
//        TextView discuta = (TextView) view.findViewById(R.id.principal_discuta);
//        discuta.setOnClickListener(this);
//        TextView faca = (TextView) view.findViewById(R.id.principal_faca);
//        faca.setOnClickListener(this);
//        TextView inspire = (TextView) view.findViewById(R.id.principal_inspire);
//        inspire.setOnClickListener(this);
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        Fragment fragment = null;
//        if(RESOLVA == v.getTag()) {
//            fragment = new DiscutaFragment();
//        }
//        else if(DISCUTA == v.getTag()) {
//            fragment = new DiscutaFragment();
//        }
//        else if(FACA == v.getTag()) {
//            fragment = new FacaFragment();
//        }
//        else if(INSPIRE == v.getTag()) {
//            fragment = new InspireFragment();
//        }
//
////        Bundle args = new Bundle();
////        args.putSerializable("PlayersID", playersID);
////        fragment.setArguments(args);
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.main, fragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
//    }
}
