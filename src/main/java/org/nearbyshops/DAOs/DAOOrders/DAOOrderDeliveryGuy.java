package org.nearbyshops.DAOs.DAOOrders;



import org.nearbyshops.Constants;
import org.nearbyshops.Model.ModelBilling.Transaction;
import org.nearbyshops.Model.ModelOrderStatus.OrderStatusHomeDelivery;
import org.nearbyshops.Model.ModelRoles.User;
import org.nearbyshops.Model.Order;
import org.nearbyshops.Model.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


@Component
public class DAOOrderDeliveryGuy {


    @Autowired
    DataSource dataSource;


    public int pickupOrder(int orderID,int deliveryGuyID)
    {
        String updateStatement = "UPDATE " + Order.TABLE_NAME

                + " SET " + Order.DELIVERY_GUY_SELF_ID + " = ?"
                + " WHERE " + Order.ORDER_ID + " = ?"
                + " AND " + Order.DELIVERY_GUY_SELF_ID + " IS NULL "
                + " AND " + Order.STATUS_HOME_DELIVERY + " <= ? ";


        Connection connection = null;
        PreparedStatement statement = null;
        int updatedRows = -1;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);
            int i = 0;

            statement.setObject(++i,deliveryGuyID);
            statement.setObject(++i,orderID);
            statement.setObject(++i,OrderStatusHomeDelivery.ORDER_PACKED);


            updatedRows = statement.executeUpdate();
//            System.out.println("Total rows updated: " + updatedRows);

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


    public int pickupOrderBackup(int orderID,int deliveryGuyID)
    {
        String updateStatement = "UPDATE " + Order.TABLE_NAME

                + " SET " + Order.STATUS_HOME_DELIVERY + " = ?,"
                + Order.DELIVERY_GUY_SELF_ID + " = ?"
                + " WHERE " + Order.ORDER_ID + " = ?";





        Connection connection = null;
        PreparedStatement statement = null;
        int updatedRows = -1;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);
            int i = 0;

            statement.setObject(++i,OrderStatusHomeDelivery.HANDOVER_REQUESTED);
            statement.setObject(++i,deliveryGuyID);
            statement.setObject(++i,orderID);


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



    public int acceptOrder(int orderID, int deliveryPersonID)
    {
        String updateStatement = "UPDATE " + Order.TABLE_NAME
                            + " SET "   + Order.STATUS_HOME_DELIVERY + " = ?"
                            + " WHERE " + Order.ORDER_ID + " = ?"
                            + " AND "   + Order.DELIVERY_GUY_SELF_ID+ " = ? ";

//        + " AND "   + Order.STATUS_HOME_DELIVERY + " = ? "


        Connection connection = null;
        PreparedStatement statement = null;
        int updatedRows = -1;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);

            int i = 0;

            statement.setObject(++i, OrderStatusHomeDelivery.OUT_FOR_DELIVERY);
            statement.setObject(++i, orderID);
//            statement.setObject(++i, OrderStatusHomeDelivery.ORDER_PACKED);
            statement.setObject(++i, deliveryPersonID);


            updatedRows = statement.executeUpdate();
//            System.out.println("Total rows updated: " + updatedRows);

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



    public int acceptOrderBackup(int orderID)
    {
        String updateStatement = "UPDATE " + Order.TABLE_NAME
                + " SET "   + Order.STATUS_HOME_DELIVERY + " = ?"
                + " WHERE " + Order.ORDER_ID + " = ?"
                + " AND "   + Order.STATUS_HOME_DELIVERY + " = ? ";


        Connection connection = null;
        PreparedStatement statement = null;
        int updatedRows = -1;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);

            int i = 0;

            statement.setObject(++i, OrderStatusHomeDelivery.OUT_FOR_DELIVERY);
            statement.setObject(++i, orderID);
            statement.setObject(++i, OrderStatusHomeDelivery.HANDOVER_REQUESTED);


            updatedRows = statement.executeUpdate();
//            System.out.println("Total rows updated: " + updatedRows);

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



    public int declineOrder(int orderID, int deliveryGuyID)
    {

        // detach or decline order by delivery guy ...

        String updateStatement = "UPDATE " + Order.TABLE_NAME
                + " SET " + Order.DELIVERY_GUY_SELF_ID + " = ?"
                + " WHERE " + Order.ORDER_ID + " = ?"
                + " AND "   + Order.STATUS_HOME_DELIVERY + " <= ? "
                + " AND "   + Order.DELIVERY_GUY_SELF_ID + " = ? ";


        Connection connection = null;
        PreparedStatement statement = null;
        int updatedRows = -1;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);

            int i = 0;

            statement.setObject(++i,null);
            statement.setObject(++i,orderID);
            statement.setObject(++i, OrderStatusHomeDelivery.ORDER_PACKED);
            statement.setObject(++i, deliveryGuyID);



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



