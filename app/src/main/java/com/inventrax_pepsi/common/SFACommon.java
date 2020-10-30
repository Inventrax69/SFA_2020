package com.inventrax_pepsi.common;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventrax_pepsi.R;
import com.inventrax_pepsi.application.AbstractApplication;
import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableCustomer;
import com.inventrax_pepsi.database.TableInvoice;
import com.inventrax_pepsi.database.TableJSONMessage;
import com.inventrax_pepsi.database.TableOrder;
import com.inventrax_pepsi.database.TableScheme;
import com.inventrax_pepsi.database.TableUserTracking;
import com.inventrax_pepsi.database.TableVehicleStock;
import com.inventrax_pepsi.database.pojos.JSONMessage;
import com.inventrax_pepsi.database.pojos.UserTracking;
import com.inventrax_pepsi.model.NavDrawerItem;
import com.inventrax_pepsi.model.OutletMenuItem;
import com.inventrax_pepsi.services.gps.GPSLocationService;
import com.inventrax_pepsi.sfa.pojos.Customer;
import com.inventrax_pepsi.sfa.pojos.OutletProfile;
import com.inventrax_pepsi.sfa.pojos.RouteList;
import com.inventrax_pepsi.sfa.pojos.User;
import com.inventrax_pepsi.util.DateUtils;
import com.inventrax_pepsi.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Author   : Naresh P.
 * Date		: 20/03/2016
 * Purpose	: SFA Common
 */

public class SFACommon {

    private TextView txtBottomBar,txtRightTopBar,txtLeftTopBar;
    private LinearLayout layoutTopBar;
    private static SFACommon sfaCommon;
    private DatabaseHelper databaseHelper;
    private TableUserTracking tableUserTracking;
    private TableCustomer tableCustomer;
    private TableInvoice tableInvoice;
    private TableOrder tableOrder;
    private TableVehicleStock tableVehicleStock;
    private TableScheme tableScheme;
    private TableJSONMessage tableJSONMessage;
    private Gson gson;

    public SFACommon(){
        gson=new  GsonBuilder().create();
        databaseHelper=DatabaseHelper.getInstance();
        tableUserTracking=databaseHelper.getTableUserTracking();
        tableCustomer=databaseHelper.getTableCustomer();
        tableInvoice=databaseHelper.getTableInvoice();
        tableOrder=databaseHelper.getTableOrder();
        tableScheme=databaseHelper.getTableScheme();
        tableVehicleStock=databaseHelper.getTableVehicleStock();
        tableJSONMessage =databaseHelper.getTableJSONMessage();
    }

    public static SFACommon getInstance(){
        if (sfaCommon!=null){
            return sfaCommon;
        }else {
            return new SFACommon();
        }
    }


    public void displayUserInfo(Activity activity) {

        try
        {
            layoutTopBar=(LinearLayout)activity.findViewById(R.id.layoutTopBar);
            User user= AppController.getUser();
            List<RouteList> userRouteList=user.getRouteList();

            txtRightTopBar=(TextView)activity.findViewById(R.id.txtRightTopBar);
            txtLeftTopBar=(TextView)activity.findViewById(R.id.txtLeftTopBar);

            txtLeftTopBar.setText("");

            txtRightTopBar.setText("");
            for (RouteList routeList:userRouteList) {
                txtRightTopBar.append( routeList.getRouteCode() + " | ");
            }

            layoutTopBar.setVisibility(LinearLayout.VISIBLE);

        }catch (Exception ex){
            Logger.Log(SFACommon.class.getName(), ex);
            return;
        }

    }

    public void displayUserInfo(Activity activity,String pageTitle,String  routeCode) {

        try
        {
            layoutTopBar=(LinearLayout)activity.findViewById(R.id.layoutTopBar);
            User user= AppController.getUser();
            List<RouteList> userRouteList=user.getRouteList();

            txtRightTopBar=(TextView)activity.findViewById(R.id.txtRightTopBar);
            txtLeftTopBar=(TextView)activity.findViewById(R.id.txtLeftTopBar);

            txtLeftTopBar.setText("");
            txtLeftTopBar.setText(pageTitle);

            txtRightTopBar.setText("");

            if (!TextUtils.isEmpty(routeCode)) {
                txtRightTopBar.append(routeCode + " | ");
            }else {
                for (RouteList routeList:userRouteList) {
                    txtRightTopBar.append( routeList.getRouteCode() + " | ");
                }
            }

            layoutTopBar.setVisibility(LinearLayout.VISIBLE);

        }catch (Exception ex){
            Logger.Log(SFACommon.class.getName(), ex);
            return;
        }

    }

