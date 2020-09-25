package org.nearbyshops.RESTEndpoints.RESTEndpointRoles;

import net.coobird.thumbnailator.Thumbnails;
import org.nearbyshops.AppProperties;
import org.nearbyshops.Constants;
import org.nearbyshops.DAOs.DAORoles.DAOShopStaff;
import org.nearbyshops.DAOs.DAORoles.DAOStaff;
import org.nearbyshops.DAOs.DAORoles.DAOUserNew;
import org.nearbyshops.DAOs.DAORoles.DAOUserUtility;
import org.nearbyshops.Model.ModelEndpoint.UserEndpoint;
import org.nearbyshops.Model.ModelRoles.ShopStaffPermissions;
import org.nearbyshops.Model.ModelRoles.StaffPermissions;
import org.nearbyshops.Model.ModelRoles.User;
import org.nearbyshops.Utility.Globals;
import org.nearbyshops.Utility.UserAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Base64;
import java.util.StringTokenizer;

import static org.nearbyshops.Utility.UserAuthentication.AUTHENTICATION_SCHEME;



@RestController
@RequestMapping ("/api/v1/User")
public class UserLoginRESTEndpoint {


    @Autowired
    DAOStaff daoStaff;

    @Autowired
    DAOShopStaff daoShopStaff;

    @Autowired
    private DAOUserNew daoUserNew;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserAuthentication userAuthentication;

    @Autowired
    private AppProperties appProperties;


    @Autowired
    private DAOUserUtility daoUserUtility;




    @PutMapping
    @RolesAllowed({Constants.ROLE_END_USER})
    public ResponseEntity<?> updateProfile(@RequestBody User user)
    {

        User userAuthenticated = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_END_USER));

        if(user==null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }


        user.setUserID(userAuthenticated.getUserID());



        int rowCount = daoUserNew.updateUser(user);



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




    @PutMapping ("/UpdateProfileByAdmin")
    @RolesAllowed({Constants.ROLE_ADMIN, Constants.ROLE_STAFF})
    public ResponseEntity<?> updateProfileByAdmin(@RequestBody User user)
    {

        User userStaff = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_STAFF));

        if(userStaff==null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }



        StaffPermissions permissions = daoStaff.getStaffPermissions(userStaff.getUserID());

        if(userStaff.getRole()== Constants.ROLE_STAFF_CODE)
        {
            if(permissions.isPermitApproveShops())
            {

            }
        }


        int rowCount = daoUserNew.updateUserByAdmin(user);


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





    @DeleteMapping
    @RolesAllowed({Constants.ROLE_STAFF})
    public ResponseEntity<?> deleteUser(@RequestParam("UserID")int userID)
    {

        User userStaff = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_STAFF));

        if(userStaff==null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }



        int rowCount = daoUserNew.deleteUser(userID);




