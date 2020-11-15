package org.nearbyshops.RESTEndpoints.RESTEndpointRoles;

import com.google.gson.Gson;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.nearbyshops.AppProperties;
import org.nearbyshops.Constants;
import org.nearbyshops.DAOs.DAORoles.*;
import org.nearbyshops.DAOs.DAOSettings.ServiceConfigurationDAO;
import org.nearbyshops.Model.ModelRoles.ShopStaffPermissions;
import org.nearbyshops.Model.ModelRoles.StaffPermissions;
import org.nearbyshops.Model.ModelRoles.User;
import org.nearbyshops.Utility.Globals;
import org.nearbyshops.Utility.UserAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Base64;
import java.util.StringTokenizer;




@RestController
@RequestMapping ("/api/v1/User/LoginUsingOTP")
public class LoginUsingOTPRESTEndpoint {


    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserAuthentication userAuthentication;

    @Autowired
    private AppProperties appProperties;


    @Autowired
    private DAOUserNew daoUser;

    @Autowired
    private DAOLoginUsingOTP daoLoginUsingOTP;

    @Autowired
    private DAOLoginUsingOTPNew daoLoginUsingOTPNew;

    @Autowired
    private DAOPhoneVerificationCodes daoPhoneVerificationCodes;

    @Autowired
    private DAOEmailVerificationCodes daoEmailVerificationCodes;

    @Autowired
    ServiceConfigurationDAO serviceConfigDAO;


    @Autowired
    DAOStaff daoStaff;

    @Autowired
    DAOShopStaff daoShopStaff;


    @Autowired
    Gson gson;


    private final OkHttpClient client = new OkHttpClient();



    Logger logger = LoggerFactory.getLogger(LoginUsingOTPRESTEndpoint.class);





    @GetMapping("/LoginUsingPhoneOTP")
    public ResponseEntity<Object> getProfileWithLogin()
    {


        String authHeader = request.getHeader(UserAuthentication.AUTHORIZATION_PROPERTY);

        if(authHeader==null)
        {
            // no authorization header therefore return null
            return null;
        }


        final String encodedUserPassword = authHeader.replaceFirst(UserAuthentication.AUTHENTICATION_SCHEME + " ", "");

        //Decode username and password
        String usernameAndPassword = new String(Base64.getDecoder().decode(encodedUserPassword.getBytes()));

        //Split username and password tokens
        final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
        final String phone = tokenizer.nextToken();
        final String phoneOTP = tokenizer.nextToken();

        //Verifying Username and password
//        logger.info(username);
//        logger.info(password);


//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }




        String generatedPassword = new BigInteger(130, Globals.random).toString(32);


        User user = new User();
        user.setToken(generatedPassword);
        user.setPhone(phone);


        boolean isOTPValid = daoPhoneVerificationCodes.checkPhoneVerificationCode(phone,phoneOTP);





        if(isOTPValid)
        {
            int rowsUpdated = daoLoginUsingOTP.upsertUserProfile(user,true);



            // get profile information and send it to user
            User userProfile = daoUser.getProfileUsingToken(phone,generatedPassword);
            userProfile.setToken(generatedPassword);
            userProfile.setPhone(phone);




            if(user.getRole() == Constants.ROLE_STAFF_CODE) {

                StaffPermissions permissions = daoStaff.getStaffPermissions(user.getUserID());

                if (permissions != null)
                {
                    user.setRt_staff_permissions(permissions);
                }
            }
            else if (user.getRole() == Constants.ROLE_SHOP_STAFF_CODE)
            {
                ShopStaffPermissions permissions = daoShopStaff.getShopStaffPermissions(user.getUserID());

                if(permissions!=null)
                {
                    user.setRt_shop_staff_permissions(permissions);
                }
            }



            if(rowsUpdated==1)
            {



//                SendSMS.sendSMS("You are logged in successfully !",
//                        user.getPhone());


                return ResponseEntity.status(HttpStatus.OK)
                        .body(userProfile);
            }
            else
            {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                        .build();
            }

        }
        else
        {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .build();
        }

    }




