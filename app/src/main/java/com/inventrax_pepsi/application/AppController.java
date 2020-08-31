package com.inventrax_pepsi.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.inventrax_pepsi.R;
import com.inventrax_pepsi.common.constants.ServiceURLConstants;
import com.inventrax_pepsi.evolute_pride.bluetooth.BluetoothComm;
import com.inventrax_pepsi.sfa.pojos.LoginInfo;
import com.inventrax_pepsi.sfa.pojos.User;
import com.prowesspride.api.Setup;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

import java.util.Map;

/**
 * Author   : Naresh P.
 * Date		: 04/03/2016 11:03 AM
 * Purpose	: Application Controller
 */

@ReportsCrashes(formUri = ServiceURLConstants.APP_CRASH_REPORT_PATH, socketTimeout = 5000, httpMethod = HttpSender.Method.POST, formKey ="",customReportContent = { ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME, ReportField.ANDROID_VERSION,ReportField.BRAND, ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.LOGCAT },mode = ReportingInteractionMode.TOAST,resToastText = R.string.res_acra_toast_text)
public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();
    public static String DEVICE_GCM_REGISTER_ID;
    private static AppController mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static User user;
    private static LoginInfo loginInfo;
    private static Context appContext;
    public static  Setup setup=null;
    public static Map<String,String> mapUserRoutes;

    /**
     * Bluetooth communication connection object
     */
    public BluetoothComm mBTcomm = null;
    public boolean connection = false;


    public static synchronized AppController getInstance() {
        return mInstance;
    }


    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        MultiDex.install(this);
        ACRA.init(this);
        AbstractApplication.CONTEXT = getApplicationContext();
        appContext= getApplicationContext();

        //LocaleHelper.onCreate(this, "te");


    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        AppController.user = user;
    }

    public static LoginInfo getLoginInfo() {
        return loginInfo;
    }

    public static void setLoginInfo(LoginInfo loginInfo) {
        AppController.loginInfo = loginInfo;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


    public boolean createConn(String sMac) {
        if (null == this.mBTcomm) {
            this.mBTcomm = new BluetoothComm(sMac);
            if (this.mBTcomm.createConn()) {
                connection = true;
                return true;
            } else {
                this.mBTcomm = null;
                connection = false;
                return false;
            }
        } else
            return true;
    }

    /**
     * Close and release the connection
     *
     * @return void
     */
    public void closeConn() {
        if (null != this.mBTcomm) {
            this.mBTcomm.closeConn();
            this.mBTcomm = null;
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

    }


}