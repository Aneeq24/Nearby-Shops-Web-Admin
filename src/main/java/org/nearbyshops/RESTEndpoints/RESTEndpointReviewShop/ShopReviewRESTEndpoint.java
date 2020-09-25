package org.nearbyshops.RESTEndpoints.RESTEndpointReviewShop;

import org.nearbyshops.DAOs.DAOReviewShop.ShopReviewDAOPrepared;
import org.nearbyshops.Model.ModelEndpointReview.ShopReviewEndPoint;
import org.nearbyshops.Model.ModelReviewShop.ShopReview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by sumeet on 9/8/16.
 */



@RestController
@RequestMapping("/api/v1/ShopReview")
public class ShopReviewRESTEndpoint {



    @Autowired
    private ShopReviewDAOPrepared shopReviewDAOPrepared;


    @PostMapping
    public ResponseEntity<Object> saveShopReview(@RequestBody ShopReview shopReview)
    {

        int idOfInsertedRow = shopReviewDAOPrepared.saveShopReview(shopReview);

        shopReview.setShopReviewID(idOfInsertedRow);




        if(idOfInsertedRow >=1)
        {

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(shopReview);


        }
        else {

            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .build();
        }
    }




    @PutMapping ("/{ShopReviewID}")
    public ResponseEntity<Object> updateReview(@RequestBody ShopReview shopReview,
                                               @PathVariable("ShopReviewID")int shopReviewID)
    {

        shopReview.setShopReviewID(shopReviewID);

        int rowCount = shopReviewDAOPrepared.updateShopReview(shopReview);


        if(rowCount >= 1)
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




    @DeleteMapping ("/{ShopReviewID}")
    public ResponseEntity<Object> deleteItem(@PathVariable("ShopReviewID")int shopReviewID)
    {

        int rowCount = shopReviewDAOPrepared.deleteShopReview(shopReviewID);

        if(rowCount>=1)
        {

            return ResponseEntity.status(HttpStatus.OK)
                    .build();
        }
        else if(rowCount == 0)
        {

            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .build();
        }

        return null;
    }








    @GetMapping
    public ResponseEntity<Object> getShopReviews(
            @RequestParam(value = "ShopID",required = false)Integer shopID,
            @RequestParam(value = "EndUserID",required = false)Integer endUserID,
            @RequestParam(value = "GetEndUser",required = false)Boolean getEndUser,
            @RequestParam(value = "SortBy",required = false) String sortBy,
            @RequestParam(value = "Limit",defaultValue = "0")int limit,
            @RequestParam(value = "Offset",defaultValue = "0")int offset,
            @RequestParam(value = "metadata_only",defaultValue = "false")boolean metaonly)
    {

//            ShopReviewEndPoint endPoint = shopReviewDAOPrepared.getEndPointMetadata(shopID,endUserID);

        ShopReviewEndPoint endPoint = new ShopReviewEndPoint();

        endPoint.setLimit(limit);
        endPoint.setOffset(offset);

        List<ShopReview> list = null;
        list = shopReviewDAOPrepared.getShopReviews(
                        shopID,endUserID,
                        sortBy,limit,offset
                );


        endPoint.setResults(list);



        //Marker
        return ResponseEntity.status(HttpStatus.OK)
                .body(endPoint);

    }




//
//        @GET
//        @Path("/{ShopReviewID}")
//        @Produces(MediaType.APPLICATION_JSON)
//        public Response getItem(@PathParam("ShopReviewID")int shopReviewID)
//        {
//
//
//
//            ShopReview item = shopReviewDAOPrepared.getShopReview(shopReviewID);
//
//            if(item!= null)
//            {
//
//                return Response.status(Response.Status.OK)
//                        .entity(item)
//                        .build();
//
//            } else
//            {
//
//                return Response.status(Response.Status.NO_CONTENT)
//                        .build();
//
//            }
//        }
//



//
//        @GET
//        @Path("/Stats/{ShopID}")
//        @Produces(MediaType.APPLICATION_JSON)
//        public Response getStats(@PathParam("ShopID")int shopID)
//        {
//
//            List<ShopReviewStatRow> rowList = shopReviewDAOPrepared.getStats(shopID);
//
//
//            if(rowList.size()>0)
//            {
//
//                return Response.status(Response.Status.OK)
//                        .entity(rowList)
//                        .build();
//
//            } else
//            {
//
//                return Response.status(Response.Status.NO_CONTENT)
//                        .build();
//
//            }
//        }


}
