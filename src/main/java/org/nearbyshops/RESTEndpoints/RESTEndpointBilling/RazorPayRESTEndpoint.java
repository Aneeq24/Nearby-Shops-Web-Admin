package org.nearbyshops.RESTEndpoints.RESTEndpointBilling;


import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.nearbyshops.AppProperties;
import org.nearbyshops.Constants;
import org.nearbyshops.DAOs.DAOSettings.MarketSettingsDAO;
import org.nearbyshops.Model.ModelBilling.RazorPayOrder;
import org.nearbyshops.Model.ModelRoles.User;
import org.nearbyshops.Model.ModelUtility.PaymentConfig;
import org.nearbyshops.Utility.UserAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;



/**
 * Created by sumeet on 13/8/17.
 */




@RestController
@RequestMapping("/api/RazorPay")
public class RazorPayRESTEndpoint {


    @Autowired
    HttpServletRequest request;

    @Autowired
    UserAuthentication userAuthentication;


    @Autowired
    AppProperties appProperties;

    @Autowired
    MarketSettingsDAO marketSettingsDAO;





    @GetMapping ("/CreateOrder/{Amount}")
    public ResponseEntity<Object> createOrder(@PathVariable("Amount") double amount)
    {

        String orderID = null;

        // Initialize client
        try {


            RazorpayClient razorpayClient = new RazorpayClient(appProperties.getRazorpay_key_id(),appProperties.getRazorpay_key_secret());

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount*100); // amount in the smallest currency unit
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "order_rcptid_11");
            orderRequest.put("payment_capture", false);

            Order order = razorpayClient.Orders.create(orderRequest);

//            System.out.println(order.toString());

            orderID = order.get("id");

//            System.out.println("Order ID : " + orderID);

//            razorpayClient.Payments.capture()


        } catch (RazorpayException e) {
            e.printStackTrace();
        }



        if(orderID==null)
        {

            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .build();

        }
        else
        {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new RazorPayOrder(orderID));

        }

    }




    @GetMapping ("/GetPaymentConfig")
    @RolesAllowed({Constants.ROLE_END_USER})
    public ResponseEntity<Object> getPaymentConfig()
    {

        User user = userAuthentication.isUserAllowed(request,
                Arrays.asList(Constants.ROLE_END_USER));


        if(user==null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }




        PaymentConfig paymentConfig = new PaymentConfig();

        paymentConfig.setCashOnDeliveryEnabled(marketSettingsDAO.getSettingsInstance().isCodEnabled());
        paymentConfig.setPayOnlineOnDeliveryEnabled(marketSettingsDAO.getSettingsInstance().isPodEnabled());
        paymentConfig.setRazorPayEnabled(marketSettingsDAO.getSettingsInstance().isRazorPayEnabled());
        paymentConfig.setRazorPayKey(appProperties.getRazorpay_key_id());


        return ResponseEntity.status(HttpStatus.OK)
                .body(paymentConfig);
    }


}
