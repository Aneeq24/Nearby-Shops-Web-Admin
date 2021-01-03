package org.nearbyshops.WebControllers;

import org.nearbyshops.AppProperties;
import org.nearbyshops.DAOs.DAORoles.DAOUserNew;
import org.nearbyshops.DAOs.DAOSalesReport;
import org.nearbyshops.DAOs.ItemCategoryDAO;
import org.nearbyshops.DAOs.ItemDAO;
import org.nearbyshops.DAOs.ItemDAOJoinOuter;

import org.nearbyshops.Model.ModelEndpoint.ShopEndPoint;
import org.nearbyshops.Model.ModelRoles.User;
import org.nearbyshops.Model.ModelSalesReport.OrderSalesStats;
import org.nearbyshops.Model.Shop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Controller
public class testController {


    @Autowired
    private HttpSession session;

    @Autowired
    private ItemCategoryDAO itemCategoryDAO;

    @Autowired
    private ItemDAOJoinOuter itemDAO;

    @Autowired
    private ItemDAO itemDAO2;

    @Autowired
    DAOUserNew daoUserNew;

    @Autowired
    private DAOSalesReport salesDAO;

    @Autowired
    private AppProperties appProperties;

    Logger logger = LoggerFactory.getLogger(testController.class);
    private User user;

    @RequestMapping("/test")
    public ModelAndView showShops() {

//		logger.info(session.getAttribute("username").toString() + " | " + session.getAttribute("token").toString());

        ModelAndView model = new ModelAndView("page");

        model.addObject("userClickSales", true);
        model.addObject("salesList", salesDAO.getShopSales(null,null, null," total_order_sales DESC ",100,0,true,false).getOrderSalesStatsList());
        ShopEndPoint sales2=salesDAO.getShopSales(null,null,null," total_order_sales DESC ",100, 0,true,false);
        List<OrderSalesStats> stats=sales2.getOrderSalesStatsList();
       /* for (OrderSalesStats shop: stats){
            System.out.println(shop.getShopID()+"-->"+shop.getTotalOrderSales());
        }*/
        System.out.println("==========================================================================================Get Method Called");
        return model;
    }




    @RequestMapping(value= "/test", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView showShops(HttpServletRequest request,
                                  @RequestParam(value="startDate", required=false) String startDate,
                                  @RequestParam(value="endDate", required=false) String endDate) throws ParseException {


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date sdate = formatter.parse(startDate);
        Date edate = formatter.parse(endDate);
//		logger.info(session.getAttribute("username").toString() + " | " + session.getAttribute("token").toString());
        System.out.println(startDate);
        System.out.println(endDate);
        java.sql.Date sDate = convertUtilToSql(sdate);
        java.sql.Date eDate = convertUtilToSql(edate);
        System.out.println(sDate);
        System.out.println(eDate);

        ModelAndView model = new ModelAndView("page");

        model.addObject("userClickSales", true);
        model.addObject("salesList", salesDAO.getShopSales(null,null, null," total_order_sales DESC ",100,0,true,false).getOrderSalesStatsList());
        ShopEndPoint sales2=salesDAO.getShopSales(sDate,eDate,null," total_order_sales DESC ",100, 0,true,false);
        List<OrderSalesStats> stats=sales2.getOrderSalesStatsList();
        /*for (OrderSalesStats shop: stats){
            System.out.println(shop.getShopID()+"-->"+shop.getTotalOrderSales());
        }*/
        System.out.println("==========================================================================================2");
        return model;
    }

    private static java.sql.Date convertUtilToSql(java.util.Date uDate) {
        java.sql.Date sDate = new java.sql.Date(uDate.getTime());
        return sDate;
    }
}
