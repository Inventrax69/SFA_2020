package com.inventrax_pepsi.kioskmode;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.app.enterprise.EnterpriseDeviceManager;
import android.app.enterprise.RestrictionPolicy;
import android.app.enterprise.kioskmode.KioskMode;
import android.app.enterprise.license.EnterpriseLicenseManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.inventrax_pepsi.kioskmode.receivers.DeviceAdministrator;

/**
 * Created by android on 4/23/2016.
 */
public class KioskUtils {

    private Context mContext;
    private DevicePolicyManager mDPM;
    private ComponentName mCN;
    private EnterpriseDeviceManager enterpriseDeviceManager;
    private RestrictionPolicy restrictionPolicy;


    public KioskUtils(Context context){
        mContext=context;
        this.enterpriseDeviceManager = (EnterpriseDeviceManager) context
                .getSystemService(EnterpriseDeviceManager.ENTERPRISE_POLICY_SERVICE);
    }

    public boolean activateAdmin() {

        // saLoggerObj.i(SAConstants.MODEL_TAG,
        // "activateDeactivateAdmin "+btnActivateDeactivateAdmin.getText().toString());

        try {
            if (mDPM == null) {
                mDPM = (DevicePolicyManager) mContext.getSystemService(Context.DEVICE_POLICY_SERVICE);
            }
            if (mCN == null) {
                mCN = new ComponentName(mContext, DeviceAdministrator.class);
            }

            if (mDPM != null && !mDPM.isAdminActive(mCN)) {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mCN);
                ((Activity) mContext).startActivityForResult(intent, 1);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.e(KioskUtils.this.getClass().getSimpleName(), e.getMessage());
        }
        return false;
    }

    public boolean deactivateAdmin() {

        // saLoggerObj.i(SAConstants.MODEL_TAG,
        // "activateDeactivateAdmin "+btnActivateDeactivateAdmin.getText().toString());

        if (mDPM == null) {
            mDPM = (DevicePolicyManager) mContext.getSystemService(Context.DEVICE_POLICY_SERVICE);
        }
        if (mCN == null) {
            mCN = new ComponentName(mContext, DeviceAdministrator.class);
        }

        try {
            if (mDPM != null && mDPM.isAdminActive(mCN)) {
                mDPM.removeActiveAdmin(mCN);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.e(KioskUtils.this.getClass().getSimpleName(), e.getMessage());
        }
        return false;
    }

    public boolean isAdminActivated(){

        if (mDPM == null) {
            mDPM = (DevicePolicyManager) mContext.getSystemService(Context.DEVICE_POLICY_SERVICE);
        }
        if (mCN == null) {
            mCN = new ComponentName(mContext, DeviceAdministrator.class);
        }

        if (mDPM != null && mDPM.isAdminActive(mCN)) {
           return true;
        } else {
            return false;
        }

    }


    public void activateELM(String elmKey) {

        EnterpriseLicenseManager elmMgr = null;

        try {
            if (elmMgr == null) {
                elmMgr = EnterpriseLicenseManager.getInstance(mContext);
            }

            elmMgr.activateLicense(elmKey);

        } catch (Exception e) {
            Log.e(KioskUtils.this.getClass().getSimpleName(), e.getMessage());
        }
    }


    public boolean enableKioskMode(boolean mode){

        KioskMode kioskModeService = KioskMode.getInstance(mContext);

        if (enterpriseDeviceManager!=null){

            restrictionPolicy=enterpriseDeviceManager.getRestrictionPolicy();

            if (restrictionPolicy!=null) {

                restrictionPolicy.allowFactoryReset(false);
                restrictionPolicy.allowWallpaperChange(false);
                restrictionPolicy.setTethering(false);
                restrictionPolicy.setAllowNonMarketApps(true);
                restrictionPolicy.allowSafeMode(false);
                restrictionPolicy.allowGoogleAccountsAutoSync(false);
                restrictionPolicy.allowAirplaneMode(false);
                //restrictionPolicy.allowSettingsChanges(false);


                if(!restrictionPolicy.isMockLocationEnabled())
                    restrictionPolicy.setMockLocation(true);



            }

        }


        try
        {
            if (kioskModeService==null)
                return false;

            // Activate Kiosk Mode
            if ( mode == true )
            {
                /*
				 * API to enable kiosk mode with the default kiosk home screen.
				 * The use of this API requires the caller to have the
				 * "android.permission.sec.MDM_KIOSK_MODE" permission with a
				 * protection level of signature. An administrator can use this
				 * API to enable kiosk mode on the device. When kiosk mode is
				 * enabled with no arguments, the user is presented with a
				 * restricted version of the native Samsung home screen. No
				 * default application shortcuts appear when the home screen is
				 * launched for the first time, including the phone application
				 * used to make emergency calls. The most recent tasks are
				 * cleared. The uninstallation and reinstallation of the default
				 * kiosk application is also disabled. The administrator can
				 * enforce additional restrictions in kiosk mode using other
				 * kiosk mode and EDM policies. An administrator receives an
				 * error in an attempt to enable kiosk mode if it is already
				 * enabled.
				 */
                if (!kioskModeService.isKioskModeEnabled()) {

                    kioskModeService.enableKioskMode();
                    kioskModeService.allowMultiWindowMode(false);


                    /* if (restrictionPolicy!=null)
                         restrictionPolicy.allowSettingsChanges(false);
                        */

                    return true;
                } else {
                    return false;
                }

            }
            else
            {
                //Deactivate Kiosk Mode

                if (kioskModeService.isKioskModeEnabled()) {

                    kioskModeService.disableKioskMode();

                    return true;
                } else {
                   return false;
                }


            }

        }catch (Exception ex){
            return false;
        }

    }


    public boolean isKioskModeEnabled(){

        KioskMode kioskModeService = KioskMode.getInstance(mContext);

        if (kioskModeService==null)
            return false;


        if (kioskModeService.isKioskModeEnabled()) {
            return true;
        }
        else {
            return false;
        }
    }


}
