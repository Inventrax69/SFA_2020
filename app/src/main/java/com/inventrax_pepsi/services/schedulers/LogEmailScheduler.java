package com.inventrax_pepsi.services.schedulers;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

import com.inventrax_pepsi.application.AbstractApplication;

/**
 * Created by android on 5/2/2016.
 */
public class LogEmailScheduler extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = LogEmailScheduler.class.getName();
    private static final String MESSAGE="message";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public LogEmailScheduler(String name) {
        super(TAG);
    }



    @Override
    protected void onHandleIntent(Intent intent) {

        Toast.makeText(AbstractApplication.get(),"On Handled",Toast.LENGTH_LONG).show();

    }
}