    public void hideUserInfo(Activity activity) {

        try
        {
            layoutTopBar=(LinearLayout)activity.findViewById(R.id.layoutTopBar);

            layoutTopBar.setVisibility(LinearLayout.GONE);

        }catch (Exception ex){
        }
    }

    public void displayUserInfo(Activity activity,Customer customer,String pageTitle) {

        try
        {
            layoutTopBar=(LinearLayout)activity.findViewById(R.id.layoutTopBar);
            txtRightTopBar=(TextView)activity.findViewById(R.id.txtRightTopBar);
            txtLeftTopBar=(TextView)activity.findViewById(R.id.txtLeftTopBar);

            User user= AppController.getUser();
            List<RouteList> userRouteList=user.getRouteList();

            txtLeftTopBar.setText("");
            txtRightTopBar.setText("");

            if (customer!=null){

                txtLeftTopBar.setText(StringUtils.toCamelCase(customer.getCustomerName()) + " [ " +  customer.getCustomerCode() + " ] ");
                txtRightTopBar.setText( DateUtils.getDate() + " | " +  customer.getOutletProfile().getRouteCode());

            }else {
                txtLeftTopBar.setText(pageTitle);
                for (RouteList routeList:userRouteList) {
                    txtRightTopBar.append( routeList.getRouteCode() + " | ");
                }
            }

            layoutTopBar.setVisibility(LinearLayout.VISIBLE);

        }catch (Exception ex){
            Logger.Log(SFACommon.class.getName(), ex);
            return;
        }

    }

    public void displayDate(Activity activity) {

        try
        {
            User user= AppController.getUser();
            List<RouteList> userRouteList=user.getRouteList();

            layoutTopBar=(LinearLayout)activity.findViewById(R.id.layoutTopBar);
            txtRightTopBar=(TextView)activity.findViewById(R.id.txtRightTopBar);
            txtLeftTopBar=(TextView)activity.findViewById(R.id.txtLeftTopBar);

            txtLeftTopBar.setText("");
            txtRightTopBar.setText("");

            txtRightTopBar.setText(DateUtils.getDate());

            for (RouteList routeList:userRouteList) {
                txtRightTopBar.append(  " | " + routeList.getRouteCode() );
            }

            layoutTopBar.setVisibility(LinearLayout.VISIBLE);

        }catch (Exception ex){
            Logger.Log(SFACommon.class.getName(), ex);
            return;
        }

    }

    public List<OutletMenuItem> getOutletMenuItemList() {

        List<OutletMenuItem> menuItemList = new ArrayList<>();


        menuItemList.add(new OutletMenuItem(R.drawable.book_order, "Book Order"));
        menuItemList.add(new OutletMenuItem(R.drawable.delivery, "Delivery"));
        menuItemList.add(new OutletMenuItem(R.drawable.order_history, "Order History"));
        menuItemList.add(new OutletMenuItem(R.drawable.discount, "Discounts"));
        menuItemList.add(new OutletMenuItem(R.drawable.outlet_update, "Update Outlet"));
        menuItemList.add(new OutletMenuItem(R.drawable.asset_request, "Asset Request"));
        menuItemList.add(new OutletMenuItem(R.drawable.asset_audit, "Asset Audit"));
        menuItemList.add(new OutletMenuItem(R.drawable.complaint, "Complaint"));
        menuItemList.add(new OutletMenuItem(R.drawable.check_out, "Check Out"));


        return menuItemList;
    }

