package com.inventrax_pepsi.common.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by android on 5/11/2016.
 */
public class AssetPulloutReason {

    public static Map<String,Integer> getAssetPulloutReasons(){

        Map<String,Integer> mapPulloutReasons=new HashMap<>();

        mapPulloutReasons.put("Select Reason",0);
        mapPulloutReasons.put("Less Sales",1);
        mapPulloutReasons.put("Outlet Closed",2);
        mapPulloutReasons.put("Replacement",3);
        mapPulloutReasons.put("Asset Damaged",4);






        return mapPulloutReasons;
    }

}
