package org.nearbyshops;

/**
 * Created by sumeet on 22/3/17.
 */
public class Constants {


    public static final String SDS_URL = "http://sds.nearbyshops.org";


    // role codes
    public static final int ROLE_ADMIN_CODE = 1;
    public static final int ROLE_STAFF_CODE = 2;
    public static final int ROLE_DELIVERY_GUY_CODE = 3;
    public static final int ROLE_SHOP_ADMIN_CODE = 4;
    public static final int ROLE_SHOP_STAFF_CODE = 5;
    public static final int ROLE_DELIVERY_GUY_SELF_CODE = 6;
    public static final int ROLE_END_USER_CODE = 7;




    // Constants for the Roles in the Application
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_STAFF = "STAFF";
    public static final String ROLE_DELIVERY_GUY = "ROLE_DELIVERY_GUY";
    public static final String ROLE_SHOP_ADMIN = "ROLE_SHOP_ADMIN";
    public static final String ROLE_SHOP_STAFF = "SHOP_STAFF";
    public static final String ROLE_DELIVERY_GUY_SELF = "ROLE_DELIVERY_GUY_SELF";
    public static final String ROLE_END_USER = "END_USER";




    public static final String NOTIFICATION_TYPE_SHOP_CREATED = "NOTIFICATION_TYPE_SHOP_CREATED";
    public static final String NOTIFICATION_TYPE_ORDER_RECEIVED = "NOTIFICATION_TYPE_ORDER_RECEIVED";
    public static final String NOTIFICATION_TYPE_ORDER_PACKED = "NOTIFICATION_TYPE_ORDER_PACKED";
    public static final String NOTIFICATION_TYPE_ORDER_UPDATES = "NOTIFICATION_TYPE_ORDER_UPDATES";
    public static final String NOTIFICATION_TYPE_GENERAL = "NOTIFICATION_TYPE_GENERAL";


    // public notification channels for firebase push notifications
    public static final String CHANNEL_END_USER = "_end_user";
    public static final String CHANNEL_SHOP_STAFF = "_shop_staff";
    public static final String CHANNEL_MARKET_STAFF = "market_staff";
    public static final String CHANNEL_DELIVERY_STAFF = "delivery_staff";


    public static final String CHANNEL_SHOP_WITH_SHOP_ID = "shop_";
    public static final String CHANNEL_END_USER_WITH_USER_ID = "end_user_";



}
