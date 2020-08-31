package com.inventrax_pepsi.fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.inventrax_pepsi.R;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by android on 4/5/2016.
 */
public class BluetoothFragment extends Fragment {

    private View rootView;

    private static int REQUEST_ENABLE_BT = 192;
    private BluetoothAdapter bluetoothAdapter;
    private UUID myUUID;
    private String myName;

    private boolean connected=false;
    private String MACAddress;

    private ThreadBeConnected myThreadBeConnected;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView=inflater.inflate(R.layout.fragment_bluetooth,container,false);

        if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)){

            Toast.makeText(getActivity(),"FEATURE_BLUETOOTH NOT support",Toast.LENGTH_LONG).show();

            return rootView;
        }

        bluetoothAdapter=getBluetoothAdapter();

        enableBluetooth();


        //generate UUID on web: http://www.famkruithof.net/uuid/uuidgen
        //have to match the UUID on the another device of the BT connection
        myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        myName = myUUID.toString();




        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();

        //Turn ON BlueTooth if it is OFF
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        setup();
    }

    private void setup() {

        myThreadBeConnected = new ThreadBeConnected();
        myThreadBeConnected.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(myThreadBeConnected!=null){
            myThreadBeConnected.cancel();
        }
    }





    private BluetoothAdapter getBluetoothAdapter(){

        return  BluetoothAdapter.getDefaultAdapter();

    }


    private void enableBluetooth(){

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_ENABLE_BT){
            if(bluetoothAdapter.isEnabled()) {

                //setup();

            } else {

                // Bluetooth Disabled

            }
        }
    }



    private class ThreadBeConnected extends Thread {

        private BluetoothServerSocket bluetoothServerSocket = null;


        public ThreadBeConnected() {
            try {
                bluetoothServerSocket =
                        bluetoothAdapter.listenUsingRfcommWithServiceRecord(myName, myUUID);



               // textStatus.setText("Waiting\n" + "bluetoothServerSocket :\n"  + bluetoothServerSocket);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            BluetoothSocket bluetoothSocket = null;

            if(bluetoothServerSocket!=null){
                try {
                    bluetoothSocket = bluetoothServerSocket.accept();

                    final BluetoothDevice remoteDevice = bluetoothSocket.getRemoteDevice();



                    final String strConnected = "Connected:\n" +remoteDevice.getName() + "\n" + remoteDevice.getAddress();


                    //connected
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            //textStatus.setText(strConnected);

                            MACAddress = remoteDevice.getAddress();

                            connected=true;


                        }
                    });

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                    final String eMessage = e.getMessage();
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            //textStatus.setText("something wrong: \n" + eMessage);
                        }
                    });
                }
            }else{
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        //textStatus.setText("bluetoothServerSocket == null");
                    }
                });
            }
        }

        public void cancel() {

            Toast.makeText(getActivity(),"close bluetoothServerSocket",Toast.LENGTH_LONG).show();

            try {
                bluetoothServerSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
