package org.nearbyshops.RESTEndpoints.RESTEndpointRoles;


import org.nearbyshops.Constants;
import org.nearbyshops.DAOs.DAORoles.DAOStaff;
import org.nearbyshops.DAOs.DAORoles.DAOUserUtility;
import org.nearbyshops.Model.ModelRoles.StaffPermissions;
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
@RequestMapping("/api/v1/User/StaffLogin")
public class StaffLoginRESTEndpoint {


    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserAuthentication userAuthentication;


    @Autowired
    DAOStaff daoStaff;

    @Autowired
    DAOUserUtility daoUserUtility;



    @PutMapping ("/UpdateStaffLocation")
    @RolesAllowed({Constants.ROLE_STAFF})
    public ResponseEntity<Object> updateStaffLocation(@RequestBody StaffPermissions permissions)
    {


        User userAuthenticated = userAuthentication.isUserAllowed(request,
                Arrays.asList(Constants.ROLE_STAFF));


        if(userAuthenticated==null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }


        permissions.setStaffUserID(userAuthenticated.getUserID());
        int rowCount = daoStaff.updateStaffLocation(permissions);


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




    @PutMapping("/UpgradeUser/{emailorphone}/{Role}/{SecretCode}")
    @RolesAllowed({Constants.ROLE_ADMIN})
    public ResponseEntity<Object> upgradeUserToShopStaff(@PathVariable("emailorphone")String emailorphone,
                                           @PathVariable("Role")int role,
                                           @PathVariable("SecretCode")int secretCode)
    {


        User userAuthenticated = userAuthentication.isUserAllowed(request,
                Arrays.asList(Constants.ROLE_ADMIN));


        if(userAuthenticated==null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }

        int userID = daoUserUtility.getUserID(emailorphone);


//        Constants.ROLE_STAFF_CODE
        int rowCount = daoStaff.upgradeUserToStaff(userID,secretCode,role);


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




    @PutMapping ("/UpdateStaffPermissions")
    @RolesAllowed({Constants.ROLE_ADMIN})
    public ResponseEntity<Object> updateStaffPermissions(@RequestBody StaffPermissions permissions)
    {

        User userAuthenticated = userAuthentication.isUserAllowed(request,
                Arrays.asList(Constants.ROLE_ADMIN));


        if(userAuthenticated==null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }



        int rowCount = daoStaff.updateStaffPermissions(permissions);


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





    @GetMapping("/GetStaffPermissions/{UserID}")
    @RolesAllowed({Constants.ROLE_ADMIN})
    public ResponseEntity<Object> getPermissionDetails(@PathVariable("UserID")int userID)
    {

        StaffPermissions permissions = daoStaff.getStaffPermissions(userID);


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
