package org.nearbyshops.Utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.SecureRandom;
import java.util.Random;



public class UtilityMethods {

    // secure randon for generating tokens
    public static SecureRandom random = new SecureRandom();


    private static Random randomNew = new Random();



    private static Gson gson;

    //Customize the gson behavior here
    public static Gson getGson() {
        if (gson == null) {
            final GsonBuilder gsonBuilder = new GsonBuilder();
            gson = gsonBuilder
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

        }
        return gson;
    }


    public static int getRandomIntegerBetweenRange(int min, int max){
        return (int)(Math.random()*((max-min)+1))+min;
    }


    public static char[] generateOTP(int length) {
        String numbers = "1234567890";

        char[] otp = new char[length];

        for(int i = 0; i< length ; i++) {
            otp[i] = numbers.charAt(randomNew.nextInt(numbers.length()));
        }

        return otp;
    }


    public static float distFrom(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 6371; //kilometers
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);

        //        System.out.println("Distance : " + dist);

        return dist;
    }

}
