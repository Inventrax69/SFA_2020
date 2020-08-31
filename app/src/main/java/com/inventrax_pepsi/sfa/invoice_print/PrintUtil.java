package com.inventrax_pepsi.sfa.invoice_print;

import android.app.Activity;
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
import android.text.TextUtils;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableCustomer;
import com.inventrax_pepsi.interfaces.DeliveryView;
import com.inventrax_pepsi.sfa.pojos.Customer;
import com.inventrax_pepsi.sfa.pojos.Invoice;
import com.inventrax_pepsi.sfa.pojos.InvoiceItem;
import com.inventrax_pepsi.sfa.pojos.InvoiceVAT;
import com.inventrax_pepsi.sfa.pojos.Order;
import com.inventrax_pepsi.sfa.pojos.OrderItem;
import com.inventrax_pepsi.sfa.pojos.OrderItemScheme;
import com.inventrax_pepsi.sfa.pojos.OutletProfile;
import com.inventrax_pepsi.util.DateUtils;
import com.inventrax_pepsi.util.ProgressDialogUtils;
import com.prowesspride.api.Printer_GEN;
import com.prowesspride.api.Setup;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;

/**
 * Created by android on 4/2/2016.
 */
public class PrintUtil {


    private final static String EXTRA_DEVICE_TYPE = "android.bluetooth.device.extra.DEVICE_TYPE";
    private BluetoothAdapter adapter = null;
    private BluetoothSocket socket = null;
    private BroadcastReceiver foundReceiver = null;
    private static InputStream bluetoothInputStream = null;
    private static OutputStream bluetoothOutputStream = null;
    private boolean discoveryFinished;
    private Hashtable<String, Hashtable<String, String>> bluetoothDeviceFound = null;
    private ArrayList<HashMap<String, Object>> showListItem = null;
    private boolean bluetoothDiscoveryFinished = false;
    private SimpleAdapter listItemAdapter = null;
    private Setup setup;
    private Context context;
    private Activity activity;
    private DeliveryView deliveryView;
    private PrintInvoice printInvoice;
    private DatabaseHelper databaseHelper;
    private TableCustomer tableCustomer;
    private Gson gson;


    public PrintUtil(Context context,Activity activity){

        this.activity=activity;
        this.context=context;
        databaseHelper=DatabaseHelper.getInstance();
        tableCustomer=databaseHelper.getTableCustomer();
        gson=new GsonBuilder().create();

    }

    public PrintUtil(){

        databaseHelper=DatabaseHelper.getInstance();
        tableCustomer=databaseHelper.getTableCustomer();
        gson=new GsonBuilder().create();

    }

    public void setSetup(Setup setup) {
        this.setup = setup;
    }

    public void setDeliveryView(DeliveryView deliveryView) {
        this.deliveryView = deliveryView;
    }

    public void setPrintInvoice(PrintInvoice printInvoice) {
        this.printInvoice = printInvoice;
    }