    public boolean checkInOut(Customer customer,int inOut,GPSLocationService gpsLocationService){

        boolean status=false;

        try
        {

            UserTracking userTracking = new UserTracking();

            OutletProfile outletProfile = customer.getOutletProfile();

            userTracking.setRouteCode(outletProfile.getRouteCode());
            userTracking.setRouteId(outletProfile.getRouteId());
            userTracking.setCustomerId(customer.getCustomerId());
            userTracking.setCustomerCode(customer.getCustomerCode());
            userTracking.setCheckInTimestamp(DatabaseHelper.getDateTime());
            userTracking.setCustomerType(customer.getCustomerGroup());
            userTracking.setLatitude("" + gpsLocationService.getLatitude());
            userTracking.setLongitude("" + gpsLocationService.getLongitude());

            if (inOut==2){

                UserTracking userTracking1=  tableUserTracking.getSingleUserTracking(customer.getCustomerId());
                userTracking1.setCheckOutTimestamp(DatabaseHelper.getDateTime());

                if(tableUserTracking.updateUserTracking(userTracking1)>0){
                    status=true;
                }

            }else {

                long userTrackId=tableUserTracking.createUserTracking(userTracking);

                if (userTrackId != 0) {

                    JSONMessage jsonMessage = new JSONMessage();
                    jsonMessage.setJsonMessage(gson.toJson(userTracking));
                    jsonMessage.setNoOfRequests(0);
                    jsonMessage.setSyncStatus(0);
                    jsonMessage.setNotificationId(userTracking.getCustomerId());
                    jsonMessage.setNotificationTypeId(3); // For User Notification Type Id is 3

                    long json_message_auto_inc_id = tableJSONMessage.createJSONMessage(jsonMessage);

                    if (json_message_auto_inc_id != 0) {

                        userTracking.setJsonMessageAutoIncId((int) json_message_auto_inc_id);
                        userTracking.setAutoIncId((int) userTrackId);

                        tableUserTracking.updateUserTracking(userTracking);

                        status = true;
                    }

                }
            }


        }catch (Exception ex){
            Logger.Log(SFACommon.class.getName(),ex);
            return status;
        }

        return status;
    }


