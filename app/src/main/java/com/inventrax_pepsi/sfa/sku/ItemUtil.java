package com.inventrax_pepsi.sfa.sku;

/**
 * Created by android on 3/28/2016.
 */
public class ItemUtil {


    public static String getItemType(int itemTypeId){

        switch (itemTypeId){
            case 1: return "RGB";
            case 2: return "PB";
            case 3: return "CAN";
            case 4: return "NR";
            case 5: return "BIB";
            default:return "";
        }

    }

}
