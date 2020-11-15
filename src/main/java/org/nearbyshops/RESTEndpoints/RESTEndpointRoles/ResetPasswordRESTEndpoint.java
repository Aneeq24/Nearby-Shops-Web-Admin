package org.nearbyshops.RESTEndpoints.RESTEndpointRoles;



import org.nearbyshops.AppProperties;
import org.nearbyshops.DAOs.DAORoles.DAOResetPassword;
import org.nearbyshops.DAOs.DAOSettings.MarketSettingsDAO;
import org.nearbyshops.DAOs.DAOSettings.ServiceConfigurationDAO;
import org.nearbyshops.Model.ModelRoles.User;
import org.nearbyshops.Model.ModelSettings.Market;
import org.nearbyshops.Model.ModelSettings.MarketSettings;
import org.nearbyshops.Utility.SendEmail;
import org.nearbyshops.Utility.SendSMS;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

import static org.nearbyshops.Utility.UtilityMethods.generateOTP;


/**
 * Created by sumeet on 14/8/17.
 */



@RestController
@RequestMapping("/api/v1/User/ResetPassword")
public class ResetPasswordRESTEndpoint {



    @Autowired
    DAOResetPassword daoResetPassword;

    @Autowired
    AppProperties appProperties;


    @Autowired
    SendSMS sendSMS;

    @Autowired
    SendEmail sendEmail;

    @Autowired
    MarketSettingsDAO marketSettingsDAO;



    @Autowired
    ServiceConfigurationDAO serviceConfigurationDAO;






    @PutMapping
    public ResponseEntity<Object> resetPassword(
            @RequestBody User user)
    {


        int rowCount = daoResetPassword.resetPassword(user);

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





    @GetMapping ("/CheckPasswordResetCode/{emailOrPhone}")
    public ResponseEntity<Object> checkPasswordResetCode(
            @PathVariable("emailOrPhone")String emailOrPhone,
            @RequestParam("ResetCode")String resetCode
    )
    {
        // Roles allowed not used for this method due to performance and effeciency requirements. Also
        // this endpoint doesnt required to be secured as it does not expose any confidential information

        boolean result = daoResetPassword.checkPasswordResetCode(emailOrPhone,resetCode);


        if(result)
        {
            return ResponseEntity.status(HttpStatus.OK)
                    .build();

        } else
        {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .build();
        }

    }





    @PutMapping ("/GenerateResetCode")
    ResponseEntity<Object> generateResetCode(@RequestBody User user)
    {

        int rowCount = 0;
        String resetCode = "";
        Timestamp timestampExpiry = null;



        if(!daoResetPassword.checkPasswordResetCodeExpired(user))
        {
            // code is expired

//            resetCode = new BigInteger(30, Globals.random).toString(32);
//            BigInteger phoneCode = new BigInteger(15, Globals.random);
//            int phoneOTP = phoneCode.intValue();


            resetCode = String.valueOf(generateOTP(5));


            timestampExpiry
                    = new Timestamp(
                    System.currentTimeMillis()
                            + appProperties.getPassword_reset_code_expiry_minutes() * 60 * 1000
            );


            rowCount =  daoResetPassword.updateResetCode(user,resetCode,timestampExpiry);


            if(rowCount==1)
            {
                // saved successfully


                if(user.getRt_registration_mode()==User.REGISTRATION_MODE_EMAIL)
                {

                    String message = "<p>You have made a request to verify your e-mail. If you did not request the e-mail verification please ignore this e-mail message.</p>"
                            +"<h2>The e-mail verification code is : " + resetCode + "</h2>" +
                            "<p>This verification code will expire at " + timestampExpiry.toLocaleString()
                            + ". Please use this code before it expires.</p>";



                    MarketSettings marketSettings = marketSettingsDAO.getSettingsInstance();
                    Market market = serviceConfigurationDAO.getMarketConfiguration();


                    Email email = EmailBuilder.startingBlank()
                            .from(market.getServiceName(),appProperties.getEmail_address_for_sender())
                            .to(user.getName(),user.getEmail())
                            .withSubject("E-mail Verification Code")
                            .withHTMLText(message)
                            .buildEmail();


                    sendEmail.getMailerInstance().sendMail(email,true);


                }
                else if(user.getRt_registration_mode()==User.REGISTRATION_MODE_PHONE)
                {
                    sendSMS.sendOTP(resetCode,user.getPhone());
                }


            }



        }
        else
        {
            // code is not expired


            User user_credentials = daoResetPassword.getResetCode(user);


            if(user.getRt_registration_mode()==User.REGISTRATION_MODE_EMAIL)
            {


                String message = "<p>You have made a request to verify your e-mail. If you did not request the e-mail verification please ignore this e-mail message.</p>"
                        +"<h2>The e-mail verification code is : " + user_credentials.getPasswordResetCode() + "</h2>" +
                        "<p>This verification code will expire at " + user_credentials.getResetCodeExpires().toLocaleString()
                        + ". Please use this code before it expires.</p>";




                MarketSettings marketSettings = marketSettingsDAO.getSettingsInstance();
                Market market = serviceConfigurationDAO.getMarketConfiguration();


                Email email = EmailBuilder.startingBlank()
                        .from(market.getServiceName(),appProperties.getEmail_address_for_sender())
                        .to(user.getName(),user.getEmail())
                        .withSubject("E-mail Verification Code")
                        .withHTMLText(message)
                        .buildEmail();


                sendEmail.getMailerInstance().sendMail(email,true);



            }
            else if(user.getRt_registration_mode()==User.REGISTRATION_MODE_PHONE)
            {
                sendSMS.sendOTP(user_credentials.getPasswordResetCode(),user.getPhone());
            }



            rowCount=1;
        }







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
