package org.nearbyshops.DAOs.DAOReviewItem;


import org.nearbyshops.Model.ModelReviewItem.FavouriteItem;
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
public class FavoriteItemDAOPrepared {



    @Autowired
    DataSource dataSource;




    public int saveFavouriteItem(FavouriteItem favouriteItem)
    {


        Connection conn = null;
        PreparedStatement statement = null;
        int idOfInsertedRow = 0;


        String insertStatement = "INSERT INTO "
                + FavouriteItem.TABLE_NAME
                + "("
                + FavouriteItem.ITEM_ID + ","
                + FavouriteItem.END_USER_ID
                + ") VALUES(?,?)";

        try {

            conn = dataSource.getConnection();
            statement = conn.prepareStatement(insertStatement,PreparedStatement.RETURN_GENERATED_KEYS);

            statement.setInt(1,favouriteItem.getItemID());
            statement.setInt(2,favouriteItem.getEndUserID());

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



    public int deleteFavouriteItem(int itemID, int endUserID)
    {

        String deleteStatement = "DELETE FROM " + FavouriteItem.TABLE_NAME
                + " WHERE " + FavouriteItem.ITEM_ID + " = ?"
                + " AND " + FavouriteItem.END_USER_ID + " = ?";

        Connection connection= null;
        PreparedStatement statement = null;
        int rowCountDeleted = 0;
        try {


            connection = dataSource.getConnection();
            statement = connection.prepareStatement(deleteStatement);

            statement.setInt(1,itemID);
            statement.setInt(2,endUserID);

            rowCountDeleted = statement.executeUpdate();
//                System.out.println("Rows Deleted Favourite Item: " + rowCountDeleted);


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

                if(connection!=null)
                {connection.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return rowCountDeleted;
    }





    public List<FavouriteItem> getFavouriteItems(
            Integer itemID,
            Integer endUserID,
            String sortBy,
            Integer limit, int offset
    ) {



        String queryJoin = "SELECT "
                + FavouriteItem.TABLE_NAME + "." + FavouriteItem.ITEM_ID + ","
                + FavouriteItem.TABLE_NAME + "." + FavouriteItem.END_USER_ID + ""
                + " FROM " + FavouriteItem.TABLE_NAME
                + " WHERE TRUE ";




        if(itemID != null)
        {
            queryJoin = queryJoin + " AND "
                    + FavouriteItem.TABLE_NAME
                    + "."
                    + FavouriteItem.ITEM_ID + " = " + itemID;
        }



        if(endUserID!=null){

            queryJoin = queryJoin + " AND " + FavouriteItem.TABLE_NAME
                                    + "." + FavouriteItem.END_USER_ID + " = " + endUserID;
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




        if(limit!=null)
        {
            queryJoin  = queryJoin + " LIMIT " + limit + " " + " OFFSET " + offset;
        }







        ArrayList<FavouriteItem> itemsList = new ArrayList<FavouriteItem>();


        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        try {


            connection = dataSource.getConnection();
            statement = connection.createStatement();

            rs = statement.executeQuery(queryJoin);



            while(rs.next())
            {
                FavouriteItem item = new FavouriteItem();

                item.setEndUserID(rs.getInt(FavouriteItem.END_USER_ID));
                item.setItemID(rs.getInt(FavouriteItem.ITEM_ID));

                itemsList.add(item);
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

        return itemsList;
    }






    public boolean checkFavourite(
            int itemID,
            int endUserID
    )
    {



        String queryJoin = "SELECT "
                + FavouriteItem.TABLE_NAME + "." + FavouriteItem.ITEM_ID + ","
                + FavouriteItem.TABLE_NAME + "." + FavouriteItem.END_USER_ID + ""
                + " FROM " + FavouriteItem.TABLE_NAME
                + " WHERE " + FavouriteItem.ITEM_ID + " = " + itemID
                + " AND " + FavouriteItem.END_USER_ID + " = " + endUserID;




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
