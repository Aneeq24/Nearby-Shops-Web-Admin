package org.nearbyshops.Model.ModelBilling;


import org.nearbyshops.Model.Order;


public class RazorPayOrder {


    // Table Name for User
    public static final String TABLE_NAME = "RAZOR_PAY_ORDER_INFO";


    // Column names
    public static final String RZP_INFO_ID = "RZP_INFO_ID";
    public static final String LOCAL_ORDER_ID = "LOCAL_ORDER_ID";
    public static final String RZP_PAYMENT_ID = "RZP_PAYMENT_ID";
    public static final String RZP_ORDER_ID = "RZP_ORDER_ID";
    public static final String PAID_AMOUNT = "PAID_AMOUNT";
    public static final String SHOP_OWNER_PAYOUT = "SHOP_OWNER_PAYOUT";



    // Create Table statement
    public static final String createTable =

            "CREATE TABLE IF NOT EXISTS "
                    + RazorPayOrder.TABLE_NAME + "("
                    + " " + RazorPayOrder.RZP_INFO_ID + " SERIAL PRIMARY KEY,"
                    + " " + RazorPayOrder.LOCAL_ORDER_ID + " int,"
                    + " " + RazorPayOrder.RZP_PAYMENT_ID + " text,"
                    + " " + RazorPayOrder.RZP_ORDER_ID + " text,"
                    + " " + RazorPayOrder.PAID_AMOUNT + " float not null default 0 ,"
                    + " " + RazorPayOrder.SHOP_OWNER_PAYOUT + " float not null default 0 ,"

                    + " FOREIGN KEY(" + RazorPayOrder.LOCAL_ORDER_ID +") REFERENCES " + Order.TABLE_NAME + "(" + Order.ORDER_ID + ") ON DELETE CASCADE "
                    + ")";


    public RazorPayOrder(String rzpOrderID) {
        this.rzpOrderID = rzpOrderID;
    }

    public RazorPayOrder() {
    }




    int rzpInfoID;
    int localOrderID;
    String rzpPaymentID;
    String rzpOrderID;
    double paidAmount;
    double shopOwnerPayout;


    public double getShopOwnerPayout() {
        return shopOwnerPayout;
    }

    public void setShopOwnerPayout(double shopOwnerPayout) {
        this.shopOwnerPayout = shopOwnerPayout;
    }

    public int getRzpInfoID() {
        return rzpInfoID;
    }

    public void setRzpInfoID(int rzpInfoID) {
        this.rzpInfoID = rzpInfoID;
    }

    public int getLocalOrderID() {
        return localOrderID;
    }

    public void setLocalOrderID(int localOrderID) {
        this.localOrderID = localOrderID;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getRzpPaymentID() {
        return rzpPaymentID;
    }

    public void setRzpPaymentID(String rzpPaymentID) {
        this.rzpPaymentID = rzpPaymentID;
    }

    public String getRzpOrderID() {
        return rzpOrderID;
    }

    public void setRzpOrderID(String rzpOrderID) {
        this.rzpOrderID = rzpOrderID;
    }
}
