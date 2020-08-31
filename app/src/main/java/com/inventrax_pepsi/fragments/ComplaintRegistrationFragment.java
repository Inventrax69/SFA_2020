package com.inventrax_pepsi.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inventrax_pepsi.R;

/**
 * Created by android on 3/4/2016.
 */
public class ComplaintRegistrationFragment extends Fragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_complaint_registration, container, false);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_complaint_registration));

    }
}
