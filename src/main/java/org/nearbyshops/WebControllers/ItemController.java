package org.nearbyshops.WebControllers;

import org.nearbyshops.AppProperties;
import org.nearbyshops.Constants;
import org.nearbyshops.DAOs.ItemCategoryDAO;
import org.nearbyshops.DAOs.ItemDAO;
import org.nearbyshops.DAOs.ItemDAOJoinOuter;
import org.nearbyshops.Model.Item;
import org.nearbyshops.Model.ModelRoles.User;
import org.nearbyshops.Utility.UserAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

@Controller
public class ItemController {



    @Autowired
    private HttpServletRequest request;


    @Autowired
    private HttpSession session;

    @Autowired
    UserAuthentication userAuthentication;

    @Autowired
    private ItemCategoryDAO itemCategoryDAO;

    @Autowired
    private ItemDAOJoinOuter itemDAOJoinOuter;

    @Autowired
    private ItemDAO itemDAO;

    @Autowired
    private AppProperties appProperties;

    Logger logger = LoggerFactory.getLogger(CategoryController.class);
    private static final Path BASE_DIR = Paths.get("./data/images/Item");
    private static final double MAX_IMAGE_SIZE_MB = 2;



    @RequestMapping(value = "/editItem/{id}")
    public ModelAndView edit(@PathVariable int id) {
        User user = userAuthentication.isUserAllowed(
                session.getAttribute("username").toString(),
                session.getAttribute("token").toString(),
                Arrays.asList(Constants.ROLE_STAFF));
        if(user==null)
        {
            logger.info("Authentication failed !");
            return null;
        }

        ModelAndView model = new ModelAndView("page");
        Item item = itemDAO.getItemDetailsForEditItem(id );
        String imageURL = appProperties.getDomain_name() + "/api/v1/Item/Image";
        model.addObject("userClickEditItem", true);
        model.addObject("imgURL", imageURL);
        model.addObject("item", item);
        //System.out.println(item.toString());
        return model;
    }



    @RequestMapping(value = "/updateItem", method = RequestMethod.POST)
    public ModelAndView editsave(@ModelAttribute("item") Item item, @RequestParam("file") MultipartFile file) {


//		logger.info(session.getAttribute("username").toString() + " | " + session.getAttribute("token").toString());


        User user = userAuthentication.isUserAllowed(
                session.getAttribute("username").toString(),
                session.getAttribute("token").toString(),
                Arrays.asList(Constants.ROLE_STAFF));


        if(user==null)
        {

            logger.info("Authentication failed !");
            return null;
        }



        ModelAndView model = new ModelAndView("success");

        if (!file.isEmpty()) {

            try {


                if(item.getItemImageURL()!=null)
                {
                    Files.deleteIfExists(BASE_DIR.resolve(item.getItemImageURL()));
                    Files.deleteIfExists(BASE_DIR.resolve("three_hundred_" + item.getItemImageURL() + ".jpg"));
                    Files.deleteIfExists(BASE_DIR.resolve("five_hundred_" + item.getItemImageURL() + ".jpg"));
                }


                // Get the file and save it somewhere
                byte[] bytes = file.getBytes();



                File theDir = new File(BASE_DIR.toString());

                // if the directory does not exist, create it
                if (!theDir.exists()) {

                    //			System.out.println("Creating directory: " + BASE_DIR.toString());

                    boolean result = false;

                    try{
                        theDir.mkdir();
                        result = true;
                    }
                    catch(Exception se){
                        //handle it
                    }
                    if(result) {
                        //				System.out.println("DIR created");
                    }
                }



                String fileName = "" + System.currentTimeMillis();

                // Copy the file to its location.
                long filesize = Files.copy(file.getInputStream(), BASE_DIR.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);


                if(filesize > MAX_IMAGE_SIZE_MB * 1048 * 1024)
                {
                    // delete file if it exceeds the file size limit
                    Files.deleteIfExists(BASE_DIR.resolve(fileName));
                }


                item.setItemImageURL(fileName);




            } catch (IOException e) {
                e.printStackTrace();
            }

        }



        int response = itemDAO.updateItem(item);

        if (response == 1) {
            model.addObject("typeSucess", true);
            model.addObject("message", "Item Updated Successfully");
            model.addObject("url", "/getCategory/" + item.getItemCategoryID());
        } else {
            model.addObject("typeSucess", false);
            model.addObject("message", "Something went Wrong");
            model.addObject("url", "/editItem/" + item.getItemID());
        }
        return model;
    }



