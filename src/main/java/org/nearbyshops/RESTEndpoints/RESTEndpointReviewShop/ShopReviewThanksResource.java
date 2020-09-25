package org.nearbyshops.RESTEndpoints.RESTEndpointReviewShop;

import org.nearbyshops.AppProperties;
import org.nearbyshops.DAOs.DAOReviewShop.ShopReviewThanksDAOPrepared;
import org.nearbyshops.Model.ModelEndpointReview.ShopReviewThanksEndpoint;
import org.nearbyshops.Model.ModelReviewShop.ShopReviewThanks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * Created by sumeet on 9/8/16.
 */

@RestController
@RequestMapping("/api/v1/ShopReviewThanks")
public class ShopReviewThanksResource {


    @Autowired
    private ShopReviewThanksDAOPrepared thanksDAOPrepared;



    @Autowired
    AppProperties appProperties;



    @PostMapping
    public ResponseEntity<Object> saveShopReviewThanks(@RequestBody ShopReviewThanks shopReviewThanks)
    {
        int idOfInsertedRow = thanksDAOPrepared.saveShopReviewThanks(shopReviewThanks);

//            shopReviewThanks.setItemID(idOfInsertedRow);

        if(idOfInsertedRow >=1)
        {

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(shopReviewThanks);

        }
        else
        {

            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .build();
        }
    }



    @DeleteMapping
    public ResponseEntity<Object> deleteItem(@RequestParam("ShopReviewID")Integer shopReviewID,
                               @RequestParam("EndUserID")Integer endUserID)
    {

        int rowCount = thanksDAOPrepared.deleteShopReviewThanks(shopReviewID,endUserID);


        if(rowCount>=1)
        {

            return ResponseEntity.status(HttpStatus.OK)
                    .build();
        }

        if(rowCount == 0)
        {

            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .build();
        }

        return null;
    }






    @GetMapping
    public ResponseEntity<Object> getShopReviewThanks(
            @RequestParam(value = "ShopReviewID",required = false)Integer shopReviewID,
            @RequestParam(value = "EndUserID",required = false)Integer endUserID,
            @RequestParam(value = "ShopID",required = false)Integer shopID,
            @RequestParam(value = "SortBy",required = false) String sortBy,
            @RequestParam(value = "Limit",defaultValue = "0")int limit,
            @RequestParam(value = "Offset",defaultValue = "0")int offset,
            @RequestParam(value = "metadata_only",defaultValue = "0")boolean metaonly)
    {

        if (limit >= appProperties.getMax_limit()) {

            limit = appProperties.getMax_limit();
        }



        ShopReviewThanksEndpoint endPoint = thanksDAOPrepared.getEndPointMetadata(shopReviewID,endUserID);

        endPoint.setLimit(limit);
        endPoint.setMax_limit(appProperties.getMax_limit());
        endPoint.setOffset(offset);

        List<ShopReviewThanks> list = null;



        if(!metaonly) {

            list =
                    thanksDAOPrepared.getShopReviewThanks(
                            shopReviewID,shopID,endUserID,
                            sortBy,limit,offset
                    );

            endPoint.setResults(list);
        }


        //Marker
        return ResponseEntity.status(HttpStatus.OK)
                .body(endPoint);

    }

}
