package org.nearbyshops.RESTEndpoints.RESTEndpointBilling;


import org.nearbyshops.AppProperties;
import org.nearbyshops.Constants;
import org.nearbyshops.DAOs.DAOBilling.DAOTransaction;
import org.nearbyshops.Model.ModelBilling.TransactionEndpoint;
import org.nearbyshops.Model.ModelRoles.User;
import org.nearbyshops.Utility.UserAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;


/**
 * Created by sumeet on 13/8/17.
 */






@RestController
@RequestMapping("/api/v1/Transaction")
public class TransactionRESTEndpoint {


    @Autowired
    HttpServletRequest request;

    @Autowired
    UserAuthentication userAuthentication;


    @Autowired
    AppProperties appProperties;


    @Autowired
    DAOTransaction daoTransaction;




    @GetMapping
    @RolesAllowed({Constants.ROLE_END_USER, Constants.ROLE_SHOP_ADMIN})
    public ResponseEntity<Object> getSelfTransactions(
            @RequestParam(value = "IsCredit",required = false) Boolean isCredit,
            @RequestParam(value = "TransactionType",required = false) Integer transactionType,
            @RequestParam(value = "SortBy",required = false) String sortBy,
            @RequestParam(value = "Limit",defaultValue = "0")int limit,
            @RequestParam(value = "Offset",defaultValue = "0")int offset,
            @RequestParam(value = "GetRowCount",defaultValue = "false")boolean getRowCount,
            @RequestParam(value = "MetadataOnly",defaultValue = "false")boolean getOnlyMetaData)
    {


        User userAuthenticated = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_END_USER));

        if(userAuthenticated==null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }


        if(limit >= appProperties.getMax_limit())
        {
            limit = appProperties.getMax_limit();
        }


        TransactionEndpoint endpoint = daoTransaction.getTransactions(
                userAuthenticated.getUserID(),isCredit,transactionType,
                sortBy,limit,offset,getRowCount,getOnlyMetaData
        );



//
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }



        //Marker
        return ResponseEntity.status(HttpStatus.OK)
                .body(endpoint);
    }






    @GetMapping ("/GetUserTransactions/{UserID}")
    @RolesAllowed({Constants.ROLE_STAFF})
    public ResponseEntity<Object> getUserTransactions(
            @PathVariable("UserID") int userID,
            @RequestParam(value = "IsCredit",required = false) Boolean isCredit,
            @RequestParam(value = "TransactionType",required = false) Integer transactionType,
            @RequestParam(value = "SortBy",required = false) String sortBy,
            @RequestParam(value = "Limit",required = false)Integer limit,
            @RequestParam(value = "Offset",required = false)Integer offset,
            @RequestParam(value = "GetRowCount",defaultValue = "false")boolean getRowCount,
            @RequestParam(value = "MetadataOnly",defaultValue = "false")boolean getOnlyMetaData
    )
    {


        if(limit!=null)
        {
            if(limit >= appProperties.getMax_limit())
            {
                limit = appProperties.getMax_limit();
            }

            if(offset==null)
            {
                offset = 0;
            }
        }





        TransactionEndpoint endpoint =  daoTransaction.getTransactions(
                userID,
                isCredit,transactionType,
                sortBy,limit,offset,getRowCount,getOnlyMetaData
        );



//
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }



        //Marker
        return ResponseEntity.status(HttpStatus.OK)
                .body(endpoint);
    }




}
