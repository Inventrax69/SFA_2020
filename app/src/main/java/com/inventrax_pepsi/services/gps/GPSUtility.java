package com.inventrax_pepsi.services.gps;

import android.location.Location;

/**
 * Created by android on 7/2/2016.
 */
public class GPSUtility {

    public static double getMetersFromLatLong(Location location1,Location location2){

        Location location;

        try
        {
            location2.getLatitude();
            location2.getLongitude();

            location1.getLatitude();
            location1.getLongitude();


            double radiusOfEarth=6373000; // In Meters 6373000; In Kilo Meters 6373;
            location =new Location("");

            location.setLatitude((location2.getLatitude()*Math.PI/180) - (location1.getLatitude()*Math.PI/180));
            location.setLongitude((location2.getLongitude()*Math.PI/180) - (location1.getLongitude()*Math.PI/180));

            double a= Math.pow(Math.sin(location.getLatitude()/2),2) + Math.cos(location1.getLatitude()) + Math.cos(location2.getLatitude())  + Math.pow(Math.sin(location.getLongitude()/2),2);
            double c= 2 * Math.atan2(Math.sqrt(a),Math.sqrt(1-a));

            return (radiusOfEarth * c);

        }catch (Exception ex){
            return 0;
        }

    }


    public static double getByLatLong(Location location1,Location location2){

        try
        {

            return location1.distanceTo(location2);


        }catch (Exception ex){
            return 0;
        }

    }


}
