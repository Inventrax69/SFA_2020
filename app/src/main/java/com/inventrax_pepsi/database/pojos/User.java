package com.inventrax_pepsi.database.pojos;

/**
 * Created by Naresh on 09-Mar-16.
 */
public class User {
    private int autoIncId,userId;
    private String userLogInId,userJSON,loginInfoJSON,loadOutJSON;

    public User(){}

    public User(int userId, String userLogInId, String userJSON, String loginInfoJSON, String loadOutJSON) {
        this.userId = userId;
        this.userLogInId = userLogInId;
        this.userJSON = userJSON;
        this.loginInfoJSON = loginInfoJSON;
        this.loadOutJSON = loadOutJSON;
    }

    public int getAutoIncId() {
        return autoIncId;
    }

    public void setAutoIncId(int autoIncId) {
        this.autoIncId = autoIncId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserLogInId() {
        return userLogInId;
    }

    public void setUserLogInId(String userLogInId) {
        this.userLogInId = userLogInId;
    }

    public String getUserJSON() {
        return userJSON;
    }

    public void setUserJSON(String userJSON) {
        this.userJSON = userJSON;
    }

    public String getLoginInfoJSON() {
        return loginInfoJSON;
    }

    public void setLoginInfoJSON(String loginInfoJSON) {
        this.loginInfoJSON = loginInfoJSON;
    }

    public String getLoadOutJSON() {
        return loadOutJSON;
    }

    public void setLoadOutJSON(String loadOutJSON) {
        this.loadOutJSON = loadOutJSON;
    }
}
