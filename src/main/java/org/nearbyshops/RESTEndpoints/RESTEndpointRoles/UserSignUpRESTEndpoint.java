package org.nearbyshops.RESTEndpoints.RESTEndpointRoles;


import org.nearbyshops.AppProperties;
import org.nearbyshops.Constants;
import org.nearbyshops.DAOs.DAORoles.DAOEmailVerificationCodes;
import org.nearbyshops.DAOs.DAORoles.DAOPhoneVerificationCodes;
import org.nearbyshops.DAOs.DAORoles.DAOUserSignUp;
import org.nearbyshops.DAOs.DAORoles.DAOUserUtility;
import org.nearbyshops.DAOs.DAOSettings.MarketSettingsDAO;
import org.nearbyshops.DAOs.DAOSettings.ServiceConfigurationDAO;
import org.nearbyshops.Model.ModelRoles.EmailVerificationCode;
import org.nearbyshops.Model.ModelRoles.PhoneVerificationCode;
import org.nearbyshops.Model.ModelRoles.User;
import org.nearbyshops.Model.ModelSettings.Market;
import org.nearbyshops.Model.ModelSettings.MarketSettings;
import org.nearbyshops.Utility.*;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;


/**
 * Created by sumeet on 14/8/17.
 */

@RestController
@RequestMapping ("/api/v1/User/SignUp")
public class UserSignUpRESTEndpoint {



    @Autowired
    private DAOUserSignUp daoUserSignUp;


    @Autowired
    MarketSettingsDAO marketSettingsDAO;

    @Autowired
    AppProperties appProperties;


    @Autowired
    SendSMS sendSMS;

    @Autowired
    SendEmail sendEmail;


    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserAuthentication userAuthentication;


    @Autowired
    DAOUserUtility daoUserUtility;

    @Autowired
    DAOEmailVerificationCodes daoEmailVerificationCodes;


    @Autowired
    DAOPhoneVerificationCodes daoPhoneVerificationCodes;



    @Autowired
    ServiceConfigurationDAO serviceConfigurationDAO;



    Logger logger = LoggerFactory.getLogger(UserSignUpRESTEndpoint.class);




    @PostMapping ("/EndUserRegistration")
    public ResponseEntity<Object> RegisterEndUser(@RequestBody User user)
    {
        return userRegistration(user, Constants.ROLE_END_USER_CODE);
    }





//    @POST
//    @Path("/ShopAdminRegistration")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntity<Object> RegisterShopAdmin(User user)
    {
        return userRegistration(user, Constants.ROLE_SHOP_ADMIN_CODE);
    }




    private ResponseEntity<Object> userRegistration(User user, int role)
    {

        MarketSettings marketSettings = marketSettingsDAO.getSettingsInstance();
        Market market = serviceConfigurationDAO.getMarketConfiguration();

        if(user==null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }


        user.setRole(role);


        int idOfInsertedRow =-1;




            idOfInsertedRow = daoUserSignUp.registerUsingEmailOrPhone(
                    user, true,true,false
            );


//            System.out.println("Email : " + user.getEmail()
//                    + "\nPassword : " + user.getPassword()
//                    + "\nRegistration Mode : " + user.getRt_registration_mode()
//                    + "\nName : " + user.getName()
//                    + "\nInsert Count : " + idOfInsertedRow
//                    + "\nVerificationCode : " + user.getRt_email_verification_code()
//            );


            if(idOfInsertedRow>=1)
            {


                if(user.getRt_registration_mode()==User.REGISTRATION_MODE_EMAIL)
                {
                    String message = "<h2>Your account has been Created. Your E-mail is : "+ user.getEmail() + ".</h2>"
                            + "<p>You can login with your email and password that you have provided. Thank you for creating your account.<p>";





                    // registration successful therefore send email to notify the user
                    Email email = EmailBuilder.startingBlank()
                            .from(market.getServiceName(),appProperties.getEmail_address_for_sender())
                            .to(user.getName(),user.getEmail())
                            .withSubject("Registration successful for your account")
                            .withHTMLText(message)
                            .buildEmail();


                    sendEmail.getMailerInstance().sendMail(email,true);
                }
                else
                {
                    String message = "";


                    if(user.getRole()== Constants.ROLE_SHOP_ADMIN_CODE)
                    {
                        message = "Thank you for registering with " + market.getServiceName();
                    }
                    else
                    {
                        message = "Congratulations your account has been registered with " + market.getServiceName();
                    }



                    sendSMS.sendSMS(message, user.getPhoneWithCountryCode());

                }

            }



            user.setUserID(idOfInsertedRow);


            if(idOfInsertedRow >=1)
            {

                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(user);

            }else {

                return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                        .build();
            }


    }