    public PrintInvoice generatePrintInvoice(Order order){

        PrintInvoice printInvoice=null;

        try {

            com.inventrax_pepsi.database.pojos.Customer  localDBCustomer =  tableCustomer.getCustomer(order.getCustomerId());

            Customer customer= gson.fromJson(localDBCustomer.getCompleteJSON(), Customer.class);

            if(customer==null){
                return null;
            }

            printInvoice=new PrintInvoice();

            printInvoice.setInvoiceNumber(order.getOrderCode());
            printInvoice.setInvoiceDate(DateUtils.getDate());
            printInvoice.setSubTotal("" + order.getDerivedPrice());
            printInvoice.setHeader(getInvoiceHeader());

            System.out.println("The Payment Mode is : " + order.getPaymentInfo().getPaymentMode() + " Payment Amount is: " + order.getPaymentInfo().getAmount() + " Payment Creation Date: " + order.getAuditInfo().getCreatedDate());

            if(order.getPaymentInfo() != null) {
                printInvoice.setPaymentMode(order.getPaymentInfo().getPaymentMode());
                printInvoice.setPaymentAmount((float) order.getPaymentInfo().getAmount());
                printInvoice.setPaymentDate(PrintInvoice.format.format(new Date()));
            }


            OutletInfo outletInfo = new OutletInfo();
            printInvoice.setOutletInfo(outletInfo);

            OutletProfile outletProfile=customer.getOutletProfile();

            if (outletProfile==null){
                return null;
            }

            outletInfo.setOutletName(customer.getCustomerName());
            outletInfo.setOutletCode(customer.getCustomerCode());
            outletInfo.setRouteName(outletProfile.getRouteName());
            outletInfo.setRouteCode(outletProfile.getRouteCode());
            outletInfo.setCityName((customer.getAddressBook()!=null && customer.getAddressBook().size()>0)?customer.getAddressBook().get(0).getCity():"");

            if(printInvoice.getLineItems() == null) {
                printInvoice.setLineItems(new ArrayList<LineItem>());
            }

            LineItem lineItem=null;

            if (order.getOrderItems()!=null) {

                for (OrderItem orderItem : order.getOrderItems()) {

                    lineItem = new LineItem();

                    lineItem.setItem(orderItem.getItemCode());
                    //
                    lineItem.setQty((int) orderItem.getQuantity() + "/" + orderItem.getUoMCode());
                    lineItem.setAmt((float) orderItem.getDerivedPrice());

                    printInvoice.getLineItems().add(lineItem);

                    System.out.println(" order Schemes Count " + orderItem.getOrderItemSchemes().size());

                    if (orderItem.getOrderItemSchemes() != null ) {

                        for (OrderItemScheme orderItemScheme : orderItem.getOrderItemSchemes()) {

                            lineItem = new LineItem();

                            lineItem.setItem(orderItemScheme.getItemCode());
                            //lineItem.setItem(orderItemScheme.getItemBrand() + " " + orderItemScheme.getItemPack());
                            lineItem.setQty((int) orderItemScheme.getValue() + "/" + orderItemScheme.getUoM());
                            lineItem.setItemPrice((float) orderItemScheme.getPrice());

                            // Modified By Santosh End
                            printInvoice.getLineItems().add(lineItem);
                        }
                    }

                    if(orderItem.getOrderItemDiscount() != null && orderItem.getOrderItemDiscount().isSpot()) {
                        lineItem.setAmt((float) (orderItem.getDerivedPrice() + orderItem.getOrderItemDiscount().getDiscountValue()));
                        lineItem.setDiscountPrice((float) orderItem.getOrderItemDiscount().getDiscountValue());
                        lineItem.setDiscountType(orderItem.getOrderItemDiscount().getDiscountType());
                    }
                }
            }
        }catch (Exception ex){
            Logger.Log(PrintUtil.class.getName(),ex);
        }

        return printInvoice;
    }


