package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by android on 3/9/2016.
 */
public class RootObject {

    @SerializedName("Orders")
    private List<Order> orders;
    @SerializedName("Invoices")
    private List<Invoice> invoices;
    @SerializedName("Customers")
    private List<Customer> customers;
    @SerializedName("Discounts")
    private List<Discount> discounts;
    @SerializedName("LogoutInfo")
    private LogoutInfo logoutInfo;
    @SerializedName("LoginInfo")
    private LoginInfo loginInfo;
    @SerializedName("Routes")
    private List<Route> routes;
    @SerializedName("Loads")
    private List<Load> loads;
    @SerializedName("Items")
    private List<Item> items;
    @SerializedName("Users")
    private List<User> users;
    @SerializedName("ServiceCode")
    private String serviceCode;
    @SerializedName("CustomerDiscounts")
    private List<CustomerDiscount> customerDiscounts;
    @SerializedName("ExecutionResponse")
    private ExecutionResponse executionResponse;
    @SerializedName("DistrictFMOs")
    private List<DistrictFMO> districtFMOs;
    @SerializedName("Schemes")
    private List<Scheme> schemes;
    @SerializedName("UserTrackings")
    private List<UserTrackHistory> userTrackings;
    @SerializedName("CustSKUOrders")
    private List<CustSKUOrder> custSKUOrders;
    @SerializedName("CustomerReturns")
    List<CustomerReturn> customerReturns;
    @SerializedName("CustomizedUserTargets")
    private List<CustomizedUserTarget> customizedUserTargets;
    @SerializedName("OutletComplaints")
    private List<OutletComplaint> outletComplaints;
    @SerializedName("AssetCaptureHistories")
    private List<AssetCaptureHistory> assetCaptureHistories;
    @SerializedName("AssetAvailInfos")
    private List<AssetAvailInfo> assetAvailInfos;
    @SerializedName("CustomerAssetPullouts")
    private List<CustomerAssetPullout> customerAssetPullouts;
    @SerializedName("CustomizedAssetInfos")
    private List<CustomizedAssetInfo> customizedAssetInfos;
    @SerializedName("AssetRequests")
    private List<AssetRequest> assetRequests;
    @SerializedName("CustomerAuditInfo")
    private CustomerAuditInfo customerAuditInfo;
    @SerializedName("CustomerTransactions")
    private List<CustomerTransaction> customerTransactions;
    @SerializedName("activeStocks")
    private List<ActiveStock> activeStockList;

    public List<CustomerTransaction> getCustomerTransactions() {
        return customerTransactions;
    }

    public void setCustomerTransactions(List<CustomerTransaction> customerTransactions) {
        this.customerTransactions = customerTransactions;
    }

    public CustomerAuditInfo getCustomerAuditInfo() {
        return customerAuditInfo;
    }

    public void setCustomerAuditInfo(CustomerAuditInfo customerAuditInfo) {
        this.customerAuditInfo = customerAuditInfo;
    }

    public List<AssetRequest> getAssetRequests() {
        return assetRequests;
    }

    public void setAssetRequests(List<AssetRequest> assetRequests) {
        this.assetRequests = assetRequests;
    }

    public List<CustomizedAssetInfo> getCustomizedAssetInfos() {
        return customizedAssetInfos;
    }

    public void setCustomizedAssetInfos(List<CustomizedAssetInfo> customizedAssetInfos) {
        this.customizedAssetInfos = customizedAssetInfos;
    }

    public List<CustomerAssetPullout> getCustomerAssetPullouts() {
        return customerAssetPullouts;
    }

    public void setCustomerAssetPullouts(List<CustomerAssetPullout> customerAssetPullouts) {
        this.customerAssetPullouts = customerAssetPullouts;
    }

    public List<AssetAvailInfo> getAssetAvailInfos() {
        return assetAvailInfos;
    }

    public void setAssetAvailInfos(List<AssetAvailInfo> assetAvailInfos) {
        this.assetAvailInfos = assetAvailInfos;
    }

    public List<CustomizedUserTarget> getCustomizedUserTargets() {
        return customizedUserTargets;
    }

    public void setCustomizedUserTargets(List<CustomizedUserTarget> customizedUserTargets) {
        this.customizedUserTargets = customizedUserTargets;
    }

    public List<AssetCaptureHistory> getAssetCaptureHistories() {
        return assetCaptureHistories;
    }

    public void setAssetCaptureHistories(List<AssetCaptureHistory> assetCaptureHistories) {
        this.assetCaptureHistories = assetCaptureHistories;
    }

    public List<OutletComplaint> getOutletComplaints() {
        return outletComplaints;
    }

    public void setOutletComplaints(List<OutletComplaint> outletComplaints) {
        this.outletComplaints = outletComplaints;
    }

    public List<CustomizedUserTarget> getCustomizedUserTarget() {
        return customizedUserTargets;
    }

    public void setCustomizedUserTarget(List<CustomizedUserTarget> customizedUserTarget) {
        this.customizedUserTargets = customizedUserTarget;
    }

    public List<CustomerReturn> getCustomerReturns() {
        return customerReturns;
    }

    public void setCustomerReturns(List<CustomerReturn> customerReturns) {
        this.customerReturns = customerReturns;
    }

    public List<CustSKUOrder> getCustSKUOrders() {
        return custSKUOrders;
    }

    public void setCustSKUOrders(List<CustSKUOrder> custSKUOrders) {
        this.custSKUOrders = custSKUOrders;
    }

    public List<UserTrackHistory> getUserTrackings() {
        return userTrackings;
    }

    public void setUserTrackings(List<UserTrackHistory> userTrackings) {
        this.userTrackings = userTrackings;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(List<Discount> discounts) {
        this.discounts = discounts;
    }

    public LogoutInfo getLogoutInfo() {
        return logoutInfo;
    }

    public void setLogoutInfo(LogoutInfo logoutInfo) {
        this.logoutInfo = logoutInfo;
    }

    public LoginInfo getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public List<Load> getLoads() {
        return loads;
    }

    public void setLoads(List<Load> loads) {
        this.loads = loads;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }


    public List<CustomerDiscount> getCustomerDiscounts() {
        return customerDiscounts;
    }

    public void setCustomerDiscounts(List<CustomerDiscount> customerDiscounts) {
        this.customerDiscounts = customerDiscounts;
    }

    public ExecutionResponse getExecutionResponse() {
        return executionResponse;
    }

    public void setExecutionResponse(ExecutionResponse executionResponse) {
        this.executionResponse = executionResponse;
    }

    public List<DistrictFMO> getDistrictFMOs() {
        return districtFMOs;
    }

    public void setDistrictFMOs(List<DistrictFMO> districtFMOs) {
        this.districtFMOs = districtFMOs;
    }

    public List<Scheme> getSchemes() {
        return schemes;
    }

    public void setSchemes(List<Scheme> schemes) {
        this.schemes = schemes;
    }

    public List<ActiveStock> getActiveStockList() {
        return activeStockList;
    }

    public void setActiveStockList(List<ActiveStock> activeStockList) {
        this.activeStockList = activeStockList;
    }
}
