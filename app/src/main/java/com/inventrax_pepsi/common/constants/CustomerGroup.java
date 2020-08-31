package com.inventrax_pepsi.common.constants;

/**
 * Created by android on 5/5/2016.
 */
public enum CustomerGroup {

    Plant(1),
    Distributor(2),
    Depot(3),
    Outlet(4),
    NationalKeyAcc(5),
    KeyOutlet(6);

    private int value;

    private CustomerGroup(int value){
        this.value=value;
    }

    public int getCustomerGroup(){
        return this.value;
    }
}
