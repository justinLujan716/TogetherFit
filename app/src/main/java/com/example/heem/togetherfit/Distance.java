package com.example.heem.togetherfit;

/**
 * Created by Heem on 11/16/16. This class to calcluate the distance between 2 points
 *
 *
 */

public class Distance {

    /*
     * This method to calcluate the distance between 2 points. It returns the distance in miles by calling disMile
     */
    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = (double) (earthRadius * c);
        double distMile = distMile(dist);
        return dist;
    }
    /*
    * This method to calcluate the distance between 2 points in miles.
    */
    public static double distMile(double distance)
    {
        return (double) (distance * 0.000621371);
    }

}
