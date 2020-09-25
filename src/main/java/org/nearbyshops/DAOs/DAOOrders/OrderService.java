package org.nearbyshops.DAOs.DAOOrders;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Refund;

import org.nearbyshops.AppProperties;
import org.nearbyshops.DAOs.DAOSettings.MarketSettingsDAO;
import org.nearbyshops.Model.ModelBilling.RazorPayOrder;
import org.nearbyshops.Model.ModelBilling.Transaction;
import org.nearbyshops.Model.ModelDelivery.DeliveryAddress;
import org.nearbyshops.Model.ModelDelivery.DeliverySlot;
import org.nearbyshops.Model.ModelEndpoint.OrderEndPoint;
import org.nearbyshops.Model.ModelOrderStatus.OrderStatusHomeDelivery;
import org.nearbyshops.Model.ModelOrderStatus.OrderStatusPickFromShop;
import org.nearbyshops.Model.ModelRoles.User;
import org.nearbyshops.Model.Order;
import org.nearbyshops.Model.OrderItem;
import org.nearbyshops.Model.Shop;
import org.nearbyshops.Model.ShopItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sumeet on 7/6/16.
 */


@Component
public class OrderService {



    @Autowired
    DataSource dataSource;

    @Autowired
    DAOOrderUtility daoOrderUtility;


    @Autowired
    AppProperties appProperties;



    public Order getOrderDetailsForPush(int orderID)
    {

        String query = "SELECT "

                 + Order.ORDER_ID + ","
                 + Order.DELIVERY_ADDRESS_ID + ","
                 + Order.DATE_TIME_PLACED + ","

                 + Order.TABLE_NAME + "." + Order.DELIVERY_CHARGES + " delivery_charge,"
//                 + Order.DELIVERY_RECEIVED + ","
//                 + Order.PAYMENT_RECEIVED + ","

                 + Order.DELIVERY_GUY_SELF_ID + ","
                 + Order.END_USER_ID + ","
                 + Order.DELIVERY_MODE + ","

                + Order.TABLE_NAME + "." + Order.SHOP_ID + " shop_id_order,"
                + Order.STATUS_HOME_DELIVERY + ","
                + Order.STATUS_PICK_FROM_SHOP + ","

//                + "end_user." + User.NAME + " ,"
//                + "end_user." + User.E_MAIL + " ,"
//                + "end_user." + User.PHONE + " ,"
//
//                + "shop_admin." + User.NAME + " ,"
//                + "shop_admin." + User.E_MAIL + " ,"
//                + "shop_admin." + User.PHONE + ""

                + "end_user." + User.NAME + " end_user_name,"
                + "end_user." + User.E_MAIL + " end_user_email,"
                + "end_user." + User.PHONE + " end_user_phone,"

                + "shop_admin." + User.NAME + " shop_admin_name,"
                + "shop_admin." + User.E_MAIL + " shop_admin_email,"
                + "shop_admin." + User.PHONE + " shop_admin_phone"

                + " FROM " + Order.TABLE_NAME
                + " INNER JOIN " + User.TABLE_NAME + " end_user ON ( " + " end_user." + User.USER_ID + " = " + Order.TABLE_NAME + "." + Order.END_USER_ID + " ) "
                + " INNER JOIN " + Shop.TABLE_NAME + " ON ( " + Order.TABLE_NAME + "." + Order.SHOP_ID + " = " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + " ) "
                + " INNER JOIN " + User.TABLE_NAME + " shop_admin ON ( " + " shop_admin." + User.USER_ID + " = " + Shop.TABLE_NAME + "." + Shop.SHOP_ADMIN_ID + " ) "
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
                order.setDeliveryAddressID(rs.getInt(Order.DELIVERY_ADDRESS_ID));
                order.setDateTimePlaced(rs.getTimestamp(Order.DATE_TIME_PLACED));

                order.setDeliveryCharges(rs.getInt("delivery_charge"));


                order.setDeliveryGuySelfID(rs.getInt(Order.DELIVERY_GUY_SELF_ID));
                order.setEndUserID(rs.getInt(Order.END_USER_ID));
                order.setDeliveryMode(rs.getInt(Order.DELIVERY_MODE));


                order.setShopID(rs.getInt("shop_id_order"));
                order.setStatusHomeDelivery(rs.getInt(Order.STATUS_HOME_DELIVERY));
                order.setStatusPickFromShop(rs.getInt(Order.STATUS_PICK_FROM_SHOP));

//                order.setDeliveryReceived(rs.getBoolean(Order.DELIVERY_RECEIVED));
//                order.setPaymentReceived(rs.getBoolean(Order.PAYMENT_RECEIVED));




                User endUser = new User();

                endUser.setName(rs.getString("end_user_name" ));
                endUser.setEmail(rs.getString("end_user_email"));
                endUser.setPhone(rs.getString("end_user_phone"));


                order.setRt_end_user_profile(endUser);




                User shopAdmin = new User();
                shopAdmin.setName(rs.getString("shop_admin_name"));
                shopAdmin.setEmail(rs.getString("shop_admin_email"));
                shopAdmin.setPhone(rs.getString("shop_admin_phone"));

                order.setRt_shop_admin_profile(shopAdmin);


            }


            //System.out.println("Total itemCategories queried " + itemCategoryList.size());



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



