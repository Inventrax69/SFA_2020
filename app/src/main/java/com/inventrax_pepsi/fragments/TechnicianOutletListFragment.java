package com.inventrax_pepsi.fragments;

/*
 * Created by Padmaja on 04/07/2019.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import com.inventrax_pepsi.R;
import com.inventrax_pepsi.adapters.TechnicianOutletListAdapter;
import com.inventrax_pepsi.model.TechnicianOutletList;

import java.util.ArrayList;
import java.util.List;

public class TechnicianOutletListFragment extends Fragment {

    private View rootView;
    private static final String classCode = "OMS_Android_CustomerListFragmnet";

    private RecyclerView rvCustomerList;
    TextView Disconnected;
    LinearLayoutManager layoutManager;

    private List<TechnicianOutletList> technicianOutletList;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.technician_outlist_fragment, container, false);

        loadFormControl();

        return rootView;
    }

    private void loadFormControl() {

        try {


            layoutManager = new LinearLayoutManager(getContext());

            technicianOutletList = new ArrayList<>();

            rvCustomerList = (RecyclerView) rootView.findViewById(R.id.rvCustomerList);
            rvCustomerList.setLayoutManager(layoutManager);
            rvCustomerList.smoothScrollToPosition(0);

            outletList();


        }catch (Exception ex){
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void showOutletList(){

        TechnicianOutletListAdapter technicianOutletListAdapter = new TechnicianOutletListAdapter(getActivity(), technicianOutletList, new TechnicianOutletListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                OutletComplaintFragment outletComplaintFragment = new OutletComplaintFragment();
                Bundle args = new Bundle();
                args.putString("json", new Gson().toJson(technicianOutletList.get(pos)));
                outletComplaintFragment.setArguments(args);
                fragmentTransaction.replace( R.id.container_body, outletComplaintFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                //new FragmentUtils().replaceFragmentWithBackStack(getActivity(), R.id.container_body, new OutletComplaintFragment());
            }
        });

        rvCustomerList.setAdapter(technicianOutletListAdapter);

    }


    public void outletList(){

        technicianOutletList.add(new TechnicianOutletList("Anil","33025","125","Anil PNS",1,new String[]{"JobOrder2020"}));
        technicianOutletList.add(new TechnicianOutletList("Sri Lakshmi","33026","126","Sri Lakshmi Kgs",3,new String[]{"JobOrder2021","JobOrder2022","JobOrder2023"}));
        technicianOutletList.add(new TechnicianOutletList("Atchi Babu","33027","127","Atchi Babu Kgs",1,new String[]{"JobOrder2020"}));
        technicianOutletList.add(new TechnicianOutletList("Prabha","33028","128","Prabha Pan Shop",2,new String[]{"JobOrder2025","JobOrder2026"}));
        technicianOutletList.add(new TechnicianOutletList("Vasundhara","33029","129","Vasundhara Tiffens",1,new String[]{"JobOrder2020"}));
        technicianOutletList.add(new TechnicianOutletList("Gayatri","33030","130","Gayatri Kgs",2,new String[]{"JobOrder2027","JobOrder2028"}));

        showOutletList();
    }


    @Override
    public void onResume() {
        super.onResume();

       // ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Outlet List");
       // ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(false);


    }


}
