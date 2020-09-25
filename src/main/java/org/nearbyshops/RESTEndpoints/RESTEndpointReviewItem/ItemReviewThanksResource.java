package org.nearbyshops.RESTEndpoints.RESTEndpointReviewItem;

import org.nearbyshops.AppProperties;
import org.nearbyshops.DAOs.DAOReviewItem.ItemReviewThanksDAOPrepared;
import org.nearbyshops.Model.ModelEndpointReview.ItemReviewThanksEndpoint;
import org.nearbyshops.Model.ModelReviewItem.ItemReviewThanks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by sumeet on 9/8/16.
 */


@RestController
@RequestMapping("/api/v1/ItemReviewThanks")
public class ItemReviewThanksResource {


    @Autowired
    private ItemReviewThanksDAOPrepared thanksDAOPrepared;



    @Autowired
    AppProperties appProperties;



    @PostMapping
    public ResponseEntity<Object> saveItemReviewThanks(@RequestBody ItemReviewThanks itemReviewThanks)
    {
        int idOfInsertedRow = thanksDAOPrepared.saveItemReviewThanks(itemReviewThanks);

//            shopReviewThanks.setItemID(idOfInsertedRow);

        if(idOfInsertedRow >=1)
        {

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(itemReviewThanks);

        }
        else
        {

            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .build();
        }
    }



    @DeleteMapping
    public ResponseEntity<Object> deleteItem(@RequestParam(value = "ItemReviewID",required = false)Integer itemReviewID,
                                             @RequestParam(value = "EndUserID",required = false)Integer endUserID)
    {

        int rowCount = thanksDAOPrepared.deleteItemReviewThanks(itemReviewID,endUserID);


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
    public ResponseEntity<Object> getItemReviewThanks(
            @RequestParam(value = "ItemReviewID",required = false)Integer itemReviewID,
            @RequestParam(value = "EndUserID",required = false)Integer endUserID,
            @RequestParam(value = "ItemID",required = false)Integer itemID,
            @RequestParam(value = "SortBy",required = false) String sortBy,
            @RequestParam(value = "Limit",defaultValue = "0")int limit,
            @RequestParam(value = "Offset",defaultValue = "0")int offset,
            @RequestParam(value = "metadata_only",defaultValue = "false")boolean metaonly)
    {


        if (limit >= appProperties.getMax_limit()) {

            limit = appProperties.getMax_limit();
        }



        ItemReviewThanksEndpoint endPoint = thanksDAOPrepared.getEndPointMetadata(itemReviewID,endUserID);

        endPoint.setLimit(limit);
        endPoint.setMax_limit(appProperties.getMax_limit());
        endPoint.setOffset(offset);

        List<ItemReviewThanks> list = null;


        if(!metaonly) {

            list =
                    thanksDAOPrepared.getItemReviewThanks(
                            itemReviewID,itemID,endUserID,
                            sortBy,limit,offset
                    );

            endPoint.setResults(list);
        }


        //Marker
        return ResponseEntity.status(HttpStatus.OK)
                .body(endPoint);

    }

}
