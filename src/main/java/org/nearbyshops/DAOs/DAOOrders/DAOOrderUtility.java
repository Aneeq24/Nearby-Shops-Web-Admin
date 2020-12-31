package org.nearbyshops.DAOs.DAOOrders;

import org.nearbyshops.Model.ModelDelivery.DeliveryAddress;
import org.nearbyshops.Model.ModelEndpoint.ShopEndPoint;
import org.nearbyshops.Model.ModelEndpoint.UserEndpoint;
import org.nearbyshops.Model.ModelRoles.DeliveryGuyData;
import org.nearbyshops.Model.ModelRoles.User;
import org.nearbyshops.Model.Order;
import org.nearbyshops.Model.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

@Component
public class DAOOrderUtility {



    @Autowired
    DataSource dataSource;




    public int checkOrderStatus(int orderID)
    {

        return 0;
    }




    public Shop getShopDetailsForCreateOrder(int ShopID)
    {

        String query = " ";

        query = "SELECT "

                + Shop.TABLE_NAME + "." + Shop.DELIVERY_CHARGES + ","
                + Shop.TABLE_NAME + "." + Shop.BILL_AMOUNT_FOR_FREE_DELIVERY + ""

                + " FROM " + Shop.TABLE_NAME
                + " WHERE "	+ Shop.TABLE_NAME + "." + Shop.SHOP_ID + "= " + ShopID;



        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        Shop shop = null;
        try {

            connection = dataSource.getConnection();

            statement = connection.createStatement();

            rs = statement.executeQuery(query);


            while(rs.next())
            {
                shop = new Shop();
                shop.setDeliveryCharges(rs.getFloat(Shop.DELIVERY_CHARGES));
                shop.setBillAmountForFreeDelivery(rs.getInt(Shop.BILL_AMOUNT_FOR_FREE_DELIVERY));
            }


        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally

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

        return shop;
    }




    public Order getOrderDetails(int orderID)
    {

        String query = "SELECT "

                    + Order.TABLE_NAME + "." + Order.ORDER_ID + ","
                    + Order.TABLE_NAME + "." + Order.SHOP_ID + ","
                    + Order.TABLE_NAME + "." + Order.DATE_TIME_PLACED + ","
                    + Order.TABLE_NAME + "." + Order.ITEM_COUNT + ","

                    + Order.TABLE_NAME + "." + Order.APP_SERVICE_CHARGE + ","
                    + Order.TABLE_NAME + "." + Order.SAVINGS_OVER_MRP + ","
                    + Order.TABLE_NAME + "." + Order.DELIVERY_CHARGES + ","
                    + Order.TABLE_NAME + "." + Order.NET_PAYABLE + ","
                    + Order.TABLE_NAME + "." + Order.ITEM_TOTAL + ","

                    + Order.TABLE_NAME + "." + Order.STATUS_HOME_DELIVERY + ","
                    + Order.TABLE_NAME + "." + Order.STATUS_PICK_FROM_SHOP + ","

                    + Order.TABLE_NAME + "." + Order.DELIVERY_MODE + ","
                    + Order.TABLE_NAME + "." + Order.DELIVERY_DATE + ","

                    + Order.TABLE_NAME + "." + Order.PAYMENT_MODE + ","
                    + Order.TABLE_NAME + "." + Order.DELIVERY_OTP + ","
                    + Order.TABLE_NAME + "." + Order.END_USER_ID + ","
                    + Order.TABLE_NAME + "." + Order.DELIVERY_GUY_SELF_ID + ","


                    + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.NAME + ","
                    + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.LATITUDE + ","
                    + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.LONGITUDE + ","
                    + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.PHONE_NUMBER + ","
                    + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.DELIVERY_ADDRESS + ","
                    + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.CITY + ","
                    + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.PINCODE + ""

                    + " FROM " + Order.TABLE_NAME
                    + " LEFT OUTER JOIN " + DeliveryAddress.TABLE_NAME + " ON (" + Order.TABLE_NAME + "." + Order.DELIVERY_ADDRESS_ID + " = " + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.ID + ")"
                    + " WHERE " + Order.ORDER_ID + " = " + orderID;


        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        Order order = null;

        try {

            connection = dataSource.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(query);

            while(rs.next())
            {
                order = new Order();

                order.setOrderID(rs.getInt(Order.ORDER_ID));
                order.setShopID(rs.getInt(Order.SHOP_ID));
                order.setDateTimePlaced(rs.getTimestamp(Order.DATE_TIME_PLACED));
                order.setItemCount((Integer) rs.getObject(Order.ITEM_COUNT));

                order.setAppServiceCharge(rs.getDouble(Order.APP_SERVICE_CHARGE));
                order.setSavingsOverMRP(rs.getDouble(Order.SAVINGS_OVER_MRP));
                order.setDeliveryCharges(rs.getDouble(Order.DELIVERY_CHARGES));
                order.setItemTotal((Double) rs.getObject(Order.ITEM_TOTAL));
                order.setNetPayable(rs.getDouble(Order.NET_PAYABLE));

                order.setStatusHomeDelivery(rs.getInt(Order.STATUS_HOME_DELIVERY));
                order.setStatusPickFromShop(rs.getInt(Order.STATUS_PICK_FROM_SHOP));

                order.setDeliveryMode(rs.getInt(Order.DELIVERY_MODE));
                order.setDeliveryDate(rs.getDate(Order.DELIVERY_DATE));

                order.setPaymentMode(rs.getInt(Order.PAYMENT_MODE));
                order.setDeliveryOTP(rs.getInt(Order.DELIVERY_OTP));
                order.setEndUserID(rs.getInt(Order.END_USER_ID));
                order.setDeliveryGuySelfID(rs.getInt(Order.DELIVERY_GUY_SELF_ID));


                DeliveryAddress address = new DeliveryAddress();
                address.setName(rs.getString(DeliveryAddress.NAME));
                address.setLatitude(rs.getDouble(DeliveryAddress.LATITUDE));
                address.setLongitude(rs.getDouble(DeliveryAddress.LONGITUDE));
                address.setPhoneNumber(rs.getLong(DeliveryAddress.PHONE_NUMBER));
                address.setDeliveryAddress(rs.getString(DeliveryAddress.DELIVERY_ADDRESS));
                address.setCity(rs.getString(DeliveryAddress.CITY));
                address.setPincode(rs.getLong(DeliveryAddress.PINCODE));
                order.setDeliveryAddress(address);


            }


        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally

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

        return order;
    }




    public Order readOrderStatus(int orderID)
    {

        String query = "SELECT "

                + Order.DELIVERY_GUY_SELF_ID + ","
                + Order.SHOP_ID + ","
                + Order.END_USER_ID + ","
                + Order.DELIVERY_MODE + ","
                + Order.STATUS_HOME_DELIVERY + ","
                + Order.STATUS_PICK_FROM_SHOP + ""

                + " FROM " + Order.TABLE_NAME
                + " WHERE " + Order.ORDER_ID + " = " + orderID;

        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        Order order = null;

        try {

            connection = dataSource.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(query);

            while(rs.next())
            {
                order = new Order();


                order.setOrderID(orderID);
                order.setDeliveryGuySelfID(rs.getInt(Order.DELIVERY_GUY_SELF_ID));
                order.setShopID(rs.getInt(Order.SHOP_ID));
                order.setEndUserID(rs.getInt(Order.END_USER_ID));
                order.setDeliveryMode(rs.getInt(Order.DELIVERY_MODE));

                order.setStatusHomeDelivery(rs.getInt(Order.STATUS_HOME_DELIVERY));
                order.setStatusPickFromShop(rs.getInt(Order.STATUS_PICK_FROM_SHOP));
            }



        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally

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

        return order;
    }




    public int getOrderPaymentMode(int orderID)
    {

        String query = "SELECT " + Order.PAYMENT_MODE + ""
                    + " FROM " + Order.TABLE_NAME
                    + " WHERE " + Order.ORDER_ID + " = " + orderID;


        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;




        try {

            connection = dataSource.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(query);

            while(rs.next())
            {
                return rs.getInt(Order.PAYMENT_MODE);
            }



        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally

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


        return 0;
    }










    // enduserid, deliverydate, deliveryMode, deliveryslot, deliveryguy, shopid, homeDeliveryStatus, PFSStatus,

    // fetch delivery guys assigned to the orders in the given shop with given status
    public UserEndpoint fetchDeliveryGuys(
            Integer deliveryMode,
            Date deliveryDate,


            Integer statusHDLessThan,
            Integer homeDeliveryStatus,

            Integer shopID,
            Integer deliverySlot,
            String sortBy,
            boolean getRowCount,
            boolean getOnlyMetadata
    )
    {

        String queryCount = "";

        String query = "SELECT "

                + " count ( " + Order.TABLE_NAME + "." + Order.ORDER_ID + " ) as order_count ,"

                + User.TABLE_NAME + "." + User.USER_ID + ","
                + User.TABLE_NAME + "." + User.NAME + ","
                + User.TABLE_NAME + "." + User.PHONE + ","
                + User.TABLE_NAME + "." + User.PROFILE_IMAGE_URL + ""

                + " FROM " + Order.TABLE_NAME
                + " INNER JOIN " + User.TABLE_NAME + " ON (" + Order.TABLE_NAME + "." + Order.DELIVERY_GUY_SELF_ID + " = " + User.TABLE_NAME + "." + User.USER_ID + ")"
//                + " LEFT OUTER JOIN " + DeliverySlot.TABLE_NAME + " ON (" + Order.TABLE_NAME + "." + Order.DELIVERY_SLOT + " = " + DeliverySlot.TABLE_NAME + "." + DeliverySlot.SLOT_ID + ")"
                + " LEFT OUTER JOIN " + Shop.TABLE_NAME + " ON (" + Shop.TABLE_NAME + "." + Shop.SHOP_ID + " = " + Order.TABLE_NAME + "." + Order.SHOP_ID + ")"
                + " WHERE " + Order.DELIVERY_MODE + " IN ( " + Order.DELIVERY_MODE_HOME_DELIVERY + " )";
//        + " WHERE " + Order.DELIVERY_MODE + " IN ( " + Order.DELIVERY_MODE_DELIVERY_BY_MARKET + " , " + Order.DELIVERY_MODE_DELIVERY_BY_VENDOR + " )";




        if(shopID != null)
        {
            query = query + " AND " + Order.SHOP_ID + " = " + shopID;
        }



        if(deliverySlot != null)
        {
            query = query + " AND " + Order.DELIVERY_SLOT + " = " + deliverySlot;
        }



        if(deliveryMode != null)
        {
            query = query + " AND " + Order.DELIVERY_MODE + " = " + deliveryMode;
        }



        if(deliveryDate != null)
        {
            query = query + " AND " + Order.DELIVERY_DATE + " = ? ";
        }




        if(homeDeliveryStatus != null)
        {

            query = query + " AND " + Order.STATUS_HOME_DELIVERY + " = " + homeDeliveryStatus;
        }


        if(statusHDLessThan != null)
        {
            query = query + " AND " + Order.STATUS_HOME_DELIVERY + " <= " + statusHDLessThan;
        }





        // all the non-aggregate columns which are present in select must be present in group by also.
        query = query
                + " group by "
                + User.TABLE_NAME + "." + User.USER_ID ;


        queryCount = query;



        if(sortBy!=null)
        {
            if(!sortBy.equals(""))
            {
                String queryPartSortBy = " ORDER BY " + sortBy;

                query = query + queryPartSortBy;
            }
        }





        queryCount = "SELECT COUNT(*) as item_count FROM (" + queryCount + ") AS temp";




        UserEndpoint endPoint = new UserEndpoint();

        ArrayList<User> usersList = new ArrayList<>();
        Connection connection = null;


        PreparedStatement statement = null;
        ResultSet rs = null;



        try {

            connection = dataSource.getConnection();

            int i = 0;


            if(!getOnlyMetadata) {


                statement = connection.prepareStatement(query);

                if(deliveryDate!=null)
                {
                    statement.setObject(++i,deliveryDate);
                }


                rs = statement.executeQuery();

                while (rs.next()) {


                    User deliveryGuy = new User();
                    deliveryGuy.setUserID(rs.getInt(User.USER_ID));
                    deliveryGuy.setName(rs.getString(User.NAME));
                    deliveryGuy.setPhone(rs.getString(User.PHONE));
                    deliveryGuy.setProfileImagePath(rs.getString(User.PROFILE_IMAGE_URL));

                    deliveryGuy.setRt_order_count(rs.getInt("order_count"));


                    usersList.add(deliveryGuy);
                }


                endPoint.setResults(usersList);
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






    // fetch shops for the delivery inventory
    public ShopEndPoint fetchShops(
            Integer endUserID,

            Integer deliveryMode,
            Date deliveryDate,

            boolean areDeliveryOrders,

            Integer statusHDLessThan,
            Integer homeDeliveryStatus,
            double latCurrent,double lonCurrent,

            boolean deliveryGuyNull,
            Integer deliveryGuyID,
            Integer deliverySlot,
            String sortBy,
            boolean getRowCount,
            boolean getOnlyMetadata
    )
    {

        String queryCount = "";

        String query = "SELECT "


                + "6371 * acos( cos( radians("
                + latCurrent + ")) * cos( radians( " + Shop.LAT_CENTER + ") ) * cos(radians( " + Shop.LON_CENTER + " ) - radians("
                + lonCurrent + "))"
                + " + sin( radians(" + latCurrent + ")) * sin(radians( " + Shop.LAT_CENTER + " ))) as pickup_distance" + ","

                + " count ( " + Order.TABLE_NAME + "." + Order.ORDER_ID + " ) as order_count ,"

                + Shop.TABLE_NAME + "." + Shop.SHOP_ID + ","
                + Shop.TABLE_NAME + "." + Shop.SHOP_NAME + ","
                + Shop.TABLE_NAME + "." + Shop.LOGO_IMAGE_PATH + ""

                + " FROM " + Order.TABLE_NAME
                + " INNER JOIN " + Shop.TABLE_NAME + " ON (" + Shop.TABLE_NAME + "." + Shop.SHOP_ID + " = " + Order.TABLE_NAME + "." + Order.SHOP_ID + ")"
                + " LEFT OUTER JOIN " + User.TABLE_NAME + " ON (" + Order.TABLE_NAME + "." + Order.DELIVERY_GUY_SELF_ID + " = " + User.TABLE_NAME + "." + User.USER_ID + ")"
//                + " LEFT OUTER JOIN " + DeliverySlot.TABLE_NAME + " ON (" + Order.TABLE_NAME + "." + Order.DELIVERY_SLOT + " = " + DeliverySlot.TABLE_NAME + "." + DeliverySlot.SLOT_ID + ")"
                + " WHERE TRUE " ;



        if(areDeliveryOrders)
        {
            query = query + " AND " + Order.DELIVERY_MODE + " IN ( " + Order.DELIVERY_MODE_HOME_DELIVERY + " )";
//            query = query + " AND " + Order.DELIVERY_MODE + " IN ( " + Order.DELIVERY_MODE_DELIVERY_BY_MARKET + " , " + Order.DELIVERY_MODE_DELIVERY_BY_VENDOR + " )";
        }



        if(endUserID != null)
        {
            query = query + " AND " + Order.END_USER_ID + " = " + endUserID;
        }


        if(statusHDLessThan != null)
        {
            query = query + " AND " + Order.STATUS_HOME_DELIVERY + " <= " + statusHDLessThan;
        }



        if(deliverySlot != null)
        {
            query = query + " AND " + Order.DELIVERY_SLOT + " = " + deliverySlot;
        }



        if(deliveryMode != null)
        {
            query = query + " AND " + Order.DELIVERY_MODE + " = " + deliveryMode;
        }



        if(deliveryDate != null)
        {
            query = query + " AND " + Order.DELIVERY_DATE + " = ? ";
        }




        if(deliveryGuyID != null)
        {
            query = query + " AND " + User.USER_ID + " = " + deliveryGuyID;
        }



        if(deliveryGuyNull)
        {
            query = query + " AND " + Order.DELIVERY_GUY_SELF_ID + " IS NULL ";
        }



        if(homeDeliveryStatus != null)
        {

            query = query + " AND " + Order.STATUS_HOME_DELIVERY + " = " + homeDeliveryStatus;
        }





        // all the non-aggregate columns which are present in select must be present in group by also.
        query = query
                + " group by "
                + Shop.TABLE_NAME + "." + Shop.SHOP_ID ;


        queryCount = query;





        if(sortBy!=null)
        {
            if(!sortBy.equals(""))
            {
                String queryPartSortBy = " ORDER BY " + sortBy;

                query = query + queryPartSortBy;
            }
        }





        queryCount = "SELECT COUNT(*) as item_count FROM (" + queryCount + ") AS temp";




        ShopEndPoint endPoint = new ShopEndPoint();

        ArrayList<Shop> shopArrayList = new ArrayList<>();
        Connection connection = null;


        PreparedStatement statement = null;
        ResultSet rs = null;


        try {

            connection = dataSource.getConnection();

            int i = 0;


            if(!getOnlyMetadata) {



                statement = connection.prepareStatement(query);


                if(deliveryDate!=null)
                {
                    statement.setObject(++i,deliveryDate);
                }



                rs = statement.executeQuery();

                while (rs.next()) {


                    Shop shop = new Shop();
                    shop.setShopID(rs.getInt(Shop.SHOP_ID));
                    shop.setShopName(rs.getString(Shop.SHOP_NAME));
                    shop.setLogoImagePath(rs.getString(Shop.LOGO_IMAGE_PATH));
                    shop.setRt_distance(rs.getDouble("pickup_distance"));

                    shop.setRt_order_count(rs.getInt("order_count"));

                    shopArrayList.add(shop);
                }


                endPoint.setResults(shopArrayList);
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
//                    System.out.println("Item Count : " + String.valueOf(endPoint.getItemCount()));
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




//
//    // fetch shops for the delivery inventory
//    public DeliverySlotEndpoint fetchDeliverySlots(
//            Integer endUserID,
//
//            Integer deliveryMode,
//            Date deliveryDate,
//
//
//            boolean areDeliveryOrders,
//
//            Integer statusHDLessThan,
//            Integer homeDeliveryStatus,
//            Integer shopID,
//
//            boolean deliveryGuyNull,
//            Integer deliveryGuyID,
//
//            String sortBy,
//            boolean getRowCount,
//            boolean getOnlyMetadata
//    )
//    {
//
//        String queryCount = "";
//
//        String query = "SELECT "
//
//                + " count ( " + Order.TABLE_NAME + "." + Order.ORDER_ID + " ) as order_count ,"
//
//                + DeliverySlot.TABLE_NAME + "." + DeliverySlot.SLOT_ID + ","
//                + DeliverySlot.TABLE_NAME + "." + DeliverySlot.SLOT_NAME + ","
//                + DeliverySlot.TABLE_NAME + "." + DeliverySlot.SLOT_START_TIME + ","
//                + DeliverySlot.TABLE_NAME + "." + DeliverySlot.DURATION_IN_HOURS + ""
//
//                + " FROM " + Order.TABLE_NAME
//                + " INNER JOIN " + DeliverySlot.TABLE_NAME + " ON (" + Order.TABLE_NAME + "." + Order.DELIVERY_SLOT + " = " + DeliverySlot.TABLE_NAME + "." + DeliverySlot.SLOT_ID + ")"
//                + " LEFT OUTER JOIN " + Shop.TABLE_NAME + " ON (" + Shop.TABLE_NAME + "." + Shop.SHOP_ID + " = " + Order.TABLE_NAME + "." + Order.SHOP_ID + ")"
//                + " LEFT OUTER JOIN " + User.TABLE_NAME + " ON (" + Order.TABLE_NAME + "." + Order.DELIVERY_GUY_SELF_ID + " = " + User.TABLE_NAME + "." + User.USER_ID + ")"
//                + " WHERE TRUE ";
//
//
//
//        if(areDeliveryOrders)
//        {
//            query = query + " AND " + Order.DELIVERY_MODE + " IN ( " + Order.DELIVERY_MODE_HOME_DELIVERY + " )";
////            query = query + " AND " + Order.DELIVERY_MODE + " IN ( " + Order.DELIVERY_MODE_DELIVERY_BY_MARKET + " , " + Order.DELIVERY_MODE_DELIVERY_BY_VENDOR + " )";
//        }
//
//
//
//        if(shopID != null)
//        {
//            query = query + " AND " + Order.TABLE_NAME + "." + Order.SHOP_ID + " = " + shopID;
//        }
//
//
//        if(endUserID != null)
//        {
//            query = query + " AND " + Order.END_USER_ID + " = " + endUserID;
//        }
//
//
//
//        if(deliveryMode != null)
//        {
//            query = query + " AND " + Order.DELIVERY_MODE + " = " + deliveryMode;
//        }
//
//
//
//        if(deliveryDate != null)
//        {
//            query = query + " AND " + Order.DELIVERY_DATE + " = ? ";
//        }
//
//
//
//
//
//
//
//        if(deliveryGuyID != null)
//        {
//            query = query + " AND " + User.USER_ID + " = " + deliveryGuyID;
//        }
//
//
//
//        if(deliveryGuyNull)
//        {
//            query = query + " AND " + Order.DELIVERY_GUY_SELF_ID + " IS NULL ";
//        }
//
//
//
//        if(homeDeliveryStatus != null)
//        {
//
//            query = query + " AND " + Order.STATUS_HOME_DELIVERY + " = " + homeDeliveryStatus;
//        }
//
//
//        if(statusHDLessThan != null)
//        {
//            query = query + " AND " + Order.STATUS_HOME_DELIVERY + " <= " + statusHDLessThan;
//        }
//
//
//
//
//
//
//        // all the non-aggregate columns which are present in select must be present in group by also.
//        query = query
//                + " group by "
//                + DeliverySlot.TABLE_NAME + "." + DeliverySlot.SLOT_ID ;
//
//
//        queryCount = query;
//
//
//
//
//
//        if(sortBy!=null)
//        {
//            if(!sortBy.equals(""))
//            {
//                String queryPartSortBy = " ORDER BY " + sortBy;
//
//                query = query + queryPartSortBy;
//            }
//        }
//
//
//
//
//
//        queryCount = "SELECT COUNT(*) as item_count FROM (" + queryCount + ") AS temp";
//
//
//
//
//        DeliverySlotEndpoint endPoint = new DeliverySlotEndpoint();
//
//
//        ArrayList<DeliverySlot> slotList = new ArrayList<>();
//        Connection connection = null;
//
//
//        PreparedStatement statement = null;
//        ResultSet rs = null;
//
//
//
//        try {
//
//            connection = dataSource.getConnection();
//
//            int i = 0;
//
//
//            if(!getOnlyMetadata) {
//
//
//                statement = connection.prepareStatement(query);
//
//                if(deliveryDate!=null)
//                {
//                    statement.setObject(++i,deliveryDate);
//                }
//
//                rs = statement.executeQuery();
//
//                while (rs.next()) {
//
//
//                    DeliverySlot deliverySlot = new DeliverySlot();
//
//                    deliverySlot.setSlotID(rs.getInt(DeliverySlot.SLOT_ID));
//                    deliverySlot.setSlotName(rs.getString(DeliverySlot.SLOT_NAME));
//                    deliverySlot.setSlotTime(rs.getTime(DeliverySlot.SLOT_START_TIME));
//                    deliverySlot.setDurationInHours(rs.getInt(DeliverySlot.DURATION_IN_HOURS));
//                    deliverySlot.setRt_order_count(rs.getInt("order_count"));
//
//                    slotList.add(deliverySlot);
//                }
//
//
//                endPoint.setResults(slotList);
//            }
//
//
//
//            if(getRowCount)
//            {
//                statement = connection.prepareStatement(queryCount);
//
//                i = 0;
//
//
//                if(deliveryDate!=null)
//                {
//                    statement.setObject(++i,deliveryDate);
//                }
//
//                rs = statement.executeQuery();
//
//                while(rs.next())
//                {
////                    System.out.println("Item Count : " + String.valueOf(endPoint.getItemCount()));
//                    endPoint.setItemCount(rs.getInt("item_count"));
//                }
//            }
//
//
//
//        } catch (SQLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//
//        finally
//
//        {
//
//            try {
//                if(rs!=null)
//                {rs.close();}
//            } catch (SQLException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//            try {
//
//                if(statement!=null)
//                {statement.close();}
//            } catch (SQLException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//            try {
//
//                if(connection!=null)
//                {connection.close();}
//            } catch (SQLException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//
//
//        return endPoint;
//    }
//




    // fetch delivery guys assigned to the orders in the given shop with given status
    public UserEndpoint fetchDeliveryGuysBackup(
            Integer homeDeliveryStatus,
            Integer shopID,
            String sortBy,
            Integer limit, Integer offset,
            boolean getRowCount,
            boolean getOnlyMetadata
    )
    {

        String queryCount = "";

        String query = "SELECT "

                + User.TABLE_NAME + "." + User.USER_ID + ","
                + User.TABLE_NAME + "." + User.NAME + ","
                + User.TABLE_NAME + "." + User.PHONE + ","
                + User.TABLE_NAME + "." + User.PROFILE_IMAGE_URL + ""

                + " FROM " + User.TABLE_NAME
                + " INNER JOIN " + Order.TABLE_NAME + " ON (" + Order.TABLE_NAME + "." + Order.DELIVERY_GUY_SELF_ID + " = " + User.TABLE_NAME + "." + User.USER_ID + ")"
                + " WHERE TRUE ";



        if(shopID != null)
        {
            query = query + " AND " + Order.SHOP_ID + " = " + shopID;
        }







        if(homeDeliveryStatus != null)
        {

            query = query + " AND " + Order.STATUS_HOME_DELIVERY + " = " + homeDeliveryStatus;
        }






        // all the non-aggregate columns which are present in select must be present in group by also.
        query = query
                + " group by "
                + User.TABLE_NAME + "." + User.USER_ID ;


        queryCount = query;



        if(sortBy!=null)
        {
            if(!sortBy.equals(""))
            {
                String queryPartSortBy = " ORDER BY " + sortBy;

                query = query + queryPartSortBy;
            }
        }



        if(limit != null)
        {

            String queryPartLimitOffset = "";

            if(offset!=null)
            {
                queryPartLimitOffset = " LIMIT " + limit + " " + " OFFSET " + offset;

            }else
            {
                queryPartLimitOffset = " LIMIT " + limit + " " + " OFFSET " + 0;
            }

            query = query + queryPartLimitOffset;
        }




        queryCount = "SELECT COUNT(*) as item_count FROM (" + queryCount + ") AS temp";




        UserEndpoint endPoint = new UserEndpoint();

        ArrayList<User> usersList = new ArrayList<>();
        Connection connection = null;


        PreparedStatement statement = null;
        ResultSet rs = null;


        PreparedStatement statementCount = null;
        ResultSet resultSetCount = null;


        try {

            connection = dataSource.getConnection();

            int i = 0;


            if(!getOnlyMetadata) {


//                statement = connection.prepareStatement(queryJoin);

                statement = connection.prepareStatement(query);


                rs = statement.executeQuery();

                while (rs.next()) {


                    User deliveryGuy = new User();
                    deliveryGuy.setUserID(rs.getInt(User.USER_ID));
                    deliveryGuy.setName(rs.getString(User.NAME));
                    deliveryGuy.setPhone(rs.getString(User.PHONE));
                    deliveryGuy.setProfileImagePath(rs.getString(User.PROFILE_IMAGE_URL));


                    usersList.add(deliveryGuy);
                }


                endPoint.setResults(usersList);
            }



            if(getRowCount)
            {
                statementCount = connection.prepareStatement(queryCount);

                i = 0;



                resultSetCount = statementCount.executeQuery();

                while(resultSetCount.next())
                {
                    endPoint.setItemCount(resultSetCount.getInt("item_count"));
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

}
