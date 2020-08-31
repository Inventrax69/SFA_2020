package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by android on 3/9/2016.
 */
public class User {

    @SerializedName("AddressBook")
    private AddressBook addressBook;
    @SerializedName("AltEmail")
    private String altEmail;
    @SerializedName("Gender")
    private String gender;
    @SerializedName("UserDataFilters")
    private List<UserDataFilter> userDataFilters;
    @SerializedName("UserType")
    private String userType;
    @SerializedName("IsDeleted")
    private boolean isDeleted;
    @SerializedName("IsActive")
    private boolean isActive;
    @SerializedName("UserTargets")
    private List<UserTarget> userTargets;
    @SerializedName("UserId")
    private int userId;
    @SerializedName("UserCode")
    private String userCode;
    @SerializedName("FirstName")
    private String firstName;
    @SerializedName("LatsName")
    private String latsName;
    @SerializedName("Email")
    private String email;
    @SerializedName("MobileNumber1")
    private String mobileNumber1;
    @SerializedName("MobileNumber2")
    private String mobileNumber2;
    @SerializedName("ManagerId")
    private int managerId;
    @SerializedName("UserTypeId")
    private int userTypeId;
    @SerializedName("LoginUserId")
    private String loginUserId;
    @SerializedName("LoginPassword")
    private String loginPassword;
    @SerializedName("RouteList")
    private List<RouteList> routeList;
    @SerializedName("RoleList")
    private List<RoleList> roleList;
    @SerializedName("AuditInfo")
    private AuditInfo  auditInfo;

    public AddressBook getAddressBook() {
        return addressBook;
    }

    public void setAddressBook(AddressBook addressBook) {
        this.addressBook = addressBook;
    }

    public String getAltEmail() {
        return altEmail;
    }

    public void setAltEmail(String altEmail) {
        this.altEmail = altEmail;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<UserDataFilter> getUserDataFilters() {
        return userDataFilters;
    }

    public void setUserDataFilters(List<UserDataFilter> userDataFilters) {
        this.userDataFilters = userDataFilters;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public List<UserTarget> getUserTargets() {
        return userTargets;
    }

    public void setUserTargets(List<UserTarget> userTargets) {
        this.userTargets = userTargets;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLatsName() {
        return latsName;
    }

    public void setLatsName(String latsName) {
        this.latsName = latsName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber1() {
        return mobileNumber1;
    }

    public void setMobileNumber1(String mobileNumber1) {
        this.mobileNumber1 = mobileNumber1;
    }

    public String getMobileNumber2() {
        return mobileNumber2;
    }

    public void setMobileNumber2(String mobileNumber2) {
        this.mobileNumber2 = mobileNumber2;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public int getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(int userTypeId) {
        this.userTypeId = userTypeId;
    }

    public String getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(String loginUserId) {
        this.loginUserId = loginUserId;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public List<RouteList> getRouteList() {
        return routeList;
    }

    public void setRouteList(List<RouteList> routeList) {
        this.routeList = routeList;
    }

    public List<RoleList> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<RoleList> roleList) {
        this.roleList = roleList;
    }

    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(AuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }
}
