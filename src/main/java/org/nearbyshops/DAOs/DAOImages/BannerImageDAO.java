package org.nearbyshops.DAOs.DAOImages;

import org.nearbyshops.Model.ItemCategory;
import org.nearbyshops.Model.ModelImages.BannerImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class BannerImageDAO {


    @Autowired
    DataSource dataSource;

    
    public int saveBannerImage(BannerImage shopImage, boolean getRowCount)
    {


        Connection connection = null;
        PreparedStatement statement = null;
        int idOfInsertedRow = -1;
        int rowCount = 0;


        String insertItemCategory = "INSERT INTO "
                + BannerImage.TABLE_NAME
                + "("
                + BannerImage.SHOP_ID + ","
                + BannerImage.IMAGE_FILENAME + ","

                + BannerImage.SORT_ORDER + ","
                + BannerImage.SHOP_TO_OPEN + ","
                + BannerImage.ITEM_TO_OPEN + ","

                + BannerImage.SHOW_IN_FRONT_SCREEN + ","
                + BannerImage.SHOW_IN_ITEMS_SCREEN + ","
                + BannerImage.SHOW_IN_SHOP_HOME + ""

                + ") VALUES(?,? ,?,?,? ,?,?,?)";




        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(insertItemCategory,PreparedStatement.RETURN_GENERATED_KEYS);

//            System.out.println("Shop ID : " + shopImage.getShopID());

            int i = 0;


            if(shopImage.getShopID()==0)
            {
                statement.setObject(++i,null);
            }
            else {
                statement.setObject(++i,shopImage.getShopID());
            }

            statement.setString(++i,shopImage.getImageFilename());
            statement.setInt(++i,shopImage.getSortOrder());

            statement.setInt(++i,shopImage.getShopIdToOpen());
            statement.setInt(++i,shopImage.getItemIDToOpen());

            statement.setBoolean(++i,shopImage.isShowInFrontScreen());
            statement.setBoolean(++i,shopImage.isShowInItemsScreen());
            statement.setBoolean(++i,shopImage.isShowInShopHome());

            rowCount = statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();

            if(rs.next())
            {
                idOfInsertedRow = rs.getInt(1);

                shopImage.setBannerImageID(idOfInsertedRow);
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

                if(connection!=null)
                {connection.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


        if(getRowCount)
        {
            return rowCount;
        }
        else
        {
            return idOfInsertedRow;
        }

    }



    public int updateBannerImage(BannerImage bannerImage)
    {

        String updateStatement = "UPDATE " + BannerImage.TABLE_NAME

                + " SET "

                + BannerImage.SHOP_ID + " = ?,"
                + BannerImage.IMAGE_FILENAME + " = ?,"

                + BannerImage.SORT_ORDER + " = ?,"
                + BannerImage.SHOP_TO_OPEN + " = ?,"
                + BannerImage.ITEM_TO_OPEN + " = ?,"

                + BannerImage.SHOW_IN_FRONT_SCREEN + " = ?,"
                + BannerImage.SHOW_IN_ITEMS_SCREEN + " = ?,"
                + BannerImage.SHOW_IN_SHOP_HOME + " = ?"

                + " WHERE " + BannerImage.BANNER_IMAGE_ID + " =?";


        Connection connection = null;
        PreparedStatement statement = null;

        int rowCountUpdated = 0;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);



            int i = 0;

            if(bannerImage.getShopID()==0)
            {
                statement.setObject(++i,null);
            }
            else {
                statement.setObject(++i,bannerImage.getShopID());
            }


            statement.setString(++i,bannerImage.getImageFilename());

            statement.setInt(++i,bannerImage.getSortOrder());
            statement.setInt(++i,bannerImage.getShopIdToOpen());
            statement.setInt(++i,bannerImage.getItemIDToOpen());

            statement.setBoolean(++i,bannerImage.isShowInFrontScreen());
            statement.setBoolean(++i,bannerImage.isShowInItemsScreen());
            statement.setBoolean(++i,bannerImage.isShowInShopHome());

            statement.setInt(++i,bannerImage.getBannerImageID());

            rowCountUpdated = statement.executeUpdate();


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

        return rowCountUpdated;
    }





    public int deleteBannerImage(int imageID)
    {

        String deleteStatement = "DELETE FROM " + BannerImage.TABLE_NAME
                                + " WHERE " + BannerImage.BANNER_IMAGE_ID + " = ?";

        Connection connection= null;
        PreparedStatement statement = null;
        int rowCountDeleted = 0;
        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(deleteStatement);
            statement.setInt(1,imageID);

            rowCountDeleted = statement.executeUpdate();

//            System.out.println("Rows Deleted: " + rowCountDeleted);

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




    public BannerImage getImageFilenameForBannerID(
            Integer bannerID
    ) {


        String query = "";


        query = "SELECT DISTINCT "

                + BannerImage.TABLE_NAME + "." + BannerImage.BANNER_IMAGE_ID + ","
                + BannerImage.TABLE_NAME + "." + BannerImage.SHOP_ID + ","
                + BannerImage.TABLE_NAME + "." + BannerImage.IMAGE_FILENAME + ""

                + " FROM " + BannerImage.TABLE_NAME
                + " WHERE " + BannerImage.TABLE_NAME + "." + BannerImage.BANNER_IMAGE_ID + " = ? ";



        BannerImage bannerImage = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(query);

            int i = 0;
            statement.setObject(++i,bannerID);


//            System.out.println(query);
            rs = statement.executeQuery();

            while(rs.next())
            {
                bannerImage = new BannerImage();

                bannerImage.setBannerImageID(rs.getInt(BannerImage.BANNER_IMAGE_ID));
                bannerImage.setShopID(rs.getInt(BannerImage.SHOP_ID));
                bannerImage.setImageFilename(rs.getString(BannerImage.IMAGE_FILENAME));
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

        return bannerImage;
    }





    public BannerImage getBannerImageDetails(int bannerImageID)
    {


        String queryJoinItems = "SELECT "

                + BannerImage.TABLE_NAME + "." + BannerImage.BANNER_IMAGE_ID + ","
                + BannerImage.TABLE_NAME + "." + BannerImage.IMAGE_FILENAME + ","

                + BannerImage.TABLE_NAME + "." + BannerImage.SORT_ORDER + ","
                + BannerImage.TABLE_NAME + "." + BannerImage.SHOP_TO_OPEN + ","
                + BannerImage.TABLE_NAME + "." + BannerImage.ITEM_TO_OPEN + ","

                + BannerImage.TABLE_NAME + "." + BannerImage.SHOW_IN_FRONT_SCREEN + ","
                + BannerImage.TABLE_NAME + "." + BannerImage.SHOW_IN_ITEMS_SCREEN + ","
                + BannerImage.TABLE_NAME + "." + BannerImage.SHOW_IN_SHOP_HOME + ""

                + " FROM " + BannerImage.TABLE_NAME
                + " WHERE " + BannerImage.TABLE_NAME + "." + BannerImage.BANNER_IMAGE_ID + " = " + bannerImageID;




        // all the non-aggregate columns which are present in select must be present in group by also.
        queryJoinItems = queryJoinItems

                + " group by "
                + BannerImage.TABLE_NAME + "." + BannerImage.BANNER_IMAGE_ID ;





        BannerImage bannerImage = null;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;


        try {

            connection = dataSource.getConnection();

            int i = 0;

            statement = connection.prepareStatement(queryJoinItems);


            rs = statement.executeQuery();

            while (rs.next()) {

                bannerImage = new BannerImage();

                bannerImage.setBannerImageID(rs.getInt(BannerImage.BANNER_IMAGE_ID));
                bannerImage.setImageFilename(rs.getString(BannerImage.IMAGE_FILENAME));

                bannerImage.setSortOrder(rs.getInt(BannerImage.SORT_ORDER));
                bannerImage.setShopIdToOpen(rs.getInt(BannerImage.SHOP_TO_OPEN));
                bannerImage.setItemIDToOpen(rs.getInt(BannerImage.ITEM_TO_OPEN));


                bannerImage.setShowInFrontScreen(rs.getBoolean(BannerImage.SHOW_IN_FRONT_SCREEN));
                bannerImage.setShowInItemsScreen(rs.getBoolean(BannerImage.SHOW_IN_ITEMS_SCREEN));
                bannerImage.setShowInShopHome(rs.getBoolean(BannerImage.SHOW_IN_SHOP_HOME));

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



        return bannerImage;
    }




    public List<BannerImage> getBannerImages(
            Integer shopID,
            String sortBy,
            boolean getFrontScreenImages,
            boolean getItemScreenImages
    ) {


        String queryJoin = "SELECT "

                + BannerImage.TABLE_NAME + "." + BannerImage.BANNER_IMAGE_ID + ","
                + BannerImage.TABLE_NAME + "." + BannerImage.IMAGE_FILENAME + ","

                + BannerImage.TABLE_NAME + "." + BannerImage.SORT_ORDER + ","
                + BannerImage.TABLE_NAME + "." + BannerImage.SHOP_TO_OPEN + ","
                + BannerImage.TABLE_NAME + "." + BannerImage.ITEM_TO_OPEN + ","

                + BannerImage.TABLE_NAME + "." + BannerImage.SHOW_IN_FRONT_SCREEN + ","
                + BannerImage.TABLE_NAME + "." + BannerImage.SHOW_IN_ITEMS_SCREEN + ","
                + BannerImage.TABLE_NAME + "." + BannerImage.SHOW_IN_SHOP_HOME + ""

                + " FROM " + BannerImage.TABLE_NAME
                + " WHERE TRUE ";



        if(shopID!=null)
        {
            queryJoin = queryJoin + " AND " + BannerImage.SHOP_ID + " = " + shopID;
        }

        if(getFrontScreenImages)
        {
            queryJoin = queryJoin + " AND " + BannerImage.SHOW_IN_FRONT_SCREEN + " = TRUE ";
        }



        if(getItemScreenImages)
        {
            queryJoin = queryJoin + " AND " + BannerImage.SHOW_IN_ITEMS_SCREEN + " = TRUE ";
        }



        if(sortBy!=null)
        {
            if(!sortBy.equals(""))
            {
                String queryPartSortBy = " ORDER BY " + sortBy;

                queryJoin = queryJoin + queryPartSortBy;
            }
        }




        ArrayList<BannerImage> bannerImagesList = new ArrayList<>();
        Connection connection = null;


        PreparedStatement statement = null;
        ResultSet rs = null;


        try {

            connection = dataSource.getConnection();

            int i = 0;

            statement = connection.prepareStatement(queryJoin);


            if(shopID!=null)
            {
                statement.setObject(++i, shopID);
            }




            rs = statement.executeQuery();

            while (rs.next()) {

                BannerImage bannerImage = new BannerImage();

                bannerImage.setBannerImageID(rs.getInt(BannerImage.BANNER_IMAGE_ID));
                bannerImage.setImageFilename(rs.getString(BannerImage.IMAGE_FILENAME));

                bannerImage.setSortOrder(rs.getInt(BannerImage.SORT_ORDER));
                bannerImage.setShopIdToOpen(rs.getInt(BannerImage.SHOP_TO_OPEN));
                bannerImage.setItemIDToOpen(rs.getInt(BannerImage.ITEM_TO_OPEN));

                bannerImage.setShowInFrontScreen(rs.getBoolean(BannerImage.SHOW_IN_FRONT_SCREEN));
                bannerImage.setShowInItemsScreen(rs.getBoolean(BannerImage.SHOW_IN_ITEMS_SCREEN));
                bannerImage.setShowInShopHome(rs.getBoolean(BannerImage.SHOW_IN_SHOP_HOME));


                bannerImagesList.add(bannerImage);
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

        return bannerImagesList;
    }

}
