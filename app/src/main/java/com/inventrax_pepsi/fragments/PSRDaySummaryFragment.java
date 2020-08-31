package com.inventrax_pepsi.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.inventrax_pepsi.R;
import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.SFACommon;
import com.inventrax_pepsi.sfa.order.CustomObject;
import com.inventrax_pepsi.sfa.order.OrderUtil;
import com.inventrax_pepsi.sfa.pojos.RouteList;
import com.inventrax_pepsi.util.ProgressDialogUtils;
import com.inventrax_pepsi.util.SpinnerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 6/22/2016.
 */
public class PSRDaySummaryFragment extends Fragment implements   AdapterView.OnItemSelectedListener {

    private View rootView;
    private ListView listView;

    private SFACommon sfaCommon;
    private Spinner spinnerRouteList;
    private String selectedRouteCode = "";
    private CharSequence[] userRouteCharSequences;
    private ArrayList<String> userRouteStringList;
    private RelativeLayout layoutRouteSelection;
    private TextView txtRouteName,txtTotalOrderValue;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_psr_day_summary,container,false);

        new ProgressDialogUtils(getActivity());

        sfaCommon = SFACommon.getInstance();

        loadFormControls();

        return rootView;
    }

    private void loadFormControls(){
        try
        {

            layoutRouteSelection = (RelativeLayout) rootView.findViewById(R.id.layoutRouteSelection);
            layoutRouteSelection.setVisibility(RelativeLayout.GONE);

            txtRouteName = (TextView) rootView.findViewById(R.id.txtRouteName);
            txtTotalOrderValue = (TextView) rootView.findViewById(R.id.txtTotalOrderValue);

            listView=(ListView)rootView.findViewById(R.id.list);


            spinnerRouteList = (Spinner) rootView.findViewById(R.id.spinnerRouteList);
            spinnerRouteList.setOnItemSelectedListener(this);

            userRouteStringList = new ArrayList<>();

            for (RouteList userRoute : AppController.getUser().getRouteList()) {
                userRouteStringList.add(userRoute.getRouteCode());
            }

            SpinnerUtils.getSpinner(getActivity(), "Select Route", spinnerRouteList, userRouteStringList);

            loadOrderHistory(spinnerRouteList.getSelectedItem().toString());

        }catch (Exception ex){
            Logger.Log(PSRDaySummaryFragment.class.getName(),ex);
            return;
        }
    }

    private void loadOrderHistory(String routeCode){

        try
        {

            OrderUtil orderUtil=new OrderUtil();


            List<String> SKUOrderHistory = getSKUOrderHistory(orderUtil.getSKUWiseOrdersByRouteCode(routeCode));


            if (SKUOrderHistory!=null) {
                listView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.label,SKUOrderHistory.toArray(new String[0])));

            }

        }catch (Exception ex){

        }
    }

    private List<String> getSKUOrderHistory(List<CustomObject> skuHistory){
        List<String> history=new ArrayList<>();
        try
        {
            if (skuHistory==null)
                return history;

            double caseQuantity=0,bottleQuantity=0;

            for (CustomObject customObject:skuHistory){

                caseQuantity+=customObject.getCaseQuantity();
                bottleQuantity+=customObject.getBottleQuantity();
                history.add(customObject.getBrandName() + " " + customObject.getItemPack() + "  MRP : " + customObject.getItemMRP()  + " [ "   + ( customObject.getCaseQuantity()>0 ? (int)customObject.getCaseQuantity() + " CS " :"" ) +  ( customObject.getBottleQuantity()>0 ? (int)customObject.getBottleQuantity() + " FB " : "" ) + " ] " ) ;

            }

            txtTotalOrderValue.setText("Total : " +  ( caseQuantity > 0 ? (int)caseQuantity + " CS  " : " " ) + (  bottleQuantity > 0 ? (int)bottleQuantity + " FB " : " " ) );

            return history;


        }catch (Exception ex){
            return history;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {

            case R.id.spinnerRouteList: {

                selectedRouteCode = spinnerRouteList.getSelectedItem().toString();

                if (AppController.mapUserRoutes!=null)
                    txtRouteName.setText(AppController.mapUserRoutes.get(selectedRouteCode));

                try {
                    ProgressDialogUtils.showProgressDialog();

                    txtTotalOrderValue.setText("");

                    loadOrderHistory(selectedRouteCode);

                    ProgressDialogUtils.closeProgressDialog();

                } catch (Exception ex) {
                    ProgressDialogUtils.closeProgressDialog();
                }


            }
            break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onResume() {
        super.onResume();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_psr_day_summary));

        sfaCommon.hideUserInfo(getActivity());
        layoutRouteSelection.setVisibility(RelativeLayout.VISIBLE);

    }
}
