package com.inventrax_pepsi.common.constants;

/**
 * Author   : Naresh P.
 * Date		: 03/03/2016
 * Purpose	: Web Service URL's and Web Methods
 */


public interface ServiceURLConstants {

    String PROTOCOL = "http://";

    //String DOMAIN = "pearlbigleap.in/SFA";
    //String DOMAIN = "pearlbigleap.in/SFA_Stage";
    //String DOMAIN = "103.252.184.181/SFA_2018/";
    //String DOMAIN = "192.168.1.172/sfa_2020";
    //String DOMAIN = "192.168.1.20/sfa2020";
    //String DOMAIN = "192.168.1.90/SFA_2018/";
    String DOMAIN = "192.168.1.242/SFA_Stage";

    String ApplicationASMX = "/mWebServices/SFAStandardWebService.asmx";

    String SOAP_ACTION_URL = "http://tempuri.org/";
    String WSDL_TARGET_NAMESPACE = "http://tempuri" +
            "" +
            ".org/";

    String CRASH_REPORT_PAGE = "/mSFA/AppCrashReportReceiver.aspx";

    String SOAP_ADDRESS = ServiceURLConstants.PROTOCOL + ServiceURLConstants.DOMAIN + ServiceURLConstants.ApplicationASMX;
    String SOAP_ADDRESS_ASMX = ServiceURLConstants.PROTOCOL + ServiceURLConstants.DOMAIN + ServiceURLConstants.ApplicationASMX;

    /*
     * Application crash report path
     * */
    String APP_CRASH_REPORT_PATH = ServiceURLConstants.PROTOCOL + ServiceURLConstants.DOMAIN + ServiceURLConstants.CRASH_REPORT_PAGE;

    /*
     * App update URL
     * */
    String UPDATE_APK_URL = ServiceURLConstants.PROTOCOL + ServiceURLConstants.DOMAIN + "/update.json";
    //String UPDATE_APK_URL = "http://192.168.1.20/SFA" + "/update.json";

    /*
     * SKU image path
     * */
    String SKU_IMAGE_URL = ServiceURLConstants.PROTOCOL + ServiceURLConstants.DOMAIN + "/SKUImages/";

    /*
     * Planogram image path
     * */
    String VISI_PLANOGRAM_IMAGE_URL = ServiceURLConstants.PROTOCOL + ServiceURLConstants.DOMAIN + "/PlanogramImages/";

    String OUTLET_IMAGE_URL = ServiceURLConstants.PROTOCOL + ServiceURLConstants.DOMAIN + "/";

    /*
     * Dashboard slider image path
     * */
    String DASHBOARD_SLIDER_IMAGE_URL = ServiceURLConstants.PROTOCOL + ServiceURLConstants.DOMAIN + "/SliderImages/";

    /*
     * To check application connectivity
     * */
    String METHOD_CHECK_INTERNET = "checkInternetConnectivity";

    /*
     * For Login
     * */
    String METHOD_LOGIN_REQUEST = "DoLogin";

    /*
     * For Logout
     * */
    String METHOD_DO_LOGOUT = "DoLogout";

    /*
     * To get SKU's based on route
     * */
    String METHOD_GET_SKU_LIST = "GetItemsByRoute";

    /*
     * To get customers based on route
     * */
    String METHOD_GET_CUSTOMER_LIST = "GetCustomers";

    /*
     * To get van inventory based on route
     * */
    String METHOD_GET_LOAD_OUT_INFO = "GetLoadOutInfo";

    /*
     * To get discounts based on customers
     * */
    String METHOD_GET_DISCOUNT_LIST = "GetDiscounts";

    /*
     * To get delivery list based on route
     * */
    String METHOD_GET_DELIVERY_LIST = "GetDeliveryOrders";

    /*
     * To create ready-sale or pre-sale order
     * */
    String METHOD_CREATE_ORDER = "CreateOrder";

    /*
     * To update payment information
     * */
    String METHOD_UPDATE_INVOICE = "UpdateBillStatus";

    /*
     * To update ready sale order status
     * */
    String METHOD_UPDATE_ORDER = "UpdateOrderStatus";

    /*
     * Customer wise check-in and check out
     * */
    String METHOD_CHECK_IN_OUT = "DoCheckInOut";

    /*
     * To get last 7 orders based on customers
     * */
    String METHOD_CUSTOMER_ORDER_HISTORY = "GetCustomerOrderHis";

    /*
     * For customer returns ( Empties,Crowns )
     * */
    String METHOD_CUSTOMER_RETURNS = "CreateCustomerReturns";

    /*
     * To get RA or PSR run rate details
     *  */
    String METHOD_USER_RUN_RATE = "GetUserRunRate";

    /*
     * To register asset complaint requests
     * */
    String METHOD_ASSET_COMPLAINT = "CreateOutletComplaints";

    /*
     * To get asset complaint history based on customers
     * */
    String METHOD_GET_COMPLAINT_HISTORY = "GetOutletComplaints";

    /*
     * To create asset audit info
     * */
    String METHOD_CREATE_ASSET_CAPTURE_HISTORY = "CreateCustomerAuditInfo";

    /*
     * To create new asset request
     * */
    String METHOD_CREATE_ASSET_REQUEST = "CreateAssetRequest";

    /*
     * To create asset pullout info
     * */
    String METHOD_CREATE_ASSET_PULLOUT = "SaveAssetPulloutInfo";

    /*
     * To get asset audit history based on customers
     * */
    String METHOD_GET_ASSET_CAPTURE_HISTORY = "GetAssetCaptureHistories";

    /*
     * To crete ready sale invoice
     * */
    String METHOD_CREATE_INVOICE = "CreateInvoice";

    /*
     * To get asset information based on route
     * */
    String METHOD_CHECK_ASSET = "GetCustomizedAssetInfo";

    String METHOD_GET_ORDERS_BY_ROUTES = "GetOrdersByRoutes";

    /*
     * To get today orders based on user
     * */
    String METHOD_GET_ORDERS_BY_USERS = "GetOrdersByUsers";

    /*
     * To create new outlet request
     * */
    String METHOD_CREATE_CUSTOMER = "CreateCustomer";

    /*
     * To update secondary load out settlement status
     * */
    String METHOD_SETTLEMENT_REQUEST = "UpdateSecondaryLoadoutDeliveryStatus";

    /*
     * To update customer check-in & check-out information
     * */
    String METHOD_SAVE_CUSTOMER_TRANSACTION = "SaveCustomerTransaction";

    String METHOD_GET_CUSTOMER_CREDIT_DUES_BY_ROUTES = "GetCustomerCreditDuesByRoutes";
    //String APP_CRASH_REPORT_PATH ="http://192.168.1.5/SFA/mSFA/AppCrashReportReceiver.aspx";


    // To get depots assigned to users
    String GET_USER_ASSIGNED_DEPOTS = "GetUserAssginedDepots";

    // To get active stock data for the selected depot
    String GET_ACTIVE_STOCK_DEPOTWISE = "GetActiveStockDepotWise";

    // To update the weekly stock at selected depot
    String UPDATE_WEEKLY_STOCK_AT_DEPOT = "UpdateWeeklyStockAtDepot";


}
