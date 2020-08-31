package com.inventrax_pepsi.services.gcm;

import android.app.Activity;
import android.content.Context;

import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.util.SoapUtils;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapPrimitive;

import java.util.ArrayList;
import java.util.List;

public class ShareExternalServer {

    SoapPrimitive mResponseBody;

    public String shareRegIdWithAppServer(final Context context,final Activity activity) {


        List<PropertyInfo> PropList = new ArrayList<PropertyInfo>();

        PropertyInfo piUserName = new PropertyInfo();
        piUserName.setName("UserID");
        piUserName.setValue("3");
        piUserName.setType(String.class);
        PropList.add(piUserName);

        PropertyInfo piPassword = new PropertyInfo();
        piPassword.setName("DeviceID");
        piPassword.setValue(AppController.DEVICE_GCM_REGISTER_ID);
        piPassword.setType(String.class);
        PropList.add(piPassword);

        mResponseBody = (SoapPrimitive) SoapUtils.callWebMethodAsString(PropList, "UpdateDeviceDetails", activity);

        if (mResponseBody == null) {

            return "";
        }

        return mResponseBody.toString();

    }

	/*private ShareExternalServer appUtil;
    private String regId;
	private AsyncTask shareRegidTask;
	private Activity activity;*/

	/*activity=this;
	appUtil = new ShareExternalServer();

	regId = getIntent().getStringExtra("regId");
	Log.d("MainActivity", "regId: " + regId);

	final Context context = this;

	new ShareRegIdAsyncTask().execute();*/


/*

	private class ShareRegIdAsyncTask extends AsyncTask<Void,Void,String>{

		@Override
		protected String doInBackground(Void... params) {

			String result="";
			try {
				result = appUtil.shareRegIdWithAppServer(getApplicationContext(), regId, activity);
			}catch (Exception ex){
				ex.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(getApplicationContext(), result,
					Toast.LENGTH_LONG).show();
		}
	}
*/

}
