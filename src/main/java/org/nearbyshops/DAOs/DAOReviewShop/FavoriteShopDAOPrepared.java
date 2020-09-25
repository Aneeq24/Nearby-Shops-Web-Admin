package org.nearbyshops.DAOs.DAOReviewShop;

import org.nearbyshops.Model.ModelReviewShop.FavouriteShop;
import org.nearbyshops.Model.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by sumeet on 8/8/16.
 */

@Component
public class FavoriteShopDAOPrepared {



    @Autowired
    DataSource dataSource;




    public int saveFavourite(FavouriteShop favouriteShop)
    {


        Connection conn = null;
        PreparedStatement statement = null;
        int idOfInsertedRow = 0;


        String insertStatement = "INSERT INTO "
                + FavouriteShop.TABLE_NAME
                + "("
                + FavouriteShop.SHOP_ID + ","
                + FavouriteShop.END_USER_ID
                + ") VALUES(?,?)";

        try {

            conn = dataSource.getConnection();
            statement = conn.prepareStatement(insertStatement,PreparedStatement.RETURN_GENERATED_KEYS);

            statement.setInt(1,favouriteShop.getShopID());
            statement.setInt(2,favouriteShop.getEndUserID());

            idOfInsertedRow = statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();

            if(rs.next())
            {
                idOfInsertedRow = rs.getInt(1);
            }


        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        finally
        {

            try {

                if(statement!=null)
                {statement.close();}

            }
            catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {

                if(conn!=null)
                {conn.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return idOfInsertedRow;

    }



    public int deleteFavourite(int shopID, int memberID)
    {

        String deleteStatement = "DELETE FROM " + FavouriteShop.TABLE_NAME
                + " WHERE " + FavouriteShop.SHOP_ID + " = ?"
                + " AND " + FavouriteShop.END_USER_ID + " = ?";

        Connection conn= null;
        PreparedStatement statement = null;
        int rowCountDeleted = 0;
        try {


            conn = dataSource.getConnection();
            statement = conn.prepareStatement(deleteStatement);

            statement.setInt(1,shopID);
            statement.setInt(2,memberID);

            rowCountDeleted = statement.executeUpdate();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        finally

        {

            try {

                if(statement!=null)
                {statement.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {

                if(conn!=null)
                {conn.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return rowCountDeleted;
    }



    public List<FavouriteShop> getFavouriteShops(
            Integer userID,
            String sortBy,
            Integer limit, Integer offset
    ) {



        String queryJoin = "SELECT "

                + Shop.TABLE_NAME + "." + Shop.LOGO_IMAGE_PATH + ","
                + Shop.TABLE_NAME + "." + Shop.SHOP_NAME + ","
                + FavouriteShop.TABLE_NAME + "." + FavouriteShop.SHOP_ID + " "

                + " FROM " + FavouriteShop.TABLE_NAME
                + " INNER JOIN " + Shop.TABLE_NAME + " ON ( " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + " = " + FavouriteShop.TABLE_NAME + "."  + FavouriteShop.SHOP_ID + " ) "
                + " WHERE TRUE ";


//
//        if(shopID != null)
//        {
//            queryJoin = queryJoin + " AND " + FavouriteShop.TABLE_NAME + "." + FavouriteShop.SHOP_ID + " = " + shopID;
//        }




        if(userID!=null){

            queryJoin = queryJoin + " AND " + FavouriteShop.TABLE_NAME + "." + FavouriteShop.END_USER_ID + " = " + userID;;
        }





        // Applying filters

        if(sortBy!=null)
        {
            if(!sortBy.equals(""))
            {
                String queryPartSortBy = " ORDER BY " + sortBy;

                queryJoin = queryJoin + queryPartSortBy;
            }
        }





        if(limit != null)
        {
            queryJoin = queryJoin + " LIMIT " + limit + " " + " OFFSET " + offset;;
        }







        ArrayList<FavouriteShop> favouriteShopsList = new ArrayList<>();


        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {


            conn = dataSource.getConnection();
            stmt = conn.createStatement();

            rs = stmt.executeQuery(queryJoin);



            while(rs.next())
            {
                FavouriteShop favouriteShop = new FavouriteShop();
                favouriteShop.setShopID(rs.getInt(FavouriteShop.SHOP_ID));

                Shop shop = new Shop();
                shop.setLogoImagePath(rs.getString(Shop.LOGO_IMAGE_PATH));
                shop.setShopName(rs.getString(Shop.SHOP_NAME));

                favouriteShop.setShopProfile(shop);


                favouriteShopsList.add(favouriteShop);
            }



        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        finally

        {

            try {
                if(rs!=null)
                {rs.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {

                if(stmt!=null)
                {stmt.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {

                if(conn!=null)
                {conn.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return favouriteShopsList;
    }



    public boolean checkFavourite(
            int shopID,
            int endUserID
    )
    {



        String queryJoin = "SELECT "
                + FavouriteShop.TABLE_NAME + "." + FavouriteShop.SHOP_ID + ","
                + FavouriteShop.TABLE_NAME + "." + FavouriteShop.END_USER_ID + ""
                + " FROM " + FavouriteShop.TABLE_NAME
                + " WHERE " + FavouriteShop.SHOP_ID + " = " + shopID
                + " AND " + FavouriteShop.END_USER_ID + " = " + endUserID;




        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        try {


            connection = dataSource.getConnection();
            statement = connection.createStatement();

            rs = statement.executeQuery(queryJoin);



            if(rs.next())
            {
                return true;
            }



        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        finally

        {

            try {
                if(rs!=null)
                {rs.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {

                if(statement!=null)
                {statement.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {

                if(connection!=null)
                {connection.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return false;
    }



}
