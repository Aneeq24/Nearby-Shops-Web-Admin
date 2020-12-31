package org.nearbyshops.Model.ModelEndpoint;

import org.nearbyshops.Model.ModelRoles.User;
import org.nearbyshops.Model.Order;
import org.nearbyshops.Model.Shop;

import java.util.List;

/**
 * Created by sumeet on 30/6/16.
 */
public class OrderEndPoint {

    private int itemCount;
    private int offset;
    private int limit;
    private int max_limit;
    private List<Order> results;

//    private List<DeliverySlot> deliverySlotList;
    private List<Shop> shopList;
    private List<User> deliveryPersonList;

    private int deliverySlotCount;
    private int shopCount;
    private int deliveryPersonCount;






    public int getItemCount() {
        return itemCount;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    public int getMax_limit() {
        return max_limit;
    }

    public List<Order> getResults() {
        return results;
    }

    public void setResults(List<Order> results) {
        this.results = results;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setMax_limit(int max_limit) {
        this.max_limit = max_limit;
    }


    public List<Shop> getShopList() {
        return shopList;
    }

    public void setShopList(List<Shop> shopList) {
        this.shopList = shopList;
    }

    public List<User> getDeliveryPersonList() {
        return deliveryPersonList;
    }

    public void setDeliveryPersonList(List<User> deliveryPersonList) {
        this.deliveryPersonList = deliveryPersonList;
    }

    public int getDeliverySlotCount() {
        return deliverySlotCount;
    }

    public void setDeliverySlotCount(int deliverySlotCount) {
        this.deliverySlotCount = deliverySlotCount;
    }

    public int getShopCount() {
        return shopCount;
    }

    public void setShopCount(int shopCount) {
        this.shopCount = shopCount;
    }

    public int getDeliveryPersonCount() {
        return deliveryPersonCount;
    }

    public void setDeliveryPersonCount(int deliveryPersonCount) {
        this.deliveryPersonCount = deliveryPersonCount;
    }

}