    public  List<NavDrawerItem>  getMenuItemsByUserType(int userType){

        List<NavDrawerItem> menuList=new ArrayList<>();

        switch (userType)
        {
            // CEO
            case 1:{

                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_home),R.drawable.ic_action_menu_home));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_outlet_list),R.drawable.ic_action_menu_outlet_list,""+( (tableCustomer.getAllCustomers()!=null && tableCustomer.getAllCustomers().size()>0)?tableCustomer.getAllCustomers().size():0)));
                //menuList.add(new NavDrawerItem( AbstractApplication.get().getString(R.string.title_scheme_list),R.drawable.ic_action_menu_scheme_list, ( ( tableScheme.getAllFilteredSchemes() !=null && tableScheme.getAllFilteredSchemes().size()>0 ) ? tableScheme.getAllFilteredSchemes().size() : 0  )+""  ));
                menuList.add(new NavDrawerItem( AbstractApplication.get().getString(R.string.title_scheme_list),R.drawable.ic_action_menu_scheme_list, ( ( tableScheme.getAllFilteredSchemes() !=null && tableScheme.getAllFilteredSchemes().size()>0 ) ? tableScheme.getAllFilteredSchemes().size() : 0  )+""  ));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_planogram),R.drawable.ic_action_planogram));

            }break;

            // VP
            case 2:{

                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_home),R.drawable.ic_action_menu_home));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_outlet_list),R.drawable.ic_action_menu_outlet_list,""+( (tableCustomer.getAllCustomers()!=null && tableCustomer.getAllCustomers().size()>0)?tableCustomer.getAllCustomers().size():0)));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.title_scheme_list),R.drawable.ic_action_menu_scheme_list, ( ( tableScheme.getAllFilteredSchemes() !=null && tableScheme.getAllFilteredSchemes().size()>0 ) ? tableScheme.getAllFilteredSchemes().size() : 0  )+""  ));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_planogram),R.drawable.ic_action_planogram));

            }break;

            // GM
            case 3:{

                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_home),R.drawable.ic_action_menu_home));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_outlet_list),R.drawable.ic_action_menu_outlet_list,""+( (tableCustomer.getAllCustomers()!=null && tableCustomer.getAllCustomers().size()>0)?tableCustomer.getAllCustomers().size():0)));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.title_scheme_list),R.drawable.ic_action_menu_scheme_list, ( ( tableScheme.getAllFilteredSchemes() !=null && tableScheme.getAllFilteredSchemes().size()>0 ) ? tableScheme.getAllFilteredSchemes().size() : 0  )+""  ));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_planogram),R.drawable.ic_action_planogram));

            }break;

            // AGM
            case 4:{

                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_home),R.drawable.ic_action_menu_home));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_outlet_list),R.drawable.ic_action_menu_outlet_list,""+( (tableCustomer.getAllCustomers()!=null && tableCustomer.getAllCustomers().size()>0)?tableCustomer.getAllCustomers().size():0)));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.title_scheme_list),R.drawable.ic_action_menu_scheme_list, ( ( tableScheme.getAllFilteredSchemes() !=null && tableScheme.getAllFilteredSchemes().size()>0 ) ? tableScheme.getAllFilteredSchemes().size() : 0  )+""  ));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_planogram),R.drawable.ic_action_planogram));

            }break;

            // TDM
            case 5:{

                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_home),R.drawable.ic_action_menu_home));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_outlet_list),R.drawable.ic_action_menu_outlet_list,""+( (tableCustomer.getAllCustomers()!=null && tableCustomer.getAllCustomers().size()>0)?tableCustomer.getAllCustomers().size():0)));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.title_scheme_list),R.drawable.ic_action_menu_scheme_list, ( ( tableScheme.getAllFilteredSchemes() !=null && tableScheme.getAllFilteredSchemes().size()>0 ) ? tableScheme.getAllFilteredSchemes().size() : 0  )+""  ));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_planogram),R.drawable.ic_action_planogram));

            }break;

            // CE
            case 6:{

                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_home),R.drawable.ic_action_menu_home));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_outlet_list),R.drawable.ic_action_menu_outlet_list,""+( (tableCustomer.getAllCustomers()!=null && tableCustomer.getAllCustomers().size()>0)?tableCustomer.getAllCustomers().size():0)));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.title_scheme_list),R.drawable.ic_action_menu_scheme_list, ( ( tableScheme.getAllFilteredSchemes() !=null && tableScheme.getAllFilteredSchemes().size()>0 ) ? tableScheme.getAllFilteredSchemes().size() : 0  )+""  ));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_outlet_registration),R.drawable.ic_action_menu_outlet_registeration));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_planogram),R.drawable.ic_action_planogram));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_check_asset),R.drawable.ic_action_planogram));

            }break;

            // RA
            case 7:{

                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_home),R.drawable.ic_action_menu_home));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_outlet_list),R.drawable.ic_action_menu_outlet_list,""+( (tableCustomer.getAllCustomers()!=null && tableCustomer.getAllCustomers().size()>0)?tableCustomer.getAllCustomers().size():0)));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_delivery_list),R.drawable.ic_action_menu_delivery , "" + ( ( tableInvoice.getAllInvoices()!=null && tableInvoice.getAllInvoices().size()>0 ) ? tableInvoice.getAllInvoices().size() : 0 )));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_order_history),R.drawable.ic_action_menu_order_history ,( ( tableOrder.getAllOrders() !=null && tableOrder.getAllOrders().size() > 0 ) ? tableOrder.getAllOrders().size() : 0 )+""));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.title_scheme_list),R.drawable.ic_action_menu_scheme_list ,( ( tableScheme.getAllFilteredSchemes() !=null && tableScheme.getAllFilteredSchemes().size()>0 ) ? tableScheme.getAllFilteredSchemes().size() : 0  )+""  ));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.StockDetails) ,R.drawable.ic_action_menu_stock_details, ( ( tableVehicleStock.getAllVehicleStocks() !=null && tableVehicleStock.getAllVehicleStocks().size()>0 ) ? tableVehicleStock.getAllVehicleStocks().size() : 0 )  + "" )  );
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_planogram),R.drawable.ic_action_planogram));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_check_asset),R.drawable.ic_action_planogram));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_day_summary),R.drawable.ic_action_menu_order_history));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_stock_upload),R.drawable.ic_action_menu_order_history));
                //menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_technician),R.drawable.ic_action_menu_order_history));

            }break;

            // PSR
            case 8:{

                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_home),R.drawable.ic_action_menu_home));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_outlet_list),R.drawable.ic_action_menu_outlet_list,""+( (tableCustomer.getAllCustomers()!=null && tableCustomer.getAllCustomers().size()>0)?tableCustomer.getAllCustomers().size():0)));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_order_history),R.drawable.ic_action_menu_order_history ,( ( tableOrder.getAllOrders() !=null && tableOrder.getAllOrders().size() > 0 ) ? tableOrder.getAllOrders().size() : 0 )+""));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.title_scheme_list),R.drawable.ic_action_menu_scheme_list ,( ( tableScheme.getAllFilteredSchemes() !=null && tableScheme.getAllFilteredSchemes().size()>0 ) ? tableScheme.getAllFilteredSchemes().size() : 0  )+""  ));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_outlet_registration),R.drawable.ic_action_menu_outlet_registeration));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_new_outlet_list),R.drawable.ic_action_menu_outlet_list,""+( (tableCustomer.getAllNewCustomers()!=null && tableCustomer.getAllNewCustomers().size()>0)?tableCustomer.getAllNewCustomers().size():0)));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_check_asset),R.drawable.ic_action_planogram));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_planogram),R.drawable.ic_action_planogram));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_psr_day_summary),R.drawable.ic_action_menu_order_history));
                menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_stock_upload),R.drawable.ic_action_menu_order_history));
                //menuList.add(new NavDrawerItem(AbstractApplication.get().getString(R.string.nav_item_technician),R.drawable.ic_action_menu_outlet_registeration));

            }break;

            /*
            default:{

                menuList.add(new NavDrawerItem(false, AbstractApplication.get().getString(R.string.nav_item_home)));
                menuList.add(new NavDrawerItem(false, AbstractApplication.get().getString(R.string.nav_item_outlet_list),""+( (tableCustomer.getAllCustomers()!=null && tableCustomer.getAllCustomers().size()>0)?tableCustomer.getAllCustomers().size():0)));
                menuList.add(new NavDrawerItem(false, AbstractApplication.get().getString(R.string.nav_item_delivery_list),  "" + ( ( tableInvoice.getAllInvoices()!=null && tableInvoice.getAllInvoices().size()>0 ) ? tableInvoice.getAllInvoices().size() : 0 )));
                menuList.add(new NavDrawerItem(false, AbstractApplication.get().getString(R.string.nav_item_order_history), ( ( tableOrder.getAllOrders() !=null && tableOrder.getAllOrders().size() > 0 ) ? tableOrder.getAllOrders().size() : 0 )+""));
                menuList.add(new NavDrawerItem(false, AbstractApplication.get().getString(R.string.title_scheme_list), ( ( tableScheme.getAllFilteredSchemes() !=null && tableScheme.getAllFilteredSchemes().size()>0 ) ? tableScheme.getAllFilteredSchemes().size() : 0  )+""  ));
                menuList.add(new NavDrawerItem(false, AbstractApplication.get().getString(R.string.StockDetails) , ( ( tableVehicleStock.getAllVehicleStocks() !=null && tableVehicleStock.getAllVehicleStocks().size()>0 ) ? tableVehicleStock.getAllVehicleStocks().size() : 0 )  + "" )  );
                menuList.add(new NavDrawerItem(false, AbstractApplication.get().getString(R.string.nav_item_outlet_registration)));


            }break;*/
        }

        return menuList;
    }




    public static int getColorByBrand(String brandName){

        switch (brandName){

            case "Slice":{
                return ContextCompat.getColor(AbstractApplication.get(), R.color.Slice);
            }
            case "Pepsi":{
                return ContextCompat.getColor(AbstractApplication.get(), R.color.Pepsi);
            }
            case "Nimbooz Masala Soda":{
                return ContextCompat.getColor(AbstractApplication.get(), R.color.NimboozMasalaSoda);
            }
            case "Mountain Dew":{
                return ContextCompat.getColor(AbstractApplication.get(), R.color.MountainDew);
            }
            case "Mirinda Orange":{
                return ContextCompat.getColor(AbstractApplication.get(), R.color.MirindaOrange);
            }
            case "Mirinda Lemon":{
                return ContextCompat.getColor(AbstractApplication.get(), R.color.MirindaOrange);
            }
            case "ESODA":{
                return ContextCompat.getColor(AbstractApplication.get(), R.color.ESODA);
            }
            case "Diet Pepsi":{
                return ContextCompat.getColor(AbstractApplication.get(), R.color.DitePepsi);
            }
            case "Aquafina":{
                return ContextCompat.getColor(AbstractApplication.get(), R.color.Aquafina);
            }
            case "7up Revive":{
                return ContextCompat.getColor(AbstractApplication.get(), R.color.SevenUpRevive);
            }
            case "7UP":{
                return ContextCompat.getColor(AbstractApplication.get(), R.color.SevenUp);
            }
        }
        return ContextCompat.getColor(AbstractApplication.get(), R.color.colorControlNormal);
    }


    public static int  getRouteId(String routeCode){

        int routeId=0;

        try
        {
            for (RouteList routeList:AppController.getUser().getRouteList())
            {
                if (routeList.getRouteCode().equalsIgnoreCase(routeCode))
                {
                    routeId = routeList.getRouteId();
                    break;
                }
            }

            return  routeId;

        }catch (Exception ex){
            return 0;
        }

    }


}