    @GetMapping ("/VerifyCredentialsUsingOTP")
    public ResponseEntity<Object> verifyCredentialsUsingOTP(
            @RequestParam("RegistrationMode")int registrationMode // 1 for email and 2 for phone
    )
    {


        String authHeader = request.getHeader(UserAuthentication.AUTHORIZATION_PROPERTY);

        if(authHeader==null)
        {
            // no authorization header therefore return null
            return null;
        }


        final String encodedUserPassword = authHeader.replaceFirst(UserAuthentication.AUTHENTICATION_SCHEME + " ", "");

        //Decode username and password
        String usernameAndPassword = new String(Base64.getDecoder().decode(encodedUserPassword.getBytes()));

        //Split username and password tokens
        final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
        final String emailOrPhone = tokenizer.nextToken();
        final String emailOrPhoneOTP = tokenizer.nextToken();



        boolean isOTPValid = false;


        if(registrationMode==User.REGISTRATION_MODE_PHONE)
        {
            isOTPValid = daoPhoneVerificationCodes.checkPhoneVerificationCode(emailOrPhone,emailOrPhoneOTP);
        }
        else if(registrationMode==User.REGISTRATION_MODE_EMAIL)
        {
            isOTPValid = daoEmailVerificationCodes.checkEmailVerificationCode(emailOrPhone,emailOrPhoneOTP);
        }




        logger.info("Log : " + emailOrPhone + " | " + emailOrPhoneOTP);
        logger.info("Log : Registration Mode - "  + registrationMode);


        if(isOTPValid)
        {


            logger.info("Log : OTP Valid");


            User user = new User();


//            int rowsUpdated = 0;



            if(registrationMode==User.REGISTRATION_MODE_PHONE)
            {
                user.setPhone(emailOrPhone);
                daoLoginUsingOTPNew.upsertUserProfilePhone(user,true);
            }
            else if(registrationMode==User.REGISTRATION_MODE_EMAIL) {

                user.setEmail(emailOrPhone);
                daoLoginUsingOTPNew.upsertUserProfileEmail(user,true);
            }




//            logger.info("Rows updated : " + rowsUpdated);



            User userProfile = daoUser.checkTokenAndGetProfile(emailOrPhone);


            if(userProfile!=null)
            {

                // get profile information and send it to user
//                userProfile.setToken(generatedToken);

                return ResponseEntity.status(HttpStatus.OK)
                        .body(userProfile);

            }
            else
            {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                        .build();
            }



        }
        else
        {

            logger.info("Log : OTP Not Valid");

            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .build();
        }


    }





//    @RequestParam("MarketID")int marketID,

    @GetMapping ("/LoginUsingGlobalCredentials")
    public ResponseEntity<Object> loginWithGlobalCredentials(
            @RequestParam("ServiceURLSDS")String serviceURLForSDS,
            @RequestParam(value = "IsPasswordAnOTP",defaultValue = "false")boolean isPasswordAnOTP, // indicates when you are trying to log in using OTP
            @RequestParam(value = "VerifyUsingPassword",defaultValue = "false")boolean verifyUsingPassword, // indicates whether you are verifying using token or password
            @RequestParam(value = "RegistrationMode",defaultValue = "0")int registrationMode, // 1 for email and 2 for phone
            @RequestParam(value = "GetServiceConfiguration",defaultValue = "false")boolean getServiceConfig,
            @RequestParam(value = "GetUserProfileGlobal",defaultValue = "false")boolean getUserProfileGlobal
    ) throws IOException
    {


        String authHeader = request.getHeader(UserAuthentication.AUTHORIZATION_PROPERTY);

        if(authHeader==null)
        {
            // no authorization header therefore return null
            return null;
        }



        boolean trusted = false;

        for(String url : appProperties.getTrusted_market_aggregators())
        {

//            logger.info("URL SDS : " + url);

            if(url.equals(serviceURLForSDS))
            {
                trusted = true;
                break;
            }
        }


        if(!trusted)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .build();
        }




