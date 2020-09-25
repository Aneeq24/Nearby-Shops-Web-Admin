package org.nearbyshops.Experimental;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@Component
public class HandlerAdapter extends HandlerInterceptorAdapter {



    Logger logger = LoggerFactory.getLogger(HandlerAdapter.class);



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // Test if the controller-method is annotated with @CustomFilter
            handlerMethod.getMethod().getAnnotation(RolesAllowed.class);

            logger.info(handlerMethod.toString());
        }


        return true;
    }



}