    public PrintInvoice generatePrintInvoice(Invoice invoice){

        PrintInvoice printInvoice=null;

        try {

            com.inventrax_pepsi.database.pojos.Customer  localDBCustomer =  tableCustomer.getCustomer(invoice.getCustomerId());

            Customer customer= gson.fromJson(localDBCustomer.getCompleteJSON(), Customer.class);

            if(customer==null){
                return null;
            }

            printInvoice=new PrintInvoice();

            printInvoice.setInvoiceNumber(invoice.getInvoiceNo());
            printInvoice.setInvoiceDate(DateUtils.getDate());
            printInvoice.setSubTotal("" + invoice.getNetAmount());
            printInvoice.setHeader(getInvoiceHeader());


            if(invoice.getPaymentInfo() != null) {
                printInvoice.setPaymentAmount((float) invoice.getPaymentInfo().getAmount());
                printInvoice.setPaymentMode(invoice.getPaymentInfo().getPaymentMode());
            }
            OutletInfo outletInfo = new OutletInfo();
            printInvoice.setOutletInfo(outletInfo);

            OutletProfile outletProfile=customer.getOutletProfile();

            if (outletProfile==null){
                return null;
            }

            outletInfo.setOutletName(customer.getCustomerName());
            outletInfo.setOutletCode(customer.getCustomerCode());
            outletInfo.setRouteName(outletProfile.getRouteName());
            outletInfo.setRouteCode(outletProfile.getRouteCode());
            outletInfo.setCityName((customer.getAddressBook()!=null && customer.getAddressBook().size()>0)?customer.getAddressBook().get(0).getCity():"");
            outletInfo.setTinNumber(customer.gettIN());
            if(printInvoice.getLineItems() == null) {
                printInvoice.setLineItems(new ArrayList<LineItem>());
            }
            LineItem lineItem=null;
            if (invoice.getInvoiceItems()!=null) {

                for (InvoiceItem invoiceItem : invoice.getInvoiceItems()) {
                    lineItem = new LineItem();
                    lineItem.setItem(TextUtils.isEmpty(invoiceItem.getItemCode())?"":invoiceItem.getItemCode());
                    //lineItem.setItem(invoiceItem.getItemBrand() + " " + invoiceItem.getItemPack());
                    lineItem.setQty((int) invoiceItem.getQuantity() + "/" + invoiceItem.getUoMCode());
                    lineItem.setAmt((float) invoiceItem.getDerivedPrice());
                    printInvoice.getLineItems().add(lineItem);

                    if(invoiceItem.getDiscountPrice() != 0 && invoiceItem.getDerivedPrice() != 0) {
                        lineItem.setDiscountPrice((float) invoiceItem.getSpotDiscountPrice());
                        lineItem.setDiscountType("");
                    }

                    if(invoiceItem.getDerivedPrice() == 0) {
                        lineItem.setItemPrice((float) invoiceItem.getDiscountPrice());
                        lineItem.setDiscountType("");
                    }

                }
            }

            if(printInvoice.getTaxBreakups() == null) {
                printInvoice.setTaxBreakups(new ArrayList<TaxBreakup>());
            }

            TaxBreakup taxBreakup = null;

            if(invoice.getInvoiceVATs() != null) {
                for (InvoiceVAT invoiceVAT : invoice.getInvoiceVATs()) {
                    taxBreakup = new TaxBreakup();

                    taxBreakup.setVAT(invoiceVAT.getvAT());
                    taxBreakup.setVatAmount(invoiceVAT.getvATAmount());

                    printInvoice.getTaxBreakups().add(taxBreakup);
                }
            }


        }catch (Exception ex){
            Logger.Log(PrintUtil.class.getName(),ex);
        }

        return printInvoice;
    }

    private InvoiceHeader getInvoiceHeader(){

        InvoiceHeader invoiceHeader = new InvoiceHeader();

        invoiceHeader.setTitle("Pearl Bottling Pvt. Ltd.");
        invoiceHeader.setDoorNo("Stock Point: D.No. 1-103");
        invoiceHeader.setStreet("Musalaiah Palem");
        invoiceHeader.setArea("Sagar Nagar, Yandada Post");
        invoiceHeader.setCity("Visakhapatnam");
        invoiceHeader.setPincode("530045");
        invoiceHeader.setTin("37200185878");
        invoiceHeader.setCin("");

        return invoiceHeader;

    }

    public void startPrint(){

        try
        {
            // Activate bluetooth in the current device
            adapter = BluetoothAdapter.getDefaultAdapter();
            adapter.startDiscovery();
            if (!adapter.isEnabled()) {
                activity.startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 0);
            }
            // Search for the bluetooth devices
            new ScanDeviceTask().execute("");

        }catch (Exception ex){
            Logger.Log(PrintUtil.class.getName(),ex);
            ProgressDialogUtils.closeProgressDialog();
        }

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

