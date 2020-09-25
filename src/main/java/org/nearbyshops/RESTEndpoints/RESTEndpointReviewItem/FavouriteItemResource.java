package org.nearbyshops.RESTEndpoints.RESTEndpointReviewItem;

import org.nearbyshops.AppProperties;
import org.nearbyshops.DAOs.DAOReviewItem.FavoriteItemDAOPrepared;
import org.nearbyshops.Model.ModelEndpointReview.FavouriteItemEndpoint;
import org.nearbyshops.Model.ModelReviewItem.FavouriteItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * Created by sumeet on 9/8/16.
 */






@RestController
@RequestMapping("/api/v1/FavouriteItem")
public class FavouriteItemResource {


    @Autowired
    AppProperties appProperties;

    @Autowired
    private FavoriteItemDAOPrepared favoriteItemDAOPrepared;





    @PostMapping
    public ResponseEntity<Object> saveFavouriteItem(@RequestBody FavouriteItem item)
    {
        int idOfInsertedRow = favoriteItemDAOPrepared.saveFavouriteItem(item);

        item.setItemID(idOfInsertedRow);

        if(idOfInsertedRow >=1)
        {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(item);

        }else {

            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .build();
        }

    }



    @DeleteMapping
    public ResponseEntity<Object> deleteItem(@RequestParam("ItemID")Integer itemID,
                               @RequestParam("EndUserID")Integer endUserID)
    {

        int rowCount = favoriteItemDAOPrepared.deleteFavouriteItem(itemID,endUserID);



        if(rowCount>=1)
        {
            return ResponseEntity.status(HttpStatus.OK)
                    .build();
        }
        else
        {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .build();
        }
    }



    @GetMapping
    public ResponseEntity<Object> getFavouriteItems(
            @RequestParam(value = "ItemID",required = false)Integer itemID,
            @RequestParam(value = "EndUserID",required = false)Integer endUserID,
            @RequestParam(value = "SortBy",required = false) String sortBy,
            @RequestParam(value = "Limit",defaultValue = "0")int limit,
            @RequestParam(value = "Offset",defaultValue = "0")int offset)

    {

        if (limit >= appProperties.getMax_limit()) {

            limit = appProperties.getMax_limit();
        }



        FavouriteItemEndpoint endPoint = new FavouriteItemEndpoint();



        endPoint.setLimit(limit);
        endPoint.setMax_limit(appProperties.getMax_limit());
        endPoint.setOffset(offset);


        List<FavouriteItem> list = null;


        list =
                favoriteItemDAOPrepared.getFavouriteItems(
                        itemID,endUserID,
                        sortBy,
                        limit,offset
                );


        endPoint.setResults(list);



        //Marker
        return ResponseEntity.status(HttpStatus.OK)
                .body(endPoint);

    }






    @GetMapping ("/CheckFavourite")
    public ResponseEntity<Object> checkFavourite(
            @RequestParam("ItemID")Integer itemID,
            @RequestParam("EndUserID")Integer endUserID)

    {

        boolean isFavourite = favoriteItemDAOPrepared.checkFavourite(
                itemID,endUserID
        );




        if(isFavourite)
        {
            //Marker
            return ResponseEntity.status(HttpStatus.OK)
                    .build();
        }
        else
        {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .build();

        }

    }



}
