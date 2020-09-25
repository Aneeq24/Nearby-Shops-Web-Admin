package org.nearbyshops.RESTEndpoints.RESTEndpointReviewShop;

import org.nearbyshops.DAOs.DAOReviewShop.FavoriteShopDAOPrepared;
import org.nearbyshops.Model.ModelEndpointReview.FavouriteShopEndpoint;
import org.nearbyshops.Model.ModelReviewShop.FavouriteShop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * Created by sumeet on 9/8/16.
 */

@RestController
@RequestMapping("/api/v1/FavouriteShop")
public class FavouriteShopResource {


    @Autowired
    private FavoriteShopDAOPrepared favoriteShopDAOPrepared;



    @PostMapping
    public ResponseEntity<Object> saveFavouriteBook(@RequestBody FavouriteShop shop)
    {
        int idOfInsertedRow = favoriteShopDAOPrepared.saveFavourite(shop);

        shop.setShopID(idOfInsertedRow);



        if(idOfInsertedRow >0)
        {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(shop);
        }

        return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                .build();

    }



    @DeleteMapping
    public ResponseEntity<Object> deleteItem(@RequestParam("ShopID")Integer shopID,
                               @RequestParam("EndUserID")Integer endUserID)
    {

        int rowCount = favoriteShopDAOPrepared.deleteFavourite(shopID,endUserID);




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
    public ResponseEntity<Object> getFavouriteShops(
            @RequestParam(value = "EndUserID",required = false)Integer endUserID,
            @RequestParam(value = "SortBy",required = false) String sortBy,
            @RequestParam(value = "Limit",required = false)Integer limit,
            @RequestParam(value = "Offset",required = false)Integer offset
    )
    {


        FavouriteShopEndpoint endpoint = new FavouriteShopEndpoint();


        List<FavouriteShop> list = favoriteShopDAOPrepared.getFavouriteShops(
                                            endUserID,
                                            sortBy,limit,offset
                                    );

        endpoint.setResults(list);


        //Marker
        return ResponseEntity.status(HttpStatus.OK)
                .body(endpoint);

    }






    @GetMapping ("/CheckFavourite")
    public ResponseEntity<Object> checkFavourite(
            @RequestParam(value = "ShopID",required = false)Integer shopID,
            @RequestParam(value = "EndUserID",required = false)Integer endUserID)

    {


        boolean isFavourite = favoriteShopDAOPrepared.checkFavourite(
                shopID,endUserID
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
