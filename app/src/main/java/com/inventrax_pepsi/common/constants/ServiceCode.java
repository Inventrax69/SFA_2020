package com.inventrax_pepsi.common.constants;

/**
 * Author   : Naresh P.
 * Date		: 08/03/2016
 * Purpose	: Service Codes
 */

public interface ServiceCode {


    String CREATE_ORDER = "CreateOrder";
    String UPDATE_ORDER = "UpdateOrder";
    String DELETE_ORDER = "DeleteOrder";
    String CREATE_USER = "CreateUser";
    String UPDATE_USER = "UpdateUser";
    String DELETE_USER = "DeleteUser";

    String GET_USER_DATA = "GetUserData";
    String CREATE_LOAD = "CreateLoad";
    String DELETE_INVOICE = "DeleteInvoice";
    String LOGIN = "DoLogin";
    String LOGOUT = "DoLogout";
    String GET_CUSTOMERS_BY_ROUTE = "GetCustomersByRoutes";
    String CREATE_ITEM = "CreateItem";
    String CREATE_ITEM_PRICE = "CreateItemPrice";
    String CREATE_ITEM_UOM = "CreateItemUoM";
    String UPDATE_ITEM = "UpdateItem";
    String UPDATE_ITEM_PRICE = "UpdateItemPrice";
    String UPDATE_ITEM_UOM = "UpdateItemUoM";
    String CREATE_ITEM_MASTER = "CreateItemMaster";
    String UPDATE_ITEM_MASTER = "UpdateItemMaster";
    String ADD_ORDER_ITEM = "AddOrderItem";
    String DELETE_ORDER_ITEM = "DeleteOrderItem";
    String UPDATE_ORDER_ITEM = "UpdateOrderItem";
    String GET_LOAD_BY_ROUTES = "GetLoadByRoutes";
    String GET_DISCOUNTS_BY_ROUTES = "GetDiscountsByRoutes";
    String GET_SCHEMES_BY_ROUTES = "GetSchemesByRoutes";
    String GET_ITEMS_WITH_PRICE_BY_DISTRICT = "GetItemsWithPriceByDistrict";
    String GET_ITEMS_WITH_PRICE_BY_TERRITORY="GetItemsByTerritory";
    String GET_ITEMS_WITH_PRICE_BY_KOL = "GetItemsWithPriceByKOL";
    String GET_DELIVERY_ORDERS_BY_ROUTES= "GetDeliveryOrdersByRoutes";
    String GET_LOAD_OUTS_BY_ROUTES = "GetLoadoutsByRoutes";
    String UPDATE_BILL_STATUS="UpdateBillStatus";
    String UPDATE_ORDER_STATUS="UpdateOrderStatus";
    String DO_CHECK_IN_OUT=" DoCheckInOut";
    String GET_CUSTOMER_ORDER_HISTORY = "GetCustomerOrderHis";
    String CREATE_CUSTOMER_RETURNS ="CreateCustomerReturns";
    String GET_USER_RUN_RATE = "GetUserRunRate";
    String CREATE_ASSET_COMPLAINTS="CreateOutletComplaints";
    String GET_COMPLAINT_HISTORY = "GetOutletComplaints";
    String CREATE_ASSET_CAPTURE = "CustomerAuditInfo";
    String CREATE_ASSET_REQUEST = "CreateAssetRequest";
    String CREATE_ASSET_PULLOUT = "SaveCustomerAssetPulloutInfo";
    String GET_ASSET_CAPTURE_HISTORY = "GetAssetCaptureHistories";
    String CREATE_INVOICE = "CreateInvoice";
    String ASSET_AVAIL_CHECK = "GetCustomizedAssetInfo";
    String GET_ORDERS_BY_ROUTES = "GetOrdersByRoutes";
    String GET_ORDERS_BY_USERS = "GetOrdersByUsers";
    String CREATE_CUSTOMER = "CreateCustomer";
    String SETTLEMENT_REQUEST = "CreateSettlementRequest";
    String UPDATE_LOAD_DELIVERY_STATUS  ="UpdateSecondaryLoadoutDeliveryStatus";
    String SAVE_CUSTOMER_TRANSACTION ="SaveCustomerTransaction";
    String GET_CUSTOMER_CREDIT_DUES_BY_ROUTES = "GetCustomerCreditDuesByRoutes";
    String GET_USER_ASSIGNED_DEPOTS = "GetUserAssginedDepots";
    String GET_ACTIVE_STOCK_DEPOTWISE = "GetActiveStockDepotWise";
    String UPDATE_WEEKLY_STOCK_AT_DEPOT = "UpdateWeeklyStockAtDepot";

}
