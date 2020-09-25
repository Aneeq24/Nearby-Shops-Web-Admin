package org.nearbyshops.RESTEndpoints.RESTEndpointsImages;

import net.coobird.thumbnailator.Thumbnails;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.nearbyshops.AppProperties;
import org.nearbyshops.Constants;
import org.nearbyshops.DAOs.DAOImages.ShopImageDAO;
import org.nearbyshops.DAOs.DAORoles.DAOUserUtility;
import org.nearbyshops.DAOs.ShopDAO;
import org.nearbyshops.Model.Image;
import org.nearbyshops.Model.ModelEndpoint.ShopImageEndPoint;
import org.nearbyshops.Model.ModelImages.ShopImage;
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
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/api/v1/ShopImage")
public class ShopImageResource {

    @Autowired
    private ShopImageDAO shopImageDAO;

    @Autowired
    DAOUserUtility daoUserUtility;


    @Autowired
    ShopDAO shopDAO;



    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserAuthentication userAuthentication;


    @Autowired
    AppProperties appProperties;




    @PostMapping
    @RolesAllowed({Constants.ROLE_SHOP_STAFF,Constants.ROLE_STAFF})
    public ResponseEntity<Object> createShopImage(@RequestBody ShopImage shopImage)
    {


        User user = userAuthentication.isUserAllowed(request,
                Arrays.asList(Constants.ROLE_STAFF,Constants.ROLE_SHOP_STAFF));


        if(user==null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }




        int idOfInsertedRow = -1;
        int rowCount = 0;



        if( user.getRole()==Constants.ROLE_SHOP_ADMIN_CODE)
        {
            shopImage.setShopID(daoUserUtility.getShopIDForShopAdmin(user.getUserID()));
        }
        else if(user.getRole()==Constants.ROLE_SHOP_STAFF_CODE )
        {
            shopImage.setShopID(daoUserUtility.getShopIDforShopStaff(user.getUserID()));
        }




        rowCount = shopImageDAO.saveShopImage(shopImage,true);



        if(rowCount ==1)
        {

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(shopImage);

        }
        else
        {

            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .build();
        }

    }




    @PutMapping("/{ImageID}")
    @RolesAllowed({Constants.ROLE_SHOP_STAFF,Constants.ROLE_STAFF})
    public ResponseEntity<Object> updateShopImage(
            @RequestBody ShopImage shopImage,
            @PathVariable("ImageID")int imageID)
    {


        User user = userAuthentication.isUserAllowed(request,
                Arrays.asList(Constants.ROLE_STAFF,Constants.ROLE_SHOP_STAFF));

        if(user==null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }


        if( user.getRole()==Constants.ROLE_SHOP_ADMIN_CODE)
        {
            shopImage.setShopID(daoUserUtility.getShopIDForShopAdmin(user.getUserID()));
        }
        else if(user.getRole()==Constants.ROLE_SHOP_STAFF_CODE )
        {
            shopImage.setShopID(daoUserUtility.getShopIDforShopStaff(user.getUserID()));
        }




        int rowCount = shopImageDAO.updateShopImage(shopImage);


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





    @DeleteMapping ("/{ImageID}")
    @RolesAllowed({Constants.ROLE_SHOP_STAFF,Constants.ROLE_STAFF})
    public ResponseEntity<Object> deleteShopImage(@PathVariable("ImageID")int imageID)
    {

        User user = userAuthentication.isUserAllowed(request,
                Arrays.asList(Constants.ROLE_STAFF,Constants.ROLE_SHOP_STAFF));

        if(user==null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }


        ShopImage shopImage = shopImageDAO.getShopImageForImageID(imageID);

        int rowCount = shopImageDAO.deleteShopImage(imageID);



        if(rowCount>=1)
        {
            if(shopImage!=null)
            {
                if(shopImage.getImageFilename()!=null)
                {
                    deleteImageFileInternal(shopImage.getImageFilename());
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





    @GetMapping
    public ResponseEntity<Object> getShopImages(
            @RequestParam(value = "ShopID",required = false)Integer shopID,
            @RequestParam(value = "SortBy",required = false) String sortBy,
            @RequestParam(value = "Limit",required = false)Integer limit,
            @RequestParam(value = "Offset",defaultValue = "0")int offset,
            @RequestParam(value = "GetRowCount",defaultValue = "false")boolean getRowCount,
            @RequestParam(value = "MetadataOnly",defaultValue = "false")boolean getOnlyMetaData)
    {

        // *********************** second Implementation



        if(limit!=null && limit >= appProperties.getMax_limit())
        {
            limit = appProperties.getMax_limit();
        }



        ShopImageEndPoint endpoint = shopImageDAO.getShopImages(
                shopID,sortBy,
                limit,offset,
                getRowCount,getOnlyMetaData
        );



        if(limit!=null)
        {
            endpoint.setLimit(limit);
            endpoint.setOffset(offset);
            endpoint.setMax_limit(appProperties.getMax_limit());
        }





//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}

        //Marker

        return ResponseEntity.status(HttpStatus.OK)
                .body(endpoint);


    }






    @GetMapping ("/ForEndUser")
    public ResponseEntity<Object> getShopImagesForEnduser(
            @RequestParam(value = "ShopID",required = false)Integer shopID,
            @RequestParam(value = "SortBy",required = false) String sortBy,
            @RequestParam(value = "Limit",required = false)Integer limit,
            @RequestParam(value = "Offset",defaultValue = "0")int offset)
    {




        if(limit!=null && limit >= appProperties.getMax_limit())
        {
            limit = appProperties.getMax_limit();
        }



        ShopImageEndPoint endPoint = new ShopImageEndPoint();


        List<ShopImage> list =
                shopImageDAO.getShopImagesForEndUser(
                        shopID,
                        sortBy
                );



        endPoint.setResults(list);
        endPoint.setShopDetails(shopDAO.getShopPhoto(shopID));



        if(limit!=null)
        {
            endPoint.setLimit(limit);
            endPoint.setOffset(offset);
            endPoint.setMax_limit(appProperties.getMax_limit());
        }



//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}



        //Marker

        return ResponseEntity.status(HttpStatus.OK)
                .body(endPoint);

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

    private static final java.nio.file.Path BASE_DIR = Paths.get("./data/images/ShopImages");
    private static final double MAX_IMAGE_SIZE_MB = 2;


    @PostMapping ("/Image")
    @RolesAllowed({Constants.ROLE_STAFF, Constants.ROLE_SHOP_STAFF})
    public ResponseEntity<Object> uploadImage(@RequestParam(name = "img") MultipartFile img,
                                      @RequestParam(value = "PreviousImageName",required = false) String previousImageName
    ) throws Exception
    {


        User user = userAuthentication.isUserAllowed(request,
                Arrays.asList(Constants.ROLE_STAFF,Constants.ROLE_SHOP_STAFF));

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





    @GetMapping ("/Image/{name}")
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
