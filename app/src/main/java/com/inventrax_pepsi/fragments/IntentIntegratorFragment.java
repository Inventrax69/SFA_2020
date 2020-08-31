package com.inventrax_pepsi.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;

/**
 * Created by Naresh on 26-Jan-16.
 */
public class IntentIntegratorFragment extends IntentIntegrator {

    private final Fragment fragment;

    public IntentIntegratorFragment(Fragment fragment) {
        super(fragment.getActivity());
        this.fragment = fragment;
    }

    @Override
    protected void startActivityForResult(Intent intent, int code) {
        fragment.startActivityForResult(intent, code);
    }
}