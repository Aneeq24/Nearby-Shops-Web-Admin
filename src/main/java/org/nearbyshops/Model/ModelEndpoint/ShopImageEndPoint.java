package org.nearbyshops.Model.ModelEndpoint;


import org.nearbyshops.Model.ModelReviewShop.ShopReview;
import org.nearbyshops.Model.Shop;
import org.nearbyshops.Model.ModelImages.ShopImage;

import java.util.List;

/**
 * Created by sumeet on 30/6/16.
 */
public class ShopImageEndPoint {

    private int itemCount;
    private int offset;
    private int limit;
    private int max_limit;
    private List<ShopImage> results;


    // check whether this item is a favourite for the given user
    private boolean isFavourite;
    private Shop shopDetails;
    private ShopReview shopReview;




    public ShopReview getShopReview() {
        return shopReview;
    }

    public void setShopReview(ShopReview shopReview) {
        this.shopReview = shopReview;
    }

    public Shop getShopDetails() {
        return shopDetails;
    }

    public void setShopDetails(Shop shopDetails) {
        this.shopDetails = shopDetails;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }


    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getMax_limit() {
        return max_limit;
    }

    public void setMax_limit(int max_limit) {
        this.max_limit = max_limit;
    }


    public List<ShopImage> getResults() {
        return results;
    }

    public void setResults(List<ShopImage> results) {
        this.results = results;
    }
}
