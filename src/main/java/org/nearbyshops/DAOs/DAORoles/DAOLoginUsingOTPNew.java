package org.nearbyshops.DAOs.DAORoles;

import org.nearbyshops.Constants;
import org.nearbyshops.Model.ModelRoles.User;
import org.nearbyshops.Model.ModelRoles.UserTokens;
import org.nearbyshops.Utility.Globals;
import org.nearbyshops.Utility.UtilityMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



@Component
public class DAOLoginUsingOTPNew {


    @Autowired
    DataSource dataSource;



    public int upsertUserProfilePhone(User userProfile,
                                      boolean getRowCount)
    {

        Connection connection = null;
        PreparedStatement statement = null;

        int idOfInsertedRow = -1;
        int rowCountItems = -1;

        String insertItemSubmission =

                "INSERT INTO " + User.TABLE_NAME
                        + "(" + User.PHONE + ","
                        + User.ROLE + ","
                        + User.SECRET_CODE + ","
                        + User.TOKEN + ""
                        + ") values(?,?,?,?)"
                        + " ON CONFLICT (" + User.PHONE + ")"
                        + " DO NOTHING ";

        //        + " SET " + User.TOKEN + " = " + " excluded." + User.TOKEN + "";




//        String insertToken = "";
//
//        insertToken = "INSERT INTO "
//                + UserTokens.TABLE_NAME
//                + "("
//                + UserTokens.LOCAL_USER_ID + ","
//                + UserTokens.TOKEN_STRING + ""
//                + ") VALUES(?,?)";



        try {

            connection = dataSource.getConnection();
            connection.setAutoCommit(false);


            String generatedToken = new BigInteger(130, Globals.random).toString(32);
            userProfile.setToken(generatedToken);


            statement = connection.prepareStatement(insertItemSubmission,PreparedStatement.RETURN_GENERATED_KEYS);
            int i = 0;

            statement.setObject(++i,userProfile.getPhone());
            statement.setObject(++i, Constants.ROLE_END_USER_CODE);
            statement.setObject(++i, Integer.parseInt(String.valueOf(UtilityMethods.generateOTP(5))));
            statement.setObject(++i,userProfile.getToken());

            rowCountItems = statement.executeUpdate();


            ResultSet rs = statement.getGeneratedKeys();

            if(rs.next())
            {
                idOfInsertedRow = rs.getInt(1);
            }



//
//            statement = connection.prepareStatement(insertToken);
//
//            i = 0;
//            statement.setInt(++i,idOfInsertedRow);
//            statement.setString(++i,userProfile.getToken());
//
//            statement.executeUpdate();


            connection.commit();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            if (connection != null) {
                try {

                    idOfInsertedRow=-1;
                    rowCountItems = 0;

                    connection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        finally
        {

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }


            try {

                if(connection!=null)
                {connection.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if(getRowCount)
        {
            return rowCountItems;
        }
        else
        {
            return idOfInsertedRow;
        }
    }




    public int upsertUserProfileEmail(User userProfile,
                                 boolean getRowCount)
    {

        Connection connection = null;
        PreparedStatement statement = null;

        int idOfInsertedRow = -1;
        int rowCountItems = -1;

        String insertItemSubmission =

                "INSERT INTO " + User.TABLE_NAME
                        + "(" + User.E_MAIL + ","
                        + User.ROLE + ","
                        + User.SECRET_CODE + ","
                        + User.TOKEN + ""
                        + ") values(?,?,?,?)"
                        + " ON CONFLICT (" + User.E_MAIL + ")"
                        + " DO NOTHING ";

//        + " SET " + User.TOKEN + " = " + " excluded." + User.TOKEN + "";


//
//        String insertToken = "";
//
//        insertToken = "INSERT INTO "
//                + UserTokens.TABLE_NAME
//                + "("
//                + UserTokens.LOCAL_USER_ID + ","
//                + UserTokens.TOKEN_STRING + ""
//                + ") VALUES(?,?)";



        try {

            connection = dataSource.getConnection();
            connection.setAutoCommit(false);



            String generatedToken = new BigInteger(130, Globals.random).toString(32);
            userProfile.setToken(generatedToken);


            statement = connection.prepareStatement(insertItemSubmission,PreparedStatement.RETURN_GENERATED_KEYS);
            int i = 0;

            statement.setObject(++i,userProfile.getEmail());
            statement.setObject(++i, Constants.ROLE_END_USER_CODE);
            statement.setObject(++i, Integer.parseInt(String.valueOf(UtilityMethods.generateOTP(5))));
            statement.setObject(++i,userProfile.getToken());


            rowCountItems = statement.executeUpdate();


            ResultSet rs = statement.getGeneratedKeys();

            if(rs.next())
            {
                idOfInsertedRow = rs.getInt(1);
            }


//
//
//
//            statement = connection.prepareStatement(insertToken);
//
//            i = 0;
//            statement.setInt(++i,idOfInsertedRow);
//            statement.setString(++i,userProfile.getToken());
//
//            statement.executeUpdate();


            connection.commit();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            if (connection != null) {
                try {

                    idOfInsertedRow=-1;
                    rowCountItems = 0;

                    connection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        finally
        {

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }


            try {

                if(connection!=null)
                {connection.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if(getRowCount)
        {
            return rowCountItems;
        }
        else
        {
            return idOfInsertedRow;
        }
    }

}
