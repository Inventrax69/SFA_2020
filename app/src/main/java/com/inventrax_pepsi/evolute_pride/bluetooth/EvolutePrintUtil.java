package com.inventrax_pepsi.evolute_pride.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.inventrax_pepsi.application.AbstractApplication;
import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.interfaces.DeliveryView;
import com.inventrax_pepsi.sfa.invoice_print.LineItem;
import com.inventrax_pepsi.sfa.invoice_print.PrintInvoice;
import com.prowesspride.api.Printer_GEN;
import com.prowesspride.api.Setup;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Hashtable;

/**
 * Created by Naresh on 04-Apr-16.
 */
public class EvolutePrintUtil {

    public static final byte REQUEST_DISCOVERY = 0x01;
    public static final byte REQUEST_ABOUT = 0x05;
    public static final int EXIT_ON_RETURN = 21;
    public final static String EXTRA_DEVICE_TYPE = "android.bluetooth.device.extra.DEVICE_TYPE";
    public static BluetoothAdapter mBT = BluetoothAdapter.getDefaultAdapter();
    public static BluetoothDevice mBDevice = null;
    final Context context= AbstractApplication.get();
    private AppController mGP=AppController.getInstance();
    private boolean mblBonded = false;
    private boolean blBleStatusBefore = false;
    private Activity activity;
    public static Hashtable<String, String> mhtDeviceInfo = new Hashtable<String, String>();
    private String sDevicetype;
    private Printer_GEN ptrGen;

    private PrintInvoice printInvoice;

    /**
     * CONST: device type bluetooth 2.1
     */
    public static final int DEVICE_TYPE_BREDR = 0x01;
    /**
     * CONST: device type bluetooth 4.0 ble
     */
    public static final int DEVICE_TYPE_BLE = 0x02;
    /**
     * CONST: device type bluetooth double mode
     */
    public static final int DEVICE_TYPE_DUMO = 0x03;

    static Setup impressSetUp = null;
    private boolean _bdiscoveryFinished;

    private DeliveryView deliveryView;


    public EvolutePrintUtil(Activity activity,DeliveryView deliveryView,PrintInvoice printInvoice){
        this.activity=activity;
        EvolutePrintUtil.impressSetUp=AppController.setup;
        mGP=AppController.getInstance();

        this.setPrintInvoice(printInvoice);
        this.setDeliveryView(deliveryView);

        new StartBluetoothDeviceTask().execute("");
    }

    public void setDeliveryView(DeliveryView deliveryView) {
        this.deliveryView = deliveryView;
    }

    public void setPrintInvoice(PrintInvoice printInvoice) {
        this.printInvoice = printInvoice;
    }


    public void connectToSocket(){
        try
        {
           // new ConnSocketTask().execute(mBDevice.getAddress());

            new PairTask().execute(mhtDeviceInfo.get("MAC"));

        }catch (Exception ex){

        }

    }