    public int deliverOrder(int orderID, int deliveryOTP, int deliveryGuyRole, int paymentMode)
    {

        String updateStatement = "UPDATE " + Order.TABLE_NAME
                + " SET "   + Order.STATUS_HOME_DELIVERY + " = ?"
                + " WHERE " + Order.ORDER_ID + " = ?"
                + " AND "   + Order.STATUS_HOME_DELIVERY + " = ? "
                + " AND "   + Order.DELIVERY_OTP + " = ? ";


        String updateAccountBalance = "UPDATE " + Shop.TABLE_NAME
                + " SET " + " " + Shop.ACCOUNT_BALANCE + " = " + Shop.ACCOUNT_BALANCE + " - " + Order.TABLE_NAME + "." + Order.DELIVERY_CHARGES
                + " FROM " + Order.TABLE_NAME
                + " WHERE " + Order.TABLE_NAME + "." + Order.SHOP_ID + " = " + Shop.TABLE_NAME + "." + Shop.SHOP_ID
                + " AND " + Order.TABLE_NAME + "." + Order.ORDER_ID + " = ?";



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

                + " 'Delivery Fee : ' ,"
                + " 'Delivery Fee for Order ID '" +  " || " + Order.TABLE_NAME + "." +  Order.ORDER_ID + "::text " + ","

                + Transaction.TRANSACTION_TYPE_APP_FEE + ","
                + Order.TABLE_NAME + "." + Order.DELIVERY_CHARGES + " ,"

                + " false " + ","
                + Shop.TABLE_NAME + "." + Shop.ACCOUNT_BALANCE + ""

                + " FROM " + User.TABLE_NAME
                + " INNER JOIN " + Shop.TABLE_NAME + " ON ( " + Shop.TABLE_NAME + "." +  Shop.SHOP_ADMIN_ID + " = " + User.TABLE_NAME + "." + User.USER_ID + " ) "
                + " INNER JOIN " + Order.TABLE_NAME + " ON ( " + Order.TABLE_NAME + "." + Order.SHOP_ID + " = " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + " ) "
                + " WHERE " + Order.TABLE_NAME + "." + Order.ORDER_ID + " = ? ";



        Connection connection = null;
        PreparedStatement statement = null;
        int updatedRows = -1;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);


            int i = 0;

            if(paymentMode==Order.PAYMENT_MODE_CASH_ON_DELIVERY || paymentMode==Order.PAYMENT_MODE_PAY_ONLINE_ON_DELIVERY)
            {
                // if the payment is done using COD or POD then delivery person needs to pay to the vendor
                statement.setObject(++i, OrderStatusHomeDelivery.DELIVERED);
                statement.setObject(++i,orderID);
                statement.setObject(++i, OrderStatusHomeDelivery.OUT_FOR_DELIVERY);
                statement.setObject(++i, deliveryOTP);

                updatedRows = statement.executeUpdate();

            }
            else if(paymentMode==Order.PAYMENT_MODE_RAZORPAY)
            {
                // if the payment is already done then set the status as payment received
                statement.setObject(++i, OrderStatusHomeDelivery.PAYMENT_RECEIVED);
                statement.setObject(++i,orderID);
                statement.setObject(++i, OrderStatusHomeDelivery.OUT_FOR_DELIVERY);
                statement.setObject(++i, deliveryOTP);

                updatedRows = statement.executeUpdate();
            }


//            System.out.println("Total rows updated: " + updatedRows);




            // charge delivery fee to the vendor in case the order is delivered by the market delivery staff
            if(deliveryGuyRole== Constants.ROLE_DELIVERY_GUY_CODE)
            {
                statement = connection.prepareStatement(updateAccountBalance);

                i = 0;

                statement.setObject(++i,orderID);
                statement.executeUpdate();


                statement = connection.prepareStatement(createTransactionRecord);

                i = 0;

                statement.setObject(++i,orderID);
                statement.executeUpdate();
            }



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




    public int returnOrder(int orderID)
    {
        String updateStatement = "UPDATE " + Order.TABLE_NAME
                + " SET "   + Order.STATUS_HOME_DELIVERY + " = ?"
                + " WHERE " + Order.ORDER_ID + " = ?"
                + " AND "   + Order.STATUS_HOME_DELIVERY + " = ? ";


        Connection connection = null;
        PreparedStatement statement = null;
        int updatedRows = -1;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);

            int i = 0;

            statement.setObject(++i, OrderStatusHomeDelivery.RETURN_REQUESTED);
            statement.setObject(++i,orderID);
            statement.setObject(++i, OrderStatusHomeDelivery.OUT_FOR_DELIVERY);


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


}
