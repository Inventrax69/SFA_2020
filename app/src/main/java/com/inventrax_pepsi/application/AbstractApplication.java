package com.inventrax_pepsi.application;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

/**
 * Author   : Naresh P.
 * Date		: 07/01/2016 11:03 AM
 * Purpose	: Abstract Application
 */


public class AbstractApplication {

    public static Context CONTEXT;
    public static FragmentActivity FRAGMENT_ACTIVITY;

    public static Context get(){
       return CONTEXT;
    }

    public static FragmentActivity getFragmentActivity(){
        return  FRAGMENT_ACTIVITY;
    }


}
