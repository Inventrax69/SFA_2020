package com.inventrax_pepsi.fragments;

import android.app.enterprise.EnterpriseDeviceManager;
import android.app.enterprise.RestrictionPolicy;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.inventrax_pepsi.R;
import com.inventrax_pepsi.application.AbstractApplication;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.SFACommon;
import com.inventrax_pepsi.services.appupdate.UpdateServiceUtils;
import com.inventrax_pepsi.util.AndroidUtils;
import com.inventrax_pepsi.util.DialogUtils;
import com.inventrax_pepsi.util.ProgressDialogUtils;

/**
 * Created by android on 3/29/2016.
 */
public class AboutFragment extends Fragment implements View.OnClickListener{

    private View rootView;
    private TextView txtVersion;
    private Button btnCheckUpdate;
    private UpdateServiceUtils updateServiceUtils;
    private TextView txtHelpLine;
    private TextView txtYouTubeLink;
    private TextView txtLocation;
    private SFACommon sfaCommon;
    private EnterpriseDeviceManager enterpriseDeviceManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView=inflater.inflate(R.layout.fragment_about,container,false);

        sfaCommon=SFACommon.getInstance();

        try {

            enterpriseDeviceManager = (EnterpriseDeviceManager) getActivity().getSystemService(EnterpriseDeviceManager.ENTERPRISE_POLICY_SERVICE);

            RestrictionPolicy restrictionPolicy = enterpriseDeviceManager.getRestrictionPolicy();

            restrictionPolicy.allowSettingsChanges(true);

        }catch (Exception ex){
            Logger.Log(AboutFragment.class.getName(), ex);
        }

        loadFormControls();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }



    private void loadFormControls(){
        try
        {
            txtVersion=(TextView)rootView.findViewById(R.id.txtVersion);
            txtVersion.setText(String.valueOf(AndroidUtils.getVersionCode()));
            btnCheckUpdate=(Button)rootView.findViewById(R.id.btnCheckUpdate);
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Light.ttf");
            btnCheckUpdate.setTypeface(tf);
            btnCheckUpdate.setOnClickListener(this);

            //btnCheckUpdate.setVisibility(Button.GONE);

            txtHelpLine=(TextView)rootView.findViewById(R.id.txtHelpLine);
            txtHelpLine.setOnClickListener(this);
            txtYouTubeLink=(TextView)rootView.findViewById(R.id.txtYouTubeLink);
            txtYouTubeLink.setOnClickListener(this);
            txtLocation=(TextView)rootView.findViewById(R.id.txtLocation);
            txtLocation.setVisibility(TextView.GONE);

        }catch (Exception ex){
            Logger.Log(AboutFragment.class.getName(), ex);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sfaCommon.displayDate(getActivity());

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_about));

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnCheckUpdate:{

                try{

                    new ProgressDialogUtils(getActivity());

                    ProgressDialogUtils.showProgressDialog(AbstractApplication.get().getString(R.string.Checking));

                    updateServiceUtils=new UpdateServiceUtils();

                    updateServiceUtils.checkUpdate();

                    ProgressDialogUtils.closeProgressDialog();

                }catch (Exception ex){

                    ProgressDialogUtils.closeProgressDialog();
                    DialogUtils.showAlertDialog(getActivity(), AbstractApplication.get().getString(R.string.Errorwhilecheckingupdate));
                    Logger.Log(AboutFragment.class.getName(), ex);
                    return;
                }

            }break;

            case R.id.txtHelpLine:{

                try
                {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "8333858060"));
                    startActivity(intent);

                }catch (Exception ex){

                }

            }break;

            case R.id.txtYouTubeLink:{

                try
                {

                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=VrrFNQ0QCp8")));

                }catch (Exception ex){

                    DialogUtils.showAlertDialog(getActivity(),"Error while opening youtube");

                }

            }break;
        }
    }


}