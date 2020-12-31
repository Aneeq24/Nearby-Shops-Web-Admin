package org.nearbyshops.Model.ModelSalesReport;

import org.nearbyshops.Model.Shop;

public class OrderSalesStats {


    double minOrderPrice;
    double maxOrderPrice;
    double avgOrderPrice;
//    int shopCount;
    int orderCount;
    double totalOrderSales;
    double totalVendorPayout;
    String shopName;
    int shopID;

//    Shop shop;


    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public double getMinOrderPrice() {
        return minOrderPrice;
    }

    public void setMinOrderPrice(double minOrderPrice) {
        this.minOrderPrice = minOrderPrice;
    }

    public double getMaxOrderPrice() {
        return maxOrderPrice;
    }

    public void setMaxOrderPrice(double maxOrderPrice) {
        this.maxOrderPrice = maxOrderPrice;
    }

    public double getAvgOrderPrice() {
        return avgOrderPrice;
    }

    public void setAvgOrderPrice(double avgOrderPrice) {
        this.avgOrderPrice = avgOrderPrice;
    }

    public double getTotalOrderSales() {
        return totalOrderSales;
    }

    public void setTotalOrderSales(double totalOrderSales) {
        this.totalOrderSales = totalOrderSales;
    }

    public double getTotalVendorPayout() {
        return totalVendorPayout;
    }

    public void setTotalVendorPayout(double totalVendorPayout) {
        this.totalVendorPayout = totalVendorPayout;
    }


}
