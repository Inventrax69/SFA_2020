package com.inventrax_pepsi.util;

import android.app.ProgressDialog;
import android.content.Context;

import com.inventrax_pepsi.application.AbstractApplication;

/**
 * Created by Naresh on 08-Jan-16.
 */
public class ProgressDialogUtils {

    private static ProgressDialog progressDialog;



    public ProgressDialogUtils(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
    }

    public ProgressDialogUtils() {
        progressDialog = new ProgressDialog(AbstractApplication.get());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
    }

    public ProgressDialogUtils(Context context, int dialogStyle) {
        progressDialog = new ProgressDialog(context, dialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
    }


    public static void showProgressDialog() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
    }

    public static void showProgressDialog(int progressStyle) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.setProgressStyle(progressStyle);
        progressDialog.show();
    }

    public static void showProgressDialog(int progressStyle,String message) {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.setMessage(message);
            progressDialog.setIndeterminate(true);
            progressDialog.setProgressStyle(progressStyle);
            progressDialog.show();
        }

    }

    public static void showProgressDialog(String message) {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    public static void closeProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public static ProgressDialog getProgressDialog(Context context) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            return progressDialog;
        } else {
            return progressDialog;
        }
    }

}
