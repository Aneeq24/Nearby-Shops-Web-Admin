package org.nearbyshops.RESTEndpoints.RESTEndpointReviewItem;



import org.nearbyshops.AppProperties;
import org.nearbyshops.DAOs.DAOReviewItem.ItemReviewDAOPrepared;
import org.nearbyshops.DAOs.DAORoles.DAOUserNew;
import org.nearbyshops.Model.ModelEndpointReview.ItemReviewEndPoint;
import org.nearbyshops.Model.ModelReviewItem.ItemReview;
import org.nearbyshops.Model.ModelReviewItem.ItemReviewStatRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * Created by sumeet on 9/8/16.
 */

@RestController
@RequestMapping("/api/v1/ItemReview")
public class ItemReviewRESTEndpoint {


    @Autowired
    private ItemReviewDAOPrepared itemReviewDAOPrepared;

    @Autowired
    private DAOUserNew endUserDAOPrepared;


    @Autowired
    AppProperties appProperties;



    @PostMapping
    public ResponseEntity<Object> saveItemReview(@RequestBody ItemReview itemReview)
    {


        int idOfInsertedRow = itemReviewDAOPrepared.saveItemReview(itemReview);

        itemReview.setItemReviewID(idOfInsertedRow);

        if(idOfInsertedRow >=1)
        {

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(itemReview);

        }
        else
        {

            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .build();
        }
    }


    @PutMapping ("/{ItemReviewID}")
    public ResponseEntity<Object> updateItem(@RequestBody ItemReview itemReview,
                                     @PathVariable("ItemReviewID")int itemReviewID)
    {

        itemReview.setItemReviewID(itemReviewID);

//            int rowCount = Globals.bookDAO.updateBook(book);

        int rowCount = itemReviewDAOPrepared.updateItemReview(itemReview);


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



    @PutMapping
    public ResponseEntity<Object> updateReviewsBulk(@RequestBody List<ItemReview> itemReviewsList)
    {
        int rowCountSum = 0;

        for(ItemReview item : itemReviewsList)
        {
            rowCountSum = rowCountSum + itemReviewDAOPrepared.updateItemReview(item);
        }

        if(rowCountSum ==  itemReviewsList.size())
        {

            return ResponseEntity.status(HttpStatus.OK)
                    .build();
        }
        else if( rowCountSum < itemReviewsList.size() && rowCountSum > 0)
        {

            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .build();
        }
        else {

            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .build();
        }
    }




    @DeleteMapping ("/{ItemReviewID}")
    public ResponseEntity<Object> deleteItem(@PathVariable("ItemReviewID")int itemReviewID)
    {

        int rowCount = itemReviewDAOPrepared.deleteItemReview(itemReviewID);

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
    public ResponseEntity<Object> getItemReviews(
            @RequestParam(value = "ItemID",required = false)Integer itemID,
            @RequestParam(value = "EndUserID",required = false)Integer endUserID,
            @RequestParam(value = "GetEndUser",required = false)Boolean getEndUser,
            @RequestParam(value = "SortBy",required = false) String sortBy,
            @RequestParam(value = "Limit",defaultValue = "0")int limit,
            @RequestParam(value = "Offset",defaultValue = "0")int offset,
            @RequestParam(value = "metadata_only",defaultValue = "false")boolean metaonly)
    {


        if (limit >= appProperties.getMax_limit()) {

            limit = appProperties.getMax_limit();
        }



        ItemReviewEndPoint endPoint = itemReviewDAOPrepared.getEndPointMetadata(itemID,endUserID);

        endPoint.setLimit(limit);
        endPoint.setMax_limit(appProperties.getMax_limit());
        endPoint.setOffset(offset);

        List<ItemReview> list = null;


        if(!metaonly) {

            list =
                    itemReviewDAOPrepared.getItemReviews(
                            itemID,endUserID,
                            sortBy,limit,offset
                    );


            if(getEndUser!=null && getEndUser)
            {
                for(ItemReview itemReview: list)
                {
                    itemReview.setRt_end_user_profile(endUserDAOPrepared.getProfile(itemReview.getEndUserID()));
                }
            }

            endPoint.setResults(list);
        }


        //Marker

        return ResponseEntity.status(HttpStatus.OK)
                .body(endPoint);

    }





    @GetMapping ("/{ItemReviewID}")
    public ResponseEntity<Object> getItem(@PathVariable("ItemReviewID")int itemReviewID)
    {
        //marker

        ItemReview item = itemReviewDAOPrepared.getItemReview(itemReviewID);

        if(item!= null)
        {

            return ResponseEntity.status(HttpStatus.OK)
                    .body(item);

        }
        else
        {

            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(item);

        }
    }



    @GetMapping("/Stats/{ItemID}")
    public ResponseEntity<Object> getStats(@PathVariable("ItemID")int itemID)
    {

        List<ItemReviewStatRow> rowList = itemReviewDAOPrepared.getStats(itemID);


        if(rowList.size()>0)
        {

            return ResponseEntity.status(HttpStatus.OK)
                    .body(rowList);

        }
        else
        {

            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .build();

        }
    }


}
