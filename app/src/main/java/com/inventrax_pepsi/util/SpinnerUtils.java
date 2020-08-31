package com.inventrax_pepsi.util;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created by Naresh on 25-Jan-16.
 */
public class SpinnerUtils {

    public static Spinner getSpinner(Activity activity,String title , Spinner spinner ,ArrayList<String> stringArrayList ){
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_item, stringArrayList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setPrompt(title);

        return spinner;
    }


}
