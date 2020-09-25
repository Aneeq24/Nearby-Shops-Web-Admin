package org.nearbyshops.RESTEndpoints.RESTEndpointRoles;


import org.nearbyshops.Constants;
import org.nearbyshops.DAOs.DAORoles.DAODeliveryGuy;
import org.nearbyshops.Model.ModelRoles.DeliveryGuyData;
import org.nearbyshops.Model.ModelRoles.User;
import org.nearbyshops.Utility.UserAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * Created by sumeet on 30/8/17.
 */



@RestController
@RequestMapping("/api/v1/User/DeliveryGuy")
public class DeliveryGuyLoginRESTEndpoint {


    @Autowired
    DAODeliveryGuy daoDeliveryGuy;


    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserAuthentication userAuthentication;



    @PutMapping ("/UpdateLocation")
    @RolesAllowed({Constants.ROLE_DELIVERY_GUY, Constants.ROLE_DELIVERY_GUY_SELF})
    public ResponseEntity<Object> updateLocation(@RequestBody DeliveryGuyData deliveryGuyData)
    {


        User user = userAuthentication.isUserAllowed(request,
                Arrays.asList(Constants.ROLE_DELIVERY_GUY,Constants.ROLE_DELIVERY_GUY_SELF));

        if(user==null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }


        deliveryGuyData.setStaffUserID(user.getUserID());


        int rowCount = daoDeliveryGuy.updateDeliveryGuyLocation(deliveryGuyData);


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



}
