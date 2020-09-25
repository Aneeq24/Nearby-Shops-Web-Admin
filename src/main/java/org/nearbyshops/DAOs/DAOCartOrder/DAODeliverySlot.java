package org.nearbyshops.DAOs.DAOCartOrder;

import org.nearbyshops.Model.ModelDelivery.DeliverySlot;
import org.nearbyshops.Model.ModelEndpoint.DeliverySlotEndpoint;
import org.nearbyshops.Model.Order;
import org.nearbyshops.Model.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;


@Component
public class DAODeliverySlot {



    @Autowired
    DataSource dataSource;


    public int saveDeliverySlot(DeliverySlot slot, boolean getRowCount)
    {

        Connection connection = null;
        PreparedStatement statement = null;
        int idOfInsertedRow = -1;
        int rowCountItems = 0;


        String insertItemCategory = "INSERT INTO "
                + DeliverySlot.TABLE_NAME
                + "("
                + DeliverySlot.IS_ENABLED + ","
                + DeliverySlot.IS_DELIVERY_SLOT + ","
                + DeliverySlot.IS_PICKUP_SLOT + ","
                + DeliverySlot.SLOT_NAME + ","

                + DeliverySlot.SLOT_START_TIME + ","
                + DeliverySlot.DURATION_IN_HOURS + ","
                + DeliverySlot.MAX_ORDERS_PER_DAY + ","
                + DeliverySlot.SHOP_ID + ""
                + ") VALUES(?,?,?,? ,?,?,?,?) ";



        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(insertItemCategory,PreparedStatement.RETURN_GENERATED_KEYS);

            int i = 0;

            statement.setBoolean(++i,slot.isEnabled());
            statement.setBoolean(++i,slot.isDeliverySlot());
            statement.setBoolean(++i,slot.isPickupSlot());
            statement.setString(++i,slot.getSlotName());

            statement.setTime(++i,slot.getSlotTime());
            statement.setInt(++i,slot.getDurationInHours());
            statement.setInt(++i,slot.getMaxOrdersPerDay());

            if(slot.getShopID()==0)
            {
                statement.setObject(++i,null);
            }
            else
            {
                statement.setInt(++i,slot.getShopID());
            }



            rowCountItems = statement.executeUpdate();

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


    public int updateDeliverySlot(DeliverySlot slot)
    {

        String updateStatement = "UPDATE " + DeliverySlot.TABLE_NAME

                + " SET "

                + DeliverySlot.IS_ENABLED + "=?,"
                + DeliverySlot.IS_DELIVERY_SLOT + "=?,"
                + DeliverySlot.IS_PICKUP_SLOT + "=?,"
                + DeliverySlot.SLOT_NAME + "=?,"

                + DeliverySlot.SLOT_START_TIME + "=?,"
                + DeliverySlot.DURATION_IN_HOURS + "=?,"
                + DeliverySlot.MAX_ORDERS_PER_DAY + "=?,"
                + DeliverySlot.SHOP_ID + "=?"

                + " WHERE " + DeliverySlot.SLOT_ID + " = ?";


        Connection connection = null;
        PreparedStatement statement = null;

        int rowCountUpdated = 0;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);

            int i = 0;

            statement.setBoolean(++i,slot.isEnabled());
            statement.setBoolean(++i,slot.isDeliverySlot());
            statement.setBoolean(++i,slot.isPickupSlot());
            statement.setString(++i,slot.getSlotName());

            statement.setTime(++i,slot.getSlotTime());
            statement.setInt(++i,slot.getDurationInHours());
            statement.setInt(++i,slot.getMaxOrdersPerDay());


            if(slot.getShopID()==0)
            {
                statement.setObject(++i,null);
            }
            else
            {
                statement.setInt(++i,slot.getShopID());
            }


            statement.setInt(++i,slot.getSlotID());


            rowCountUpdated = statement.executeUpdate();
//			System.out.println("Total rows updated: " + rowCountUpdated);


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



    public int enableSlot(int slotID, boolean isEnabled)
    {

        String updateStatement = "UPDATE " + DeliverySlot.TABLE_NAME
                + " SET " + DeliverySlot.IS_ENABLED + "=?"
                + " WHERE " + DeliverySlot.SLOT_ID + " = ?";


        Connection connection = null;
        PreparedStatement statement = null;

        int rowCountUpdated = 0;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);

            int i = 0;

            statement.setBoolean(++i,isEnabled);
            statement.setInt(++i,slotID);

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



    public int deleteItem(int slotID)
    {
        String deleteStatement = "DELETE FROM " + DeliverySlot.TABLE_NAME + " WHERE " + DeliverySlot.SLOT_ID + " = ?";

        Connection connection= null;
        PreparedStatement statement = null;
        int rowCountDeleted = 0;
        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(deleteStatement);
            statement.setInt(1,slotID);

            rowCountDeleted = statement.executeUpdate();

//			System.out.println("Rows Deleted: " + rowCountDeleted);

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





    public DeliverySlotEndpoint getDeliverySlots(
            Integer shopID,
            boolean isShopNull,
            String searchString,
            String sortBy,
            Integer limit, int offset,
            boolean getRowCount,
            boolean getOnlyMetadata
    )
    {

        String queryCount = "";


        String queryJoin = "SELECT "

                + DeliverySlot.TABLE_NAME + "." + DeliverySlot.SLOT_ID + ","
                + DeliverySlot.TABLE_NAME + "." + DeliverySlot.IS_ENABLED + ","
                + DeliverySlot.TABLE_NAME + "." + DeliverySlot.IS_PICKUP_SLOT + ","
                + DeliverySlot.TABLE_NAME + "." + DeliverySlot.IS_DELIVERY_SLOT + ","
                + DeliverySlot.TABLE_NAME + "." + DeliverySlot.SLOT_NAME + ","
                + DeliverySlot.TABLE_NAME + "." + DeliverySlot.SLOT_START_TIME + ","
                + DeliverySlot.TABLE_NAME + "." + DeliverySlot.DURATION_IN_HOURS + ","
                + DeliverySlot.TABLE_NAME + "." + DeliverySlot.MAX_ORDERS_PER_DAY + ","
                + DeliverySlot.TABLE_NAME + "." + DeliverySlot.SHOP_ID + ""

                + " FROM " + DeliverySlot.TABLE_NAME
                + " LEFT OUTER JOIN " + Shop.TABLE_NAME + " ON ( " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + " = " + DeliverySlot.TABLE_NAME + "." + DeliverySlot.SHOP_ID + " ) "
                + " WHERE TRUE ";


//        if(shopID!=null)
//        {
//            queryJoin = queryJoin + " AND " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + " = ? ";
//        }


        if(shopID != null)
        {
            queryJoin = queryJoin + " AND " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + " = " + shopID;
        }



        if(isShopNull)
        {
            queryJoin = queryJoin + " AND " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + " IS NULL ";
        }



        if(searchString !=null)
        {
            String queryPartSearch = DeliverySlot.TABLE_NAME + "." + DeliverySlot.SLOT_NAME +" ilike '%" + searchString + "%'";
            queryJoin = queryJoin + " AND " + queryPartSearch;
        }



        // all the non-aggregate columns which are present in select must be present in group by also.
        queryJoin = queryJoin
                + " group by "
                + DeliverySlot.TABLE_NAME + "." + DeliverySlot.SLOT_ID + ","
                + Shop.TABLE_NAME + "." + Shop.SHOP_ID;



        queryCount = queryJoin;



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






        queryCount = "SELECT COUNT(*) as item_count FROM (" + queryCount + ") AS temp";




        DeliverySlotEndpoint endPoint = new DeliverySlotEndpoint();

        ArrayList<DeliverySlot> slotList = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;


        try {

            connection = dataSource.getConnection();

            int i = 0;


            if(!getOnlyMetadata) {


                statement = connection.prepareStatement(queryJoin);




                rs = statement.executeQuery();

                while (rs.next()) {

                    DeliverySlot slot = new DeliverySlot();

                    slot.setSlotID(rs.getInt(DeliverySlot.SLOT_ID));
                    slot.setEnabled(rs.getBoolean(DeliverySlot.IS_ENABLED));
                    slot.setPickupSlot(rs.getBoolean(DeliverySlot.IS_PICKUP_SLOT));
                    slot.setDeliverySlot(rs.getBoolean(DeliverySlot.IS_DELIVERY_SLOT));

                    slot.setSlotName(rs.getString(DeliverySlot.SLOT_NAME));
                    slot.setSlotTime(rs.getTime(DeliverySlot.SLOT_START_TIME));
                    slot.setDurationInHours(rs.getInt(DeliverySlot.DURATION_IN_HOURS));

                    slot.setMaxOrdersPerDay(rs.getInt(DeliverySlot.MAX_ORDERS_PER_DAY));
                    slot.setShopID(rs.getInt(DeliverySlot.SHOP_ID));

                    slotList.add(slot);
                }


                endPoint.setResults(slotList);
            }



            if(getRowCount)
            {
                statement = connection.prepareStatement(queryCount);

                rs = statement.executeQuery();

                while(rs.next())
                {

                    endPoint.setItemCount(rs.getInt("item_count"));
//					System.out.println("Item Count ItemDAO : " + String.valueOf(endPoint.getItemCount()));
                }
            }



//			System.out.println("Item By CategoryID " + itemList.size());

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

        return endPoint;
    }





    public DeliverySlotEndpoint getAvailableSlots(
            Integer shopID,
            Date deliveryDate,
            String sortBy,
            boolean getRowCount,
            boolean getOnlyMetadata
    )
    {

        String queryCount = "";


        String queryJoin = "SELECT "

                + DeliverySlot.TABLE_NAME + "." + DeliverySlot.SLOT_ID + ","
                + DeliverySlot.TABLE_NAME + "." + DeliverySlot.SLOT_NAME + ","
                + DeliverySlot.TABLE_NAME + "." + DeliverySlot.SLOT_START_TIME + ","
                + DeliverySlot.TABLE_NAME + "." + DeliverySlot.DURATION_IN_HOURS + ","
                + " count ( " + Order.TABLE_NAME + "." + Order.ORDER_ID + " ) as order_count "

                + " FROM " + DeliverySlot.TABLE_NAME
                + " LEFT OUTER JOIN " + Shop.TABLE_NAME + " ON ( " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + " = " + DeliverySlot.TABLE_NAME + "." + DeliverySlot.SHOP_ID + " ) "
                + " LEFT OUTER JOIN " + Order.TABLE_NAME + " ON ( " + Order.TABLE_NAME + "." + Order.DELIVERY_SLOT + " = " + DeliverySlot.TABLE_NAME + "." + DeliverySlot.SLOT_ID + " ) "
                + " WHERE TRUE ";



        if(deliveryDate!=null)
        {
            queryJoin = queryJoin + " AND " + Order.TABLE_NAME + "." + Order.DELIVERY_DATE + " = ? ";
        }




        if(shopID != null)
        {
            queryJoin = queryJoin + " AND " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + " = " + shopID;
        }
        else
        {
            queryJoin = queryJoin + " AND " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + " IS NULL ";
        }





        // all the non-aggregate columns which are present in select must be present in group by also.
        queryJoin = queryJoin
                + " group by "
                + DeliverySlot.TABLE_NAME + "." + DeliverySlot.SLOT_ID;





        // all the non-aggregate columns which are present in select must be present in group by also.
        queryJoin = queryJoin
                + " having "
                + " count ( " + Order.TABLE_NAME + "." + Order.ORDER_ID + " ) < " + DeliverySlot.MAX_ORDERS_PER_DAY;




        queryCount = queryJoin;



        if(sortBy!=null)
        {
            if(!sortBy.equals(""))
            {
                String queryPartSortBy = " ORDER BY " + sortBy;


                queryJoin = queryJoin + queryPartSortBy;
            }
        }






        queryCount = "SELECT COUNT(*) as item_count FROM (" + queryCount + ") AS temp";




        DeliverySlotEndpoint endPoint = new DeliverySlotEndpoint();

        ArrayList<DeliverySlot> slotList = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;


        try {

            connection = dataSource.getConnection();

            int i = 0;


            if(!getOnlyMetadata) {


                statement = connection.prepareStatement(queryJoin);


                if(deliveryDate!=null)
                {
                    statement.setObject(++i,deliveryDate);
                }


                rs = statement.executeQuery();

                while (rs.next()) {

                    DeliverySlot slot = new DeliverySlot();

                    slot.setSlotID(rs.getInt(DeliverySlot.SLOT_ID));

                    slot.setSlotName(rs.getString(DeliverySlot.SLOT_NAME));
                    slot.setSlotTime(rs.getTime(DeliverySlot.SLOT_START_TIME));
                    slot.setDurationInHours(rs.getInt(DeliverySlot.DURATION_IN_HOURS));

                    System.out.println("Order Count : " + String.valueOf(rs.getInt("order_count")));

                    slotList.add(slot);
                }


                endPoint.setResults(slotList);
            }



            if(getRowCount)
            {
                statement = connection.prepareStatement(queryCount);

                i = 0;

                if(deliveryDate!=null)
                {
                    statement.setObject(++i,deliveryDate);
                }



                rs = statement.executeQuery();

                while(rs.next())
                {

                    endPoint.setItemCount(rs.getInt("item_count"));
                }
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

        return endPoint;
    }






    public DeliverySlotEndpoint getAvailableSlotsBackup(
            Integer shopID,
            boolean isPickupSlot,
            boolean isDeliverySlot,
            Date deliveryDate,
            String sortBy,
            boolean getRowCount,
            boolean getOnlyMetadata
    )
    {

        String queryCount = "";



        String queryJoin = "SELECT "
                + DeliverySlot.TABLE_NAME + "." + DeliverySlot.SLOT_ID + ","
                + DeliverySlot.TABLE_NAME + "." + DeliverySlot.SLOT_NAME + ","
                + DeliverySlot.TABLE_NAME + "." + DeliverySlot.SLOT_START_TIME + ","
                + DeliverySlot.TABLE_NAME + "." + DeliverySlot.DURATION_IN_HOURS + ""
                + " FROM " + DeliverySlot.TABLE_NAME
                + " LEFT OUTER JOIN " + Shop.TABLE_NAME + " ON ( " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + " = " + DeliverySlot.TABLE_NAME + "." + DeliverySlot.SHOP_ID + " ) "
                + " WHERE  " + DeliverySlot.TABLE_NAME + "." + DeliverySlot.IS_ENABLED + " = TRUE ";



        if(deliveryDate!=null)
        {
            queryJoin = queryJoin + " AND " + DeliverySlot.MAX_ORDERS_PER_DAY  + " > "
                    + " ( "
                    + " SELECT " + " count ( " + Order.TABLE_NAME + "." + Order.ORDER_ID + " ) "
                    + " FROM " + Order.TABLE_NAME
                    + " WHERE ( " + Order.TABLE_NAME + "." + Order.DELIVERY_SLOT + " = " + DeliverySlot.TABLE_NAME + "." + DeliverySlot.SLOT_ID + " ) "
                    + " AND " + Order.TABLE_NAME + "." + Order.DELIVERY_DATE + " = ? "
                    + " ) ";
        }



        if(isPickupSlot)
        {
            queryJoin = queryJoin + " AND " + DeliverySlot.TABLE_NAME + "." + DeliverySlot.IS_PICKUP_SLOT + " = TRUE ";
        }



        if(isDeliverySlot)
        {
            queryJoin = queryJoin + " AND " + DeliverySlot.TABLE_NAME + "." + DeliverySlot.IS_DELIVERY_SLOT + " = TRUE ";
        }



        if(shopID != null)
        {
            queryJoin = queryJoin + " AND " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + " = " + shopID;
        }
        else
        {
            queryJoin = queryJoin + " AND " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + " IS NULL ";
        }





        // all the non-aggregate columns which are present in select must be present in group by also.
        queryJoin = queryJoin
                + " group by "
                + DeliverySlot.TABLE_NAME + "." + DeliverySlot.SLOT_ID;





        // all the non-aggregate columns which are present in select must be present in group by also.
//        queryJoin = queryJoin
//                + " having "
//                + " count ( " + Order.TABLE_NAME + "." + Order.ORDER_ID + " ) < " + DeliverySlot.MAX_ORDERS_PER_DAY;




        queryCount = queryJoin;



        if(sortBy!=null)
        {
            if(!sortBy.equals(""))
            {
                String queryPartSortBy = " ORDER BY " + sortBy;


                queryJoin = queryJoin + queryPartSortBy;
            }
        }






        queryCount = "SELECT COUNT(*) as item_count FROM (" + queryCount + ") AS temp";




        DeliverySlotEndpoint endPoint = new DeliverySlotEndpoint();

        ArrayList<DeliverySlot> slotList = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;


        try {

            connection = dataSource.getConnection();

            int i = 0;


            if(!getOnlyMetadata) {


                statement = connection.prepareStatement(queryJoin);


                if(deliveryDate!=null)
                {
                    statement.setObject(++i,deliveryDate);
                }


                rs = statement.executeQuery();

                while (rs.next()) {

                    DeliverySlot slot = new DeliverySlot();

                    slot.setSlotID(rs.getInt(DeliverySlot.SLOT_ID));

                    slot.setSlotName(rs.getString(DeliverySlot.SLOT_NAME));
                    slot.setSlotTime(rs.getTime(DeliverySlot.SLOT_START_TIME));
                    slot.setDurationInHours(rs.getInt(DeliverySlot.DURATION_IN_HOURS));

//                    System.out.println("Order Count : " + String.valueOf(rs.getInt("order_count")));

                    slotList.add(slot);
                }


                endPoint.setResults(slotList);
            }



            if(getRowCount)
            {
                statement = connection.prepareStatement(queryCount);

                i = 0;

                if(deliveryDate!=null)
                {
                    statement.setObject(++i,deliveryDate);
                }



                rs = statement.executeQuery();

                while(rs.next())
                {

                    endPoint.setItemCount(rs.getInt("item_count"));
                }
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

        return endPoint;
    }









    public void createSampleSlots(int shopID)
    {
        DeliverySlot morningSlot = new DeliverySlot(true,true,true,"Morning",50,shopID,new Time(6,0,0),3,1);
        DeliverySlot afternoonSlot = new DeliverySlot(true,true,true,"Afternoon",50,shopID,new Time(13,0,0),3,1);
        DeliverySlot eveningSlot = new DeliverySlot(true,true,true,"Evening",50,shopID,new Time(18,0,0),3,1);

        saveDeliverySlot(morningSlot,false);
        saveDeliverySlot(afternoonSlot,false);
        saveDeliverySlot(eveningSlot,false);
    }

}
