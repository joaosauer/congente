package com.hackathon.congente.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hackathon.congente.R;
import com.hackathon.congente.database.DataBase;

public class PrincipalFragment extends Fragment implements View.OnClickListener{
    public static int RESOLVA   = 0;
    public static int DISCUTA   = 1;
    public static int FACA      = 2;
    public static int INSPIRE   = 3;

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
        resolva.setTag(RESOLVA);
        resolva.setOnClickListener(this);
        TextView discuta = (TextView) view.findViewById(R.id.principal_discuta);
        discuta.setTag(DISCUTA);
        discuta.setOnClickListener(this);
        TextView faca = (TextView) view.findViewById(R.id.principal_faca);
        faca.setTag(FACA);
        faca.setOnClickListener(this);
        TextView inspire = (TextView) view.findViewById(R.id.principal_inspire);
        inspire.setTag(INSPIRE);
        inspire.setOnClickListener(this);
        ImageView menuButton = (ImageView) view.findViewById(R.id.principal_menu);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout menu = (LinearLayout) getView().findViewById(R.id.menu_view);
                menu.setVisibility(View.VISIBLE);
            }
        });
        ImageView menuCloseButton = (ImageView) view.findViewById(R.id.principal_menu_aberto);
        menuCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout menu = (LinearLayout) getView().findViewById(R.id.menu_view);
                menu.setVisibility(View.GONE);
            }
        });
        ListView menuList = (ListView) view.findViewById(R.id.menu_list);
        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = getResources().getStringArray(R.array.nav_drawer_items)[position];
                if(text.compareTo("Logout") == 0) {
                    DataBase.getDataBase(getActivity()).deleteUserData();

                    //retornando para o começo do app
                    Fragment fragment =  new LoginFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main, fragment);
                    fragmentTransaction.commit();
                }
                else if(text.compareTo("Perfil") == 0) {

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        if(RESOLVA == v.getTag()) {
            fragment = new ResolvaFragment();
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