        final String encodedUserPassword = authHeader.replaceFirst(UserAuthentication.AUTHENTICATION_SCHEME + " ", "");

        //Decode username and password
        String usernameAndPassword = new String(Base64.getDecoder().decode(encodedUserPassword.getBytes()));

        //Split username and password tokens
        final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
        final String username = tokenizer.nextToken();
        final String password = tokenizer.nextToken();

        //Verifying Username and password
//        logger.info(username);
//        logger.info(password);


//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }



//        logger.info("Username : " + phone + " | Password : " + password);



        String credentials = Credentials.basic(username, password);

        String url = "";

        if(isPasswordAnOTP)
        {
            url = serviceURLForSDS + "/api/v1/User/LoginGlobal/VerifyCredentialsUsingOTP?RegistrationMode=" + registrationMode;
        }
        else
        {
            url = serviceURLForSDS + "/api/v1/User/LoginGlobal/VerifyCredentials?VerifyUsingPassword=" + verifyUsingPassword;
        }





        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", credentials)
                .build();



        User userProfileGlobal;
        String generatedTokenGlobal;




        try (okhttp3.Response response = client.newCall(request).execute()) {


//            if (!response.isSuccessful())
//            {
//                return Response.status(Response.Status.BAD_REQUEST)
//                        .build();
//            }

//            Headers responseHeaders = response.headers();
//            for (int i = 0; i < responseHeaders.size(); i++) {
//                logger.info(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//            }


//            logger.info("Response Code : " + response.code());

            if(response.code()!=200)
            {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .build();
            }




//            logger.info(response.body().string());
            userProfileGlobal = gson.fromJson(response.body().string(),User.class);

            generatedTokenGlobal = userProfileGlobal.getToken();
        }






//        String generatedTokenLocal = new BigInteger(130, Globals.random).toString(32);


//        User user = new User();
//        user.setPassword(generatedPassword);
//        user.setPhone(phone);



//        userProfileGlobal.setToken(generatedTokenLocal);




        int rowsUpdated = 0;



        if(daoLoginUsingOTP.checkUserExists(userProfileGlobal.getEmail(),userProfileGlobal.getPhone())!=null)
        {
            // user exist ...  update profile
            rowsUpdated = daoLoginUsingOTP.updateUserProfile(userProfileGlobal);
        }
        else
        {


            // check if user account has existing associations

//            if(daoLoginUsingOTP.checkUserExistsUsingAssociations(userProfileGlobal.getUserID(),serviceURLForSDS)!=null)
//            {
//                // account exists
//                rowsUpdated = daoLoginUsingOTP.updateUserProfileAssociated(userProfileGlobal,serviceURLForSDS);
//
//            }
//            else
//            {
//                // user account does not exist ... insert profile
//                rowsUpdated = daoLoginUsingOTP.insertUserProfile(userProfileGlobal,serviceURLForSDS,true);
//
//            }


            rowsUpdated = daoLoginUsingOTP.insertUserProfile(userProfileGlobal,serviceURLForSDS,true);
        }


        logger.info("Row Count : " + rowsUpdated);




//        int rowsUpdated = daoLoginUsingOTP.upsertUserProfileNew(userProfileGlobal,true);


        try {


            // get profile information and send it to user
            User userProfile = daoUser.checkTokenAndGetProfile(username);
//            userProfile.setToken(generatedTokenLocal);



            if (rowsUpdated == 1) {



                if (getServiceConfig) {
//                    userProfile.setServiceConfigurationLocal(serviceConfigDAO.getServiceConfigurationFromTable(0.0, 0.0));
                    userProfile.setServiceConfigurationLocal(serviceConfigDAO.getMarketConfiguration());
                }



                if (getUserProfileGlobal) {

                    userProfileGlobal.setToken(generatedTokenGlobal);
                    userProfile.setUserProfileGlobal(userProfileGlobal);

                }


//                SendSMS.sendSMS("You are logged in successfully !",
//                        user.getPhone());

                return ResponseEntity.status(HttpStatus.OK)
                        .body(userProfile);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .build();
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();

            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .build();

        }
    }



}
