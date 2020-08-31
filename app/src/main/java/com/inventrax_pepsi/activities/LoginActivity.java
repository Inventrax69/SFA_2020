package com.inventrax_pepsi.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.inventrax_pepsi.R;
import com.inventrax_pepsi.application.AbstractApplication;
import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.locale.LocaleHelper;
import com.inventrax_pepsi.model.Selectedlanguage;
import com.inventrax_pepsi.services.appupdate.UpdateServiceUtils;
import com.inventrax_pepsi.services.gcm.GCMRegisterUtils;
import com.inventrax_pepsi.services.sfa_background_services.BackgroundServiceFactory;
import com.inventrax_pepsi.sfa.login.LoginPresenter;
import com.inventrax_pepsi.sfa.login.LoginPresenterImpl;
import com.inventrax_pepsi.sfa.login.LoginView;
import com.inventrax_pepsi.util.DialogUtils;
import com.inventrax_pepsi.util.NetworkUtils;
import com.inventrax_pepsi.util.ProgressDialogUtils;
import com.inventrax_pepsi.util.SharedPreferencesUtils;

/**
 * Author   : Naresh P.
 * Date		: 28/03/2016 08:49 AM
 * Purpose	: Login Activity
 */


public class LoginActivity extends AppCompatActivity implements LoginView, View.OnClickListener {

    private EditText inputUserId, inputPassword;
    private TextInputLayout inputLayoutUserId, inputLayoutPassword;
    private Button btnLogin;
    private LoginPresenter loginPresenter;
    private CheckBox chkRememberPassword;
    private ProgressDialogUtils progressDialogUtils;
    private SharedPreferencesUtils sharedPreferencesUtils;
    private BackgroundServiceFactory backgroundServiceFactory;
    private Spinner spinnerLanguagenames;
    private Resources resources;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loadFormControls();

