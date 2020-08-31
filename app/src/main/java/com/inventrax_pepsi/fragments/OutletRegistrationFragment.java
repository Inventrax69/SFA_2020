package com.inventrax_pepsi.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.inventrax_pepsi.R;
import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.SFACommon;
import com.inventrax_pepsi.common.constants.CustomerGroup;
import com.inventrax_pepsi.common.constants.JsonMessageNotificationType;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableCustomer;
import com.inventrax_pepsi.database.TableJSONMessage;
import com.inventrax_pepsi.database.pojos.JSONMessage;
import com.inventrax_pepsi.services.gps.GPSLocationService;
import com.inventrax_pepsi.services.sfa_background_services.BackgroundServiceFactory;
import com.inventrax_pepsi.sfa.outlet.registration.OutletRegistrationValidator;
import com.inventrax_pepsi.sfa.pojos.AddressBook;
import com.inventrax_pepsi.sfa.pojos.AuditInfo;
import com.inventrax_pepsi.sfa.pojos.Customer;
import com.inventrax_pepsi.sfa.pojos.OutletAsset;
import com.inventrax_pepsi.sfa.pojos.OutletProfile;
import com.inventrax_pepsi.sfa.pojos.RouteList;
import com.inventrax_pepsi.util.CameraUtils;
import com.inventrax_pepsi.util.DateUtils;
import com.inventrax_pepsi.util.DialogUtils;
import com.inventrax_pepsi.util.FileUtils;
import com.inventrax_pepsi.util.FragmentGUI;
import com.inventrax_pepsi.util.FragmentUtils;
import com.inventrax_pepsi.util.NetworkUtils;
import com.inventrax_pepsi.util.QRScannerUtils;
import com.inventrax_pepsi.util.SpinnerUtils;
import com.inventrax_pepsi.util.ValidationUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by android on 3/4/2016.
 */
