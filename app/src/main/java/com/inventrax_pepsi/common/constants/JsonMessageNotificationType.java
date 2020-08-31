package com.inventrax_pepsi.common.constants;

/**
 * Author   : Naresh P.
 * Date		: 27/03/2016
 * Purpose	: Order Status
 */


public enum JsonMessageNotificationType {

    Order(1),
    Invoice(2),
    UserTracking(3),
    CustomerReturn(4),
    AssetComplaint(5),
    AssetCapture(6),
    ReadySaleInvoice(7),
    AssetPullout(8),
    AssetRequest(9),
    Customer(10),
    CustomerTrans(11);

    private int value;

    private JsonMessageNotificationType(int value){
        this.value=value;
    }

    public int getNotificationType(){
        return this.value;
    }
}
