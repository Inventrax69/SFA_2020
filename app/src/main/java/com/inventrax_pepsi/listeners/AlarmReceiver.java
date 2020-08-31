package com.inventrax_pepsi.listeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.inventrax_pepsi.application.AbstractApplication;

/**
 * Created by android on 5/3/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {

    //private static long TIME_INTERVAL = 60 * 60 * 1000 ; // For Production 1 Hour

    private static long TIME_INTERVAL = 60 * 1000 ; // For Production 1 Minute

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Alarm running", Toast.LENGTH_SHORT).show();

       // new EmailAsyncTask().execute();

    }


    private class EmailAsyncTask extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected void onPreExecute() {



        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try
            {

                final Intent emailIntent = new Intent(
                        android.content.Intent.ACTION_SEND);
//
                String address = "nareshp@inventrax.com";
                String subject = "Error Log";
                String emailtext = "Please check the attached document";
//
                emailIntent.setType("plain/text");

                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                        new String[] { address });

                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);

                //emailIntent.putExtra(Intent.EXTRA_STREAM,Uri.parse("file://" + strFile));

                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, emailtext);

                AbstractApplication.get().startActivity( Intent.createChooser(emailIntent,"Sending Log Email"));


            }catch (Exception ex){

                return false;
            }


            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {


        }
    }



}