public class OutletRegistrationFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private View rootView;

    private TextView txtRouteName,txtOutletPicture;
    private TextInputLayout input_layout_outlet_code,input_layout_outlet_name,input_layout_owner_name,input_layout_phone_number1,input_layout_email,input_layout_door_no,input_layout_land_mark,input_layout_address,input_layout_pin_code,input_layout_tin_number,input_layout_aadhar_no;
    private EditText inputOutletCode,inputOutletName,inputOwnerName,inputPhoneNumber1,inputPhoneNumber2,inputEmail,inputDoorNumber,inputLandMark,inputAddress,inputPinCode,inputTinNumber,inputAadhaarNumber;
    private Spinner spinnerChannelCode;
    private RadioGroup rbgAccountType,rbgOutletType,rbgPaymentMode;
    private RadioButton rbPCI,rbMIX,rbCCX,rbGeneral,rbKey,rbCash,rbCheque;
    private CheckBox chkIsDisplayAccount,chkIsPetSelling,chkIsCreditAccount;
    private Button btnCancel,btnSubmit,btnImageCapture,bntAddAsset;
    private LinearLayout layoutAssetDetails;
    private FrameLayout frmlAssetDetails;
    private TableLayout tblAssetDetails;
    private OutletRegistrationValidator outletRegistrationValidator;
    private InputFilter inputFilter;
    private String blockCharacterSet = "'~#^|$%&*!\"()?=+_";
    private SFACommon sfaCommon;
    private Spinner spinnerRouteList;
    private ArrayList<String> userRouteStringList;
    private Customer customer;
    private GPSLocationService gpsLocationService;

    // Asset Details
    private MaterialDialog materialAssetDialog,materialImageViewDialog;
    private CheckBox chkNoSerialNo,chkHasNightGuard;
    private RadioGroup rbgCoolerType;
    private RadioButton rbVISI,rbEBC;
    private TextInputLayout input_layout_pepsi_serial_number,input_layout_qr_code;
    private EditText inputQRCode,inputPepsiSerialNumber,inputOEMNumber;
    private Spinner spinnerAssetMake,spinnerAssetCapacity;
    private Button btnAssetCancel,btnAssetSubmit,btnCaptureAssetPicture,btnAssetDelete;
    private TableLayout.LayoutParams tableLayoutParams;
    private TableRow.LayoutParams tableRowParams;
    private ImageView imgCaptureImage;
    private Map<String,OutletAsset> mapOutletAssets;
    private TextView txtAssetPictureDetails;


    private DatabaseHelper databaseHelper;
    private TableCustomer tableCustomer;
    private TableJSONMessage tableJsonMessage;
    private Gson gson;

    private BackgroundServiceFactory backgroundServiceFactory;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        try {

            rootView = inflater.inflate(R.layout.fragment_outlet_registration, container, false);

            sfaCommon = SFACommon.getInstance();

            FragmentGUI.setView(rootView);

            backgroundServiceFactory = BackgroundServiceFactory.getInstance();
            backgroundServiceFactory.setActivity(getActivity());
            backgroundServiceFactory.setContext(getContext());


            loadFormControls();


            this.customer = (Customer) gson.fromJson(getArguments().getString("customerJSON"), Customer.class);

            if (customer!=null){

                displayCustomerDetails(customer);

            }

        }catch (Exception ex){
            return rootView;
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_outlet_registration));
        sfaCommon.hideUserInfo(getActivity());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnSubmit:{

                submitRegForm();

            }break;

            case R.id.btnCancel:{

                //FragmentUtils.replaceFragment(getActivity(),R.id.container_body,new OutletRegistrationFragment());

            }break;

            case R.id.btnImageCapture:{

                CameraUtils.captureImage(this);

            }break;

            case R.id.bntAddAsset:{

                QRScannerUtils.scanQRCode(this);

            }break;

            case R.id.btnAssetCancel:{

                materialAssetDialog.dismiss();

            }break;

            case R.id.btnAssetSubmit:{

                submitAssetRegForm();

            }break;

            case R.id.btnCaptureAssetPicture:{

                CameraUtils.captureImage(this);

            }break;

            case R.id.btnAssetDelete:{

                DialogUtils.showConfirmDialog(getActivity(), "", "Are you sure you want to delete?", "Yes", "No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (DialogInterface.BUTTON_POSITIVE == which ){

                            deleteAsset();

                        }else {
                            dialog.dismiss();
                        }

                    }

                });


            }break;

            default:{

                if (v instanceof TextView) {

                    if (mapOutletAssets != null && mapOutletAssets.size() > 0 && mapOutletAssets.containsKey(((TextView) v).getText().toString()))
                    {
                        displayAssetDetails(mapOutletAssets.get(((TextView) v).getText().toString()));

                    }

                }


            }break;

        }

    }

    private void submitRegForm(){

        try
        {
            if (!outletRegistrationValidator.validateOutletName())
                return;

            if (!outletRegistrationValidator.validateOutletOwnerName())
                return;

            if (spinnerChannelCode.getSelectedItem().toString().trim().equalsIgnoreCase("Select Channel"))
            {
                DialogUtils.showAlertDialog(getActivity(),"Please select channel type");
                return;
            }

            if (!outletRegistrationValidator.validateMobileNumber1())
                return;

            if (!outletRegistrationValidator.validateLandmark())
                return;

            if (!outletRegistrationValidator.validateAddress())
                return;

            if (!TextUtils.isEmpty(inputAadhaarNumber.getText().toString()))
                if (!outletRegistrationValidator.validateAadhaarNumber())
                    return;


            if (gpsLocationService.getLatitude() == 0 || gpsLocationService.getLongitude() == 0) {
                DialogUtils.showAlertDialog(getActivity(), "Location Information Is Not Available");
                return;
            }

            saveCustomerInLocalDB(getCustomer());

        }catch (Exception ex){
            Logger.Log(OutletRegistrationFragment.class.getName(),ex);
            return;
        }

    }

    private void saveCustomerInLocalDB(Customer customer) {

        try
        {
            if (customer == null)
                return;

            com.inventrax_pepsi.database.pojos.Customer localCustomer=tableCustomer.getCustomer(customer.getCustomerCode());

            if (localCustomer==null) {
                localCustomer = new com.inventrax_pepsi.database.pojos.Customer();
                localCustomer.setIsNewCustomer(1);
            }

            localCustomer.setCompleteJSON(gson.toJson(customer));
            localCustomer.setCustomerCode(customer.getCustomerCode());
            localCustomer.setCustomerName(customer.getCustomerName());
            localCustomer.setCustomerType(customer.getCustomerGroup());
            localCustomer.setCustomerTypeId(customer.getCustomerGroupId());
            localCustomer.setRouteId(customer.getOutletProfile().getRouteId());
            localCustomer.setRouteNo(customer.getOutletProfile().getRouteCode());
            localCustomer.setCustomerId(customer.getCustomerId());


            long auto_inc_id = 0;

            if (tableCustomer.getCustomer(customer.getCustomerCode())==null) {
                auto_inc_id = tableCustomer.createCustomer(localCustomer);
            }
            else {
                tableCustomer.updateCustomer(localCustomer);
                auto_inc_id = localCustomer.getAutoIncId();
            }

            if (auto_inc_id>0){

                JSONMessage jsonMessage=new JSONMessage();

                jsonMessage.setJsonMessage(localCustomer.getCompleteJSON());
                jsonMessage.setNoOfRequests(0);
                jsonMessage.setNotificationId((int)auto_inc_id);
                jsonMessage.setNotificationTypeId(JsonMessageNotificationType.Customer.getNotificationType()); // 10 -> Customer Type Notification
                jsonMessage.setSyncStatus(0);

                long json_msg_auto_inc_id = tableJsonMessage.createJSONMessage(jsonMessage);

                if (json_msg_auto_inc_id>0){

                    localCustomer.setAutoIncId((int)auto_inc_id);
                    localCustomer.setJsonMessageAutoIncId((int)json_msg_auto_inc_id);

                    tableCustomer.updateCustomer(localCustomer);

                    DialogUtils.showAlertDialog(getActivity(),"Outlet Updated Successfully");

                    Intent counterBroadcastIntent=new Intent();
                    counterBroadcastIntent.setAction("com.inventrax.broadcast.counter");
                    getActivity().sendBroadcast(counterBroadcastIntent);

                    //Need to invoke backend service

                    if (NetworkUtils.getConnectivityStatusAsBoolean(getContext()))
                        backgroundServiceFactory.initiateCustomerService();

                    if (localCustomer.getIsNewCustomer()==0)
                    {
                        OutletDashboardNewFragment outletDashboardNewFragment=new OutletDashboardNewFragment();

                        Bundle bundle=new Bundle();
                        bundle.putString("customerJSON",gson.toJson(customer));
                        outletDashboardNewFragment.setArguments(bundle);

                        FragmentUtils.replaceFragmentWithBackStack(getActivity(), R.id.container_body, outletDashboardNewFragment);

                    }else {

                        FragmentUtils.replaceFragmentWithBackStack(getActivity(), R.id.container_body, new NewOutletListFragment());

                    }

                }
            }


        }catch (Exception ex){
            Logger.Log(OutletRegistrationFragment.class.getName(),ex);
            return;
        }

    }

    private void submitAssetRegForm(){

        try
        {

            if (!chkNoSerialNo.isChecked()){

                if (TextUtils.isEmpty(inputPepsiSerialNumber.getText().toString()))
                {
                    DialogUtils.showAlertDialog(getActivity(),"Please enter pepsi serial number");
                    return;
                }
            }

            if (spinnerAssetMake.getSelectedItem().toString().trim().equalsIgnoreCase("Select Asset Make"))
            {
                DialogUtils.showAlertDialog(getActivity(),"Please select asset make");
                return;
            }

            if (spinnerAssetCapacity.getSelectedItem().toString().trim().equalsIgnoreCase("Select Volume"))
            {
                DialogUtils.showAlertDialog(getActivity(),"Please select asset volume");
                return;
            }

            addAssetDetails();


        }catch (Exception ex){
            Logger.Log(OutletRegistrationFragment.class.getName(),ex);
            return;
        }
    }

    private void addAssetDetails(){

        try
        {
            if( mapOutletAssets == null)
                return;

            OutletAsset outletAsset=mapOutletAssets.get(inputQRCode.getText().toString().trim());

            if (outletAsset == null)
                return;

            outletAsset.setQrCode(inputQRCode.getText().toString().trim());
            outletAsset.setPepsiSerialNumber(inputPepsiSerialNumber.getText().toString().trim());
            outletAsset.setOemNumber(inputOEMNumber.getText().toString().trim());
            outletAsset.setAssetMake(spinnerAssetMake.getSelectedItem().toString());
            outletAsset.setVolume(spinnerAssetCapacity.getSelectedItem().toString());
            outletAsset.setVISI(((RadioButton) materialAssetDialog.findViewById(rbgCoolerType.getCheckedRadioButtonId())).getText().toString().trim().equalsIgnoreCase("VISI")?true:false);
            outletAsset.setHasNightguard(chkHasNightGuard.isChecked());

            outletAsset.setLatitude(gpsLocationService.getLatitude()+"");
            outletAsset.setLongitude(gpsLocationService.getLongitude()+"");

            mapOutletAssets.put(outletAsset.getQrCode(),outletAsset);

            materialAssetDialog.dismiss();

            displayAssetInfo();

        }catch (Exception ex){
            materialAssetDialog.dismiss();
            Logger.Log(OutletRegistrationFragment.class.getName(),ex);
            return;
        }
    }

    private void deleteAsset() {

        try
        {
            if ( mapOutletAssets !=null && mapOutletAssets.size()>0 && mapOutletAssets.containsKey(inputQRCode.getText().toString().trim()) )
            {
                mapOutletAssets.remove(inputQRCode.getText().toString().trim());

                if (materialAssetDialog.isShowing())
                    materialAssetDialog.dismiss();

                displayAssetInfo();

                DialogUtils.showAlertDialog(getActivity(),"Asset deleted successfully");

                return;

            }else {

                if (materialAssetDialog.isShowing())
                    materialAssetDialog.dismiss();

                DialogUtils.showAlertDialog(getActivity(),"No asset available to delete");

                return;
            }

        }catch (Exception ex){

            if (materialAssetDialog.isShowing())
                materialAssetDialog.dismiss();

            Logger.Log(OutletRegistrationFragment.class.getName(),ex);
            return;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        try {

            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

            if (scanResult != null && !TextUtils.isEmpty(scanResult.getContents())) {

                // QR Code length cannot exceed 10
                if (scanResult.getContents().trim().length() > 10 || !ValidationUtils.isDigit(scanResult.getContents().trim())) {
                    DialogUtils.showAlertDialog(getActivity(), "Please scan valid qr code");
                    return;
                }

                if ( mapOutletAssets != null && mapOutletAssets.size()>0 && mapOutletAssets.containsKey(scanResult.getContents().toString().trim()))
                {
                    DialogUtils.showAlertDialog(getActivity(), "QR Code already scanned");
                    return;
                }

                mapOutletAssets.put(scanResult.getContents().toString().trim(),new OutletAsset());

                clearAssetDetails();

                inputQRCode.setText(scanResult.getContents().toString().trim());

                materialAssetDialog.show();

            }

            // if the result is capturing Image
            if (requestCode == CameraUtils.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
                if (resultCode == getActivity().RESULT_OK) {
                    // successfully captured the image

                    processImage();

                } else if (resultCode == getActivity().RESULT_CANCELED) {
                    // user cancelled Image capture
                    Toast.makeText(getActivity().getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT).show();
                } else {
                    // failed to capture image
                    Toast.makeText(getActivity().getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
                }
            }

        }catch (Exception ex){
            Logger.Log(OutletRegistrationFragment.class.getName(),ex);
            return;
        }


    }

    private void processImage(){

        try
        {
            OutletAsset outletAsset=mapOutletAssets.get(inputQRCode.getText().toString().trim());

            if (CameraUtils.fileUri != null) {

                String filePath = CameraUtils.compressImage(CameraUtils.fileUri.getPath(), getContext());

                File file = new File(CameraUtils.fileUri.getPath());

                Bitmap bitmapImage = BitmapFactory.decodeFile(filePath);

                imgCaptureImage.setImageBitmap(bitmapImage);

                materialImageViewDialog.show();

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();

                if (outletAsset!=null) {
                    outletAsset.setImageName(file.getName());
                    outletAsset.setImagePath(filePath);
                    txtAssetPictureDetails.setText(file.getName());
                    outletAsset.setImageData(Base64.encodeToString(imageBytes, Base64.DEFAULT));
                }else {

                    txtOutletPicture.setText(file.getName());
                    txtOutletPicture.setVisibility(TextView.VISIBLE);

                    if(customer==null)
                        customer=new Customer();

                    if (customer.getOutletProfile()==null)
                        customer.setOutletProfile(new OutletProfile());

                    customer.getOutletProfile().setImagePath(filePath);
                    customer.getOutletProfile().setImageName(file.getName());
                    customer.getOutletProfile().setImageSource(Base64.encodeToString(imageBytes, Base64.DEFAULT));
                }

                FileUtils.forceDelete(CameraUtils.getStoredImagesDirectory());
                FileUtils.forceDelete(CameraUtils.getCompressedImagesDirectory());

            }

        }
        catch (Exception ex){
            Logger.Log(OutletRegistrationFragment.class.getName(),ex);
            return;
        }

    }

    private Customer getCustomer(){



        if (customer==null)
            customer=new Customer();

        OutletProfile outletProfile=customer.getOutletProfile();
        if (outletProfile==null) {
            outletProfile = new OutletProfile();
            customer.setOutletProfile(outletProfile);
        }

        try
        {

            if (TextUtils.isEmpty(spinnerRouteList.getSelectedItem().toString()))
            {
                DialogUtils.showAlertDialog(getActivity(),"No route assigned to this user");
                return null;
            }

            outletProfile.setOutletAssets(new ArrayList<OutletAsset>(mapOutletAssets.values()));

            outletProfile.setLatitude(gpsLocationService.getLatitude()+"");
            outletProfile.setLongitude(gpsLocationService.getLongitude()+"");
            outletProfile.setRouteCode(spinnerRouteList.getSelectedItem().toString());
            outletProfile.setRouteName(txtRouteName.getText().toString());
            outletProfile.setRouteId(SFACommon.getRouteId(spinnerRouteList.getSelectedItem().toString()));

            customer.setAuditInfo(new AuditInfo());

            customer.getAuditInfo().setCreatedDate(DateUtils.getDate(DateUtils.YYYYMMDDHHMMSS_DATE_FORMAT));
            customer.getAuditInfo().setUserId(AppController.getUser().getUserId());
            customer.getAuditInfo().setUserName(AppController.getUser().getLoginUserId());

            customer.setBillingPriceTypeId(1);

            customer.setdBA("");
            customer.setCustomerCode(inputOutletCode.getText().toString());

            if (TextUtils.isEmpty(customer.getCustomerCode()))
            {
                customer.setCustomerCode(DateUtils.getDate("yyyyMMdd") + "" + DateUtils.getHourMinuteSeconds());
            }
            outletProfile.setOutletCode(customer.getCustomerCode());

            customer.setCustomerName(inputOutletName.getText().toString().trim());
            customer.setContactPersonName(inputOwnerName.getText().toString());

            outletProfile.setChannelCode(spinnerChannelCode.getSelectedItem().toString());
            outletProfile.setAccountType(((RadioButton) rootView.findViewById(rbgAccountType.getCheckedRadioButtonId())).getText().toString());

            switch (((RadioButton) rootView.findViewById(rbgAccountType.getCheckedRadioButtonId())).getText().toString().toLowerCase()){
                case "pci":{
                    outletProfile.setAccountTypeId(1);
                }break;
                case "ccx":{
                    outletProfile.setAccountTypeId(2);
                }break;
                case "mix":{
                    outletProfile.setAccountTypeId(3);
                }break;
            }

            outletProfile.setIsTraditional(true);
            customer.setCustomerGroupId(((RadioButton) rootView.findViewById(rbgOutletType.getCheckedRadioButtonId())).getText().toString().equalsIgnoreCase("General")? CustomerGroup.Outlet.getCustomerGroup():CustomerGroup.KeyOutlet.getCustomerGroup());
            outletProfile.setPaymentMode(((RadioButton) rootView.findViewById(rbgPaymentMode.getCheckedRadioButtonId())).getText().toString());

            outletProfile.setDisplayAccount(chkIsDisplayAccount.isChecked());
            outletProfile.setIsPetSelling(chkIsPetSelling.isChecked());
            customer.setCreditAccount(chkIsCreditAccount.isChecked());

            customer.setMobileNo1(inputPhoneNumber1.getText().toString());
            customer.setMobileNo2(inputPhoneNumber2.getText().toString());

            customer.setEmail(inputEmail.getText().toString());

            List<AddressBook> addressBooks=new ArrayList<AddressBook>();
            addressBooks.add(new AddressBook());

            customer.setAddressBook(addressBooks);

            customer.getAddressBook().get(0).setAuditInfo(customer.getAuditInfo());
            customer.getAddressBook().get(0).setDoorNo(inputDoorNumber.getText().toString());
            customer.getAddressBook().get(0).setLandmark(inputLandMark.getText().toString());
            customer.getAddressBook().get(0).setAddressLine1(inputAddress.getText().toString());
            outletProfile.setAadhaarNumber(inputAadhaarNumber.getText().toString());
            customer.getAddressBook().get(0).setPincode(inputPinCode.getText().toString());
            customer.settIN(inputTinNumber.getText().toString());




        }catch (Exception ex){
            Logger.Log(OutletRegistrationFragment.class.getName(),ex);
            return null;
        }

        return customer;
    }

    private void displayCustomerDetails(Customer customer){

        try
        {
            inputOutletCode.setText(customer.getCustomerCode());
            inputOutletName.setText(customer.getCustomerName());
            inputOwnerName.setText(customer.getContactPersonName());

            if(customer.getOutletProfile()==null)
                return;

            spinnerChannelCode.setSelection(Arrays.asList(getResources().getStringArray(R.array.channelArray)).indexOf(customer.getOutletProfile().getChannelCode().trim()));

            if (customer.getOutletProfile().getAccountType() != "") {
                switch (customer.getOutletProfile().getAccountTypeId()) {
                    case 1: {
                        rbgAccountType.check(R.id.rbPCI);
                    }
                    break;
                    case 2: {
                        rbgAccountType.check(R.id.rbCCX);
                    }
                    break;
                    case 3: {
                        rbgAccountType.check(R.id.rbMIX);
                    }
                    break;
                }
            }

            if (customer.getCustomerGroupId()==CustomerGroup.KeyOutlet.getCustomerGroup()){
                rbgOutletType.check(R.id.rbKey);
            }else {
                rbgOutletType.check(R.id.rbGeneral);
            }

            if( !TextUtils.isEmpty(customer.getOutletProfile().getPaymentMode()) && customer.getOutletProfile().getPaymentMode().equalsIgnoreCase("cheque")){
                rbgPaymentMode.check(R.id.rbCheque);
            }else {
                rbgPaymentMode.check(R.id.rbCash);
            }


            chkIsDisplayAccount.setChecked(customer.getOutletProfile().isDisplayAccount());
            chkIsCreditAccount.setChecked(customer.isCreditAccount());
            chkIsPetSelling.setChecked(customer.getOutletProfile().isPetSelling());

            inputPhoneNumber1.setText(customer.getMobileNo1());
            inputPhoneNumber2.setText(customer.getMobileNo2());
            inputEmail.setText(customer.getEmail());

            if (customer.getAddressBook()!=null && customer.getAddressBook().size()>0) {
                inputDoorNumber.setText(customer.getAddressBook().get(0).getDoorNo());
                inputLandMark.setText(customer.getAddressBook().get(0).getLandmark());
                inputAddress.setText(customer.getAddressBook().get(0).getAddressLine1());
                inputPinCode.setText(customer.getAddressBook().get(0).getPincode());
            }

            inputAadhaarNumber.setText(customer.getOutletProfile().getAadhaarNumber());
            inputTinNumber.setText(customer.gettIN());

            spinnerRouteList.setSelection(userRouteStringList.indexOf(customer.getOutletProfile().getRouteCode()));
            txtRouteName.setText(AppController.mapUserRoutes.get(spinnerRouteList.getSelectedItem().toString()));


            if (customer.getOutletProfile()==null)
                return;

            if( customer.getOutletProfile().getOutletAssets() == null || customer.getOutletProfile().getOutletAssets().size() == 0 )
                return;

            mapOutletAssets = new HashMap();

            for (OutletAsset outletAsset:customer.getOutletProfile().getOutletAssets()){

                if (!mapOutletAssets.containsKey(outletAsset.getQrCode()))
                    mapOutletAssets.put(outletAsset.getQrCode(),outletAsset);

            }

            displayAssetInfo();

        }catch (Exception ex){
            Logger.Log(OutletRegistrationFragment.class.getName(),ex);
            DialogUtils.showAlertDialog(getActivity(),"Error while displaying outlet details");
            return;
        }

    }

    private void displayAssetDetails(OutletAsset outletAsset){

        try
        {
            if (outletAsset==null)
                return;

            if (TextUtils.isEmpty(outletAsset.getPepsiSerialNumber()))
             chkNoSerialNo.setChecked(true);

            chkHasNightGuard.setChecked(outletAsset.isHasNightguard());


            if (outletAsset.isVISI())
                rbgCoolerType.check(R.id.rbVISI);
            else
                rbgCoolerType.check(R.id.rbEBC);

            inputQRCode.setText(outletAsset.getQrCode());
            inputPepsiSerialNumber.setText(outletAsset.getPepsiSerialNumber());
            inputOEMNumber.setText(outletAsset.getOemNumber());
            txtAssetPictureDetails.setText(outletAsset.getImageName());

            spinnerAssetMake.setSelection(Arrays.asList(getResources().getStringArray(R.array.array_asset_make)).indexOf(outletAsset.getAssetMake()));
            spinnerAssetCapacity.setSelection(Arrays.asList(getResources().getStringArray(R.array.array_cooler_capacity)).indexOf(outletAsset.getVolume()));

            materialAssetDialog.show();

        }catch (Exception ex){
            Logger.Log(OutletRegistrationFragment.class.getName(),ex);
            DialogUtils.showAlertDialog(getActivity(),"Error while displaying asset details");
            return;
        }

    }

    private void displayAssetInfo(){
        try
        {

            tblAssetDetails.removeAllViews();

            if ( mapOutletAssets==null || mapOutletAssets.size() == 0 )
                layoutAssetDetails.setVisibility(LinearLayout.GONE);
            else
                layoutAssetDetails.setVisibility(LinearLayout.VISIBLE);

            TableRow rowHeader = new TableRow(getActivity());
            //rowHeader.setBackgroundColor(Color.parseColor("#000000"));
            rowHeader.setBackgroundColor(Color.parseColor("#075ba1"));

            rowHeader.addView(getTexView("QR Code", "QR Code", 16, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);
            rowHeader.addView(getTexView("Type", "Type", 16, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);
            rowHeader.addView(getTexView("Asset Make", "Asset Make", 16, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);
            rowHeader.addView(getTexView("Volume", "Volume", 16, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);
            rowHeader.addView(getTexView("PEPSI Serial No.", "PEPSI Serial No.", 16, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);
            rowHeader.addView(getTexView("OEM No.", "OEM No.", 16, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);
            rowHeader.addView(getTexView("Image", "Image", 16, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);

            tblAssetDetails.addView(rowHeader, tableLayoutParams);

            for (OutletAsset asset : mapOutletAssets.values()) {

                TableRow rowAssetDetails = new TableRow(getActivity());
                rowAssetDetails.setBackgroundColor(Color.parseColor("#000000"));

                TextView txtQRCode = getTexView("QR Code", asset.getQrCode(), 16, Color.BLACK, false, true, Color.WHITE);
                txtQRCode.setOnClickListener(this);
                txtQRCode.setPaintFlags(txtQRCode.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                txtQRCode.setTextColor(Color.BLUE);

                rowAssetDetails.addView(txtQRCode, tableRowParams);

                rowAssetDetails.addView(getTexView("Type",( asset.isVISI() == true ? "VISI" : "EBC" )  , 16, Color.BLACK, false, true, Color.WHITE), tableRowParams);
                rowAssetDetails.addView(getTexView("Asset Make", asset.getAssetMake(), 16, Color.BLACK, false, true, Color.WHITE), tableRowParams);
                rowAssetDetails.addView(getTexView("Volume", asset.getVolume(), 16, Color.BLACK, false, true, Color.WHITE), tableRowParams);
                rowAssetDetails.addView(getTexView("PEPSI Serial No.", asset.getPepsiSerialNumber(), 16, Color.BLACK, false, true, Color.WHITE), tableRowParams);
                rowAssetDetails.addView(getTexView("OEM No.", asset.getOemNumber(), 16, Color.BLACK, false, true, Color.WHITE), tableRowParams);
                rowAssetDetails.addView(getTexView("Image", asset.getImageName(), 16, Color.BLACK, false, true, Color.WHITE), tableRowParams);

                tblAssetDetails.addView(rowAssetDetails, tableLayoutParams);

            }


        }catch (Exception ex){

            Logger.Log(OutletRegistrationFragment.class.getName(),ex);
            return;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {

            case R.id.spinnerRouteList: {

                if (AppController.mapUserRoutes != null)
                    txtRouteName.setText(AppController.mapUserRoutes.get(spinnerRouteList.getSelectedItem().toString()));
            }
            break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        try {



        }catch (Exception ex){

            Logger.Log(OutletRegistrationFragment.class.getName(),ex);
            return;

        }

    }

    public void loadFormControls(){

        try
        {
            outletRegistrationValidator=new OutletRegistrationValidator(getActivity(),getContext());

            inputFilter = new InputFilter() {

                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                    if (source != null && blockCharacterSet.contains(("" + source))) {
                        return "";
                    }
                    return null;
                }
            };

            gpsLocationService = new GPSLocationService(getContext());

            gson = new GsonBuilder().create();

            databaseHelper = DatabaseHelper.getInstance();
            tableCustomer = databaseHelper.getTableCustomer();
            tableJsonMessage = databaseHelper.getTableJSONMessage();

            txtRouteName = (TextView) rootView.findViewById(R.id.txtRouteName);
            spinnerRouteList = (Spinner) rootView.findViewById(R.id.spinnerRouteList);
            spinnerRouteList.setOnItemSelectedListener(this);

            userRouteStringList = new ArrayList<>();

            for (RouteList userRoute : AppController.getUser().getRouteList()) {
                userRouteStringList.add(userRoute.getRouteCode());
            }

            SpinnerUtils.getSpinner(getActivity(), "Select Route", spinnerRouteList, userRouteStringList);

            tableLayoutParams = new TableLayout.LayoutParams();
            tableRowParams = new TableRow.LayoutParams();
            tableRowParams.setMargins(1, 1, 1, 1);

            input_layout_outlet_code = FragmentGUI.getTextInputLayout(R.id.input_layout_outlet_code);
            input_layout_outlet_name = FragmentGUI.getTextInputLayout(R.id.input_layout_outlet_name);
            outletRegistrationValidator.setInput_layout_outlet_name(input_layout_outlet_name );
            input_layout_owner_name = FragmentGUI.getTextInputLayout(R.id.input_layout_owner_name);
            outletRegistrationValidator.setInput_layout_owner_name(input_layout_owner_name );
            input_layout_phone_number1 = FragmentGUI.getTextInputLayout(R.id.input_layout_phone_number1);
            outletRegistrationValidator.setInput_layout_phone_number1(input_layout_phone_number1);
            input_layout_email = FragmentGUI.getTextInputLayout(R.id.input_layout_email);
            outletRegistrationValidator.setInput_layout_email(input_layout_email);
            input_layout_door_no = FragmentGUI.getTextInputLayout(R.id.input_layout_door_no);
            input_layout_land_mark = FragmentGUI.getTextInputLayout(R.id.input_layout_land_mark);
            outletRegistrationValidator.setInput_layout_land_mark(input_layout_land_mark);
            input_layout_address = FragmentGUI.getTextInputLayout(R.id.input_layout_address);
            outletRegistrationValidator.setInput_layout_address(input_layout_address);
            input_layout_pin_code = FragmentGUI.getTextInputLayout(R.id.input_layout_pin_code);
            input_layout_tin_number = FragmentGUI.getTextInputLayout(R.id.input_layout_tin_number);
            input_layout_aadhar_no =  FragmentGUI.getTextInputLayout(R.id.input_layout_aadhar_no);
            outletRegistrationValidator.setInput_layout_aadhar_no(input_layout_aadhar_no);

            inputOutletCode = FragmentGUI.getEditText(R.id.inputOutletCode);
            inputOutletName = FragmentGUI.getEditText(R.id.inputOutletName);
            inputOutletName.setFilters(new InputFilter[] { inputFilter });
            outletRegistrationValidator.setInputOutletName(inputOutletName);
            inputOwnerName = FragmentGUI.getEditText(R.id.inputOwnerName);
            inputOwnerName.setFilters(new InputFilter[] { inputFilter });
            outletRegistrationValidator.setInputOwnerName(inputOwnerName);
            inputPhoneNumber1 = FragmentGUI.getEditText(R.id.inputPhoneNumber1);
            outletRegistrationValidator.setInputPhoneNumber1(inputPhoneNumber1);
            inputPhoneNumber2 = FragmentGUI.getEditText(R.id.inputPhoneNumber2);
            inputPhoneNumber2.setFilters(new InputFilter[] { inputFilter });
            outletRegistrationValidator.setInputPhoneNumber2(inputPhoneNumber2);
            inputEmail = FragmentGUI.getEditText(R.id.inputEmail);
            inputEmail.setFilters(new InputFilter[] { inputFilter });
            outletRegistrationValidator.setInputEmail(inputEmail);
            inputDoorNumber = FragmentGUI.getEditText(R.id.inputDoorNumber);
            inputDoorNumber.setFilters(new InputFilter[] { inputFilter });
            inputLandMark = FragmentGUI.getEditText(R.id.inputLandMark);
            inputLandMark.setFilters(new InputFilter[] { inputFilter });
            outletRegistrationValidator.setInputLandMark(inputLandMark);
            inputAddress = FragmentGUI.getEditText(R.id.inputAddress);
            inputAddress.setFilters(new InputFilter[] { inputFilter });
            outletRegistrationValidator.setInputAddress(inputAddress);
            inputPinCode = FragmentGUI.getEditText(R.id.inputPinCode);
            inputTinNumber = FragmentGUI.getEditText(R.id.inputTinNumber);
            inputTinNumber.setFilters(new InputFilter[] { inputFilter });
            inputAadhaarNumber = FragmentGUI.getEditText(R.id.inputAadhaarNumber);
            inputAadhaarNumber.setFilters(new InputFilter[] { inputFilter });
            outletRegistrationValidator.setInputAadhaarNumber(inputAadhaarNumber);

            spinnerChannelCode = FragmentGUI.getSpinner(R.id.spinnerChannelCode);

            rbgAccountType = FragmentGUI.getRadioGroup(R.id.rbgAccountType);
            rbgOutletType = FragmentGUI.getRadioGroup(R.id.rbgOutletType);
            rbgPaymentMode = FragmentGUI.getRadioGroup(R.id.rbgPaymentMode);

            rbPCI = FragmentGUI.getRadioButton(R.id.rbPCI);
            rbMIX = FragmentGUI.getRadioButton(R.id.rbMIX);
            rbCCX = FragmentGUI.getRadioButton(R.id.rbCCX);
            rbGeneral = FragmentGUI.getRadioButton(R.id.rbGeneral);
            rbKey = FragmentGUI.getRadioButton(R.id.rbKey);
            rbCash = FragmentGUI.getRadioButton(R.id.rbCash);
            rbCheque  = FragmentGUI.getRadioButton(R.id.rbCheque);

            chkIsDisplayAccount = FragmentGUI.getCheckBox(R.id.chkIsDisplayAccount);
            chkIsPetSelling = FragmentGUI.getCheckBox(R.id.chkIsPetSelling);
            chkIsCreditAccount = FragmentGUI.getCheckBox(R.id.chkIsCreditAccount);

            btnCancel = FragmentGUI.getButton(R.id.btnCancel);
            btnCancel.setOnClickListener(this);
            btnSubmit = FragmentGUI.getButton(R.id.btnSubmit);
            btnSubmit.setOnClickListener(this);
            btnImageCapture = FragmentGUI.getButton(R.id.btnImageCapture);
            btnImageCapture.setOnClickListener(this);
            bntAddAsset  = FragmentGUI.getButton(R.id.bntAddAsset);
            bntAddAsset.setOnClickListener(this);

            layoutAssetDetails = FragmentGUI.getLinearLayout(R.id.layoutAssetDetails);
            layoutAssetDetails.setVisibility(LinearLayout.GONE);

            frmlAssetDetails = FragmentGUI.getFrameLayout(R.id.frmlAssetDetails);
            tblAssetDetails = FragmentGUI.getTableLayout(R.id.tblAssetDetails);

            txtOutletPicture = FragmentGUI.getTextView(R.id.txtOutletPicture);
            txtOutletPicture.setVisibility(TextView.GONE);


            // check if GPS enabled
            if (!gpsLocationService.canGetLocation()) {
                // Ask user to enable GPS/network in settings
                gpsLocationService.showSettingsAlert();
                return;
            }

            loadAssetFormControls();

            loadImageViewFormControls();

        }catch (Exception ex){
            Logger.Log(OutletRegistrationFragment.class.getName(),ex);
            DialogUtils.showAlertDialog(getActivity(),"Error while initializing");
            return;
        }

    }

    private void loadAssetFormControls(){
        try
        {
            MaterialDialog.Builder builderAssetDialog = new MaterialDialog.Builder(getActivity())
                    .title("Asset Details")
                    .customView(R.layout.fragment_asset_dialog, true)
                    .cancelable(false);

            materialAssetDialog = builderAssetDialog.build();

            chkNoSerialNo = (CheckBox) materialAssetDialog.findViewById(R.id.chkNoSerialNo);
            chkHasNightGuard = (CheckBox) materialAssetDialog.findViewById(R.id.chkHasNightGuard);
            rbgCoolerType = (RadioGroup) materialAssetDialog.findViewById(R.id.rbgCoolerType);
            rbVISI = (RadioButton)materialAssetDialog.findViewById(R.id.rbVISI);
            rbEBC = (RadioButton)materialAssetDialog.findViewById(R.id.rbEBC);;
            input_layout_pepsi_serial_number = (TextInputLayout) materialAssetDialog.findViewById(R.id.input_layout_pepsi_serial_number);
            input_layout_qr_code = (TextInputLayout) materialAssetDialog.findViewById(R.id.input_layout_qr_code);
            inputQRCode = (EditText) materialAssetDialog.findViewById(R.id.inputQRCode) ;
            inputPepsiSerialNumber = (EditText) materialAssetDialog.findViewById(R.id.inputPepsiSerialNumber);
            inputOEMNumber = (EditText) materialAssetDialog.findViewById(R.id.inputOEMNumber);
            spinnerAssetMake = (Spinner) materialAssetDialog.findViewById(R.id.spinnerAssetMake);
            spinnerAssetCapacity= (Spinner) materialAssetDialog.findViewById(R.id.spinnerAssetCapacity);
            btnAssetCancel = (Button)materialAssetDialog.findViewById(R.id.btnAssetCancel);
            btnAssetCancel.setOnClickListener(this);
            btnAssetSubmit = (Button)materialAssetDialog.findViewById(R.id.btnAssetSubmit);
            btnAssetSubmit.setOnClickListener(this);
            btnCaptureAssetPicture = (Button)materialAssetDialog.findViewById(R.id.btnCaptureAssetPicture);
            btnCaptureAssetPicture.setOnClickListener(this);
            btnAssetDelete = (Button)materialAssetDialog.findViewById(R.id.btnAssetDelete);
            btnAssetDelete.setOnClickListener(this);
            txtAssetPictureDetails = (TextView) materialAssetDialog.findViewById(R.id.txtAssetPictureDetails);


            mapOutletAssets=new HashMap<>();



        }catch (Exception ex){
            Logger.Log(OutletRegistrationFragment.class.getName(),ex);
            DialogUtils.showAlertDialog(getActivity(),"Error while initializing");
            return;
        }
    }

    private void clearAssetDetails(){
        try
        {
            inputQRCode.setText("");
            inputOEMNumber.setText("");
            inputPepsiSerialNumber.setText("");
            spinnerAssetCapacity.setSelection(0);
            spinnerAssetMake.setSelection(0);
            chkHasNightGuard.setChecked(false);
            chkNoSerialNo.setChecked(false);
            txtAssetPictureDetails.setText("");


        }catch (Exception ex){
            Logger.Log(OutletRegistrationFragment.class.getName(),ex);
            return;
        }
    }

    private void loadImageViewFormControls(){

        try
        {
            MaterialDialog.Builder builderImageDialog = new MaterialDialog.Builder(getActivity())
                    .title("Captured Image")
                    .customView(R.layout.dialog_image_view, true)
                    .positiveText("Ok");

            materialImageViewDialog = builderImageDialog.build();

            imgCaptureImage =(ImageView)materialImageViewDialog.findViewById(R.id.imgCaptureImage);

        }catch (Exception ex){
            Logger.Log(OutletRegistrationFragment.class.getName(),ex);
            return;
        }

    }

    public TextView getTexView(String ID, String TextViewValue, float textSize, int mColor, boolean isBold, boolean IsBackGroundColor, int BackGroundColor) {


        try {

            TextView textView = new TextView(getActivity());

            textView.setText(TextViewValue);
            textView.setTextSize(textSize);
            textView.setPadding(10, 10, 10, 10);
            textView.setTextColor(mColor);
            textView.setGravity(Gravity.LEFT);
            if (isBold)
                textView.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);
            if (IsBackGroundColor)
                textView.setBackgroundColor(BackGroundColor);
            return textView;

        } catch (Exception ex) {

            return null;
        }
    }

}
