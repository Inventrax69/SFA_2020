package com.inventrax_pepsi.sfa.outlet.registration;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.inventrax_pepsi.util.ValidationUtils;

/**
 * Created by android on 5/4/2016.
 */
public class OutletRegistrationValidator {

    private TextInputLayout input_layout_outlet_code,input_layout_outlet_name,input_layout_owner_name,input_layout_phone_number1,input_layout_email,input_layout_door_no,input_layout_land_mark,input_layout_address,input_layout_pin_code,input_layout_tin_number,input_layout_aadhar_no;
    private EditText inputOutletCode,inputOutletName,inputOwnerName,inputPhoneNumber1,inputPhoneNumber2,inputEmail,inputDoorNumber,inputLandMark,inputAddress,inputPinCode,inputTinNumber,inputAadhaarNumber;
    private Activity activity;
    private Context context;

    public OutletRegistrationValidator(){

    }

    public OutletRegistrationValidator(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    public void setInput_layout_outlet_code(TextInputLayout input_layout_outlet_code) {
        this.input_layout_outlet_code = input_layout_outlet_code;
    }

    public void setInput_layout_outlet_name(TextInputLayout input_layout_outlet_name) {
        this.input_layout_outlet_name = input_layout_outlet_name;
    }

    public void setInput_layout_owner_name(TextInputLayout input_layout_owner_name) {
        this.input_layout_owner_name = input_layout_owner_name;
    }

    public void setInput_layout_phone_number1(TextInputLayout input_layout_phone_number1) {
        this.input_layout_phone_number1 = input_layout_phone_number1;
    }

    public void setInput_layout_email(TextInputLayout input_layout_email) {
        this.input_layout_email = input_layout_email;
    }

    public void setInput_layout_door_no(TextInputLayout input_layout_door_no) {
        this.input_layout_door_no = input_layout_door_no;
    }

    public void setInput_layout_land_mark(TextInputLayout input_layout_land_mark) {
        this.input_layout_land_mark = input_layout_land_mark;
    }

    public void setInput_layout_address(TextInputLayout input_layout_address) {
        this.input_layout_address = input_layout_address;
    }

    public void setInput_layout_pin_code(TextInputLayout input_layout_pin_code) {
        this.input_layout_pin_code = input_layout_pin_code;
    }

    public void setInput_layout_tin_number(TextInputLayout input_layout_tin_number) {
        this.input_layout_tin_number = input_layout_tin_number;
    }

    public void setInput_layout_aadhar_no(TextInputLayout input_layout_aadhar_no) {
        this.input_layout_aadhar_no = input_layout_aadhar_no;
    }

    public void setInputOutletCode(EditText inputOutletCode) {
        this.inputOutletCode = inputOutletCode;
    }

    public void setInputOutletName(EditText inputOutletName) {
        this.inputOutletName = inputOutletName;
    }

    public void setInputOwnerName(EditText inputOwnerName) {
        this.inputOwnerName = inputOwnerName;
    }

    public void setInputPhoneNumber1(EditText inputPhoneNumber1) {
        this.inputPhoneNumber1 = inputPhoneNumber1;
    }

    public void setInputPhoneNumber2(EditText inputPhoneNumber2) {
        this.inputPhoneNumber2 = inputPhoneNumber2;
    }

    public void setInputEmail(EditText inputEmail) {
        this.inputEmail = inputEmail;
    }

    public void setInputDoorNumber(EditText inputDoorNumber) {
        this.inputDoorNumber = inputDoorNumber;
    }

    public void setInputLandMark(EditText inputLandMark) {
        this.inputLandMark = inputLandMark;
    }

    public void setInputAddress(EditText inputAddress) {
        this.inputAddress = inputAddress;
    }

    public void setInputPinCode(EditText inputPinCode) {
        this.inputPinCode = inputPinCode;
    }

    public void setInputTinNumber(EditText inputTinNumber) {
        this.inputTinNumber = inputTinNumber;
    }

    public void setInputAadhaarNumber(EditText inputAadhaarNumber) {
        this.inputAadhaarNumber = inputAadhaarNumber;
    }


    public boolean validateOutletName() {
        String outletName = inputOutletName.getText().toString().trim();

        if (TextUtils.isEmpty(outletName))
        {            input_layout_outlet_name.setError("Please enter a valid outlet name");
            requestFocus(inputOutletName);
            return false;
        } else {
            input_layout_outlet_name.setErrorEnabled(false);
        }

        return true;
    }

    public boolean validateOutletOwnerName() {

        String outletName = inputOwnerName.getText().toString().trim();

        if (TextUtils.isEmpty(outletName))
        {            input_layout_owner_name.setError("Please enter a valid owner name");
            requestFocus(inputOwnerName);
            return false;
        } else {
            input_layout_owner_name.setErrorEnabled(false);
        }

        return true;
    }

    public boolean validateMobileNumber1() {

        String mobileNo = inputPhoneNumber1.getText().toString().trim();

        if (TextUtils.isEmpty(mobileNo) || !ValidationUtils.validatePhoneNumber(mobileNo)) {
            input_layout_phone_number1.setError("Please enter a valid mobile number");
            requestFocus(inputPhoneNumber1);
            return false;
        } else {
            input_layout_phone_number1.setErrorEnabled(false);
        }

        return true;
    }



    public boolean validateAddress() {
        String address = inputAddress.getText().toString().trim();

        if (TextUtils.isEmpty(address)) {
            input_layout_address.setError("Please enter a valid address");
            requestFocus(inputAddress);
            return false;
        } else {
            input_layout_address.setErrorEnabled(false);
        }

        return true;
    }

    public boolean validateLandmark() {

        String landmark = inputLandMark.getText().toString().trim();

        if (TextUtils.isEmpty(landmark) ) {
            input_layout_land_mark.setError("Please enter a valid landmark");
            requestFocus(inputLandMark);
            return false;
        } else {
            input_layout_land_mark.setErrorEnabled(false);
        }

        return true;
    }

    public boolean validateAadhaarNumber(){

        String aadhaarNumber = inputAadhaarNumber.getText().toString().trim();

        if ( !TextUtils.isEmpty(aadhaarNumber) && aadhaarNumber.length() != 12  ) {
            input_layout_aadhar_no.setError("Please enter a valid aadhaar number");
            requestFocus(inputAadhaarNumber);
            return false;
        } else {
            input_layout_aadhar_no.setErrorEnabled(false);
        }

        return true;
    }



    private void requestFocus(View view) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }



}