    private BroadcastReceiver _mPairingRequest = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice device = null;
            if (intent.getAction().equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() == BluetoothDevice.BOND_BONDED)
                    mblBonded = true;
                else
                    mblBonded = false;
            }
        }
    };

    // Turn on Bluetooth of the device
    private class StartBluetoothDeviceTask extends AsyncTask<String, String, Integer> {
        private static final int RET_BULETOOTH_IS_START = 0x0001;
        private static final int RET_BLUETOOTH_START_FAIL = 0x04;
        private static final int miWATI_TIME = 15;
        private static final int miSLEEP_TIME = 150;

        @Override
        public void onPreExecute() {

            blBleStatusBefore = mBT.isEnabled();
        }

        @Override
        protected Integer doInBackground(String... arg0) {
            int iWait = miWATI_TIME * 1000;
			/* BT isEnable */
            if (!mBT.isEnabled()) {
                mBT.enable();
                //Wait miSLEEP_TIME seconds, start the Bluetooth device before you start scanning
                while (iWait > 0) {
                    if (!mBT.isEnabled())
                        iWait -= miSLEEP_TIME;
                    else
                        break;
                    SystemClock.sleep(miSLEEP_TIME);
                }
                if (iWait < 0)
                    return RET_BLUETOOTH_START_FAIL;
            }
            return RET_BULETOOTH_IS_START;
        }

        /**
         * After blocking cleanup task execution
         */
        @Override
        public void onPostExecute(Integer result) {

            if (RET_BLUETOOTH_START_FAIL == result) {
                // Turning ON Bluetooth failed
                mBT.disable();

            } else if (RET_BULETOOTH_IS_START == result) {
                // Bluetooth of the device successfully started
                // Start the nearby device search
                // Display nearby devices

                 new scanDeviceTask().execute("");

            }
        }
    }


    private BroadcastReceiver _foundReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            /* bluetooth device profiles*/
            Hashtable<String, String> htDeviceInfo = new Hashtable<String, String>();
            Log.d("Evolute Print", ">>Scan for Bluetooth devices");
			/* get the search results */
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			/* create found device profiles to htDeviceInfo*/
            Bundle b = intent.getExtras();
            htDeviceInfo.put("RSSI", String.valueOf(b.get(BluetoothDevice.EXTRA_RSSI)));
            if (null == device.getName())
                htDeviceInfo.put("NAME", "Null");
            else
                htDeviceInfo.put("NAME", device.getName());
            htDeviceInfo.put("COD", String.valueOf(b.get(BluetoothDevice.EXTRA_CLASS)));
            if (device.getBondState() == BluetoothDevice.BOND_BONDED)
                htDeviceInfo.put("BOND", "Bounded");
            else
                htDeviceInfo.put("BOND", "UnBounded");
            String sDeviceType = String.valueOf(b.get(EXTRA_DEVICE_TYPE));
            if (!sDeviceType.equals("null"))
                htDeviceInfo.put("DEVICE_TYPE", sDeviceType);
            else
                htDeviceInfo.put("DEVICE_TYPE", "-1");

        }
    };

    /**
     * Bluetooth scanning is finished processing.(broadcast listener)
     */
    private BroadcastReceiver _finshedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Evolute Print", ">>Bluetooth scanning is finished");
            _bdiscoveryFinished = true; //set scan is finished
            activity.unregisterReceiver(_foundReceiver);
            activity.unregisterReceiver(_finshedReceiver);


        }
    };


    private class scanDeviceTask extends AsyncTask<String, String, Integer> {
        /**
         * Constants: Bluetooth is not turned on
         */
        private static final int RET_BLUETOOTH_NOT_START = 0x0001;
        /**
         * Constant: the device search complete
         */
        private static final int RET_SCAN_DEVICE_FINISHED = 0x0002;
        /**
         * Wait a Bluetooth device starts the maximum time (in S)
         */
        private static final int miWATI_TIME = 10;
        /**
         * Every thread sleep time (in ms)
         */
        private static final int miSLEEP_TIME = 150;


        @Override
        public void onPreExecute() {

            _bdiscoveryFinished = true;

            startSearch();
        }

        @Override
        protected Integer doInBackground(String... params) {
            if (!mBT.isEnabled())
                return RET_BLUETOOTH_NOT_START;
            int iWait = miWATI_TIME * 1000;
            //Wait miSLEEP_TIME seconds, start the Bluetooth device before you start scanning
            while (iWait > 0) {
                if (_bdiscoveryFinished)
                    return RET_SCAN_DEVICE_FINISHED;
                else
                    iWait -= miSLEEP_TIME;
                SystemClock.sleep(miSLEEP_TIME);
                ;
            }
            return RET_SCAN_DEVICE_FINISHED;
        }

        @Override
        public void onProgressUpdate(String... progress) {
        }

        @Override
        public void onPostExecute(Integer result) {

            if (mBT.isDiscovering())
                mBT.cancelDiscovery();

            if (RET_SCAN_DEVICE_FINISHED == result) {

                connectToSocket();

            } else if (RET_BLUETOOTH_NOT_START == result) {
                // Bluetooth Not Started
            }
        }
    }

    private void startSearch() {
        _bdiscoveryFinished = false;

		/* Register Receiver*/
        IntentFilter discoveryFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        activity.registerReceiver(_finshedReceiver, discoveryFilter);
        IntentFilter foundFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        activity.registerReceiver(_foundReceiver, foundFilter);
        mBT.startDiscovery();//start scan

    }


    /*   This method shows the PairTask  PairTask operation */
    private class PairTask extends AsyncTask<String, String, Integer> {
        /**
         * Constants: the pairing is successful
         */
        static private final int RET_BOND_OK = 0x00;
        /**
         * Constants: Pairing failed
         */
        static private final int RET_BOND_FAIL = 0x01;
        /**
         * Constants: Pairing waiting time (15 seconds)
         */
        static private final int iTIMEOUT = 1000 * 15;

        /**
         * Thread start initialization
         */
        @Override
        public void onPreExecute() {

            activity.registerReceiver(_mPairingRequest, new IntentFilter(BluetoothPair.PAIRING_REQUEST));
            activity.registerReceiver(_mPairingRequest, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));
        }

        /* Task of PairTask performing in the background*/
        @Override
        protected Integer doInBackground(String... arg0) {
            final int iStepTime = 150;
            int iWait = iTIMEOUT;
            try {
                mBDevice = mBT.getRemoteDevice(arg0[0]);//arg0[0] is MAC address
                BluetoothPair.createBond(mBDevice);
                mblBonded = false;
            } catch (Exception e1) {
                Log.d("Evolute Print", "create Bond failed!");
                e1.printStackTrace();
                return RET_BOND_FAIL;
            }
            while (!mblBonded && iWait > 0) {
                SystemClock.sleep(iStepTime);
                iWait -= iStepTime;
            }
            if (iWait > 0) {
                //RET_BOND_OK
                Log.e("Application", "create Bond failed! RET_BOND_OK ");
            } else {
                //RET_BOND_FAIL
                Log.e("Application", "create Bond failed! RET_BOND_FAIL ");
            }
            return (int) ((iWait > 0) ? RET_BOND_OK : RET_BOND_FAIL);
        }

        /* This displays the status messages of PairTask in the dialog box */
        @Override
        public void onPostExecute(Integer result) {
            activity.unregisterReceiver(_mPairingRequest);
            if (RET_BOND_OK == result) {
                mhtDeviceInfo.put("BOND", "Bonded");
            } else {

                try {
                    BluetoothPair.removeBond(mBDevice);
                } catch (Exception e) {

                    e.printStackTrace();
                }

                new ConnSocketTask().execute(mBDevice.getAddress());
            }
        }
    }


    /*   This method shows the connSocketTask  PairTask operation */
    private class ConnSocketTask extends AsyncTask<String, String, Integer> {
        /**
         * Constants: connection fails
         */
        private static final int CONN_FAIL = 0x01;
        /**
         * Constant: the connection is established
         */
        private static final int CONN_SUCCESS = 0x02;


        /**
         * Thread start initialization
         */
        @Override
        public void onPreExecute() {

        }

        /* Task of connSocketTask performing in the background*/
        @Override
        protected Integer doInBackground(String... arg0) {
            if (mGP.createConn(arg0[0]))
                return CONN_SUCCESS;
            else
                return CONN_FAIL;
        }

        /* This displays the status messages of connSocketTask in the dialog box */
        @Override
        public void onPostExecute(Integer result) {

            if (CONN_SUCCESS == result) {

                try {
                    //Reading device serial number
                    sDevicetype = impressSetUp.sGetDevSerNo(BluetoothComm.mosOut, BluetoothComm.misIn);
                    Log.e("Setup ", "DEVICE TYPE.........>" + sDevicetype);
                    sDevicetype = sDevicetype.substring(0, 2);
                    Log.e("Setup ", "DEVICE TYPE.........>" + sDevicetype);

                    new PrintTask().execute("");

                } catch (Exception ex){
                    ex.printStackTrace();
                }

            } else {
               // On Failure
            }
        }
    }


    private class PrintTask extends AsyncTask<String, String, Integer> {

        @Override
        protected void onPreExecute() {
            deliveryView.showProgress();
        }

        @Override
        protected Integer doInBackground(String... params) {

            int value = 0;

            try {

                InputStream input = BluetoothComm.misIn;
                OutputStream outstream = BluetoothComm.mosOut;
                ptrGen = new Printer_GEN(impressSetUp, outstream, input);

                if (printInvoice==null || input ==null || outstream == null )
                    return value;

                ptrGen.iFlushBuf();

                DecimalFormat format = new DecimalFormat();
                format.setMaximumFractionDigits(2);
                format.setMinimumFractionDigits(2);

                ptrGen.iAddData(Printer_GEN.FONT_LARGE_BOLD, getArrangedText(true, printInvoice.getHeader().getTitle().trim()));
                ptrGen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, printInvoice.getHeader().getDoorNo().trim() + ", " + printInvoice.getHeader().getStreet().trim() + ", " + printInvoice.getHeader().getArea().trim() + "," + printInvoice.getHeader().getCity());
                ptrGen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, getArrangedText(false, "TIN:" + printInvoice.getHeader().getTin() + "   CIN: " + printInvoice.getHeader().getCin()));
                ptrGen.iAddData(Printer_GEN.FONT_LARGE_BOLD, PrintInvoice.getHorizontalLine());
                ptrGen.iAddData(Printer_GEN.FONT_SMALL_BOLD, getArrangedText(true, printInvoice.getOutletInfo().getOutletName() + "[" + printInvoice.getOutletInfo().getOutletCode() + "] "));
                ptrGen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, printInvoice.getOutletInfo().getRouteName() + "[" + printInvoice.getOutletInfo().getRouteCode() + "]");
                ptrGen.iAddData(Printer_GEN.FONT_LARGE_BOLD, PrintInvoice.getHorizontalLine());
                ptrGen.iAddData(Printer_GEN.FONT_SMALL_BOLD, "Inv. No: " + printInvoice.getInvoiceNumber() + "  Dt: " + printInvoice.getInvoiceDate());
                ptrGen.iAddData(Printer_GEN.FONT_LARGE_BOLD, PrintInvoice.getHorizontalLine());
                ptrGen.iAddData(Printer_GEN.FONT_SMALL_BOLD, "S.No. Item              Qty/UoM      Amt ");
                ptrGen.iAddData(Printer_GEN.FONT_LARGE_BOLD, PrintInvoice.getHorizontalLine());
                int count = 0;
                float subTotal = 0;
                for (LineItem line : printInvoice.getLineItems()) {
                    count++;
                    String countStr = "";
                    if(String.valueOf(count).length() == 1) {
                        countStr += count + ")" + "  ";
                    } else if (String.valueOf(count).length() == 2) {
                        countStr += count + ")" + " ";
                    }

                    ptrGen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, ""
                            + countStr
                            + line.getItem() + new String(new char[22 - line.getItem().length()]).replace('\0', ' ')
                            + line.getQty() + new String(new char[7 - line.getQty().length()]).replace('\0', ' ')
                            + new String(new char[9 - String.valueOf(format.format(line.getAmt())).length()]).replace('\0', ' ') + format.format(line.getAmt()));
                    ptrGen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, " ");
                    subTotal += line.getAmt();
                }

                ptrGen.iAddData(Printer_GEN.FONT_LARGE_BOLD, PrintInvoice.getHorizontalLine());
                ptrGen.iAddData(Printer_GEN.FONT_SMALL_BOLD, "Sub Total" + new String(new char[20]).replace('\0', ' ') + "Rs. " + format.format(subTotal));
                ptrGen.iAddData(Printer_GEN.FONT_LARGE_BOLD, PrintInvoice.getHorizontalLine());
                ptrGen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, "This Invoice value includes VAT 5% on Fruit Drinks & 14.5% CSD items");
                ptrGen.iAddData(Printer_GEN.FONT_LARGE_BOLD, PrintInvoice.getHorizontalLine());
                ptrGen.iAddData(Printer_GEN.FONT_LARGE_NORMAL,  " *** CUSTOMER COPY *** ");
                ptrGen.iAddData(Printer_GEN.FONT_LARGE_NORMAL,  "       THANK YOU!       ");
                ptrGen.iAddData(Printer_GEN.FONT_LARGE_BOLD, PrintInvoice.getHorizontalLine());
                ptrGen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, " Inventrax SFA Ver. 1.0 ");


                if (ptrGen.iStartPrinting(1)==0){
                    value=1;
                }

            } catch (Exception e) {

                Logger.Log(EvolutePrintUtil.class.getName(), e);
                e.printStackTrace();


            }

            return value;
        }

        @Override
        protected void onPostExecute(Integer integer) {

            deliveryView.closeProgress();

            if (integer==1){

                deliveryView.showPrintStatus(1,"Successfully Printed");

            }else {

                deliveryView.showPrintStatus(0,"Error while printing");

            }

        }
    }

    public static String getArrangedText(boolean largeFont, String text) {
        int small = 42;
        int large = 24;
        int empty = 0;
        try {
            StringBuilder data = new StringBuilder();
            if (largeFont && text != null) {
                empty = (large - text.trim().length()) / 2;
            } else {
                empty = (small - text.trim().length()) / 2;
            }

            System.out.println("The empry Storage is :::" + empty);

            for (int i = 0; i <= empty-1; i++) {
                data.append(" ");
            }

            data.append(text.trim());

            for (int i = 0; i <= empty-1; i++) {
                data.append(" ");
            }

            text = data.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

}
