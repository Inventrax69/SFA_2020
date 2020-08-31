package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 4/5/2016.
 */
public class CustomizedUserTarget {

    @SerializedName("VisitedOutlet")
    private String visitedOutlet;
    @SerializedName("NextOutlet")
    private String nextOutlet;
    @SerializedName("MTDSale")
    private double mTDSale;
    @SerializedName("MTDPlan")
    private double mTDPlan;
    @SerializedName("MTDRunRate")
    private double mTDRunRate;
    @SerializedName("YTDRunRate")
    private double yTDRunRate;
    @SerializedName("CurrentRunRate")
    private double currentRunRate;
    @SerializedName("UserId")
    private int userId;
    @SerializedName("UserName")
    private String userName;
    @SerializedName("UserType")
    private String userType;
    @SerializedName("UserTypeId")
    private int userTypeId;
    @SerializedName("LineSalesCall")
    private double lineSalesCall;
    @SerializedName("TotalLinesSold")
    private double totalLinesSold;
    @SerializedName("DropSize")
    private double dropSize;
    @SerializedName("Sales")
    private double sales;
    @SerializedName("TotalCalls")
    private int totalCalls;
    @SerializedName("StrikeCalls")
    private int strikeCalls;
    @SerializedName("StrikeRate")
    private double strikeRate;
    @SerializedName("ScheduledCalls")
    private int scheduledCalls;
    @SerializedName("CompletedCalls")
    private int completedCalls;
    @SerializedName("BookedOrders")
    private int bookedOrders;
    @SerializedName("DeliveredOrders")
    private int deliveredOrders;
    @SerializedName("StartDate")
    private String startDate;
    @SerializedName("EndDate")
    private String endDate;
    @SerializedName("RouteId")
    private int routeId;
    @SerializedName("RouteCode")
    private String routeCode;
    @SerializedName("RouteName")
    private String routeName;
    @SerializedName("AgentName")
    private String agentName;
    @SerializedName("LRBVolumePlan")
    private double lRBVolumePlan;
    @SerializedName("LRBVolumeOrders")
    private double lRBVolumeOrders;
    @SerializedName("LRBVolumeDelivered")
    private double lRBVolumeDelivered;
    @SerializedName("AOPTarget")
    private double aOPTarget;
    @SerializedName("BOMRequiredRunRate")
    private double bOMRequiredRunRate;
    @SerializedName("BOMSales")
    private double bOMSales;
    @SerializedName("CurrVol")
    private double currVol;
    @SerializedName("VisitedOutletId")
    private int visitedOutletId;
    @SerializedName("NextOutletId")
    private int nextOutletId;


    public int getVisitedOutletId() {
        return visitedOutletId;
    }

    public void setVisitedOutletId(int visitedOutletId) {
        this.visitedOutletId = visitedOutletId;
    }

    public int getNextOutletId() {
        return nextOutletId;
    }

    public void setNextOutletId(int nextOutletId) {
        this.nextOutletId = nextOutletId;
    }

    public String getVisitedOutlet() {
        return visitedOutlet;
    }

    public void setVisitedOutlet(String visitedOutlet) {
        this.visitedOutlet = visitedOutlet;
    }

    public String getNextOutlet() {
        return nextOutlet;
    }

    public void setNextOutlet(String nextOutlet) {
        this.nextOutlet = nextOutlet;
    }

    public double getmTDSale() {
        return mTDSale;
    }

    public void setmTDSale(double mTDSale) {
        this.mTDSale = mTDSale;
    }

    public double getmTDPlan() {
        return mTDPlan;
    }

    public void setmTDPlan(double mTDPlan) {
        this.mTDPlan = mTDPlan;
    }

    public double getmTDRunRate() {
        return mTDRunRate;
    }

    public void setmTDRunRate(double mTDRunRate) {
        this.mTDRunRate = mTDRunRate;
    }

    public double getyTDRunRate() {
        return yTDRunRate;
    }

    public void setyTDRunRate(double yTDRunRate) {
        this.yTDRunRate = yTDRunRate;
    }

    public double getCurrentRunRate() {
        return currentRunRate;
    }

    public void setCurrentRunRate(double currentRunRate) {
        this.currentRunRate = currentRunRate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public int getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(int userTypeId) {
        this.userTypeId = userTypeId;
    }

    public double getLineSalesCall() {
        return lineSalesCall;
    }

    public void setLineSalesCall(double lineSalesCall) {
        this.lineSalesCall = lineSalesCall;
    }

    public double getTotalLinesSold() {
        return totalLinesSold;
    }

    public void setTotalLinesSold(double totalLinesSold) {
        this.totalLinesSold = totalLinesSold;
    }

    public double getDropSize() {
        return dropSize;
    }

    public void setDropSize(double dropSize) {
        this.dropSize = dropSize;
    }

    public double getSales() {
        return sales;
    }

    public void setSales(double sales) {
        this.sales = sales;
    }

    public int getTotalCalls() {
        return totalCalls;
    }

    public void setTotalCalls(int totalCalls) {
        this.totalCalls = totalCalls;
    }

    public int getStrikeCalls() {
        return strikeCalls;
    }

    public void setStrikeCalls(int strikeCalls) {
        this.strikeCalls = strikeCalls;
    }

    public double getStrikeRate() {
        return strikeRate;
    }

    public void setStrikeRate(double strikeRate) {
        this.strikeRate = strikeRate;
    }

    public int getScheduledCalls() {
        return scheduledCalls;
    }

    public void setScheduledCalls(int scheduledCalls) {
        this.scheduledCalls = scheduledCalls;
    }

    public int getCompletedCalls() {
        return completedCalls;
    }

    public void setCompletedCalls(int completedCalls) {
        this.completedCalls = completedCalls;
    }

    public int getBookedOrders() {
        return bookedOrders;
    }

    public void setBookedOrders(int bookedOrders) {
        this.bookedOrders = bookedOrders;
    }

    public int getDeliveredOrders() {
        return deliveredOrders;
    }

    public void setDeliveredOrders(int deliveredOrders) {
        this.deliveredOrders = deliveredOrders;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public double getlRBVolumePlan() {
        return lRBVolumePlan;
    }

    public void setlRBVolumePlan(double lRBVolumePlan) {
        this.lRBVolumePlan = lRBVolumePlan;
    }

    public double getlRBVolumeOrders() {
        return lRBVolumeOrders;
    }

    public void setlRBVolumeOrders(double lRBVolumeOrders) {
        this.lRBVolumeOrders = lRBVolumeOrders;
    }

    public double getlRBVolumeDelivered() {
        return lRBVolumeDelivered;
    }

    public void setlRBVolumeDelivered(double lRBVolumeDelivered) {
        this.lRBVolumeDelivered = lRBVolumeDelivered;
    }

    public double getaOPTarget() {
        return aOPTarget;
    }

    public void setaOPTarget(double aOPTarget) {
        this.aOPTarget = aOPTarget;
    }

    public double getbOMRequiredRunRate() {
        return bOMRequiredRunRate;
    }

    public void setbOMRequiredRunRate(double bOMRequiredRunRate) {
        this.bOMRequiredRunRate = bOMRequiredRunRate;
    }

    public double getbOMSales() {
        return bOMSales;
    }

    public void setbOMSales(double bOMSales) {
        this.bOMSales = bOMSales;
    }

    public double getCurrVol() {
        return currVol;
    }

    public void setCurrVol(double currVol) {
        this.currVol = currVol;
    }
}
