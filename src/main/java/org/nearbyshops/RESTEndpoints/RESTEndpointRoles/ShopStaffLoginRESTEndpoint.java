package org.nearbyshops.RESTEndpoints.RESTEndpointRoles;


import org.nearbyshops.Constants;
import org.nearbyshops.DAOs.DAORoles.DAOShopStaff;
import org.nearbyshops.DAOs.DAORoles.DAOUserUtility;
import org.nearbyshops.Model.ModelRoles.ShopStaffPermissions;
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
 * Created by sumeet on 30/8/17.
 */



@RestController
@RequestMapping("/api/v1/User/ShopStaffLogin")
public class ShopStaffLoginRESTEndpoint {



    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserAuthentication userAuthentication;



    @Autowired
    DAOShopStaff daoShopStaff;

    @Autowired
    DAOUserUtility daoUserUtility;




    @PutMapping ("/UpdateStaffLocation")
    @RolesAllowed({Constants.ROLE_SHOP_STAFF})
    public ResponseEntity<Object> updateStaffLocation(ShopStaffPermissions permissions)
    {

        User userAuthenticated = userAuthentication.isUserAllowed(request,
                Arrays.asList(Constants.ROLE_SHOP_STAFF));


        if(userAuthenticated==null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }



        permissions.setStaffUserID((userAuthenticated.getUserID()));
        int rowCount = daoShopStaff.updateShopStaffLocation(permissions);



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




    @PutMapping ("/UpgradeUser/{emailorphone}/{Role}/{SecretCode}")
    @RolesAllowed({Constants.ROLE_SHOP_ADMIN})
    public ResponseEntity<Object> upgradeUserToShopStaff(@PathVariable("emailorphone")String emailorphone,
                                           @PathVariable("Role")int role,
                                           @PathVariable("SecretCode")int secretCode)
    {


        User userAuthenticated = userAuthentication.isUserAllowed(request,
                Arrays.asList(Constants.ROLE_SHOP_ADMIN));


        if(userAuthenticated==null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }


        int shopAdminID = userAuthenticated.getUserID();
        int shopID = daoUserUtility.getShopIDForShopAdmin(shopAdminID);


        int userID = daoUserUtility.getUserID(emailorphone);
        int rowCount = daoShopStaff.upgradeUserToStaff(userID,shopID,secretCode,role);


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




    @PutMapping("/UpdateStaffPermissions")
    @RolesAllowed({Constants.ROLE_SHOP_ADMIN})
    public ResponseEntity<Object> updateStaffPermissions(ShopStaffPermissions permissions)
    {


        User userAuthenticated = userAuthentication.isUserAllowed(request,
                Arrays.asList(Constants.ROLE_SHOP_ADMIN));


        if(userAuthenticated==null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }



        int shopAdminID = userAuthenticated.getUserID();
        int shopID = daoUserUtility.getShopIDForShopAdmin(shopAdminID);
        permissions.setShopID(shopID);


        int rowCount = daoShopStaff.updateShopStaffPermissions(permissions);


        if(rowCount >= 1)
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





    @GetMapping ("/GetStaffPermissions/{UserID}")
    @RolesAllowed({Constants.ROLE_SHOP_ADMIN})
    public ResponseEntity<Object> getPermissionDetails(@PathVariable("UserID")int userID)
    {

        ShopStaffPermissions permissions = daoShopStaff.getShopStaffPermissions(userID);


        if(permissions!=null)
        {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(permissions);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .build();
        }

    }




}
