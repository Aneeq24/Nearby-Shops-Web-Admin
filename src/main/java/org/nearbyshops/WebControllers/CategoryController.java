package org.nearbyshops.WebControllers;

import org.nearbyshops.AppProperties;
import org.nearbyshops.Constants;
import org.nearbyshops.DAOs.ItemCategoryDAO;
import org.nearbyshops.DAOs.ItemDAO;
import org.nearbyshops.DAOs.ItemDAOJoinOuter;
import org.nearbyshops.Model.Item;
import org.nearbyshops.Model.ItemCategory;
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
import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private HttpServletRequest request;


    @Autowired
    private HttpSession session;

    @Autowired
    UserAuthentication userAuthentication;

    @Autowired
    private ItemCategoryDAO itemCategoryDAO;

    @Autowired
    private ItemDAOJoinOuter itemDAO;

    @Autowired
    private AppProperties appProperties;

    Logger logger = LoggerFactory.getLogger(CategoryController.class);
    private static final java.nio.file.Path BASE_DIR = Paths.get("./data/images/ItemCategory");
    private static final double MAX_IMAGE_SIZE_MB = 2;

    @RequestMapping("/getCategory/{id}")
    @ResponseBody
    public ModelAndView getCategories(@PathVariable("id") int id) {

        ModelAndView model = new ModelAndView("page");

        User user = userAuthentication.isUserAllowed(
                session.getAttribute("username").toString(),
                session.getAttribute("token").toString(),
                Arrays.asList(Constants.ROLE_STAFF));
        if(user==null)
        {
            logger.info("Authentication failed !");
            return new ModelAndView("redirect:/home?error=Authentication failed !");
        }else{

            model.addObject("category", itemCategoryDAO.getItemCategoryDetails(id));
            List<ItemCategory> categories=itemCategoryDAO.getItemCategoriesSimple(id,null,null,null,null, null).getResults();
            List<Item> items=itemDAO.getItems(id,null,null, null,100 ,0, true, false).getResults();
            model.addObject("category", itemCategoryDAO.getItemCategoryDetails(id));
            model.addObject("userClickItems", true);
            model.addObject("itemList", items);
            model.addObject("SubCategoriesList", categories);
            itemCategoryDAO.getItemCategoryDetails(id);
        }

        return model;
    }

    @RequestMapping(value = "/editCategory/{id}")
    public ModelAndView editCategory(@PathVariable int id) {

        ModelAndView model = new ModelAndView("page");
        String imageURL = appProperties.getDomain_name() + "/api/v1/ItemCategory/Image";
        User user = userAuthentication.isUserAllowed(
                session.getAttribute("username").toString(),
                session.getAttribute("token").toString(),
                Arrays.asList(Constants.ROLE_STAFF));
        if(user==null)
        {
            logger.info("Authentication failed !");
            return new ModelAndView("redirect:/home?error=Authentication failed !");
        }else{
            model.addObject("category", itemCategoryDAO.getItemCategoryDetails(id));
            model.addObject("userClickEditCategory", true);
            model.addObject("imgURL", imageURL);
        }
        return model;
    }

    @RequestMapping(value = "/updateCategory", method = RequestMethod.POST)
    public ModelAndView updateItem(@ModelAttribute("category") ItemCategory category,
                                   @RequestParam("file") MultipartFile file) {

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


                if(category.getImagePath()!=null)
                {
                    Files.deleteIfExists(BASE_DIR.resolve(category.getImagePath()));
                    Files.deleteIfExists(BASE_DIR.resolve("three_hundred_" + category.getImagePath() + ".jpg"));
                    Files.deleteIfExists(BASE_DIR.resolve("five_hundred_" + category.getImagePath() + ".jpg"));
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
                category.setImagePath(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        int rowCount = itemCategoryDAO.updateItemCategory(category);

        if (rowCount==1) {
            model.addObject("typeSucess", true);
            model.addObject("message", "Category Updated Successfully");
            model.addObject("url", "/getCategory/" + category.getParentCategoryID());
        } else {
            model.addObject("typeSucess", false);
            model.addObject("message", "Something went Wrong");
            model.addObject("url", "/editCategory/" + category.getParentCategoryID());
        }
        return model;
    }

    @RequestMapping(value = "/addCategory/toCategory/{id}")
    public ModelAndView addItem(@PathVariable int id) {

        ModelAndView model = new ModelAndView("page");
        if (session.getAttribute("token") != null) {
            model.addObject("userClickAddCategory", true);
            model.addObject("ParentCategory", id);
            model.addObject("category", new ItemCategory());
        } else {
            return new ModelAndView("redirect:/home?error=Session Expired");
        }
        return model;
    }

    @RequestMapping(value = "/saveCategory", method = RequestMethod.POST)
    public ModelAndView saveItem(@ModelAttribute("item") ItemCategory category,
                                 @RequestParam("file") MultipartFile file) {

        ModelAndView model = new ModelAndView("success");

        User user = userAuthentication.isUserAllowed(
                session.getAttribute("username").toString(),
                session.getAttribute("token").toString(),
                Arrays.asList(Constants.ROLE_STAFF));


        if(user==null)
        {

            logger.info("Authentication failed !");
            return null;
        }

        if (!file.isEmpty()) {

            try {

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
                category.setImagePath(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        int response = itemCategoryDAO.saveItemCategory(category,false);

        if (response >= 1) {
            model.addObject("typeSucess", true);
            model.addObject("message", "Category Inserted Successfully");
            model.addObject("url", "/getCategory/" + category.getParentCategoryID());
        } else {
            model.addObject("typeSucess", false);
            model.addObject("message", "Something went Wrong");
            model.addObject("url", "/getCategory/" + category.getParentCategoryID());
        }
        return model;
    }

    @RequestMapping(value = "/delete/inCategory/{parentCategoryId}/category/{id}")
    @ResponseBody
    public ModelAndView deleteItem(@PathVariable("parentCategoryId") int categoryid, @PathVariable("id") int id) {

        ModelAndView model = new ModelAndView("success");
        try {
            int response = itemCategoryDAO.deleteItemCategory(id);
            System.out.println(response);
            model.addObject("responseCode", response);
            if (response == 1) {
                model.addObject("typeSucess", true);
                model.addObject("message", "Category Deleted Successfully");
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