    public Order getOrderDetails(int orderID)
    {

        String query = "SELECT "

                + Order.ORDER_ID + ","
                + Order.DELIVERY_ADDRESS_ID + ","
                + Order.DATE_TIME_PLACED + ","

                + Order.DELIVERY_CHARGES + " ,"
//                 + Order.DELIVERY_RECEIVED + ","
//                 + Order.PAYMENT_RECEIVED + ","

                + Order.DELIVERY_GUY_SELF_ID + ","
                + Order.END_USER_ID + ","
                + Order.PICK_FROM_SHOP + ","

                + Order.SHOP_ID + ","
                + Order.STATUS_HOME_DELIVERY + ","
                + Order.STATUS_PICK_FROM_SHOP + ","



                + " FROM " + Order.TABLE_NAME
                + " INNER JOIN " + Shop.TABLE_NAME + " ON ( " + Order.TABLE_NAME + "." + Order.SHOP_ID + " = " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + " ) "
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
                order.setDeliveryAddressID(rs.getInt(Order.DELIVERY_ADDRESS_ID));
                order.setDateTimePlaced(rs.getTimestamp(Order.DATE_TIME_PLACED));

                order.setDeliveryGuySelfID(rs.getInt(Order.DELIVERY_GUY_SELF_ID));
                order.setEndUserID(rs.getInt(Order.END_USER_ID));
                order.setPickFromShop(rs.getBoolean(Order.PICK_FROM_SHOP));

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





    public OrderEndPoint getOrdersListForEndUser(
            Integer endUserID, Integer shopID,
            Integer deliveryMode,
            Integer homeDeliveryStatus, Integer pickFromShopStatus,
            Integer deliveryGuyID,
            Boolean pendingOrders,
            String searchString,
            String sortBy,
            int limit, int offset,
            boolean getRowCount,
            boolean getOnlyMetadata
    )
    {

        String queryCount = "";

        String query = "SELECT "

                + Order.TABLE_NAME + "." + Order.ORDER_ID + ","
                + Order.TABLE_NAME + "." + Order.SHOP_ID + ","
                + Order.TABLE_NAME + "." + Order.DATE_TIME_PLACED + ","
                + Order.TABLE_NAME + "." + Order.ITEM_COUNT + ","
                + Order.TABLE_NAME + "." + Order.NET_PAYABLE + ","
                + Order.TABLE_NAME + "." + Order.STATUS_HOME_DELIVERY + ","
                + Order.TABLE_NAME + "." + Order.STATUS_PICK_FROM_SHOP + ","

                + Order.TABLE_NAME + "." + Order.DELIVERY_MODE + ","
                + Order.TABLE_NAME + "." + Order.DELIVERY_DATE + ","
                + Order.TABLE_NAME + "." + Order.PAYMENT_MODE + ","
                + DeliverySlot.TABLE_NAME + "." + DeliverySlot.SLOT_NAME + ","

                + Shop.TABLE_NAME + "." + Shop.SHOP_NAME + ","
                + Shop.TABLE_NAME + "." + Shop.SHOP_ADDRESS + ","
                + Shop.TABLE_NAME + "." + Shop.LOGO_IMAGE_PATH + ""

                + " FROM " + Order.TABLE_NAME
                + " LEFT OUTER JOIN " + Shop.TABLE_NAME + " ON (" + Order.TABLE_NAME + "." + Order.SHOP_ID + " = " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + ")"
                + " LEFT OUTER JOIN " + DeliverySlot.TABLE_NAME + " ON (" + Order.TABLE_NAME + "." + Order.DELIVERY_SLOT + " = " + DeliverySlot.TABLE_NAME + "." + DeliverySlot.SLOT_ID + ")"
                + " WHERE TRUE ";





        if(endUserID !=null)
        {
            query = query + " AND " + Order.TABLE_NAME + "." + Order.END_USER_ID + " = " + endUserID;

        }



        if(shopID != null)
        {
            query = query + " AND " + Order.TABLE_NAME + "." + Order.SHOP_ID + " = " + shopID;
        }




        if(searchString != null)
        {

            String queryPartSearch = " ( " + " CAST ( " + Order.TABLE_NAME + "." + Order.ORDER_ID + " AS text )" + " ilike '%" + searchString + "%'" + ") ";

            query = query + " AND " + queryPartSearch;
        }





        if(pendingOrders!=null)
        {
            String queryPartPending = "";


            if(pendingOrders)
            {
                queryPartPending = "(" + Order.STATUS_HOME_DELIVERY + " < " + OrderStatusHomeDelivery.PAYMENT_RECEIVED + ")";

            }
            else
            {
                queryPartPending = "(" + Order.STATUS_HOME_DELIVERY + " = " + OrderStatusHomeDelivery.PAYMENT_RECEIVED + ")";

            }



            query = query + " AND " + queryPartPending;
        }




        if(homeDeliveryStatus != null)
        {

            query = query + " AND " + Order.STATUS_HOME_DELIVERY + " = " + homeDeliveryStatus;
        }




        if(pickFromShopStatus != null)
        {
            query = query + " AND " + Order.STATUS_PICK_FROM_SHOP + " = " + pickFromShopStatus;
        }





        if(deliveryMode != null)
        {
            query = query + " AND " + Order.DELIVERY_MODE + " = " + deliveryMode;
        }




        if(deliveryGuyID != null)
        {
            query = query + " AND " + Order.DELIVERY_GUY_SELF_ID + " = " + deliveryGuyID;
        }




        // all the non-aggregate columns which are present in select must be present in group by also.
        query = query
                + " group by "
                + Order.TABLE_NAME + "." + Order.ORDER_ID + ","
                + DeliverySlot.TABLE_NAME + "." + DeliverySlot.SLOT_ID + ","
                + Shop.TABLE_NAME + "." + Shop.SHOP_ID ;



        queryCount = query;





        if(sortBy!=null && !sortBy.equals(""))
        {
            String queryPartSortBy = " ORDER BY " + sortBy;
            query = query + queryPartSortBy;
        }




        query = query + " LIMIT " + limit + " " + " OFFSET " + offset;



        queryCount = "SELECT COUNT(*) as item_count FROM (" + queryCount + ") AS temp";




        OrderEndPoint endPoint = new OrderEndPoint();

        ArrayList<Order> ordersList = new ArrayList<>();


        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;




        try {

            connection = dataSource.getConnection();

            int i = 0;


            if(!getOnlyMetadata) {


                statement = connection.prepareStatement(query);


                rs = statement.executeQuery();

                while (rs.next()) {

                    Order order = new Order();
                    order.setOrderID(rs.getInt(Order.ORDER_ID));
                    order.setShopID(rs.getInt(Order.SHOP_ID));
                    order.setStatusHomeDelivery(rs.getInt(Order.STATUS_HOME_DELIVERY));
                    order.setStatusPickFromShop(rs.getInt(Order.STATUS_PICK_FROM_SHOP));
                    order.setDateTimePlaced(rs.getTimestamp(Order.DATE_TIME_PLACED));
                    order.setItemCount((Integer) rs.getObject(Order.ITEM_COUNT));
                    order.setNetPayable(rs.getDouble(Order.NET_PAYABLE));

                    order.setDeliveryMode(rs.getInt(Order.DELIVERY_MODE));
                    order.setDeliveryDate(rs.getDate(Order.DELIVERY_DATE));
                    order.setPaymentMode(rs.getInt(Order.PAYMENT_MODE));


                    DeliverySlot deliverySlot = new DeliverySlot();
                    deliverySlot.setSlotName(rs.getString(DeliverySlot.SLOT_NAME));
                    order.setDeliverySlot(deliverySlot);


                    Shop shop = new Shop();
                    shop.setShopName(rs.getString(Shop.SHOP_NAME));
                    shop.setLogoImagePath(rs.getString(Shop.LOGO_IMAGE_PATH));
                    shop.setShopAddress(rs.getString(Shop.SHOP_ADDRESS));

                    order.setShop(shop);

                    ordersList.add(order);
                }


                endPoint.setResults(ordersList);
            }






            if(getRowCount)
            {
                statement = connection.prepareStatement(queryCount);
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




    public OrderEndPoint getOrdersList(
            Integer endUserID, Integer shopID,
            Integer deliveryMode,
            Integer homeDeliveryStatus, Integer pickFromShopStatus,
            Integer deliveryGuyID,
            Boolean pendingOrders,
            String searchString,
            String sortBy,
            int limit, int offset,
            boolean getRowCount,
            boolean getOnlyMetadata
    )
    {

        String queryCount = "";

        String query = "SELECT "

                + Order.TABLE_NAME + "." + Order.ORDER_ID + ","
                + Order.TABLE_NAME + "." + Order.SHOP_ID + ","
                + Order.TABLE_NAME + "." + Order.DATE_TIME_PLACED + ","
                + Order.TABLE_NAME + "." + Order.ITEM_COUNT + ","
                + Order.TABLE_NAME + "." + Order.NET_PAYABLE + ","
//                + Order.TABLE_NAME + "." + Order.PICK_FROM_SHOP + ","
                + Order.TABLE_NAME + "." + Order.STATUS_HOME_DELIVERY + ","
                + Order.TABLE_NAME + "." + Order.STATUS_PICK_FROM_SHOP + ","

//                + Order.TABLE_NAME + "." + Order.ITEM_TOTAL + ","
//                + Order.TABLE_NAME + "." + Order.APP_SERVICE_CHARGE + ","
//                + Order.TABLE_NAME + "." + Order.DELIVERY_CHARGES + ","


                + Order.TABLE_NAME + "." + Order.DELIVERY_MODE + ","
                + Order.TABLE_NAME + "." + Order.DELIVERY_DATE + ","
                + Order.TABLE_NAME + "." + Order.PAYMENT_MODE + ","
                + DeliverySlot.TABLE_NAME + "." + DeliverySlot.SLOT_NAME + ","



//                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.ID + ","
                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.NAME + ","
                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.PHONE_NUMBER + ","
                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.DELIVERY_ADDRESS + ","
                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.CITY + ","
                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.PINCODE + ""

                + " FROM " + Order.TABLE_NAME
                + " LEFT OUTER JOIN " + DeliveryAddress.TABLE_NAME + " ON (" + Order.TABLE_NAME + "." + Order.DELIVERY_ADDRESS_ID + " = " + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.ID + ")"
                + " LEFT OUTER JOIN " + DeliverySlot.TABLE_NAME + " ON (" + Order.TABLE_NAME + "." + Order.DELIVERY_SLOT + " = " + DeliverySlot.TABLE_NAME + "." + DeliverySlot.SLOT_ID + ")"
                + " WHERE TRUE ";





        if(endUserID !=null)
        {
            query = query + " AND " + Order.TABLE_NAME + "." + Order.END_USER_ID + " = " + endUserID;

        }



        if(shopID != null)
        {
            query = query + " AND " + Order.TABLE_NAME + "." + Order.SHOP_ID + " = " + shopID;
        }




        if(searchString != null)
        {

            String queryPartSearch = " ( " + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.NAME +" ilike '%" + searchString + "%'"
                    + " or CAST ( " + Order.TABLE_NAME + "." + Order.ORDER_ID + " AS text )" + " ilike '%" + searchString + "%'" + ") ";


            query = query + " AND " + queryPartSearch;
        }





        if(pendingOrders!=null)
        {
            String queryPartPending = "";


            if(pendingOrders)
            {
                queryPartPending = "(" + Order.STATUS_HOME_DELIVERY + " < " + OrderStatusHomeDelivery.PAYMENT_RECEIVED + ")";

            }
            else
            {
                queryPartPending = "(" + Order.STATUS_HOME_DELIVERY + " = " + OrderStatusHomeDelivery.PAYMENT_RECEIVED + ")";

            }



            query = query + " AND " + queryPartPending;
        }




        if(homeDeliveryStatus != null)
        {

            query = query + " AND " + Order.STATUS_HOME_DELIVERY + " = " + homeDeliveryStatus;
        }




        if(pickFromShopStatus != null)
        {
            query = query + " AND " + Order.STATUS_PICK_FROM_SHOP + " = " + pickFromShopStatus;
        }





        if(deliveryMode != null)
        {
            query = query + " AND " + Order.DELIVERY_MODE + " = " + deliveryMode;
        }




        if(deliveryGuyID != null)
        {
            query = query + " AND " + Order.DELIVERY_GUY_SELF_ID + " = " + deliveryGuyID;
        }




        // all the non-aggregate columns which are present in select must be present in group by also.
        query = query
                + " group by "
                + Order.TABLE_NAME + "." + Order.ORDER_ID + ","
                + DeliverySlot.TABLE_NAME + "." + DeliverySlot.SLOT_ID + ","
                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.ID ;



        queryCount = query;





        if(sortBy!=null && !sortBy.equals(""))
        {
            String queryPartSortBy = " ORDER BY " + sortBy;
            query = query + queryPartSortBy;
        }




        query = query + " LIMIT " + limit + " " + " OFFSET " + offset;



        queryCount = "SELECT COUNT(*) as item_count FROM (" + queryCount + ") AS temp";




        OrderEndPoint endPoint = new OrderEndPoint();

        ArrayList<Order> ordersList = new ArrayList<>();


        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;




        try {

            connection = dataSource.getConnection();

            int i = 0;


            if(!getOnlyMetadata) {


                statement = connection.prepareStatement(query);


                rs = statement.executeQuery();

                while (rs.next()) {

                    Order order = new Order();
                    order.setOrderID(rs.getInt(Order.ORDER_ID));
                    order.setShopID(rs.getInt(Order.SHOP_ID));
//                    order.setPickFromShop(rs.getBoolean(Order.PICK_FROM_SHOP));
                    order.setStatusHomeDelivery(rs.getInt(Order.STATUS_HOME_DELIVERY));
                    order.setStatusPickFromShop(rs.getInt(Order.STATUS_PICK_FROM_SHOP));
                    order.setDateTimePlaced(rs.getTimestamp(Order.DATE_TIME_PLACED));
                    order.setItemCount((Integer) rs.getObject(Order.ITEM_COUNT));
                    order.setNetPayable(rs.getDouble(Order.NET_PAYABLE));

                    order.setDeliveryMode(rs.getInt(Order.DELIVERY_MODE));
                    order.setPaymentMode(rs.getInt(Order.PAYMENT_MODE));
                    order.setDeliveryDate(rs.getDate(Order.DELIVERY_DATE));

//                    order.setItemTotal((Double) rs.getObject(Order.ITEM_TOTAL));
//                    order.setAppServiceCharge(rs.getDouble(Order.APP_SERVICE_CHARGE));
//                    order.setDeliveryCharges(rs.getDouble(Order.DELIVERY_CHARGES));

                    DeliverySlot deliverySlot = new DeliverySlot();
                    deliverySlot.setSlotName(rs.getString(DeliverySlot.SLOT_NAME));
                    order.setDeliverySlot(deliverySlot);


                    DeliveryAddress address = new DeliveryAddress();
                    address.setName(rs.getString(DeliveryAddress.NAME));
                    address.setPhoneNumber(rs.getLong(DeliveryAddress.PHONE_NUMBER));
                    address.setDeliveryAddress(rs.getString(DeliveryAddress.DELIVERY_ADDRESS));
                    address.setCity(rs.getString(DeliveryAddress.CITY));
                    address.setPincode(rs.getLong(DeliveryAddress.PINCODE));
                    order.setDeliveryAddress(address);



                    ordersList.add(order);
                }


                endPoint.setResults(ordersList);
            }






            if(getRowCount)
            {
                statement = connection.prepareStatement(queryCount);
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





    public OrderEndPoint getOrdersListForDelivery(
            Date deliveryDate,
            boolean isASAPDelivery,
            Integer deliverySlotID,
                   Integer shopID,
                   Integer deliveryGuyID,
                   boolean deliveryGuyNull,
                   Integer deliveryMode,
                   Integer statusHDLessThan,
                   Integer homeDeliveryStatus,
                   Double latCenter, Double lonCenter,
                   String searchString,
                   String sortBy,
                   int limit, int offset,
                   boolean getRowCount,
                   boolean getOnlyMetadata
    )
    {


        String queryCount = "";

        String query = "SELECT "


                + "6371 * acos( cos( radians("
                + latCenter + ")) * cos( radians( " + DeliveryAddress.LATITUDE + ") ) * cos(radians( " + DeliveryAddress.LONGITUDE + " ) - radians("
                + lonCenter + "))"
                + " + sin( radians(" + latCenter + ")) * sin(radians( " + DeliveryAddress.LATITUDE + " ))) as delivery_distance" + ","


                + "6371 * acos( cos( radians("
                + latCenter + ")) * cos( radians( " + Shop.LAT_CENTER + ") ) * cos(radians( " + Shop.LON_CENTER + " ) - radians("
                + lonCenter + "))"
                + " + sin( radians(" + latCenter + ")) * sin(radians( " + Shop.LAT_CENTER + " ))) as pickup_distance" + ","


                + Order.TABLE_NAME + "." + Order.ORDER_ID + ","
                + Order.TABLE_NAME + "." + Order.SHOP_ID + " as shop_id,"
                + Order.TABLE_NAME + "." + Order.DATE_TIME_PLACED + ","
                + Order.TABLE_NAME + "." + Order.ITEM_COUNT + ","
                + Order.TABLE_NAME + "." + Order.NET_PAYABLE + ","
                + Order.TABLE_NAME + "." + Order.STATUS_HOME_DELIVERY + ","
                + Order.TABLE_NAME + "." + Order.STATUS_PICK_FROM_SHOP + ","

                + Order.TABLE_NAME + "." + Order.DELIVERY_GUY_SELF_ID + ","

                + Order.TABLE_NAME + "." + Order.DELIVERY_MODE + ","
                + Order.TABLE_NAME + "." + Order.DELIVERY_DATE + ","
                + Order.TABLE_NAME + "." + Order.PAYMENT_MODE + ","

                + DeliverySlot.TABLE_NAME + "." + DeliverySlot.SLOT_NAME + ","


                + Shop.TABLE_NAME + "." + Shop.LAT_CENTER + ","
                + Shop.TABLE_NAME + "." + Shop.LON_CENTER + ","



                + User.TABLE_NAME + "." + User.USER_ID + ","
                + User.TABLE_NAME + "." + User.NAME + ","
                + User.TABLE_NAME + "." + User.PHONE + ","
                + User.TABLE_NAME + "." + User.PROFILE_IMAGE_URL + ","



//                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.ID + ","
                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.LATITUDE + ","
                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.LONGITUDE + ","

                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.NAME + ","
                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.PHONE_NUMBER + ","
                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.DELIVERY_ADDRESS + ","
                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.CITY + ","
                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.PINCODE + ""

                + " FROM " + Order.TABLE_NAME
                + " LEFT OUTER JOIN " + DeliveryAddress.TABLE_NAME + " ON (" + Order.TABLE_NAME + "." + Order.DELIVERY_ADDRESS_ID + " = " + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.ID + ")"
                + " LEFT OUTER JOIN " + DeliverySlot.TABLE_NAME + " ON (" + Order.TABLE_NAME + "." + Order.DELIVERY_SLOT + " = " + DeliverySlot.TABLE_NAME + "." + DeliverySlot.SLOT_ID + ")"
                + " LEFT OUTER JOIN " + Shop.TABLE_NAME + " ON (" + Shop.TABLE_NAME + "." + Shop.SHOP_ID + " = " + Order.TABLE_NAME + "." + Order.SHOP_ID + ")"
                + " LEFT OUTER JOIN " + User.TABLE_NAME + " ON (" + Order.TABLE_NAME + "." + Order.DELIVERY_GUY_SELF_ID + " = " + User.TABLE_NAME + "." + User.USER_ID + ")"
                + " WHERE " + Order.DELIVERY_MODE + " IN ( " + Order.DELIVERY_MODE_HOME_DELIVERY + " )";

//        + " WHERE " + Order.DELIVERY_MODE + " IN ( " + Order.DELIVERY_MODE_DELIVERY_BY_MARKET + " , " + Order.DELIVERY_MODE_DELIVERY_BY_VENDOR + " )";


        if(shopID != null)
        {
            query = query + " AND " + Order.TABLE_NAME + "." + Order.SHOP_ID + " = " + shopID;
        }


        if(isASAPDelivery)
        {
            query = query + " AND " + Order.TABLE_NAME + "." + Order.DELIVERY_SLOT + " IS NULL ";
        }




        if(searchString != null)
        {

            String queryPartSearch = " ( " + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.NAME +" ilike '%" + searchString + "%'"
                    + " or CAST ( " + Order.TABLE_NAME + "." + Order.ORDER_ID + " AS text )" + " ilike '%" + searchString + "%'" + ") ";


            query = query + " AND " + queryPartSearch;
        }






        if(homeDeliveryStatus != null)
        {
            query = query + " AND " + Order.STATUS_HOME_DELIVERY + " = " + homeDeliveryStatus;
        }



        if(statusHDLessThan != null)
        {
            query = query + " AND " + Order.STATUS_HOME_DELIVERY + " <= " + statusHDLessThan;
        }



        if(deliverySlotID != null)
        {
            query = query + " AND " + Order.DELIVERY_SLOT + " = " + deliverySlotID;
        }




        if(deliveryDate != null)
        {
            query = query + " AND " + Order.DELIVERY_DATE + " = ?";
        }





        if(deliveryMode != null)
        {

            query = query + " AND " + Order.DELIVERY_MODE + " = " + deliveryMode;
        }




        if(deliveryGuyID != null)
        {
            query = query + " AND " + Order.DELIVERY_GUY_SELF_ID + " = " + deliveryGuyID;
        }



        if(deliveryGuyNull)
        {
            query = query + " AND " + Order.DELIVERY_GUY_SELF_ID + " IS NULL ";
        }









        // all the non-aggregate columns which are present in select must be present in group by also.
        query = query
                + " group by "
                + Order.TABLE_NAME + "." + Order.ORDER_ID + ","
                + DeliverySlot.TABLE_NAME + "." + DeliverySlot.SLOT_ID + ","
                + Shop.TABLE_NAME + "." + Shop.SHOP_ID + ","
                + User.TABLE_NAME + "." + User.USER_ID + ","
                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.ID ;



        queryCount = query;





        if(sortBy!=null && !sortBy.equals(""))
        {
            String queryPartSortBy = " ORDER BY " + sortBy;
            query = query + queryPartSortBy;
        }




        query = query + " LIMIT " + limit + " " + " OFFSET " + offset;



        queryCount = "SELECT COUNT(*) as item_count FROM (" + queryCount + ") AS temp";




        OrderEndPoint endPoint = new OrderEndPoint();

        ArrayList<Order> ordersList = new ArrayList<>();


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


                    Order order = new Order();
                    order.setOrderID(rs.getInt(Order.ORDER_ID));
                    order.setShopID(rs.getInt("shop_id"));
                    order.setStatusHomeDelivery(rs.getInt(Order.STATUS_HOME_DELIVERY));
                    order.setStatusPickFromShop(rs.getInt(Order.STATUS_PICK_FROM_SHOP));
                    order.setDateTimePlaced(rs.getTimestamp(Order.DATE_TIME_PLACED));
                    order.setItemCount((Integer) rs.getObject(Order.ITEM_COUNT));
                    order.setNetPayable(rs.getDouble(Order.NET_PAYABLE));


                    order.setDeliveryGuySelfID(rs.getInt(Order.DELIVERY_GUY_SELF_ID));
                    order.setDeliveryMode(rs.getInt(Order.DELIVERY_MODE));
                    order.setDeliveryDate(rs.getDate(Order.DELIVERY_DATE));
                    order.setPaymentMode(rs.getInt(Order.PAYMENT_MODE));

                    order.setRt_deliveryDistance(rs.getDouble("delivery_distance"));
                    order.setRt_pickupDistance(rs.getDouble("pickup_distance"));


                    DeliverySlot deliverySlot = new DeliverySlot();
                    deliverySlot.setSlotName(rs.getString(DeliverySlot.SLOT_NAME));
                    order.setDeliverySlot(deliverySlot);


                    DeliveryAddress address = new DeliveryAddress();
                    address.setName(rs.getString(DeliveryAddress.NAME));
                    address.setPhoneNumber(rs.getLong(DeliveryAddress.PHONE_NUMBER));
                    address.setDeliveryAddress(rs.getString(DeliveryAddress.DELIVERY_ADDRESS));
                    address.setCity(rs.getString(DeliveryAddress.CITY));
                    address.setPincode(rs.getLong(DeliveryAddress.PINCODE));

                    address.setLatitude(rs.getDouble(DeliveryAddress.LATITUDE));
                    address.setLongitude(rs.getDouble(DeliveryAddress.LONGITUDE));



                    Shop shop = new Shop();

                    shop.setLatCenter(rs.getDouble(Shop.LAT_CENTER));
                    shop.setLonCenter(rs.getDouble(Shop.LON_CENTER));



//                    address.setRt_distance(rs.getDouble("distance"));


                    order.setDeliveryAddress(address);
                    order.setShop(shop);




                    User deliveryGuy = new User();
                    deliveryGuy.setUserID(rs.getInt(User.USER_ID));
                    deliveryGuy.setName(rs.getString(User.NAME));
                    deliveryGuy.setPhone(rs.getString(User.PHONE));
                    deliveryGuy.setProfileImagePath(rs.getString(User.PROFILE_IMAGE_URL));
                    order.setRt_delivery_guy_profile(deliveryGuy);

                    order.setDeliveryGuySelfID(deliveryGuy.getUserID());



                    ordersList.add(order);
                }


                endPoint.setResults(ordersList);
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






    public OrderEndPoint getOrdersListWithDeliveryProfile(
            Integer endUserID, Integer shopID,
            Boolean pickFromShop,
            Integer homeDeliveryStatus, Integer pickFromShopStatus,
            Integer deliveryGuyID,
            Double latCenter, Double lonCenter,
            Boolean pendingOrders,
            String searchString,
            String sortBy,
            int limit, int offset,
            boolean getRowCount,
            boolean getOnlyMetadata
    )
    {

        String queryCount = "";

        String query = "SELECT "

                + "6371 * acos( cos( radians("
                + latCenter + ")) * cos( radians( " + DeliveryAddress.LATITUDE + ") ) * cos(radians( " + DeliveryAddress.LONGITUDE + " ) - radians("
                + lonCenter + "))"
                + " + sin( radians(" + latCenter + ")) * sin(radians( " + DeliveryAddress.LATITUDE + " ))) as distance" + ","

                + Order.TABLE_NAME + "." + Order.ORDER_ID + ","
                + Order.TABLE_NAME + "." + Order.SHOP_ID + ","
                + Order.TABLE_NAME + "." + Order.DATE_TIME_PLACED + ","
                + Order.TABLE_NAME + "." + Order.ITEM_COUNT + ","
                + Order.TABLE_NAME + "." + Order.NET_PAYABLE + ","
                + Order.TABLE_NAME + "." + Order.PICK_FROM_SHOP + ","
                + Order.TABLE_NAME + "." + Order.STATUS_HOME_DELIVERY + ","
                + Order.TABLE_NAME + "." + Order.STATUS_PICK_FROM_SHOP + ","

                + Order.TABLE_NAME + "." + Order.ITEM_TOTAL + ","
                + Order.TABLE_NAME + "." + Order.APP_SERVICE_CHARGE + ","
                + Order.TABLE_NAME + "." + Order.DELIVERY_CHARGES + ","



//                + Order.TABLE_NAME + "." + Order.END_USER_ID + ","

//                + Order.TABLE_NAME + "." + Order.DELIVERY_ADDRESS_ID + ","
//                + Order.TABLE_NAME + "." + Order.DELIVERY_GUY_SELF_ID + ","
//                + Order.TABLE_NAME + "." + Order.DELIVERY_OTP + ","
//                + Order.TABLE_NAME + "." + Order.IS_CANCELLED_BY_END_USER + ","
//                + Order.TABLE_NAME + "." + Order.REASON_FOR_CANCELLED_BY_SHOP + ","
//                + Order.TABLE_NAME + "." + Order.REASON_FOR_CANCELLED_BY_USER + ","
//                + Order.TABLE_NAME + "." + Order.REASON_FOR_ORDER_RETURNED + ","
//


//                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.ID + ","
                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.NAME + ","
                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.PHONE_NUMBER + ","
                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.DELIVERY_ADDRESS + ","
                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.CITY + ","
                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.PINCODE + ","



//                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.END_USER_ID + ","
//                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.LANDMARK + ","
                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.LATITUDE + ","
                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.LONGITUDE + ","

//

                + User.TABLE_NAME + "." + User.USER_ID + ","
                + User.TABLE_NAME + "." + User.NAME + ","
                + User.TABLE_NAME + "." + User.PHONE + ","
                + User.TABLE_NAME + "." + User.PROFILE_IMAGE_URL + ""

                + " FROM " + Order.TABLE_NAME
                + " LEFT OUTER JOIN " + DeliveryAddress.TABLE_NAME + " ON (" + Order.TABLE_NAME + "." + Order.DELIVERY_ADDRESS_ID + " = " + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.ID + ")"
                + " LEFT OUTER JOIN " + User.TABLE_NAME + " ON (" + Order.TABLE_NAME + "." + Order.DELIVERY_GUY_SELF_ID + " = " + User.TABLE_NAME + "." + User.USER_ID + ")"
                + " WHERE TRUE ";





        if(endUserID !=null)
        {
            query = query + " AND " + Order.TABLE_NAME + "." + Order.END_USER_ID + " = " + endUserID;

        }



        if(shopID != null)
        {
            query = query + " AND " + Order.SHOP_ID + " = " + shopID;
        }




        if(searchString != null)
        {

            String queryPartSearch = " ( " + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.NAME +" ilike '%" + searchString + "%'"
                    + " or CAST ( " + Order.TABLE_NAME + "." + Order.ORDER_ID + " AS text )" + " ilike '%" + searchString + "%'" + ") ";


            query = query + " AND " + queryPartSearch;
        }





        if(pendingOrders!=null)
        {
            String queryPartPending = "";


            if(pendingOrders)
            {
                queryPartPending = "(" + Order.STATUS_HOME_DELIVERY + " < " + OrderStatusHomeDelivery.PAYMENT_RECEIVED + ")";

            }
            else
            {
                queryPartPending = "(" + Order.STATUS_HOME_DELIVERY + " = " + OrderStatusHomeDelivery.PAYMENT_RECEIVED + ")";

            }



            query = query + " AND " + queryPartPending;
        }




        if(homeDeliveryStatus != null)
        {

            query = query + " AND " + Order.STATUS_HOME_DELIVERY + " = " + homeDeliveryStatus;
        }




        if(pickFromShopStatus != null)
        {
            query = query + " AND " + Order.STATUS_PICK_FROM_SHOP + " = " + pickFromShopStatus;
        }



        if(pickFromShop != null)
        {

            query = query + " AND " + Order.PICK_FROM_SHOP + " = " + pickFromShop;
        }




        if(deliveryGuyID != null)
        {
            query = query + " AND " + Order.DELIVERY_GUY_SELF_ID + " = " + deliveryGuyID;
        }




        // all the non-aggregate columns which are present in select must be present in group by also.
        query = query
                + " group by "
                + User.TABLE_NAME + "." + User.USER_ID + ","
                + Order.TABLE_NAME + "." + Order.ORDER_ID + ","
                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.ID ;


        queryCount = query;





        if(sortBy!=null && !sortBy.equals(""))
        {
            String queryPartSortBy = " ORDER BY " + sortBy;
            query = query + queryPartSortBy;
        }




        query = query + " LIMIT " + limit + " " + " OFFSET " + offset;



        queryCount = "SELECT COUNT(*) as item_count FROM (" + queryCount + ") AS temp";




        OrderEndPoint endPoint = new OrderEndPoint();

        ArrayList<Order> ordersList = new ArrayList<>();


        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;




        try {

            connection = dataSource.getConnection();

            int i = 0;


            if(!getOnlyMetadata) {


                statement = connection.prepareStatement(query);


                rs = statement.executeQuery();

                while (rs.next()) {

                    Order order = new Order();

                    order.setOrderID(rs.getInt(Order.ORDER_ID));
                    order.setShopID(rs.getInt(Order.SHOP_ID));
                    order.setPickFromShop(rs.getBoolean(Order.PICK_FROM_SHOP));
                    order.setStatusHomeDelivery(rs.getInt(Order.STATUS_HOME_DELIVERY));
                    order.setStatusPickFromShop(rs.getInt(Order.STATUS_PICK_FROM_SHOP));
                    order.setDateTimePlaced(rs.getTimestamp(Order.DATE_TIME_PLACED));
                    order.setItemCount((Integer) rs.getObject(Order.ITEM_COUNT));
                    order.setNetPayable(rs.getDouble(Order.NET_PAYABLE));

                    order.setItemTotal((Double) rs.getObject(Order.ITEM_TOTAL));
                    order.setAppServiceCharge(rs.getDouble(Order.APP_SERVICE_CHARGE));
                    order.setDeliveryCharges(rs.getDouble(Order.DELIVERY_CHARGES));





//                    order.setEndUserID(rs.getInt(Order.END_USER_ID));
//                    order.setDeliveryAddressID(rs.getInt(Order.DELIVERY_ADDRESS_ID));
//                    order.setDeliveryGuySelfID(rs.getInt(Order.DELIVERY_GUY_SELF_ID));
//                    order.setDeliveryOTP((Integer) rs.getObject(Order.DELIVERY_OTP));

//
//
//                    order.setCancelledByEndUser(rs.getBoolean(Order.IS_CANCELLED_BY_END_USER));
//                    order.setReasonCancelledByShop(rs.getString(Order.REASON_FOR_CANCELLED_BY_SHOP));
//                    order.setReasonCancelledByUser(rs.getString(Order.REASON_FOR_CANCELLED_BY_USER));
//                    order.setReasonForOrderReturned(rs.getString(Order.REASON_FOR_ORDER_RETURNED));






                    DeliveryAddress address = new DeliveryAddress();

                    address.setName(rs.getString(DeliveryAddress.NAME));
                    address.setPhoneNumber(rs.getLong(DeliveryAddress.PHONE_NUMBER));
                    address.setDeliveryAddress(rs.getString(DeliveryAddress.DELIVERY_ADDRESS));
                    address.setCity(rs.getString(DeliveryAddress.CITY));
                    address.setPincode(rs.getLong(DeliveryAddress.PINCODE));
                    address.setRt_distance(rs.getDouble("distance"));

                    address.setLatitude(rs.getDouble(DeliveryAddress.LATITUDE));
                    address.setLongitude(rs.getDouble(DeliveryAddress.LONGITUDE));




//                    address.setEndUserID(rs.getInt(DeliveryAddress.END_USER_ID));

//                    address.setId(rs.getInt(DeliveryAddress.ID));
//                    address.setLandmark(rs.getString(DeliveryAddress.LANDMARK));



                    order.setDeliveryAddress(address);



                    User deliveryGuy = new User();
                    deliveryGuy.setUserID(rs.getInt(User.USER_ID));
                    deliveryGuy.setName(rs.getString(User.NAME));
                    deliveryGuy.setPhone(rs.getString(User.PHONE));
                    deliveryGuy.setProfileImagePath(rs.getString(User.PROFILE_IMAGE_URL));

                    order.setRt_delivery_guy_profile(deliveryGuy);


                    ordersList.add(order);
                }


                endPoint.setResults(ordersList);
            }






            if(getRowCount)
            {
                statement = connection.prepareStatement(queryCount);
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






    public int updateOrder(Order order)
    {
        String updateStatement = "UPDATE " + Order.TABLE_NAME

                + " SET "
                + Order.END_USER_ID + " = ?,"
                + " " + Order.SHOP_ID + " = ?,"
                + " " + Order.STATUS_HOME_DELIVERY + " = ?,"
                + " " + Order.STATUS_PICK_FROM_SHOP + " = ?,"

//                + " " + Order.PAYMENT_RECEIVED + " = ?,"
//                + " " + Order.DELIVERY_RECEIVED + " = ?,"

                + " " + Order.DELIVERY_CHARGES + " = ?,"
                + " " + Order.DELIVERY_ADDRESS_ID + " = ?,"
                      + Order.DELIVERY_GUY_SELF_ID + " = ?,"
                      + Order.PICK_FROM_SHOP + " = ?"
                + " WHERE " + Order.ORDER_ID + " = ?";



        Connection connection = null;
        PreparedStatement statement = null;
        int updatedRows = -1;

        int i = 0;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);


            statement.setObject(++i,order.getEndUserID());
            statement.setObject(++i,order.getShopID());
            statement.setObject(++i,order.getStatusHomeDelivery());
            statement.setObject(++i,order.getStatusPickFromShop());

//            statement.setObject(5,order.getPaymentReceived());
//            statement.setObject(6,order.getDeliveryReceived());

            statement.setObject(++i,order.getDeliveryCharges());
            statement.setObject(++i,order.getDeliveryAddressID());
            statement.setObject(++i,order.getDeliveryGuySelfID());
            statement.setObject(++i,order.isPickFromShop());
            statement.setObject(++i,order.getOrderID());


            updatedRows = statement.executeUpdate();
            System.out.println("Total rows updated: " + updatedRows);

            //conn.close();

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

        return updatedRows;
    }




    public int setOrderCancelled(int orderID, int shopID, int endUserID)
    {


        String cancelOrderByShop = "UPDATE " + Order.TABLE_NAME
                + " SET " + Order.STATUS_HOME_DELIVERY + " = ?,"
                          + Order.STATUS_PICK_FROM_SHOP + " = ?"
                + " WHERE " + Order.ORDER_ID + " = ?"
                + " AND " + Order.SHOP_ID + " = ?";


        String cancelOrderByEndUser = "UPDATE " + Order.TABLE_NAME

                + " SET " + Order.STATUS_HOME_DELIVERY + " = ?,"
                + Order.STATUS_PICK_FROM_SHOP + " = ?"
                + " WHERE " + Order.ORDER_ID + " = ?"
                + " AND " + Order.END_USER_ID + " = ?";


        // restore the item available quantity from the inventory
        String updateQuantity =
                " Update " + ShopItem.TABLE_NAME +
                        " SET " +  ShopItem.AVAILABLE_ITEM_QUANTITY + " = " +  ShopItem.AVAILABLE_ITEM_QUANTITY + " + " +  OrderItem.ITEM_QUANTITY +
                        " from " +  OrderItem.TABLE_NAME + "," + Order.TABLE_NAME +
                        " where " + OrderItem.TABLE_NAME + "." + OrderItem.ITEM_ID + " = " + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_ID +
                        " and " + Order.TABLE_NAME+ "." + Order.ORDER_ID + " = " + OrderItem.TABLE_NAME+ "."  + OrderItem.ORDER_ID +
                        " and " + ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ID + " = " + Order.TABLE_NAME + "." + Order.SHOP_ID +
                        " and " + Order.TABLE_NAME + "." + Order.ORDER_ID + " = ?";



//        + " SET " + " " + Shop.ACCOUNT_BALANCE + " = " + Shop.ACCOUNT_BALANCE + " - ( " + Order.DELIVERY_CHARGES + " + " + Order.ITEM_TOTAL + " )"


        String updateAccountBalance = "UPDATE " + Shop.TABLE_NAME
                + " SET " + " " + Shop.ACCOUNT_BALANCE + " = " + Shop.ACCOUNT_BALANCE + " - " + Order.TABLE_NAME + "." + Order.VENDOR_PAYOUT
                + " FROM " + Order.TABLE_NAME
                + " WHERE " + Order.TABLE_NAME + "." + Order.SHOP_ID + " = " + Shop.TABLE_NAME + "." + Shop.SHOP_ID
                + " AND " + Order.TABLE_NAME + "." + Order.ORDER_ID + " = ?"
                + " AND " + Order.TABLE_NAME + "." + Order.PAYMENT_MODE + " = ? ";



        String createTransactionRecord = "INSERT INTO " + Transaction.TABLE_NAME
                + "("

                + Transaction.USER_ID + ","

                + Transaction.TITLE + ","
                + Transaction.DESCRIPTION + ","

                + Transaction.TRANSACTION_TYPE + ","
                + Transaction.TRANSACTION_AMOUNT + ","

                + Transaction.IS_CREDIT + ","

                + Transaction.BALANCE_AFTER_TRANSACTION + ""

                + ") "
                + " SELECT "

                + User.TABLE_NAME + "." + User.USER_ID + ","

                + " 'Order Cancelled : ' ,"
                + " 'Adjustments for Cancellation for Order ID '" +  " || " + Order.TABLE_NAME + "." +  Order.ORDER_ID + "::text " + ","

                + Transaction.TRANSACTION_TYPE_APP_FEE + ","
                + Order.VENDOR_PAYOUT + " ,"

                + " false " + ","
                + Shop.TABLE_NAME + "." + Shop.ACCOUNT_BALANCE + ""

                + " FROM " + User.TABLE_NAME
                + " INNER JOIN " + Shop.TABLE_NAME + " ON ( " + Shop.TABLE_NAME + "." +  Shop.SHOP_ADMIN_ID + " = " + User.TABLE_NAME + "." + User.USER_ID + " ) "
                + " INNER JOIN " + Order.TABLE_NAME + " ON ( " + Order.TABLE_NAME + "." + Order.SHOP_ID + " = " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + " ) "
                + " WHERE " + Order.TABLE_NAME + "." + Order.ORDER_ID + " = ? "
                + " AND " + Order.TABLE_NAME + "." + Order.PAYMENT_MODE + " = ? ";





        String queryRazorPay = "SELECT "

                + RazorPayOrder.LOCAL_ORDER_ID + ","
                + RazorPayOrder.RZP_PAYMENT_ID + ","
                + RazorPayOrder.RZP_ORDER_ID + ","
                + RazorPayOrder.PAID_AMOUNT + ","
                + RazorPayOrder.SHOP_OWNER_PAYOUT + ""

                + " FROM " + RazorPayOrder.TABLE_NAME
                + " WHERE " + RazorPayOrder.LOCAL_ORDER_ID + " = " + orderID;




        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        int updatedRows = -1;

        try {

            connection = dataSource.getConnection();


            if(shopID>0)
            {
                statement = connection.prepareStatement(cancelOrderByShop);

                statement.setObject(1,OrderStatusHomeDelivery.CANCELLED);
                statement.setObject(2,OrderStatusPickFromShop.CANCELLED);
                statement.setObject(3,orderID);
                statement.setObject(4,shopID);

                updatedRows = statement.executeUpdate();
            }


            if(endUserID>0)
            {
                statement = connection.prepareStatement(cancelOrderByEndUser);

                statement.setObject(1,OrderStatusHomeDelivery.CANCELLED);
                statement.setObject(2,OrderStatusPickFromShop.CANCELLED);
                statement.setObject(3,orderID);
                statement.setObject(4,endUserID);

                updatedRows = statement.executeUpdate();
            }




            statement = connection.prepareStatement(updateQuantity);
            statement.setObject(1,orderID);

            statement.executeUpdate();



            statement = connection.prepareStatement(updateAccountBalance);

            int i = 0;

            statement.setObject(++i,orderID);
            statement.setObject(++i,Order.PAYMENT_MODE_RAZORPAY);
            statement.executeUpdate();


            statement = connection.prepareStatement(createTransactionRecord);

            i = 0;

            statement.setObject(++i,orderID);
            statement.setObject(++i,Order.PAYMENT_MODE_RAZORPAY);
            statement.executeUpdate();





            statement = connection.prepareStatement(queryRazorPay);
            rs = statement.executeQuery();
            RazorPayOrder razorPayOrder;

            while(rs.next())
            {

                razorPayOrder = new RazorPayOrder();
                razorPayOrder.setPaidAmount(rs.getDouble(RazorPayOrder.PAID_AMOUNT));
                razorPayOrder.setRzpPaymentID(rs.getString(RazorPayOrder.RZP_PAYMENT_ID));
                razorPayOrder.setRzpOrderID(rs.getString(RazorPayOrder.RZP_ORDER_ID));


                try {

                    // refund a payment because the order is cancelled !

                    RazorpayClient razorpayClient = new RazorpayClient(appProperties.getRazorpay_key_id(), appProperties.getRazorpay_key_secret());
                    Refund refund = razorpayClient.Payments.refund(razorPayOrder.getRzpPaymentID());

                } catch (RazorpayException e) {
                    // Handle Exception
                    System.out.println(e.getMessage());
                }
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

        return updatedRows;
    }


    public int cancelOrderByShop(int orderID, int shopID)
    {

        Order order = daoOrderUtility.readOrderStatus(orderID);

        if(order!=null) {


            if(order.getDeliveryMode()==Order.DELIVERY_MODE_PICKUP_FROM_SHOP)
            {
                int status = order.getStatusPickFromShop();

                if (status == OrderStatusPickFromShop.ORDER_PLACED ||
                        status == OrderStatusPickFromShop.ORDER_CONFIRMED ||
                        status == OrderStatusPickFromShop.ORDER_PACKED ||
                        status == OrderStatusPickFromShop.ORDER_READY_FOR_PICKUP)
                {

                    return setOrderCancelled(order.getOrderID(),shopID,0);
                }



            }
            else if(order.getDeliveryMode()==Order.DELIVERY_MODE_HOME_DELIVERY)
            {
//                else if(order.getDeliveryMode()==Order.DELIVERY_MODE_DELIVERY_BY_MARKET||order.getDeliveryMode()==Order.DELIVERY_MODE_DELIVERY_BY_VENDOR)

                int status = order.getStatusHomeDelivery();

                if (status == OrderStatusHomeDelivery.ORDER_PLACED ||
                        status == OrderStatusHomeDelivery.ORDER_CONFIRMED ||
                        status == OrderStatusHomeDelivery.ORDER_PACKED ||
                        status == OrderStatusHomeDelivery.RETURNED_ORDERS)
                {
                    return setOrderCancelled(order.getOrderID(),shopID,0);
                }

            }


        }

        return 0;
    }


    public int cancelOrderByEndUser(int orderID, int endUserID)
    {

        Order order = daoOrderUtility.readOrderStatus(orderID);

        if(order!=null) {


            if(order.getDeliveryMode()==Order.DELIVERY_MODE_PICKUP_FROM_SHOP)
            {
                int status = order.getStatusPickFromShop();

                if (status == OrderStatusPickFromShop.ORDER_PLACED ||
                        status == OrderStatusPickFromShop.ORDER_CONFIRMED ||
                        status == OrderStatusPickFromShop.ORDER_PACKED ||
                        status == OrderStatusPickFromShop.ORDER_READY_FOR_PICKUP)
                {

                    return setOrderCancelled(order.getOrderID(),0,endUserID);
                }


            }
            else if(order.getDeliveryMode()==Order.DELIVERY_MODE_HOME_DELIVERY)
            {
//                else if(order.getDeliveryMode()==Order.DELIVERY_MODE_DELIVERY_BY_MARKET||order.getDeliveryMode()==Order.DELIVERY_MODE_DELIVERY_BY_VENDOR)

                int status = order.getStatusHomeDelivery();

                if (status == OrderStatusHomeDelivery.ORDER_PLACED ||
                        status == OrderStatusHomeDelivery.ORDER_CONFIRMED ||
                        status == OrderStatusHomeDelivery.ORDER_PACKED)
                {
                    return setOrderCancelled(order.getOrderID(),0,endUserID);
                }

            }


        }

        return 0;
    }



    public int setCancelledByEndUser(int orderID, int endUserID)
    {
        String updateStatement = "UPDATE " + Order.TABLE_NAME

                + " SET " + Order.STATUS_HOME_DELIVERY + " = ?,"
                            + Order.STATUS_PICK_FROM_SHOP + " = ?"
                + " WHERE " + Order.ORDER_ID + " = ?"
                + " AND " + Order.END_USER_ID + " = ?";



        // restore the item available quantity from the inventory
        String updateQuantity =
                " Update " + ShopItem.TABLE_NAME +
                        " SET " +  ShopItem.AVAILABLE_ITEM_QUANTITY + " = " +  ShopItem.AVAILABLE_ITEM_QUANTITY + " + " +  OrderItem.ITEM_QUANTITY +
                        " from " +  OrderItem.TABLE_NAME + "," + Order.TABLE_NAME +
                        " where " + OrderItem.TABLE_NAME + "." + OrderItem.ITEM_ID + " = " + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_ID +
                        " and " + Order.TABLE_NAME+ "." + Order.ORDER_ID + " = " + OrderItem.TABLE_NAME+ "."  + OrderItem.ORDER_ID +
                        " and " + ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ID + " = " + Order.TABLE_NAME + "." + Order.SHOP_ID +
                        " and " + Order.TABLE_NAME + "." + Order.ORDER_ID + " = ?";




        String updateAccountBalance = "UPDATE " + Shop.TABLE_NAME
                + " SET " + " " + Shop.ACCOUNT_BALANCE + " = " + Shop.ACCOUNT_BALANCE + " - " + Order.TABLE_NAME + "." + Order.VENDOR_PAYOUT
                + " FROM " + Order.TABLE_NAME
                + " WHERE " + Order.TABLE_NAME + "." + Order.SHOP_ID + " = " + Shop.TABLE_NAME + "." + Shop.SHOP_ID
                + " AND " + Order.TABLE_NAME + "." + Order.ORDER_ID + " = ?"
                + " AND " + Order.TABLE_NAME + "." + Order.PAYMENT_MODE + " = ? ";



        String createTransactionRecord = "INSERT INTO " + Transaction.TABLE_NAME
                + "("

                + Transaction.USER_ID + ","

                + Transaction.TITLE + ","
                + Transaction.DESCRIPTION + ","

                + Transaction.TRANSACTION_TYPE + ","
                + Transaction.TRANSACTION_AMOUNT + ","

                + Transaction.IS_CREDIT + ","

                + Transaction.BALANCE_AFTER_TRANSACTION + ""

                + ") "
                + " SELECT "

                + User.TABLE_NAME + "." + User.USER_ID + ","

                + " 'Order Cancelled : ' ,"
                + " 'Adjustments for Cancellation for Order ID '" +  " || " + Order.TABLE_NAME + "." +  Order.ORDER_ID + "::text " + ","

                + Transaction.TRANSACTION_TYPE_APP_FEE + ","
                + Order.VENDOR_PAYOUT + " ,"

                + " false " + ","
                + Shop.TABLE_NAME + "." + Shop.ACCOUNT_BALANCE + ""

                + " FROM " + User.TABLE_NAME
                + " INNER JOIN " + Shop.TABLE_NAME + " ON ( " + Shop.TABLE_NAME + "." +  Shop.SHOP_ADMIN_ID + " = " + User.TABLE_NAME + "." + User.USER_ID + " ) "
                + " INNER JOIN " + Order.TABLE_NAME + " ON ( " + Order.TABLE_NAME + "." + Order.SHOP_ID + " = " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + " ) "
                + " WHERE " + Order.TABLE_NAME + "." + Order.ORDER_ID + " = ? "
                + " AND " + Order.TABLE_NAME + "." + Order.PAYMENT_MODE + " = ? ";




        String queryRazorPay = "SELECT "

                + RazorPayOrder.LOCAL_ORDER_ID + ","
                + RazorPayOrder.RZP_PAYMENT_ID + ","
                + RazorPayOrder.RZP_ORDER_ID + ","
                + RazorPayOrder.PAID_AMOUNT + ","
                + RazorPayOrder.SHOP_OWNER_PAYOUT + ""

                + " FROM " + RazorPayOrder.TABLE_NAME
                + " WHERE " + RazorPayOrder.LOCAL_ORDER_ID + " = " + orderID;




        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        int updatedRows = -1;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);

            statement.setObject(1,OrderStatusHomeDelivery.CANCELLED);
            statement.setObject(2,OrderStatusPickFromShop.CANCELLED);
            statement.setObject(3,orderID);
            statement.setObject(4,endUserID);


            updatedRows = statement.executeUpdate();


            statement = connection.prepareStatement(updateQuantity);
            statement.setObject(1,orderID);

            statement.executeUpdate();



            statement = connection.prepareStatement(updateAccountBalance);

            int i = 0;

            statement.setObject(++i,orderID);
            statement.setObject(++i,Order.PAYMENT_MODE_RAZORPAY);
            statement.executeUpdate();


            statement = connection.prepareStatement(createTransactionRecord);

            i = 0;

            statement.setObject(++i,orderID);
            statement.setObject(++i,Order.PAYMENT_MODE_RAZORPAY);
            statement.executeUpdate();





            statement = connection.prepareStatement(queryRazorPay);
            rs = statement.executeQuery();
            RazorPayOrder razorPayOrder;

            while(rs.next())
            {

                razorPayOrder = new RazorPayOrder();
                razorPayOrder.setPaidAmount(rs.getDouble(RazorPayOrder.PAID_AMOUNT));
                razorPayOrder.setRzpPaymentID(rs.getString(RazorPayOrder.RZP_PAYMENT_ID));
                razorPayOrder.setRzpOrderID(rs.getString(RazorPayOrder.RZP_ORDER_ID));


                try {

                    // refund a payment because the order is cancelled !

                    RazorpayClient razorpayClient = new RazorpayClient(appProperties.getRazorpay_key_id(), appProperties.getRazorpay_key_secret());
                    Refund refund = razorpayClient.Payments.refund(razorPayOrder.getRzpPaymentID());

                } catch (RazorpayException e) {
                    // Handle Exception
                    System.out.println(e.getMessage());
                }
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

        return updatedRows;
    }


}
