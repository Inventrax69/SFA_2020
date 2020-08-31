package com.inventrax_pepsi.database.pojos;

/**
 * Author   : Naresh P.
 * Date		: 19/04/2016
 * Purpose	: Asset Capture
 */

public class AssetCapture {

    private int autoIncId,assetId,customerId,jsonMessageAutoIncId;
    private String QRCode,json;

    public int getAutoIncId() {
        return autoIncId;
    }

    public void setAutoIncId(int autoIncId) {
        this.autoIncId = autoIncId;
    }

    public int getAssetId() {
        return assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getJsonMessageAutoIncId() {
        return jsonMessageAutoIncId;
    }

    public void setJsonMessageAutoIncId(int jsonMessageAutoIncId) {
        this.jsonMessageAutoIncId = jsonMessageAutoIncId;
    }

    public String getQRCode() {
        return QRCode;
    }

    public void setQRCode(String QRCode) {
        this.QRCode = QRCode;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
