package com.inventrax_pepsi.common.constants;

/**
 * Author   : Naresh P.
 * Date		: 27/03/2016
 * Purpose	: Order Status
 */


public enum OrderStatus {

    Initiated(1),
    Open(2),
    InProcess(3),
    InTransit(4),
    Partial(5),
    Cancelled(6),
    Completed(7);

    private int value;

    private OrderStatus(int value){
        this.value=value;
    }

    public int getStatus(){
        return this.value;
    }
}
