package com.inventrax_pepsi.common.constants;

/**
 * Created by android on 5/5/2016.
 */
public enum AccountType {

    PCI(1),
    CCX(2),
    MIX(3),
    PEPSI(4);

    private int value;

    private AccountType(int value){
        this.value=value;
    }

    public int getAccountType(){
        return this.value;
    }

}
