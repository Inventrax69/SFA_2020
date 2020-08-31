package com.inventrax_pepsi.util;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by Naresh on 28-Feb-16.
 */
public class MaterialDialogUtils {

    private Context context;
    private MaterialDialog.Builder builder;
    private MaterialDialog materialDialog;
    public String positiveText="Ok";
    public String negativeText="Cancel";

    public MaterialDialogUtils(Context context){
        this.context=context;
        builder=new MaterialDialog.Builder(context);
    }

    public void showAlertDialog(String message){
        builder.content(message);
        builder.positiveText(positiveText);
        materialDialog=builder.build();
        materialDialog.show();
    }


}
