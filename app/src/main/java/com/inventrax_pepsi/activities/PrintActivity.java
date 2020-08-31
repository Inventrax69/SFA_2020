package com.inventrax_pepsi.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleAdapter;

import com.inventrax_pepsi.R;
import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.sfa.invoice_print.InvoiceHeader;
import com.inventrax_pepsi.sfa.invoice_print.LineItem;
import com.inventrax_pepsi.sfa.invoice_print.OutletInfo;
import com.inventrax_pepsi.sfa.invoice_print.PrintInvoice;
import com.inventrax_pepsi.sfa.invoice_print.PrintUtil;
import com.prowesspride.api.Printer_GEN;
import com.prowesspride.api.Setup;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;

/**
 * Author   : Naresh P.
 * Date		: 26/04/2016 11:03 AM
 * Purpose	: Print Activity
 */

public class PrintActivity extends AppCompatActivity {


    private Button btnPrint;
    private BluetoothAdapter adapter = null;
    private BluetoothSocket socket = null;
    private static BroadcastReceiver foundReceiver = null;
    public static InputStream bluetoothInputStream = null;
    public static OutputStream bluetoothOutputStream = null;
    private boolean discoveryFinished;
    private Hashtable<String, Hashtable<String, String>> bluetoothDeviceFound = null;
    private ArrayList<HashMap<String, Object>> showListItem = null;
    private boolean bluetoothDiscoveryFinished = false;
    private SimpleAdapter listItemAdapter = null;
    private static Setup setup;

    public final static String EXTRA_DEVICE_TYPE = "android.bluetooth.device.extra.DEVICE_TYPE";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            setup = new Setup();

            boolean result = setup.blActivateLibrary(this, R.raw.licencefull_pride_gen);

