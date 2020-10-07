package org.nearbyshops.Experimental.RESTEndpoints;

import com.google.gson.Gson;
import org.nearbyshops.AppProperties;
import org.nearbyshops.Constants;
import org.nearbyshops.DAOs.ItemCategoryDAO;
import org.nearbyshops.Model.ItemCategory;
import org.nearbyshops.Model.ModelRoles.User;
import org.nearbyshops.Utility.UserAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;


//@RestController
//@RequestMapping("/InitialEndpoint")
public class SampleRESTEndpoint {

    Logger logger = LoggerFactory.getLogger(SampleRESTEndpoint.class);

    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Basic";


    @Autowired
    private UserAuthentication userAuthentication;

    @Autowired
    private AppProperties properties;


    @Autowired
    private HttpServletRequest context;


    @Autowired
    ItemCategoryDAO itemCategoryDAO;


    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    Gson gson;



    @GetMapping(value = "/Hello")
    @RolesAllowed({Constants.ROLE_END_USER})
    public String getEmployees() {


//        User user = userAuthentication.isUserAllowed(context, Arrays.asList(Constants.ROLE_SHOP_ADMIN));
////        logger.info("User JSON = " + gson.toJson(user));
////        logger.info("App Properties = " + gson.toJson(properties));
//
//
//
//        if(user==null)
//        {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .build();
//        }



//        ItemCategory itemCategory = (ItemCategory) context.getAttribute("item_cat");
//        int rowCount = itemCategoryDAO.saveItemCategory(itemCategory,true);
//        logger.info("Item Category Object : " + itemCategory.getCategoryName() + " | " + itemCategory.getCategoryDescription());



        return "Hello World !";
    }






    @GetMapping(value = "/HelloNew")
    @RolesAllowed({Constants.ROLE_END_USER})
    public ItemCategory helloNew(HttpServletResponse response) {


        ItemCategory itemCategory = (ItemCategory) context.getAttribute("item_cat");


        int rowCount = itemCategoryDAO.saveItemCategory(itemCategory,true);


        logger.info("Item Category Object : " + itemCategory.getCategoryName() + " | " + itemCategory.getCategoryDescription());


//        return "hello world !";




        if(rowCount==1)
        {
            response.setStatus(200);
            return itemCategory;
        }
        else
        {
            response.setStatus(304);
            return null;
        }

    }





    @GetMapping(value = "/HelloJDBC")
    public String getDataUsingJDBC() {

//        jdbcTemplate.execute("Insert into Item (item_name, item_desc) values ('hello','description')");
        return "hello jdbc ";
    }


}
