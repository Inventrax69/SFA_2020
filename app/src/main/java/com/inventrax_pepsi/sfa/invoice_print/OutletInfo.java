package com.inventrax_pepsi.sfa.invoice_print;

/**
 * Created by android on 4/2/2016.
 */
public class OutletInfo {

    private String outletName;
    private String outletCode;
    private String routeCode;
    private String routeName;
    private String cityName;
    private String tinNumber;
    private boolean isCreditAccount;







    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getOutletCode() {
        return outletCode;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public String getTinNumber() {
        return tinNumber;
    }

    public void setTinNumber(String tinNumber) {
        this.tinNumber = tinNumber;
    }

    public boolean isCreditAccount() {
        return isCreditAccount;
    }

    public void setCreditAccount(boolean creditAccount) {
        isCreditAccount = creditAccount;
    }
}
