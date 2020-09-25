package org.nearbyshops.Model.ModelDelivery;



import org.nearbyshops.Model.Shop;

import java.sql.Time;


/**
 * Created by sumeet on 10/6/16.
 */
public class DeliverySlot {


    // Table Name
    public static final String TABLE_NAME = "DELIVERY_SLOT";


    // column Names
    public static final String SLOT_ID = "SLOT_ID";
    public static final String IS_ENABLED = "IS_ENABLED";
//    public static final String IS_DELIVERY_OR_PICKUP_SLOT = "IS_DELIVERY_OR_PICKUP_SLOT";
    public static final String IS_PICKUP_SLOT = "IS_PICKUP_SLOT";
    public static final String IS_DELIVERY_SLOT = "IS_DELIVERY_SLOT";
    public static final String SLOT_NAME = "SLOT_NAME";
    public static final String SLOT_START_TIME = "SLOT_START_TIME";
    public static final String DURATION_IN_HOURS = "DURATION_IN_HOURS";
    public static final String MAX_ORDERS_PER_DAY = "MAX_ORDERS_PER_DAY";
    public static final String SHOP_ID = "SHOP_ID"; // null indicates delivery by market
    public static final String CLOSING_HOURS = "CLOSING_HOURS"; // hours before the start time the booking closes



    //    public static final String SLOT_SORT_ORDER = "SLOT_SORT_ORDER";


    // create Delivery Address
    public static final String createTable = "CREATE TABLE IF NOT EXISTS " + DeliverySlot.TABLE_NAME + "("
            + " " + DeliverySlot.SLOT_ID + " SERIAL PRIMARY KEY,"
            + " " + DeliverySlot.IS_ENABLED + " boolean not null default true,"
            + " " + DeliverySlot.IS_DELIVERY_SLOT + " boolean not null default true,"
            + " " + DeliverySlot.IS_PICKUP_SLOT + " boolean not null default true,"
            + " " + DeliverySlot.SLOT_NAME + " text,"
            + " " + DeliverySlot.SLOT_START_TIME + " time without time zone NOT NULL DEFAULT now(),"
            + " " + DeliverySlot.DURATION_IN_HOURS + " int not null default 2,"
            + " " + DeliverySlot.MAX_ORDERS_PER_DAY + " int not null default 0,"
            + " " + DeliverySlot.SHOP_ID + " int,"
            + " " + DeliverySlot.CLOSING_HOURS + " int not null default 2,"
            + " FOREIGN KEY(" + DeliverySlot.SHOP_ID +") REFERENCES " + Shop.TABLE_NAME + "(" + Shop.SHOP_ID + ") ON DELETE CASCADE "
            + ")";



//    + " " + DeliverySlot.IS_DELIVERY_OR_PICKUP_SLOT + " boolean not null default true,"
//    + " " + DeliverySlot.SLOT_SORT_ORDER + " int not null default 0,"


    // instance variables
    private int slotID;
    private boolean isEnabled;
    private boolean isDeliverySlot;
    private boolean isPickupSlot;
    private String slotName;
    private int maxOrdersPerDay;
    private int shopID;

    private Time slotTime;
    private int durationInHours;
    private int closingHours;


    private int rt_order_count;


    public DeliverySlot() {
    }


    public DeliverySlot(boolean isEnabled, boolean isDeliverySlot, boolean isPickupSlot, String slotName, int maxOrdersPerDay, int shopID, Time slotTime, int durationInHours, int closingHours) {
        this.isEnabled = isEnabled;
        this.isDeliverySlot = isDeliverySlot;
        this.isPickupSlot = isPickupSlot;
        this.slotName = slotName;
        this.maxOrdersPerDay = maxOrdersPerDay;
        this.shopID = shopID;
        this.slotTime = slotTime;
        this.durationInHours = durationInHours;
        this.closingHours = closingHours;
    }


    // getter and setter methods


    public boolean isDeliverySlot() {
        return isDeliverySlot;
    }

    public void setDeliverySlot(boolean deliverySlot) {
        isDeliverySlot = deliverySlot;
    }

    public boolean isPickupSlot() {
        return isPickupSlot;
    }

    public void setPickupSlot(boolean pickupSlot) {
        isPickupSlot = pickupSlot;
    }

    public int getRt_order_count() {
        return rt_order_count;
    }

    public void setRt_order_count(int rt_order_count) {
        this.rt_order_count = rt_order_count;
    }

    public int getClosingHours() {
        return closingHours;
    }

    public void setClosingHours(int closingHours) {
        this.closingHours = closingHours;
    }

    public int getDurationInHours() {
        return durationInHours;
    }

    public void setDurationInHours(int durationInHours) {
        this.durationInHours = durationInHours;
    }

    public Time getSlotTime() {
        return slotTime;
    }

    public void setSlotTime(Time slotTime) {
        this.slotTime = slotTime;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public int getSlotID() {
        return slotID;
    }

    public void setSlotID(int slotID) {
        this.slotID = slotID;
    }


    public String getSlotName() {
        return slotName;
    }

    public void setSlotName(String slotName) {
        this.slotName = slotName;
    }

    public int getMaxOrdersPerDay() {
        return maxOrdersPerDay;
    }

    public void setMaxOrdersPerDay(int maxOrdersPerDay) {
        this.maxOrdersPerDay = maxOrdersPerDay;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }
}