    @GetMapping ("/CheckUsernameExists/{username}")
    public ResponseEntity<Object> checkUsername(@PathVariable("username")String username)
    {
        // Roles allowed not used for this method due to performance and effeciency requirements. Also
        // this endpoint doesnt required to be secured as it does not expose any confidential information

        boolean result = daoUserUtility.checkUsernameExists(username);


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






    @GetMapping ("/CheckEmailVerificationCode/{email}")
    public ResponseEntity<Object> checkEmailVerificationCode(
            @PathVariable("email")String email,
            @RequestParam("VerificationCode")String verificationCode
    )
    {

        // Roles allowed not used for this method due to performance and effeciency requirements. Also
        // this endpoint doesnt required to be secured as it does not expose any confidential information
        boolean result = daoEmailVerificationCodes.checkEmailVerificationCode(email,verificationCode);


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





    @PutMapping("/SendEmailVerificationCode/{email}")
    public ResponseEntity<Object> sendEmailVerificationCode(@PathVariable("email")String email)
    {

        int rowCount = 0;


        EmailVerificationCode verificationCode = daoEmailVerificationCodes.checkEmailVerificationCode(email);

        if(verificationCode==null)
        {

            // verification code not generated for this email so generate one and send this to the user
            String emailVerificationCode = String.valueOf(UtilityMethods.generateOTP(5));

            Timestamp timestampExpiry
                    = new Timestamp(
                    System.currentTimeMillis()
                            + appProperties.getEmail_verification_code_expiry_minutes() *60*1000
            );


            rowCount = daoEmailVerificationCodes.insertEmailVerificationCode(
                    email,emailVerificationCode,timestampExpiry,true
            );


            if(rowCount==1)
            {
                // saved successfully



                String htmlText = "";



                htmlText = "<p>You have made a request to verify your e-mail. If you did not request the e-mail verification please ignore this e-mail message.</p>"
                        + "<h2>The e-mail verification code is : " + emailVerificationCode + "</h2>" +
                        "<p>This verification code will expire at " +
                        timestampExpiry.toLocalDateTime().getHour() + ":" + timestampExpiry.toLocalDateTime().getMinute()
                        + ". Please use this code before it expires.<p>";



                MarketSettings marketSettings = marketSettingsDAO.getSettingsInstance();
                Market market = serviceConfigurationDAO.getMarketConfiguration();

                Email emailComposed = EmailBuilder.startingBlank()
                        .from(market.getServiceName(),appProperties.getEmail_address_for_sender())
                        .to("user",email)
                        .withSubject("E-mail Verification Code")
                        .withHTMLText(htmlText)
                        .buildEmail();

                sendEmail.getMailerInstance().sendMail(emailComposed,true);

            }


        }
        else
        {

            System.out.println("Email Verification Code : " + verificationCode.getVerificationCode());


            String htmlText = "";

            htmlText = "<p>You have made a request to verify your e-mail. If you did not request the e-mail verification please ignore this e-mail message.</p>"
                    + "<h2>The e-mail verification code is : " + verificationCode.getVerificationCode() + "</h2>" +
                    "<p>This verification code will expire at " + verificationCode.getTimestampExpires().toLocalDateTime().toString() + ". Please use this code before it expires.<p>";

            MarketSettings marketSettings = marketSettingsDAO.getSettingsInstance();
            Market market = serviceConfigurationDAO.getMarketConfiguration();



            Email emailComposed = EmailBuilder.startingBlank()
                    .from(market.getServiceName(),appProperties.getEmail_address_for_sender())
                    .to("user",email)
                    .withSubject("E-mail Verification Code")
                    .withHTMLText(htmlText)
                    .buildEmail();

            sendEmail.getMailerInstance().sendMail(emailComposed,true);


            rowCount = 1;
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




    /* Methods for Phone Verification for Sign-Up */



    @GetMapping ("/CheckPhoneVerificationCode/{phone}")
    public ResponseEntity<Object> checkPhoneVerificationCode(
            @PathVariable("phone")String phone,
            @RequestParam("VerificationCode")String verificationCode
    )
    {
        // Roles allowed not used for this method due to performance and effeciency requirements. Also
        // this endpoint doesnt required to be secured as it does not expose any confidential information

        boolean result = daoPhoneVerificationCodes.checkPhoneVerificationCode(phone,verificationCode);


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




    @PutMapping ("/SendPhoneVerificationCode/{phone}")
    public ResponseEntity<Object> sendPhoneVerificationCode(@PathVariable("phone")String phone)
    {

        int rowCount = 0;


        PhoneVerificationCode verificationCode = daoPhoneVerificationCodes.checkPhoneVerificationCode(phone);

        if(verificationCode==null)
        {
            // verification code not generated for this email so generate one and send this to the user


            char[] phoneOTP = UtilityMethods.generateOTP(4);

            Timestamp timestampExpiry
                    = new Timestamp(
                    System.currentTimeMillis()
                            + appProperties.getPhone_otp_expiry_minutes() *60*1000
            );


            rowCount = daoPhoneVerificationCodes.insertPhoneVerificationCode(
                    phone,String.valueOf(phoneOTP),timestampExpiry,true
            );


            if(rowCount==1)
            {
                // saved successfully

                logger.info("Phone Verification Code : " + phoneOTP);


                sendSMS.sendOTP(String.valueOf(phoneOTP),phone);

            }


        }
        else
        {

            logger.info("Phone Verification Code : " + verificationCode.getVerificationCode());

            sendSMS.sendOTP(verificationCode.getVerificationCode(),phone);

            rowCount = 1;
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
