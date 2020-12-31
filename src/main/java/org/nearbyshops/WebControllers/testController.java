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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

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

        ModelAndView model = new ModelAndView("sales");
            model.addObject("userClickSales", true);
            model.addObject("salesList", salesDAO.getShopSales(null,null, null," total_order_sales DESC ",100,0,true,false));
        ShopEndPoint sales2=salesDAO.getShopSales(null,null,null," total_order_sales DESC ",100, 0,true,false);
        List<OrderSalesStats> stats=sales2.getOrderSalesStatsList();
        for (OrderSalesStats shop: stats){
            System.out.println(shop.getShopID()+"-->"+shop.getTotalOrderSales());
        }
        return model;
    }
}