            Log.v("Bl Result : ", "" + (result == true ? "Yes" : "No"));

        }catch (Exception ex){
            Logger.Log(AppController.class.getName(), ex);
        }

        setContentView(R.layout.activity_print);

        btnPrint=(Button)findViewById(R.id.btnPrint);
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    // Activate bluetooth in the current device
                    adapter = BluetoothAdapter.getDefaultAdapter();
                    adapter.startDiscovery();
                    if (!adapter.isEnabled()) {
                        startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 0);
                    }
                    // Search for the bluetooth devices
                    new ScanDeviceTask().execute("");

                    /*Intent i = new Intent(MainActivity.this, PrintActivity.class);
                    startActivity(i);
                    */

                    new PrintTask().execute();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });



    }


    private class ScanDeviceTask extends AsyncTask<String, String, Integer> {

        private static final int RET_BLUETOOTH_NOT_START = 0x0001;
        private static final int RET_SCAN_DEVICE_FINISHED = 0x0002;
        private static final int miWATI_TIME = 10;
        private static final int miSLEEP_TIME = 150;

        @Override
        protected Integer doInBackground(String... params) {
            if (!adapter.isEnabled())
                return RET_BLUETOOTH_NOT_START;
            int iWait = miWATI_TIME * 1000;
            //Wait miSLEEP_TIME seconds, start the Bluetooth device before you start scanning
            while (iWait > 0) {
                if (discoveryFinished)
                    return RET_SCAN_DEVICE_FINISHED;
                else
                    iWait -= miSLEEP_TIME;
                SystemClock.sleep(miSLEEP_TIME);
            }
            return RET_SCAN_DEVICE_FINISHED;

        }

        @Override
        public void onPreExecute() {
            communicate();
            startSearch();
        }
    }

    private void communicate() {
        try {
            Set<BluetoothDevice> devices = adapter.getBondedDevices();
            System.out.println("The Broadcast receiver is : " + foundReceiver);
            for (BluetoothDevice bluetoothDevice : devices) {
                if (bluetoothDevice != null && bluetoothDevice.getName() != null && bluetoothDevice.getName().trim().contains("ESBAA")) {
                    boolean paired = bluetoothDevice.createBond();
                    System.out.println("Paired Status : " + paired);
                    for (ParcelUuid parcelUuid : bluetoothDevice.getUuids()) {
                        socket = bluetoothDevice.createRfcommSocketToServiceRecord(parcelUuid.getUuid());
                        socket.connect();
                        System.out.println("Paired Status : " + paired);
                        bluetoothInputStream = socket.getInputStream();
                        bluetoothOutputStream = socket.getOutputStream();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void startSearch() {
        try {

            foundReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
            /* bluetooth device profiles*/

                    Hashtable<String, String> deviceInfo = new Hashtable<String, String>();
            /* get the search results */
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if(device != null) {
                        Bundle bundle = intent.getExtras();

                        if(bundle != null) {

                            deviceInfo.put("RSSI", String.valueOf(bundle.get(BluetoothDevice.EXTRA_RSSI)));
                            if (device.getName() == null || device.getName().isEmpty())
                                deviceInfo.put("NAME", "Null");
                            else
                                deviceInfo.put("NAME", device.getName());
                            deviceInfo.put("COD", String.valueOf(bundle.get(BluetoothDevice.EXTRA_CLASS)));
                            if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                                System.out.println("bonded");
                                deviceInfo.put("BOND", "Bounded");
                            } else {
                                System.out.println("nothing bonded");
                                deviceInfo.put("BOND", "Not Bounded");
                            }
                            String deviceType = String.valueOf(bundle.get(EXTRA_DEVICE_TYPE));
                            if (deviceType != null && !deviceType.equals("null"))
                                deviceInfo.put("DEVICE_TYPE", deviceType);
                            else
                                deviceInfo.put("DEVICE_TYPE", "-1");
            /*adding scan to the device profiles*/
                            if (bluetoothDeviceFound == null) {
                                bluetoothDeviceFound = new Hashtable<String, Hashtable<String, String>>();
                            }
                            bluetoothDeviceFound.put(device.getAddress(), deviceInfo);
                        }
                    }

                    System.out.println("The device info : " + deviceInfo);

			/*Refresh show list*/
                    showDevices();
                }
            };


            bluetoothDiscoveryFinished = false;

            System.out.println("bluetoothDeviceFound" + bluetoothDeviceFound);

            if (bluetoothDeviceFound == null) {
                bluetoothDeviceFound = new Hashtable<String, Hashtable<String, String>>();
                bluetoothDeviceFound.clear();
                IntentFilter fliter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                registerReceiver(foundReceiver, fliter);
                adapter.startDiscovery();

            } else {
                System.out.println("not going to if condition");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // To show the devices found
    private void showDevices() {
        try {

            System.out.println("Control coming here");

            if (showListItem == null) {
                showListItem = new ArrayList<HashMap<String, Object>>();
            }

            if (listItemAdapter == null) {
                listItemAdapter = new SimpleAdapter(
                        getApplicationContext(),
                        showListItem,
                        R.layout.activity_main,
                        new String[]{
                                "NAME",
                                "MAC",
                                "COD",
                                "RSSI",
                                "DEVICE_TYPE",
                                "BOND"
                        },
                        new int[]{
                                R.id.device_item_ble_name,
                                R.id.device_item_ble_mac,
                                R.id.device_item_ble_cod,
                                R.id.device_item_ble_rssi,
                                R.id.device_item_ble_device_type,
                                R.id.device_item_ble_bond
                        });
            }
            showListItem.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // Pairing a bluetooth device
    private boolean pairDevice(BluetoothDevice device) {
        boolean paired = false;
        try {
            Class<? extends BluetoothDevice> bluetoothDeviceClass = device.getClass();
            Method createBondMethod = bluetoothDeviceClass.getMethod("createBond");
            Boolean returnValue = (Boolean) createBondMethod.invoke(device);
            paired = returnValue.booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paired;
    }

    // Unpair a device from bluetooth

    private boolean unPairDevice(BluetoothDevice device) {
        boolean unpaired = false;
        try {
            Class<? extends BluetoothDevice> bluetoothDeviceClass = device.getClass();
            Method removeBondMethod = bluetoothDeviceClass.getMethod("removeBond");
            Boolean returnValue = (Boolean) removeBondMethod.invoke(device);
            unpaired = returnValue.booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return unpaired;
    }




    // Set bluetooth Pin for the connecting device

    public boolean setPin(BluetoothDevice device, String pin) {
        boolean setted = false;
        try {
            Class<? extends BluetoothDevice> bluetoothDeviceClass = device.getClass();
            Method removeBondMethod = bluetoothDeviceClass.getDeclaredMethod("setPin", new Class[]{byte[].class});
            Boolean returnValue = (Boolean) removeBondMethod.invoke(device, new Object[]{pin.getBytes()});
            setted = returnValue.booleanValue();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return setted;
    }

    // Cancel pairing user input

    public boolean cancelPairingUserInput(BluetoothDevice device) {
        boolean cancelled = false;
        try {
            Class<? extends BluetoothDevice> bluetoothDeviceClass = device.getClass();
            Method createBondMethod = bluetoothDeviceClass.getMethod("cancelPairingUserInput");
            Boolean returnValue = (Boolean) createBondMethod.invoke(device);
            cancelled = returnValue.booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cancelled;
    }

    // Cancel Pairing

    public boolean cancelPairing(BluetoothDevice device) {
        boolean cancelled = false;
        try {
            Class<? extends BluetoothDevice> bluetoothDeviceClass = device.getClass();
            Method createBondMethod = bluetoothDeviceClass.getMethod("cancelBondProcess");
            Boolean returnValue = (Boolean) createBondMethod.invoke(device);
            cancelled = returnValue.booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cancelled;
    }



    private class PrintTask extends AsyncTask<String, String, Integer> {


        @Override
        protected Integer doInBackground(String... params) {

            int value = 0;
            try {


                System.out.println("The device serial number is : " + PrintActivity.setup.sGetDevSerNo(PrintActivity.bluetoothOutputStream, PrintActivity.bluetoothInputStream));
                Printer_GEN gen = new Printer_GEN(PrintActivity.setup, PrintActivity.bluetoothOutputStream, PrintActivity.bluetoothInputStream);


                DecimalFormat format = new DecimalFormat();
                format.setMaximumFractionDigits(2);
                format.setMinimumFractionDigits(2);

                PrintInvoice invoice = new PrintInvoice();

                InvoiceHeader header = new InvoiceHeader();
                invoice.setHeader(header);

                header.setTitle("Pearl Bottling Pvt. Ltd.");
                header.setDoorNo("Stock Point: D.No. 1-103");
                header.setStreet("Musalaiah Palem");
                header.setArea("Sagar Nagar, Yandada Post");
                header.setCity("Visakhapatnam");
                header.setPincode("530045");
                header.setTin("37200185878");
                header.setCin("");



                OutletInfo outletInfo = new OutletInfo();
                invoice.setOutletInfo(outletInfo);

                outletInfo.setOutletName("Balaji Pan Shop");
                outletInfo.setOutletCode("123456");
                outletInfo.setRouteName("M.V.P colony");
                outletInfo.setRouteCode("SNR-10");
                outletInfo.setCityName("Visakhapatnam");

                invoice.setInvoiceNumber("1234433456743");
                invoice.setInvoiceDate("01-04-2016");


                if(invoice.getLineItems() == null) {
                    invoice.setLineItems(new ArrayList<LineItem>());
                }
                LineItem lineItem = new LineItem();
                lineItem.setItem("Mirinda (O) 200ML");
                lineItem.setQty("4/CS");
                lineItem.setAmt((float) 960.00);
                invoice.getLineItems().add(lineItem);

                lineItem = new LineItem();
                lineItem.setItem("7Up 200ML ");
                lineItem.setQty("2/BT");
                lineItem.setAmt((float) 0.00);
                invoice.getLineItems().add(lineItem);

                lineItem = new LineItem();
                lineItem.setItem("7Up Revive 200ML");
                lineItem.setQty("60/CS");
                lineItem.setAmt((float) 14400.00);
                invoice.getLineItems().add(lineItem);

                lineItem = new LineItem();
                lineItem.setItem("Mirinda (O) 300ML");
                lineItem.setQty("3/CS");
                lineItem.setAmt((float) 1440.00);
                invoice.getLineItems().add(lineItem);

                lineItem = new LineItem();
                lineItem.setItem("Slice 250ML");
                lineItem.setQty("3/CS");
                lineItem.setAmt((float) 1152.00);
                invoice.getLineItems().add(lineItem);

                /**
                 * general bean coding
                 *
                 * Note:
                 * The above invoice value includes VAT as follows
                 *
                 * 1. On Fruit Drinks - 5 %
                 * 2. On CSD item     - 14.5%
                 */

                gen.iAddData(Printer_GEN.FONT_LARGE_BOLD, PrintUtil.getArrangedText(true, invoice.getHeader().getTitle().trim()));
                //gen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, getArrangedText(false, invoice.getHeader().getArea().trim() + ","));
                //gen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, invoice.getHeader().getArea().trim() + ",");
                gen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, invoice.getHeader().getDoorNo().trim() + ", " + invoice.getHeader().getStreet().trim() + ", " + invoice.getHeader().getArea().trim() + "," + invoice.getHeader().getCity());
                gen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, PrintUtil.getArrangedText(false, "TIN:" + invoice.getHeader().getTin() + "   CIN: " + invoice.getHeader().getCin()));
                gen.iAddData(Printer_GEN.FONT_LARGE_BOLD, PrintInvoice.getHorizontalLine());
                gen.iAddData(Printer_GEN.FONT_SMALL_BOLD, PrintUtil.getArrangedText(true, invoice.getOutletInfo().getOutletName() + "[" + invoice.getOutletInfo().getOutletCode() + "] "));
                gen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, invoice.getOutletInfo().getRouteName() + "[" + invoice.getOutletInfo().getRouteCode() + "]");
                gen.iAddData(Printer_GEN.FONT_LARGE_BOLD, PrintInvoice.getHorizontalLine());
                gen.iAddData(Printer_GEN.FONT_SMALL_BOLD, "Inv. No: " + invoice.getInvoiceNumber() + "  Dt: " + invoice.getInvoiceDate());
                gen.iAddData(Printer_GEN.FONT_LARGE_BOLD, PrintInvoice.getHorizontalLine());
                gen.iAddData(Printer_GEN.FONT_SMALL_BOLD, "S.No. Item              Qty/UoM      Amt ");
                gen.iAddData(Printer_GEN.FONT_LARGE_BOLD, PrintInvoice.getHorizontalLine());
                int count = 0;
                float subTotal = 0;
                for (LineItem line : invoice.getLineItems()) {
                    count++;
                    String countStr = "";
                    if(String.valueOf(count).length() == 1) {
                        countStr += count + ")" + "  ";
                    } else if (String.valueOf(count).length() == 2) {
                        countStr += count + ")" + " ";
                    }

                    gen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, ""
                            + countStr
                            + line.getItem() + new String(new char[22 - line.getItem().length()]).replace('\0', ' ')
                            + line.getQty() + new String(new char[7 - line.getQty().length()]).replace('\0', ' ')
                            + new String(new char[9 - String.valueOf(format.format(line.getAmt())).length()]).replace('\0', ' ') + format.format(line.getAmt()));
                    gen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, " ");
                    subTotal += line.getAmt();
                }

                gen.iAddData(Printer_GEN.FONT_LARGE_BOLD, PrintInvoice.getHorizontalLine());
                gen.iAddData(Printer_GEN.FONT_SMALL_BOLD, "Sub Total" + new String(new char[20]).replace('\0', ' ') + "Rs. " + format.format(subTotal));
                gen.iAddData(Printer_GEN.FONT_LARGE_BOLD, PrintInvoice.getHorizontalLine());
                gen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, "This Invoice value includes VAT 5% on Fruit Drinks & 14.5% CSD items");
                gen.iAddData(Printer_GEN.FONT_LARGE_BOLD, PrintInvoice.getHorizontalLine());
                gen.iAddData(Printer_GEN.FONT_LARGE_NORMAL,  " *** CUSTOMER COPY *** ");
                gen.iAddData(Printer_GEN.FONT_LARGE_NORMAL,  "       THANK YOU!       ");
                gen.iAddData(Printer_GEN.FONT_LARGE_BOLD, PrintInvoice.getHorizontalLine());
                gen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, " Inventrax SFA Ver. 1.0 ");


                gen.iStartPrinting(1);
                gen.iFlushBuf();



            } catch (Exception e) {
                e.printStackTrace();
            }

            return value;
        }
    }



}
