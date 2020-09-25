package org.nearbyshops.RESTEndpoints.RESTEndpointsImages;

import net.coobird.thumbnailator.Thumbnails;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.nearbyshops.Constants;
import org.nearbyshops.DAOs.DAOImages.BannerImageDAO;
import org.nearbyshops.DAOs.DAORoles.DAOUserUtility;
import org.nearbyshops.Model.Image;
import org.nearbyshops.Model.ModelImages.BannerImage;
import org.nearbyshops.Model.ModelRoles.User;
import org.nearbyshops.Utility.UserAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.logging.Logger;


@RestController
@RequestMapping("/api/v1/BannerImages")
public class BannerImageResource {


    @Autowired
    DAOUserUtility daoUserUtility;

    @Autowired
    BannerImageDAO bannerImageDAO;


    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserAuthentication userAuthentication;


    Logger logger  = Logger.getLogger(BannerImageResource.class.getName());



    @PostMapping
    @RolesAllowed({Constants.ROLE_SHOP_STAFF,Constants.ROLE_STAFF})
    public ResponseEntity<Object> createBannerImage(@RequestBody BannerImage bannerImage)
    {


        User user = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_STAFF,Constants.ROLE_SHOP_STAFF));

        if(user==null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }



        int idOfInsertedRow = -1;
        int rowCount = 0;

        if( user.getRole()==Constants.ROLE_SHOP_ADMIN_CODE)
        {
            bannerImage.setShopID(daoUserUtility.getShopIDForShopAdmin(user.getUserID()));
        }
        else if(user.getRole()==Constants.ROLE_SHOP_STAFF_CODE )
        {
            bannerImage.setShopID(daoUserUtility.getShopIDforShopStaff(user.getUserID()));
        }



        rowCount = bannerImageDAO.saveBannerImage(bannerImage,true);



        if(rowCount ==1)
        {

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(bannerImage);

        }
        else
        {

            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .build();
        }

    }




    @PutMapping ("/{ImageID}")
    @RolesAllowed({Constants.ROLE_SHOP_STAFF,Constants.ROLE_STAFF})
    public ResponseEntity<Object> updateBannerImage(@RequestBody BannerImage bannerImage,
                                                    @PathVariable("ImageID")int imageID)
    {


        User user = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_STAFF,Constants.ROLE_SHOP_STAFF));

        if(user==null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }



        if( user.getRole()==Constants.ROLE_SHOP_ADMIN_CODE)
        {
            bannerImage.setShopID(daoUserUtility.getShopIDForShopAdmin(user.getUserID()));
        }
        else if(user.getRole()==Constants.ROLE_SHOP_STAFF_CODE )
        {
            bannerImage.setShopID(daoUserUtility.getShopIDforShopStaff(user.getUserID()));
        }



        int rowCount = bannerImageDAO.updateBannerImage(bannerImage);


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





    @DeleteMapping ("/{BannerID}")
    @RolesAllowed({Constants.ROLE_SHOP_STAFF,Constants.ROLE_STAFF})
    public ResponseEntity<Object> deleteBannerImage(@PathVariable("BannerID")int bannerID)
    {


        User user = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_STAFF,Constants.ROLE_SHOP_STAFF));

        if(user==null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }


        int rowCount = bannerImageDAO.deleteBannerImage(bannerID);

        BannerImage bannerImage = bannerImageDAO.getImageFilenameForBannerID(bannerID);


        if(rowCount>=1)
        {
            if(bannerImage!=null)
            {
                if(bannerImage.getImageFilename()!=null)
                {
                    deleteImageFileInternal(bannerImage.getImageFilename());
                }

            }
        }




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



    @GetMapping ("/GetBannerImageDetails")
    public ResponseEntity<?> getBannerImageDetails(
            @RequestParam("BannerImageID")int bannerImageID
    )
    {

//        logger.info("Banner Image ID :" +  String.valueOf(bannerImageID));


        BannerImage bannerImage = bannerImageDAO.getBannerImageDetails(bannerImageID);

        if(bannerImage!=null)
        {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(bannerImage);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .build();
        }

    }






    // Image Utility Methods

    public boolean deleteImageFileInternal(String fileName)
    {
        boolean deleteStatus = false;

//        System.out.println("Filename: " + fileName);

        try {

            //Files.delete(BASE_DIR.resolve(fileName));
            deleteStatus = Files.deleteIfExists(BASE_DIR.resolve(fileName));

            // delete thumbnails
            Files.deleteIfExists(BASE_DIR.resolve("three_hundred_" + fileName + ".jpg"));
            Files.deleteIfExists(BASE_DIR.resolve("five_hundred_" + fileName + ".jpg"));
            Files.deleteIfExists(BASE_DIR.resolve("seven_hundred_" + fileName + ".jpg"));
            Files.deleteIfExists(BASE_DIR.resolve("nine_hundred_" + fileName + ".jpg"));


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return deleteStatus;
    }





    public static String saveNewImage(String serviceURL,String imageID)
    {
        try
        {
            serviceURL = serviceURL + "/api/v1/ItemImage/Image/" + imageID;

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(serviceURL)
                    .build();

            okhttp3.Response response = null;
            response = client.newCall(request).execute();
//			response.body().byteStream();
//			System.out.println();

            return uploadNewImage(response.body().byteStream());

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return null;
    }




    public static String uploadNewImage(InputStream in)
    {

        File theDir = new File(BASE_DIR.toString());

        // if the directory does not exist, create it
        if (!theDir.exists()) {

//            System.out.println("Creating directory: " + BASE_DIR.toString());

            boolean result = false;

            try{
                theDir.mkdir();
                result = true;
            }
            catch(Exception se){
                //handle it
            }
            if(result) {
//                System.out.println("DIR created");
            }
        }



        String fileName = "" + System.currentTimeMillis();


        try {

            // Copy the file to its location.
            long filesize = 0;

            filesize = Files.copy(in, BASE_DIR.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

            if(filesize > MAX_IMAGE_SIZE_MB * 1048 * 1024)
            {
                // delete file if it exceeds the file size limit
                Files.deleteIfExists(BASE_DIR.resolve(fileName));
                return null;
            }

            createThumbnails(fileName);

            Image image = new Image();
            image.setPath(fileName);

            // Return a 201 Created response with the appropriate Location header.

        }
        catch (IOException e) {
            e.printStackTrace();

            return null;
        }

        return fileName;
    }





    // Image MEthods

    private static final java.nio.file.Path BASE_DIR = Paths.get("./data/images/BannerImages");
    private static final double MAX_IMAGE_SIZE_MB = 2;


    @PostMapping ("/Image")
    @RolesAllowed({Constants.ROLE_STAFF, Constants.ROLE_SHOP_STAFF})
    public ResponseEntity<Object> uploadImage(@RequestParam(name = "img") MultipartFile img,
                                                @RequestParam(value = "PreviousImageName",required = false) String previousImageName
    ) throws Exception
    {



        User user = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_STAFF,Constants.ROLE_SHOP_STAFF));

        if(user==null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }



        if(previousImageName!=null)
        {
            Files.deleteIfExists(BASE_DIR.resolve(previousImageName));
            Files.deleteIfExists(BASE_DIR.resolve("three_hundred_" + previousImageName + ".jpg"));
            Files.deleteIfExists(BASE_DIR.resolve("five_hundred_" + previousImageName + ".jpg"));
            Files.deleteIfExists(BASE_DIR.resolve("seven_hundred_" + previousImageName + ".jpg"));
            Files.deleteIfExists(BASE_DIR.resolve("nine_hundred_" + previousImageName + ".jpg"));
        }


        File theDir = new File(BASE_DIR.toString());

        // if the directory does not exist, create it
        if (!theDir.exists()) {

//            System.out.println("Creating directory: " + BASE_DIR.toString());

            boolean result = false;

            try{
                theDir.mkdir();
                result = true;
            }
            catch(Exception se){
                //handle it
            }
            if(result) {
//                System.out.println("DIR created");
            }
        }



        String fileName = "" + System.currentTimeMillis();

        // Copy the file to its location.
        long filesize = Files.copy(img.getInputStream(), BASE_DIR.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

        if(filesize > MAX_IMAGE_SIZE_MB * 1048 * 1024)
        {
            // delete file if it exceeds the file size limit
            Files.deleteIfExists(BASE_DIR.resolve(fileName));


            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .build();
        }


        createThumbnails(fileName);


        Image image = new Image();
        image.setPath(fileName);

        // Return a 201 Created response with the appropriate Location header.

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(image);
    }



    private static void createThumbnails(String filename)
    {
        try {

            Thumbnails.of(BASE_DIR.toString() + "/" + filename)
                    .size(300,300)
                    .outputFormat("jpg")
                    .toFile(new File(BASE_DIR.toString() + "/" + "three_hundred_" + filename));



            Thumbnails.of(BASE_DIR.toString() + "/" + filename)
                    .size(500,500)
                    .outputFormat("jpg")
                    .toFile(new File(BASE_DIR.toString() + "/" + "five_hundred_" + filename));


            Thumbnails.of(BASE_DIR.toString() + "/" + filename)
                    .size(700,700)
                    .outputFormat("jpg")
                    .toFile(new File(BASE_DIR.toString() + "/" + "seven_hundred_" + filename));


            Thumbnails.of(BASE_DIR.toString() + "/" + filename)
                    .size(900,900)
                    .outputFormat("jpg")
                    .toFile(new File(BASE_DIR.toString() + "/" + "nine_hundred_" + filename));



        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    @GetMapping("/Image/{name}")
    public ResponseEntity<?> getImage(@PathVariable("name") String fileName, HttpServletResponse response) {

        //fileName += ".jpg";
        java.nio.file.Path dest = BASE_DIR.resolve(fileName);

        if (!Files.exists(dest)) {

            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .build();
        }


        try {

            StreamUtils.copy(Files.newInputStream(dest), response.getOutputStream());

            return ResponseEntity.status(HttpStatus.OK)
                    .build();


        } catch (IOException e) {
            e.printStackTrace();
        }


        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }



    @DeleteMapping ("/Image/{name}")
    @RolesAllowed({Constants.ROLE_STAFF, Constants.ROLE_SHOP_STAFF})
    public ResponseEntity<Object> deleteImageFile(@PathVariable("name")String fileName)
    {


        User user = userAuthentication.isUserAllowed(request,
                Arrays.asList(Constants.ROLE_STAFF,Constants.ROLE_SHOP_STAFF));

        if(user==null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }



        boolean deleteStatus = false;

        try {


            //Files.delete(BASE_DIR.resolve(fileName));
            deleteStatus = Files.deleteIfExists(BASE_DIR.resolve(fileName));

            // delete thumbnails
            Files.deleteIfExists(BASE_DIR.resolve("three_hundred_" + fileName + ".jpg"));
            Files.deleteIfExists(BASE_DIR.resolve("five_hundred_" + fileName + ".jpg"));
            Files.deleteIfExists(BASE_DIR.resolve("seven_hundred_" + fileName + ".jpg"));
            Files.deleteIfExists(BASE_DIR.resolve("nine_hundred_" + fileName + ".jpg"));


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        if(!deleteStatus)
        {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .build();

        }
        else
        {
            return ResponseEntity.status(HttpStatus.OK)
                    .build();
        }

    }


}
