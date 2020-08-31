package com.inventrax_pepsi.services.sfa_background_services;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;

/**
 * Created by Naresh on 21-Mar-16.
 */
public class CustomerOrderHistoryResultReceiver extends ResultReceiver {
    /**
     * Create a new ResultReceive to receive results.  Your
     * {@link #onReceiveResult} method will be called from the thread running
     * <var>handler</var> if given, or from an arbitrary thread if null.
     *
     * @param handler
     */

    private Receiver onReceiveResult;

    public CustomerOrderHistoryResultReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver) {
        onReceiveResult = receiver;
    }

    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle resultData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (onReceiveResult != null) {
            onReceiveResult.onReceiveResult(resultCode, resultData);
        }
    }

}