        loginPresenter = new LoginPresenterImpl(this);
    }



    private void loadFormControls() {

        try {

            if (TextUtils.isEmpty(AppController.DEVICE_GCM_REGISTER_ID))
                new GCMRegisterUtils();


            backgroundServiceFactory = BackgroundServiceFactory.getInstance();
            backgroundServiceFactory.setActivity(this);
            spinnerLanguagenames = (Spinner) findViewById(R.id.languagespinner);
            resources = getResources();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.language_array, android.R.layout.simple_spinner_dropdown_item);
            spinnerLanguagenames.setAdapter(adapter);
            spinnerLanguagenames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    switch (position) {
                        case 0:
                            LocaleHelper.setLocale(AppController.getInstance().getApplicationContext(), "en");
                            Selectedlanguage.setLanguage(2);
                            settinglogintextvalues();
                            validateUserId();
                            validatePassword();
                            break;
                        case 1: {
                            LocaleHelper.setLocale(AppController.getInstance().getApplicationContext(), "te");
                            DialogUtils.showAlertDialog(LoginActivity.this, resources.getString(R.string.message));
                            Selectedlanguage.setLanguage(1);
                            settinglogintextvalues();
                            validateUserId();
                            validatePassword();
                        }
                        break;
                        case 2: {
                            LocaleHelper.setLocale(AppController.getInstance().getApplicationContext(), "en");
                            DialogUtils.showAlertDialog(LoginActivity.this, "Your application language changed to English");
                            Selectedlanguage.setLanguage(2);
                            settinglogintextvalues();
                            validateUserId();
                            validatePassword();
                        }

                        break;
                        default:
                            break;
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            inputLayoutUserId = (TextInputLayout) findViewById(R.id.input_layout_user_id);
            inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);

            inputUserId = (EditText) findViewById(R.id.input_user_id);
            //inputUserId.setText("mani@inventrax.com");

            inputPassword = (EditText) findViewById(R.id.input_password);
            //inputPassword.setText("12345");

            chkRememberPassword = (CheckBox) findViewById(R.id.chkRememberPassword);

            inputUserId.addTextChangedListener(new LoginViewTextWatcher(inputUserId));
            inputPassword.addTextChangedListener(new LoginViewTextWatcher(inputPassword));

            btnLogin = (Button) findViewById(R.id.btnLogin);
            btnLogin.setOnClickListener(this);

            progressDialogUtils = new ProgressDialogUtils(this);

            sharedPreferencesUtils = new SharedPreferencesUtils("LoginActivity", getApplicationContext());

            if (sharedPreferencesUtils.loadPreferenceAsBoolean("isRememberPasswordChecked", false)) {
                inputUserId.setText(sharedPreferencesUtils.loadPreference("userId", ""));
                inputPassword.setText(sharedPreferencesUtils.loadPreference("password", ""));
                chkRememberPassword.setChecked(true);

            }



            // Check for new updates
            new UpdateServiceUtils().checkUpdate();


        } catch (Exception ex) {
            Logger.Log(LoginActivity.class.getName(),ex);
            DialogUtils.showAlertDialog(this,"Error while initializing controls");
            return;

        }
    }

    @Override
    protected void onDestroy() {
        loginPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void showProgress() {
        progressDialogUtils.showProgressDialog(AbstractApplication.get().getString(R.string.Loadingmessage));
    }

    @Override
    public void hideProgress() {
        progressDialogUtils.closeProgressDialog();
    }

    @Override
    public void setUsernameError() {

            inputLayoutUserId.setError(resources.getString(R.string.err_msg_user_id));
            requestFocus(inputUserId);
    }

    @Override
    public void setPasswordError() {

            inputLayoutPassword.setError(resources.getString(R.string.err_msg_password));
            requestFocus(inputPassword);
    }

    @Override
    public void showLoginError(String message) {
        DialogUtils.showAlertDialog(this, message);
        return;
    }

    @Override
    public void navigateToHome() {

        sharedPreferencesUtils.savePreference("login_status", true);

        showProgress();

        // Check for new updates
        new UpdateServiceUtils().checkUpdate();

        initiateBackgroundServices();

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnLogin) {
            submitForm();
        }
    }

    /**
     * Validating form
     */
    private void submitForm() {

        if (!validateUserId()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        if (NetworkUtils.isInternetAvailable()){

            loginPresenter.validateCredentials(inputUserId.getText().toString(), inputPassword.getText().toString(),chkRememberPassword.isChecked());

        }else {
            DialogUtils.showAlertDialog(this,AbstractApplication.get().getString(R.string.internetenablemessage));
            return;

        }


    }

    private static boolean isValidUserId(String userId) {
        return !TextUtils.isEmpty(userId);
    }

    private boolean validateUserId() {

        String userId = inputUserId.getText().toString().trim();

            if (userId.isEmpty() || !isValidUserId(userId)) {

                    inputLayoutUserId.setError(resources.getString(R.string.err_msg_user_id));
                    requestFocus(inputUserId);
                    return false;
                }

             else {
                inputLayoutUserId.setErrorEnabled(false);
            }
                 return  true;
    }

    private boolean validatePassword() {

            if (inputPassword.getText().toString().trim().isEmpty()) {
                inputLayoutPassword.setError(resources.getString(R.string.err_msg_password));
                requestFocus(inputPassword);
                return false;
            } else {
                inputLayoutPassword.setErrorEnabled(false);
            }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class LoginViewTextWatcher implements TextWatcher {

        private View view;

        private LoginViewTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_user_id:
                    validateUserId();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;
            }
        }
    }


    private void initiateBackgroundServices() {

        try
        {

            if(NetworkUtils.isInternetAvailable()) {

                backgroundServiceFactory.initiateOutletListService();

               // backgroundServiceFactory.initiateSKUListService();
               // backgroundServiceFactory.initiateDiscountService();
               // backgroundServiceFactory.initiateLoadOutService();
               // backgroundServiceFactory.initiateDeliveryListService();

                hideProgress();

                this.startActivity(new Intent(this, MainActivity.class));
                this.finish();


            }else {
                DialogUtils.showAlertDialog(this,AbstractApplication.get().getString(R.string.internetenablemessage));
                return;

            }


        }catch (Exception ex){
            Logger.Log(LoginActivity.class.getName(),ex);
            DialogUtils.showAlertDialog(this,"Error while initiating background services");
            return;
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        /*try {

            EnterpriseDeviceManager enterpriseDeviceManager = (EnterpriseDeviceManager)getSystemService(EnterpriseDeviceManager.ENTERPRISE_POLICY_SERVICE);

            RestrictionPolicy restrictionPolicy = enterpriseDeviceManager.getRestrictionPolicy();

            restrictionPolicy.allowSettingsChanges(false);

        }catch (Exception ex){

        }*/

        //android.provider.Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, 255);

    }

    @Override
    protected void onPause() {
        super.onPause();
       // android.provider.Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, 25);
    }
    public void settinglogintextvalues()
    {
       // inputUserId.setHint(resources.getString(R.string.hint_user_id));
        //inputPassword.setHint(resources.getString(R.string.hint_password));
        chkRememberPassword.setText(resources.getString(R.string.login_chk_remember_password));
        btnLogin.setText(R.string.login_sign_in_button_text);

    }
}
