package org.nearbyshops.Experimental;

import org.nearbyshops.Model.ItemCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;



//@Component
public class RequestFilter implements Filter {


    Logger logger = LoggerFactory.getLogger(RequestFilter.class);


    @Autowired
    private HttpServletRequest context;




    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("Filter Initialized !");
    }




    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        logger.info("Filter Executed !");
        logger.info(servletRequest.getRemoteHost() + " | " + servletRequest.getRemoteAddr() + " | " + servletRequest.getRemotePort());

        ItemCategory itemCategory = new ItemCategory();
        itemCategory.setCategoryName("Fruits");
        itemCategory.setCategoryDescription("Description for Fruits Category !");

        servletRequest.setAttribute("item_cat", itemCategory);

        filterChain.doFilter(servletRequest, servletResponse);

    }






    @Override
    public void destroy() {

        logger.info("Filter Destroyed !");
    }



}
