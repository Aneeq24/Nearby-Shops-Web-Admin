package org.nearbyshops.WebControllers;

import org.nearbyshops.DAOs.DAOSalesReport;
import org.nearbyshops.Model.ModelEndpoint.ShopEndPoint;
import org.nearbyshops.Model.ModelSalesReport.OrderSalesStats;
import org.nearbyshops.Utility.UserAuthentication;
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
public class SalesController {



    @Autowired
    private HttpServletRequest request;


    @Autowired
    private HttpSession session;

    @Autowired
    private DAOSalesReport salesDAO;


    @Autowired
    UserAuthentication userAuthentication;

    Logger logger = LoggerFactory.getLogger(SalesController.class);

    @RequestMapping("/sales")
    public ModelAndView showShops() {

        ModelAndView model = new ModelAndView("page");
        if (session.getAttribute("token") != null) {
            model.addObject("userClickSales", true);
            model.addObject("salesList", salesDAO.getShopSales(null,null, null," total_order_sales DESC ",100,0,true,false).getOrderSalesStatsList());
        } else {
            return new ModelAndView("redirect:/home?error=Session Expired");
        }
        return model;
    }



    @RequestMapping(value= "/sales", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView showShops(HttpServletRequest request,
                                  @RequestParam(value="startDate", required=false) String startDate,
                                  @RequestParam(value="endDate", required=false) String endDate) throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        java.sql.Date sDate = null;
        java.sql.Date eDate = null;

        if(startDate!=null && endDate!=null)
        {
            sDate =  new java.sql.Date(formatter.parse(startDate).getTime());
            eDate = new java.sql.Date(formatter.parse(endDate).getTime());
        }


//        logger.info("Start Date : " + startDate + " | " + " End Date : " + endDate);


        logger.info("Total Sales : " + salesDAO.getTotalSales());
        logger.info("Order Count : " + salesDAO.getOrderCount());
        logger.info("Vendor Count : " + salesDAO.getVendorCount());
        logger.info("User Count : " + salesDAO.getUserCount());

        ModelAndView model = new ModelAndView("page");

        model.addObject("userClickSales", true);
        model.addObject("salesList", salesDAO.getShopSales(sDate,eDate, null," total_order_sales DESC ",100,0,true,false).getOrderSalesStatsList());


        return model;
    }




    private static java.sql.Date convertUtilToSql(Date uDate) {
        java.sql.Date sDate = new java.sql.Date(uDate.getTime());
        return sDate;
    }

}
