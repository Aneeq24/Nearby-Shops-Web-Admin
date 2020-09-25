package org.nearbyshops.Model.ModelDelivery;



import org.nearbyshops.Model.Shop;

import java.sql.Time;

/**
 * Created by sumeet on 10/6/16.
 */
public class SlotVendorAvailibility {


    // Table Name
    public static final String TABLE_NAME = "DELIVERY_SLOT_VENDOR";


    // column Names
    public static final String SLOT_VENDOR_ID = "SLOT_VENDOR_ID";
    public static final String DELIVERY_SLOT_ID = "DELIVERY_SLOT_ID";
    public static final String SHOP_ID = "SHOP_ID"; // null indicates delivery by market

    public static final String IS_ENABLED = "IS_ENABLED";
    public static final String ALLOW_PICKUP = "ALLOW_PICKUP";
    public static final String ALLOW_DELIVERY = "ALLOW_DELIVERY";
    public static final String MAX_ORDERS_PER_DAY = "MAX_ORDERS_PER_DAY";





    // create Delivery Address
    public static final String createTable = "CREATE TABLE IF NOT EXISTS " + SlotVendorAvailibility.TABLE_NAME + "("
            + " " + SlotVendorAvailibility.SLOT_VENDOR_ID + " SERIAL PRIMARY KEY,"
            + " " + SlotVendorAvailibility.DELIVERY_SLOT_ID + " int not null,"
            + " " + SlotVendorAvailibility.SHOP_ID + " int not null,"

            + " " + SlotVendorAvailibility.IS_ENABLED + " boolean not null default true,"
            + " " + SlotVendorAvailibility.ALLOW_DELIVERY + " boolean not null default true,"
            + " " + SlotVendorAvailibility.ALLOW_PICKUP + " boolean not null default true,"
            + " " + SlotVendorAvailibility.MAX_ORDERS_PER_DAY + " int not null default 0,"

            + " FOREIGN KEY(" + SlotVendorAvailibility.SHOP_ID +") REFERENCES " + Shop.TABLE_NAME + "(" + Shop.SHOP_ID + ") ON DELETE CASCADE, "
            + " FOREIGN KEY(" + SlotVendorAvailibility.DELIVERY_SLOT_ID +") REFERENCES " + DeliverySlot.TABLE_NAME + "(" + DeliverySlot.SLOT_ID + ") ON DELETE CASCADE, "
            + " UNIQUE (" + SlotVendorAvailibility.SHOP_ID + ", " + SlotVendorAvailibility.DELIVERY_SLOT_ID + ")"
            + ")";




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
