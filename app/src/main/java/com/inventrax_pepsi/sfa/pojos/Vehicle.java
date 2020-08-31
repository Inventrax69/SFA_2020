package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Naresh on 30-Mar-16.
 */
public class Vehicle {
    @SerializedName("VehicleNo")
    private String vehicleNo;
    @SerializedName("VehcleId")
    private int vehcleId;
    @SerializedName("LoadId")
    private int loadId;
    @SerializedName("LoadVehicleId")
    private int loadVehicleId;

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public int getVehcleId() {
        return vehcleId;
    }

    public void setVehcleId(int vehcleId) {
        this.vehcleId = vehcleId;
    }

    public int getLoadId() {
        return loadId;
    }

    public void setLoadId(int loadId) {
        this.loadId = loadId;
    }

    public int getLoadVehicleId() {
        return loadVehicleId;
    }

    public void setLoadVehicleId(int loadVehicleId) {
        this.loadVehicleId = loadVehicleId;
    }
}
