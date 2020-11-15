package org.nearbyshops.Utility;

import org.nearbyshops.Constants;
import org.nearbyshops.DAOs.DAORoles.DAOUserNew;
import org.nearbyshops.DAOs.DAORoles.DAOUserTokens;
import org.nearbyshops.Model.ModelRoles.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;



@Component
public class UserAuthentication {


    Logger logger = LoggerFactory.getLogger(UserAuthentication.class);

    public static final String AUTHORIZATION_PROPERTY = "Authorization";
    public static final String AUTHENTICATION_SCHEME = "Basic";


    @Autowired
    DAOUserNew daoUser;

    @Autowired
    DAOUserTokens daoUserToken;



    public User isUserAllowed(HttpServletRequest request, List<String> rolesAllowed)
    {

//        logger.info("Auth Header : " + request.getHeader("Authorization"));

        String authHeader = request.getHeader(AUTHORIZATION_PROPERTY);

        if(authHeader==null)
        {
            // no authorization header therefore return null
            return null;
        }


        final String encodedUserPassword = authHeader.replaceFirst(AUTHENTICATION_SCHEME + " ", "");


//        final Set<String> rolesSet = new HashSet<String>(Arrays.asList(roles));

        //Decode username and password
        String usernameAndPassword = new String(Base64.getDecoder().decode(encodedUserPassword.getBytes()));

        //Split username and password tokens
        final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
        final String username = tokenizer.nextToken();
        final String password = tokenizer.nextToken();


//        logger.info("Username : Password = " + username + " : " + password);


        return isUserAllowed(username,password,rolesAllowed);
    }


    private User isUserAllowed(final String username, final String password, List<String> rolesSet)
    {

        User user = daoUserToken.verifyUserSimpleToken(username,password);


        if(user == null)
        {
            return null;
        }



        for(String role : rolesSet)
        {


            if(role.equals(Constants.ROLE_ADMIN))
            {

                if(user.getRole()== Constants.ROLE_ADMIN_CODE)
                {
                    return user;
                }

            }
            else if(role.equals(Constants.ROLE_STAFF))
            {

                if(user.getRole()== Constants.ROLE_ADMIN_CODE ||
                        user.getRole()== Constants.ROLE_STAFF_CODE)
                {
                    return user;
                }

            }
            else if(role.equals(Constants.ROLE_SHOP_ADMIN))
            {

                if(user.getRole()== Constants.ROLE_SHOP_ADMIN_CODE)
                {
                    return user;
                }

            }
            else if(role.equals(Constants.ROLE_DELIVERY_GUY_SELF))
            {

                if(user.getRole()== Constants.ROLE_DELIVERY_GUY_SELF_CODE)
                {
                    return user;
                }

            }
            else if(role.equals(Constants.ROLE_SHOP_STAFF))
            {

                if(user.getRole()== Constants.ROLE_SHOP_ADMIN_CODE ||
                        user.getRole()== Constants.ROLE_SHOP_STAFF_CODE)
                {
                    return user;
                }
            }
            else if(role.equals(Constants.ROLE_DELIVERY_GUY))
            {

                if(user.getRole()== Constants.ROLE_DELIVERY_GUY_CODE)
                {
                    return user;
                }

            }
            else if(role.equals(Constants.ROLE_END_USER))
            {

                return user;
            }

        }


        return null;
    }


}