//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}


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




    @PutMapping ("/UpdateEmail")
    @RolesAllowed({Constants.ROLE_END_USER})
    public ResponseEntity<?> updateEmail(@RequestBody User user)
    {

        User userAuthenticated = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_END_USER));

        if(userAuthenticated==null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }



        user.setUserID(userAuthenticated.getUserID());


        int rowCount = daoUserNew.updateEmail(user);

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



    @PutMapping ("/UpdatePhone")
    @RolesAllowed({Constants.ROLE_END_USER})
    ResponseEntity<?> updatePhone(@RequestBody User user)
    {

        User userAuthenticated = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_END_USER));

        if(userAuthenticated==null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }


        user.setUserID(userAuthenticated.getUserID());

        int rowCount = daoUserNew.updatePhone(user);


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




    @PutMapping ("/ChangePassword/{OldPassword}")
    @RolesAllowed({Constants.ROLE_END_USER})
    ResponseEntity<?> changePassword(@RequestBody User user, @PathVariable("OldPassword")String oldPassword)
    {

        User userAuthenticated = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_END_USER));

        if(userAuthenticated==null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }



        int rowCount = daoUserNew.updatePassword(user,oldPassword);


        if(rowCount >= 1)
        {
            return ResponseEntity.status(HttpStatus.OK)
                    .build();
        }
        if(rowCount == 0)
        {

            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .build();
        }

        return null;
    }




    @GetMapping ("/GetProfile")
    @RolesAllowed({Constants.ROLE_END_USER})
    public ResponseEntity<?> getProfile()
    {

        User userAuthenticated = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_END_USER));

        if(userAuthenticated==null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }



        User user = daoUserNew.getProfile(userAuthenticated.getUserID());


        if(user!=null)
        {

            return ResponseEntity.status(HttpStatus.OK)
                    .body(user);

        }
        else
        {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .build();
        }

    }







    @GetMapping ("/GetProfileWithLogin")
    public ResponseEntity<?> getProfileWithLogin()
    {

        String authHeader = request.getHeader(UserAuthentication.AUTHORIZATION_PROPERTY);

        final String encodedUserPassword = authHeader.replaceFirst(AUTHENTICATION_SCHEME + " ", "");

        //Decode username and password
        String usernameAndPassword = new String(Base64.getDecoder().decode(encodedUserPassword.getBytes()));

        //Split username and password tokens
        final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
        final String username = tokenizer.nextToken();
        final String password = tokenizer.nextToken();

        //Verifying Username and password
//        System.out.println(username);
//        System.out.println(password);



        String token = new BigInteger(130, Globals.random).toString(32);
        Timestamp timestampExpiry = new Timestamp(System.currentTimeMillis() + appProperties.getToken_duration_minutes() * 60 * 10);

        User user = daoUserNew.getProfile(username,password);




        if(user!=null)
        {

            user.setToken(token);
            user.setTimestampTokenExpires(timestampExpiry);

            // password is required for updating the token
            user.setPassword(password);

            int rowsUpdated = daoUserNew.updateToken(user);

            // we choose not to send password over the wire for security reasons
            user.setPassword(null);




            

            if(user.getRole() == Constants.ROLE_STAFF_CODE)
            {

                StaffPermissions permissions = daoStaff.getStaffPermissions(user.getUserID());
                user.setRt_staff_permissions(permissions);

            }
            else if (user.getRole() == Constants.ROLE_SHOP_STAFF_CODE)
            {

                ShopStaffPermissions permissions = daoShopStaff.getShopStaffPermissions(user.getUserID());
                user.setRt_shop_staff_permissions(permissions);
            }







//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }


            if(rowsUpdated==1)
            {

                return ResponseEntity.status(HttpStatus.OK)
                        .body(user);
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





    @GetMapping ("/GetToken")
    public ResponseEntity<?> getToken()
    {


        String authHeader = request.getHeader(UserAuthentication.AUTHORIZATION_PROPERTY);
        final String encodedUserPassword = authHeader.replaceFirst(UserAuthentication.AUTHENTICATION_SCHEME + " ", "");

        //Decode username and password
        String usernameAndPassword = new String(Base64.getDecoder().decode(encodedUserPassword.getBytes()));
//        System.out.println("Username:Password" + usernameAndPassword);

        //Split username and password tokens
        final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
        final String username = tokenizer.nextToken();
        final String password = tokenizer.nextToken();

        //Verifying Username and password
//        System.out.println(username);
//        System.out.println(password);


        String token = new BigInteger(130, Globals.random).toString(32);

        Timestamp timestampExpiry = new Timestamp(System.currentTimeMillis() + appProperties.getToken_duration_minutes()*60*1000);


//        Date dt = new Date();
//        DateTime dtOrg = new DateTime(System.currentTimeMillis());
//        DateTime dtPlusOne = dtOrg.plusDays(1);
//        timestamp  = new Timestamp(dtPlusOne.getMillis());


        User user = new User();
        user.setUsername(username);
        user.setPhone(username); // username could be phone number
        user.setEmail(username); // username could be email

        user.setPassword(password);
        user.setToken(token);
        user.setTimestampTokenExpires(timestampExpiry);

        int rowsUpdated = daoUserNew.updateToken(user);




        if(rowsUpdated==1)
        {

            User userProfile = daoUserNew.getProfileUsingToken(username,token);
            userProfile.setToken(token);
            userProfile.setPassword(null);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(userProfile);
        }
        else if(rowsUpdated==0)
        {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .build();
        }
        else
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }

    }




    @GetMapping
    @RolesAllowed({Constants.ROLE_STAFF, Constants.ROLE_SHOP_STAFF})
    public ResponseEntity<?> getUsers(
            @RequestParam(value = "UserRole",required = false) Integer userRole,
            @RequestParam(value = "Gender",required = false) Boolean gender,
            @RequestParam(value = "SearchString",required = false) String searchString,
            @RequestParam(value = "SortBy",required = false) String sortBy,
            @RequestParam(value = "Limit",defaultValue = "0")int limit,
            @RequestParam(value = "Offset",defaultValue = "0")int offset,
            @RequestParam(value = "GetRowCount",defaultValue = "false")boolean getRowCount,
            @RequestParam(value = "MetadataOnly",defaultValue = "false")boolean getOnlyMetaData)
    {



            User userAuthenticated = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_SHOP_STAFF,Constants.ROLE_STAFF));

            if(userAuthenticated==null)
            {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .build();
            }



            Integer shopID = null;

            if(userAuthenticated.getRole()== Constants.ROLE_SHOP_ADMIN_CODE)
            {
                shopID = daoUserUtility.getShopIDForShopAdmin(userAuthenticated.getUserID());
            }
            else if(userAuthenticated.getRole()== Constants.ROLE_SHOP_STAFF_CODE)
            {
                shopID = daoUserUtility.getShopIDforShopStaff(userAuthenticated.getUserID());
            }



            if(limit >= appProperties.getMax_limit())
            {
                limit = appProperties.getMax_limit();
            }






            UserEndpoint endPoint = daoUserNew.getUsers(
                    userRole,
                    shopID, gender,
                    searchString,
                    sortBy,
                    limit,offset,
                    getRowCount,getOnlyMetaData
            );



            endPoint.setLimit(limit);
            endPoint.setOffset(offset);
            endPoint.setMax_limit(appProperties.getMax_limit());


//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


//        System.out.println("Fetch Users List : " );

        //Marker
            return ResponseEntity.status(HttpStatus.OK)
                    .body(endPoint);
    }





    @GetMapping ("/GetUserDetails/{UserID}")
    @RolesAllowed({Constants.ROLE_ADMIN, Constants.ROLE_STAFF})
    public ResponseEntity<?> getUserDetails(@PathVariable("UserID")int userID)
    {

        User user = daoUserNew.getUserDetails(userID);


        if(user!=null)
        {

            return ResponseEntity.status(HttpStatus.OK)
                    .body(user);

        }
        else
        {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .build();
        }

    }






    // Image MEthods

    private static final java.nio.file.Path BASE_DIR = Paths.get("./data/images/User");
    private static final double MAX_IMAGE_SIZE_MB = 2;






    private void createThumbnails(String filename)
    {
        try {

            Thumbnails.of(BASE_DIR.toString() + "/" + filename)
                    .size(300,300)
                    .outputFormat("jpg")
                    .toFile(new File(BASE_DIR.toString() + "/" + "three_hundred_" + filename));

            //.toFile(new File("five-" + filename + ".jpg"));

            //.toFiles(Rename.PREFIX_DOT_THUMBNAIL);


            Thumbnails.of(BASE_DIR.toString() + "/" + filename)
                    .size(500,500)
                    .outputFormat("jpg")
                    .toFile(new File(BASE_DIR.toString() + "/" + "five_hundred_" + filename));



        } catch (IOException e) {
            e.printStackTrace();
        }
    }







}
