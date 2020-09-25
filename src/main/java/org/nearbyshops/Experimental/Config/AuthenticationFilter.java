//package org.nearbyshops.Config;
//
//
//import org.nearbyshops.Constants;
//import org.nearbyshops.DAOs.DAORoles.DAOUserNew;
//import org.nearbyshops.DAOs.DAORoles.DAOUserTokens;
//import org.nearbyshops.Model.ModelRoles.User;
//import org.nearbyshops.Utility.Globals;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import javax.annotation.security.DenyAll;
//import javax.annotation.security.RolesAllowed;
//import javax.ws.rs.ForbiddenException;
//import javax.ws.rs.NotAuthorizedException;
//import javax.ws.rs.container.ContainerRequestContext;
//import javax.ws.rs.container.ContainerRequestFilter;
//import javax.ws.rs.container.ResourceInfo;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.MultivaluedMap;
//import javax.ws.rs.ext.Provider;
//import java.lang.reflect.Method;
//import java.util.*;
//
///**
// * Created by sumeet on 9/9/16.
// */
//
//
//
//
//@Provider
//public class AuthenticationFilter implements ContainerRequestFilter {
//
//
//    @Autowired
//    private DAOUserNew daoUser;
//
//
//    @Autowired
//    private DAOUserTokens daoUserToken;
//
//
//    @Context
//    private ResourceInfo resourceInfo;
//
//
//
//    Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
//
//
//
//    private static final String AUTHORIZATION_PROPERTY = "Authorization";
//    private static final String AUTHENTICATION_SCHEME = "Basic";
//
//
//    @Override
//    public void filter(ContainerRequestContext requestContext) {
//
//        Method method = resourceInfo.getResourceMethod();
//
////        System.out.println("Security Filter");
//
//        logger.info("Security Filter Jersey !");
//
//            if (method.isAnnotationPresent(DenyAll.class)) {
//
//                throw new ForbiddenException("Access is ErrorNBSAPI !");
//            }
//
//
//
//            //Verify user access
//            if (method.isAnnotationPresent(RolesAllowed.class)) {
//                RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
//                Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));
//
//                //Get request headers
//                final MultivaluedMap<String, String> headers = requestContext.getHeaders();
//
//                //Fetch authorization header
//                final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);
//
//                //If no authorization information present; block access
//                if (authorization == null || authorization.isEmpty()) {
////                requestContext.abortWith(ACCESS_DENIED);
//
////                System.out.println("Access Denied : Auth header empty ! ");
//                    throw new NotAuthorizedException("Access is Denied ! Credentials not present");
//                }
//
//
//                final String encodedUserPassword = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");
//
//                //Decode username and password
//                String usernameAndPassword = new String(Base64.getDecoder().decode(encodedUserPassword.getBytes()));
//
//
//
//
////            System.out.println("Username:Password | " + usernameAndPassword);
//
//                //Split username and password tokens
//                final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
//                final String username = tokenizer.nextToken();
//                final String password = tokenizer.nextToken();
//
//
//                //Verifying Username and password
////            System.out.println(username);
////            System.out.println(password);
//
//
//
//
////            requestContext.setSecurityContext(new SecurityContext() {
////                @Override
////                public Principal getUserPrincipal() {
////                    return null;
////                }
////
////                @Override
////                public boolean isUserInRole(String s) {
////                    return false;
////                }
////
////                @Override
////                public boolean isSecure() {
////                    return false;
////                }
////
////                @Override
////                public String getAuthenticationScheme() {
////                    return null;
////                }
////            });
//
////            requestContext.setProperty("sample",);
//
//
//
//                Globals.accountApproved = isUserAllowed(username, password, rolesSet);
//
//
//            }
//
//    }
//
//
//
//    private Object isUserAllowed(final String username, final String password, final Set<String> rolesSet)
//    {
//
//        User user = daoUserToken.verifyUser(username,password);
//
//
//        if(user == null)
//        {
////            System.out.println("User NULL ");
//            throw new NotAuthorizedException("We are not able to identify you !");
//        }
//
//
////        System.out.println("Role : " + user.getRole());
//
//
//
////        if(user.isBlocked())
////        {
//////            System.out.println("User Blocked : ID :  " + user.getUserID());
////
////            throw new WebApplicationException("Message Response",Response.Status.NO_CONTENT);
//////
//////            return Response.status(Response.Status.NOT_MODIFIED)
//////                    .build();
////        }
//
//
//
//
//        //        boolean isEnabled = false;
//
//        //Step 1. Fetch password from database and match with password in argument
//        //If both match then get the defined role for user from database and continue; else return isAllowed [false]
//        //Access the database and do this part yourself
//        //String userRole = userMgr.getUserRole(username);
//
////        int roleID = -1;
//
//        for(String role : rolesSet)
//        {
////                System.out.println("Shop Admin null ...");
//
//
//            if(role.equals(Constants.ROLE_ADMIN))
//            {
//
//                if(user.getRole()== Constants.ROLE_ADMIN_CODE)
//                {
//                    return user;
//                }
//
//            }
//            else if(role.equals(Constants.ROLE_STAFF))
//            {
//
//                if(user.getRole()== Constants.ROLE_ADMIN_CODE ||
//                        user.getRole()== Constants.ROLE_STAFF_CODE)
//                {
//                    return user;
//                }
//
//            }
//            else if(role.equals(Constants.ROLE_SHOP_ADMIN))
//            {
//
//                if(user.getRole()== Constants.ROLE_SHOP_ADMIN_CODE)
//                {
//                    return user;
//                }
//
//            }
//            else if(role.equals(Constants.ROLE_DELIVERY_GUY_SELF))
//            {
//
//                if(user.getRole()== Constants.ROLE_DELIVERY_GUY_SELF_CODE)
//                {
//                    return user;
//                }
//
//            }
//            else if(role.equals(Constants.ROLE_SHOP_STAFF))
//            {
//
//                if(user.getRole()== Constants.ROLE_SHOP_ADMIN_CODE ||
//                        user.getRole()== Constants.ROLE_SHOP_STAFF_CODE)
//                {
//                    return user;
//                }
//            }
//            else if(role.equals(Constants.ROLE_DELIVERY_GUY))
//            {
//
//                if(user.getRole()== Constants.ROLE_DELIVERY_GUY_CODE)
//                {
//                    return user;
//                }
//
//            }
//            else if(role.equals(Constants.ROLE_END_USER))
//            {
//
//                return user;
//            }
//
//        }
//
//
//
//
////        System.out.println("Access Denied  : Role not allowed ");
//        throw new NotAuthorizedException("Access is Denied ! Not permitted !. ");
//    }
//
//
//}
//
//
