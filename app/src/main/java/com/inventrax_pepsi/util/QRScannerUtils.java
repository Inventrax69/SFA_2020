package com.inventrax_pepsi.util;

import android.support.v4.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.inventrax_pepsi.activities.QRPortraitCaptureActivity;
import com.inventrax_pepsi.fragments.IntentIntegratorFragment;

/**
 * Created by Naresh on 26-Jan-16.
 */
public class QRScannerUtils {


    public static void scanQRCode(Fragment fragment){
        IntentIntegratorFragment integrator = new IntentIntegratorFragment(fragment);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setCaptureActivity(QRPortraitCaptureActivity.class);
        integrator.setPrompt("Scan QR Code");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

   /* public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {
            String re = scanResult.getContents();

            txtResponse.setText(re);
            DialogUtils.showAlertDialog(getActivity(),re);
        }
    }*/

}