            deliveryView.showProgress();

            communicate();

            startSearch();

        }

        @Override
        protected void onPostExecute(Integer integer) {

            deliveryView.closeProgress();

            try {

                new PrintTask().execute();

            }catch (Exception ex){
                ProgressDialogUtils.closeProgressDialog();
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

                if (printInvoice==null || PrintUtil.bluetoothOutputStream ==null || PrintUtil.bluetoothInputStream == null )
                    return value;

                System.out.println("The device serial number is : " + setup.sGetDevSerNo(PrintUtil.bluetoothOutputStream, PrintUtil.bluetoothInputStream));

                Printer_GEN gen = new Printer_GEN(setup, PrintUtil.bluetoothOutputStream, PrintUtil.bluetoothInputStream);

                gen.iFlushBuf();

                DecimalFormat format = new DecimalFormat();
                format.setMaximumFractionDigits(2);
                format.setMinimumFractionDigits(2);

                //gen.iBmpPrint(context, R.drawable.jaipuria_logo);

                gen.iAddData(Printer_GEN.FONT_LARGE_BOLD, PrintUtil.getArrangedText(true, printInvoice.getHeader().getTitle().trim()));
                gen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, printInvoice.getHeader().getDoorNo().trim() + ", " + printInvoice.getHeader().getStreet().trim() + ", " + printInvoice.getHeader().getArea().trim() + "," + printInvoice.getHeader().getCity());
                gen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, PrintUtil.getArrangedText(false, "TIN:" + printInvoice.getHeader().getTin() + "   CIN: " + printInvoice.getHeader().getCin()));
                gen.iAddData(Printer_GEN.FONT_LARGE_BOLD, PrintInvoice.getHorizontalLine());
                if(printInvoice.getOutletInfo().getTinNumber() != null && !printInvoice.getOutletInfo().getTinNumber().isEmpty()) {
                    gen.iAddData(Printer_GEN.FONT_LARGE_BOLD, PrintInvoice.RETAIL_INVOICE);
                    gen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, "Customer Tin : " + printInvoice.getOutletInfo().getTinNumber());
                } else {
                    gen.iAddData(Printer_GEN.FONT_LARGE_BOLD, PrintInvoice.PROFORMA_INVOICE);
                }

                gen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, PrintUtil.getArrangedText(true, printInvoice.getOutletInfo().getOutletName() + "[" + printInvoice.getOutletInfo().getOutletCode() + "] " + (printInvoice.getOutletInfo().isCreditAccount() ? "CREDIT" : "CASH")));
                //gen.iAddData(Printer_GEN.FONT_SMALL_BOLD, PrintUtil.getArrangedText(false,(printInvoice.getOutletInfo().isCreditAccount() ? "CREDIT" : "CASH")));
                gen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, printInvoice.getOutletInfo().getRouteName() + "[" + printInvoice.getOutletInfo().getRouteCode() + "]");
                gen.iAddData(Printer_GEN.FONT_LARGE_BOLD, PrintInvoice.getHorizontalLine());
                gen.iAddData(Printer_GEN.FONT_SMALL_BOLD, "Inv. No: " + printInvoice.getInvoiceNumber() + " Dt: " + printInvoice.getInvoiceDate());
                gen.iAddData(Printer_GEN.FONT_LARGE_BOLD, PrintInvoice.getHorizontalLine());
                gen.iAddData(Printer_GEN.FONT_SMALL_BOLD, "S.No. Item              Qty/UoM      Amt ");
                gen.iAddData(Printer_GEN.FONT_LARGE_BOLD, PrintInvoice.getHorizontalLine());
                int count = 0;
                float subTotal = 0;
                float totalDiscount = 0;
                for (LineItem line : printInvoice.getLineItems()) {
                    /*count++;
                    String countStr = "";
                    if(String.valueOf(count).length() == 1) {
                        countStr += count + ")" + "  ";
                    } else if (String.valueOf(count).length() == 2) {
                        countStr += count + ")" + " ";
                    }*/

                    if(line.getAmt() != 0) {
                        count++;
                        String countStr = "";
                        if(String.valueOf(count).length() == 1) {
                            countStr += count + ")" + "  ";
                        } else if (String.valueOf(count).length() == 2) {
                            countStr += count + ")" + " ";
                        }

                        gen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, ""
                                + countStr
                                + line.getItem() + new String(new char[20 - line.getItem().length()]).replace('\0', ' ')
                                + line.getQty() + new String(new char[7 - line.getQty().length()]).replace('\0', ' ')
                                + new String(new char[11 - String.valueOf(format.format(line.getAmt())).length()]).replace('\0', ' ') + format.format(line.getAmt()));
                        gen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, " ");
                    } else if(line.getAmt() == 0) {
                        totalDiscount += line.getItemPrice();
                        gen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, ""
                                + "(T) "
                                + line.getItem() + new String(new char[20 - line.getItem().length()]).replace('\0', ' ')
                                + line.getQty() + new String(new char[7 - line.getQty().length()]).replace('\0', ' ')
                                + new String(new char[11 - String.valueOf(format.format(line.getItemPrice())).length()]).replace('\0', ' ') + format.format(line.getItemPrice()));
                        gen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, " ");
                        gen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, "Scheme Less   " +
                                new String(new char[20 - String.valueOf(line.getDiscountType()).length()]).replace('\0', ' ') +
                                line.getDiscountType() +
                                new String(new char[8 - String.valueOf(format.format(line.getItemPrice())).length()]).replace('\0', ' ') +
                                format.format(line.getItemPrice()));
                        gen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, " ");
                    }
                    if(line.getDiscountPrice() != 0 && line.getAmt() != 0) {
                        totalDiscount += line.getDiscountPrice();
                        gen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, "Discount Less " +
                                new String(new char[20 - String.valueOf(line.getDiscountType()).length()]).replace('\0', ' ') +
                                line.getDiscountType() +
                                new String(new char[8 - format.format(line.getDiscountPrice()).length()]).replace('\0', ' ') +
                                format.format(line.getDiscountPrice()));
                        gen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, " ");
                    }
                    // Santosh Code End
                    subTotal += line.getAmt();
                }

                gen.iAddData(Printer_GEN.FONT_LARGE_BOLD, PrintInvoice.getHorizontalLine());
                gen.iAddData(Printer_GEN.FONT_SMALL_BOLD, "Grand Total" + new String(new char[18]).replace('\0', ' ') + "Rs. " + format.format(subTotal));
                gen.iAddData(Printer_GEN.FONT_LARGE_BOLD, PrintInvoice.getHorizontalLine());

                /*
                Tax Break Up information - Start
                 */

                gen.iAddData(Printer_GEN.FONT_LARGE_NORMAL,"      VAT BREAKUP       ");
                gen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, " ");
                for (TaxBreakup taxBreakup : printInvoice.getTaxBreakups()) {
                    gen.iAddData(Printer_GEN.FONT_SMALL_NORMAL,
                            ""
                                    + "VAT    "
                                    + taxBreakup.getVAT() + "%" + new String(new char[21 - String.valueOf(taxBreakup.getVAT()).length()]).replace('\0', ' ')
                                    + "Rs. " + new String(new char[9 - String.valueOf(format.format(taxBreakup.getVatAmount())).length()]).replace('\0', ' ')
                                    + format.format(taxBreakup.getVatAmount()));
/*                    gen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, "VAT    " + taxBreakup.getVAT() + "" +
                            "%" + new String(new char[17]).replace('\0', ' ') + "Rs. " + format.format(taxBreakup.getVatAmount()));*/
                }


                gen.iAddData(Printer_GEN.FONT_LARGE_BOLD, PrintInvoice.getHorizontalLine());
                gen.iAddData(Printer_GEN.FONT_SMALL_BOLD, "Cash Discount" + new String(new char[16]).replace('\0', ' ') + "Rs. " + format.format(totalDiscount));
                gen.iAddData(Printer_GEN.FONT_LARGE_BOLD, PrintInvoice.getHorizontalLine());

                /*
                Tax Break Up information - End
                 */


                /*gen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, "This Invoice value includes VAT 5% on Fruit Drinks & 14.5% CSD items");
                gen.iAddData(Printer_GEN.FONT_LARGE_BOLD, PrintInvoice.getHorizontalLine());*/
                gen.iAddData(Printer_GEN.FONT_LARGE_NORMAL,  " *** CUSTOMER COPY *** ");
                gen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, "       THANK YOU!       ");
                gen.iAddData(Printer_GEN.FONT_LARGE_BOLD, PrintInvoice.getHorizontalLine());
                //if(printInvoice.getOutletInfo().getTinNumber() != null && !printInvoice.getOutletInfo().getTinNumber().isEmpty()) {
                if(printInvoice.getOutletInfo().getTinNumber() == null || printInvoice.getOutletInfo().getTinNumber().isEmpty()) {
                    gen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, "   NOT FOR INPUT TAX    ");
                    gen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, "    CREDIT PURPOSE     ");
                    gen.iAddData(Printer_GEN.FONT_LARGE_BOLD, PrintInvoice.getHorizontalLine());
                }

                gen.iAddData(Printer_GEN.FONT_LARGE_NORMAL,  "    COLLECTION RECEIPT    ");
                gen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, " ");
                gen.iAddData(Printer_GEN.FONT_SMALL_NORMAL,  "PAYMENT MODE    " + ( TextUtils.isEmpty(printInvoice.getPaymentMode())?"CASH":printInvoice.getPaymentMode() )  );
                gen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, " ");
                gen.iAddData(Printer_GEN.FONT_SMALL_NORMAL,  "PAYMENT AMOUNT  " + format.format(subTotal));
                gen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, " ");
                gen.iAddData(Printer_GEN.FONT_SMALL_NORMAL,  "PAYMENT DATE    " + PrintInvoice.format.format(new Date()));


                gen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, " ");
                gen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, " ");
                gen.iAddData(Printer_GEN.FONT_SMALL_NORMAL, " ");

                gen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, "   CUSTOMER SIGNATURE   ");
                gen.iAddData(Printer_GEN.FONT_LARGE_BOLD, PrintInvoice.getHorizontalLine());
                gen.iAddData(Printer_GEN.FONT_LARGE_NORMAL, " Inventrax SFA Ver. 1.0 ");

                if (gen.iStartPrinting(1)==0){
                    gen.iPaperFeed();
                    value=1;
                }

            } catch (Exception e) {
                e.printStackTrace();
                Logger.Log(PrintUtil.class.getName(),e);
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
                        PrintUtil.bluetoothInputStream = socket.getInputStream();
                        PrintUtil.bluetoothOutputStream = socket.getOutputStream();
                    }
                }
            }


        } catch (Exception e) {
            Logger.Log(PrintUtil.class.getName(),e);
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

                }
            };


            bluetoothDiscoveryFinished = false;

            System.out.println("bluetoothDeviceFound" + bluetoothDeviceFound);

            if (bluetoothDeviceFound == null) {
                bluetoothDeviceFound = new Hashtable<String, Hashtable<String, String>>();
                bluetoothDeviceFound.clear();
                IntentFilter fliter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                activity.registerReceiver(foundReceiver, fliter);
                adapter.startDiscovery();

            } else {
                System.out.println("not going to if condition");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.Log(PrintUtil.class.getName(), e);
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

            System.out.println("The empty Storage is :::" + empty);

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

}
