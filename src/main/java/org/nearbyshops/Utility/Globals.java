package org.nearbyshops.Utility;

import java.security.SecureRandom;




/**
 * Created by sumeet on 22/3/17.
 */
public class Globals {


    // secure randon for generating tokens
    public static SecureRandom random = new SecureRandom();



    public static boolean licensingRestrictionsEnabled = false;
    public static double maxMarketRangeInKms = 30;
    public static double maxVendorCount = 30;

}