    @RequestMapping(value = "/addItem/toCategory/{id}")
    public ModelAndView addItem(@PathVariable int id) {

        ModelAndView model = new ModelAndView("page");

        User user = userAuthentication.isUserAllowed(
                session.getAttribute("username").toString(),
                session.getAttribute("token").toString(),
                Arrays.asList(Constants.ROLE_STAFF));


        if(user==null)
        {

            logger.info("Authentication failed !");
            return null;
        }
            Item item = new Item();
            model.addObject("userClickAddItem", true);
            model.addObject("categoryID", id);
            model.addObject("item", item);

        return model;
    }



    @RequestMapping(value = "/saveItem", method = RequestMethod.POST)
    public ModelAndView saveItem(@ModelAttribute("item") Item item, @RequestParam("file") MultipartFile file) {

        User user = userAuthentication.isUserAllowed(
                session.getAttribute("username").toString(),
                session.getAttribute("token").toString(),
                Arrays.asList(Constants.ROLE_STAFF));


        if(user==null)
        {

            logger.info("Authentication failed !");
            return null;
        }



        ModelAndView model = new ModelAndView("success");

        if (!file.isEmpty()) {

            try {


                if(item.getItemImageURL()!=null)
                {
                    Files.deleteIfExists(BASE_DIR.resolve(item.getItemImageURL()));
                    Files.deleteIfExists(BASE_DIR.resolve("three_hundred_" + item.getItemImageURL() + ".jpg"));
                    Files.deleteIfExists(BASE_DIR.resolve("five_hundred_" + item.getItemImageURL() + ".jpg"));
                }


                // Get the file and save it somewhere
                byte[] bytes = file.getBytes();



                File theDir = new File(BASE_DIR.toString());

                // if the directory does not exist, create it
                if (!theDir.exists()) {

                    //			System.out.println("Creating directory: " + BASE_DIR.toString());

                    boolean result = false;

                    try{
                        theDir.mkdir();
                        result = true;
                    }
                    catch(Exception se){
                        //handle it
                    }
                    if(result) {
                        //				System.out.println("DIR created");
                    }
                }



                String fileName = "" + System.currentTimeMillis();

                // Copy the file to its location.
                long filesize = Files.copy(file.getInputStream(), BASE_DIR.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);


                if(filesize > MAX_IMAGE_SIZE_MB * 1048 * 1024)
                {
                    // delete file if it exceeds the file size limit
                    Files.deleteIfExists(BASE_DIR.resolve(fileName));
                }


                item.setItemImageURL(fileName);




            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        int response = itemDAO.saveItem(item,false);

        if (response >= 1) {
            model.addObject("typeSucess", true);
            model.addObject("message", "Record Inserted Successfully");
            model.addObject("url", "/getCategory/" + item.getItemCategoryID());
        } else {
            model.addObject("typeSucess", false);
            model.addObject("message", "Something went Wrong");
            model.addObject("url", "/editItem/" + item.getItemID());
        }

        return model;
    }




    @RequestMapping(value = "/delete/inCategory/{categoryid}/item/{id}")
    @ResponseBody
    public ModelAndView deleteItem(@PathVariable("categoryid") int categoryid, @PathVariable("id") int id) {

        ModelAndView model = new ModelAndView("success");
        try {
            int response = itemDAO.deleteItem(id);
            model.addObject("responseCode", response);
            if (response == 1) {
                model.addObject("typeSucess", true);
                model.addObject("message", "Item Deleted Successfully");
                model.addObject("url", "/getCategory/" + categoryid);
            } else {
                model.addObject("typeSucess", false);
                model.addObject("message", "Something went Wrong");
                model.addObject("url", "/getCategory/" + categoryid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }
}
