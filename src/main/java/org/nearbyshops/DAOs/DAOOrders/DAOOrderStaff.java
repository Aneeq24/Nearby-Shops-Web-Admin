package org.nearbyshops.DAOs.DAOOrders;


import org.nearbyshops.Model.ModelOrderStatus.OrderStatusHomeDelivery;
import org.nearbyshops.Model.ModelOrderStatus.OrderStatusPickFromShop;
import org.nearbyshops.Model.Order;
import org.nearbyshops.Model.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


@Component
public class DAOOrderStaff {


    @Autowired
    DataSource dataSource;


    public int confirmOrder(int orderID)
    {
        String updateStatement = "UPDATE " + Order.TABLE_NAME

                + " SET " + " " + Order.STATUS_HOME_DELIVERY + " = ?"
                + " WHERE " + Order.ORDER_ID + " = ?"
                + " AND "  + Order.STATUS_HOME_DELIVERY + " = ? ";


        Connection connection = null;
        PreparedStatement statement = null;
        int updatedRows = -1;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);

            int i = 0;

            statement.setObject(++i, OrderStatusHomeDelivery.ORDER_CONFIRMED);
            statement.setObject(++i,orderID);
            statement.setObject(++i, OrderStatusHomeDelivery.ORDER_PLACED);

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




    public int setOrderPacked(int orderID)
    {
        String updateStatement = "UPDATE " + Order.TABLE_NAME

                + " SET " + " " + Order.STATUS_HOME_DELIVERY + " = ?"
                + " WHERE " + Order.ORDER_ID + " = ?"
                + " AND "  + Order.STATUS_HOME_DELIVERY + " = ? ";


        Connection connection = null;
        PreparedStatement statement = null;
        int updatedRows = -1;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);

            int i = 0;

            statement.setObject(++i, OrderStatusHomeDelivery.ORDER_PACKED);
            statement.setObject(++i,orderID);
            statement.setObject(++i, OrderStatusHomeDelivery.ORDER_CONFIRMED);




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




    public int deliverBySelf(int orderID)
    {

        String updateStatement = "UPDATE " + Order.TABLE_NAME

                + " SET " + " " + Order.STATUS_HOME_DELIVERY + " = ?"
                + " WHERE " + Order.ORDER_ID + " = ?"
                + " AND "  + Order.STATUS_HOME_DELIVERY + " = ? "
                + " AND "  + Order.DELIVERY_GUY_SELF_ID + " IS NULL ";




        Connection connection = null;
        PreparedStatement statement = null;
        int updatedRows = -1;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);

            int i = 0;

            statement.setObject(++i, OrderStatusHomeDelivery.OUT_FOR_DELIVERY);
            statement.setObject(++i,orderID);
            statement.setObject(++i, OrderStatusHomeDelivery.ORDER_PACKED);




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



    public int deliverOrder(int orderID)
    {
        String updateStatement = "UPDATE " + Order.TABLE_NAME

                + " SET " + " " + Order.STATUS_HOME_DELIVERY + " = ?"
                + " WHERE " + Order.ORDER_ID + " = ?"
                + " AND "  + Order.STATUS_HOME_DELIVERY + " = ? "
                + " AND "  + Order.DELIVERY_GUY_SELF_ID + " IS NULL ";

        Connection connection = null;
        PreparedStatement statement = null;
        int updatedRows = -1;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);

            int i = 0;

            statement.setObject(++i, OrderStatusHomeDelivery.PAYMENT_RECEIVED);
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



    public int returnOrder(int orderID)
    {
        String updateStatement = "UPDATE " + Order.TABLE_NAME

                + " SET " + " " + Order.STATUS_HOME_DELIVERY + " = ?"
                + " WHERE " + Order.ORDER_ID + " = ?"
                + " AND "  + Order.STATUS_HOME_DELIVERY + " = ? "
                + " AND "  + Order.DELIVERY_GUY_SELF_ID + " IS NULL ";

        Connection connection = null;
        PreparedStatement statement = null;
        int updatedRows = -1;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);

            int i = 0;

            statement.setObject(++i, OrderStatusHomeDelivery.RETURNED_ORDERS);
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



    public int handoverToDelivery(int orderID,int deliveryGuyID)
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



    public int undoHandover(int orderID)
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


//            statement.setObject(1,order.getEndUserID());
//            statement.setObject(1,order.getShopID());
            statement.setObject(++i,OrderStatusHomeDelivery.ORDER_PACKED);
//            statement.setObject(4,order.getStatusPickFromShop());
//            statement.setObject(2,order.getPaymentReceived());
//            statement.setObject(3,order.getDeliveryReceived());
//            statement.setObject(7,order.getDeliveryCharges());
//            statement.setObject(8,order.getDeliveryAddressID());
            statement.setObject(++i,null);
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




    public int acceptReturn(int orderID)
    {

        String updateStatement = "UPDATE " + Order.TABLE_NAME
                                + " SET " + Order.STATUS_HOME_DELIVERY + " = ?,"
                                            + Order.DELIVERY_GUY_SELF_ID + " = ?"

                                + " WHERE " + Order.ORDER_ID + " = ?"
                                + " AND "  + Order.STATUS_HOME_DELIVERY + " = ? ";


        Connection connection = null;
        PreparedStatement statement = null;
        int updatedRows = -1;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);

            int i = 0;

            statement.setObject(++i, OrderStatusHomeDelivery.RETURNED_ORDERS);
            statement.setObject(++i, null);
            statement.setObject(++i, orderID);
            statement.setObject(++i, OrderStatusHomeDelivery.RETURN_REQUESTED);

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




    public int unpackOrder_delete(int orderID)
    {

        String deleteStatement = "DELETE FROM " + OrderItem.TABLE_NAME
                                + " WHERE " + OrderItem.ORDER_ID + " = ?";

        String deleteStatementOrder = "DELETE FROM " + Order.TABLE_NAME
                                    + " WHERE " + Order.ORDER_ID + " = ?";


        Connection connection= null;
        PreparedStatement statement = null;
        int rowCountDeleted = 0;
        try {


            connection = dataSource.getConnection();
            connection.setAutoCommit(false);

            statement = connection.prepareStatement(deleteStatement);
            statement.setInt(1,orderID);
            rowCountDeleted = statement.executeUpdate();


            statement = connection.prepareStatement(deleteStatementOrder);
            statement.setInt(1,orderID);
            rowCountDeleted = rowCountDeleted + statement.executeUpdate();


            System.out.println("Rows Deleted: " + rowCountDeleted);


            connection.commit();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();


            if (connection != null) {
                try {

//                    idOfInsertedRow=-1;
//                    rowCountItems = 0;

                    connection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
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




    public int paymentReceived(int orderID)
    {

        String updateStatement = "UPDATE " + Order.TABLE_NAME

                + " SET " + " " + Order.STATUS_HOME_DELIVERY + " = ?"
                + " WHERE " + Order.ORDER_ID + " = ?"
                + " AND "  + Order.STATUS_HOME_DELIVERY + " = ? ";





        Connection connection = null;
        PreparedStatement statement = null;
        int updatedRows = -1;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);

            int i = 0;

            statement.setObject(++i, OrderStatusHomeDelivery.PAYMENT_RECEIVED);
            statement.setObject(++i,orderID);
            statement.setObject(++i, OrderStatusHomeDelivery.DELIVERED);

            updatedRows = statement.executeUpdate();




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




    public int confirmOrderPFS(int orderID)
    {
        String updateStatement = "UPDATE " + Order.TABLE_NAME

                + " SET " + " " + Order.STATUS_PICK_FROM_SHOP + " = ?"
                + " WHERE " + Order.ORDER_ID + " = ?"
                + " AND "  + Order.STATUS_PICK_FROM_SHOP + " = ? ";


        Connection connection = null;
        PreparedStatement statement = null;
        int updatedRows = -1;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);

            int i = 0;

            statement.setObject(++i, OrderStatusPickFromShop.ORDER_CONFIRMED);
            statement.setObject(++i,orderID);
            statement.setObject(++i, OrderStatusPickFromShop.ORDER_PLACED);

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



    public int setOrderPackedPFS(int orderID)
    {
        String updateStatement = "UPDATE " + Order.TABLE_NAME
                + " SET " + Order.STATUS_PICK_FROM_SHOP + " = ?"
                + " WHERE " + Order.ORDER_ID + " = ?"
                + " AND "  + Order.STATUS_PICK_FROM_SHOP + " = ? ";


//        + " AND "  + Order.SHOP_ID + " = ? "



        Connection connection = null;
        PreparedStatement statement = null;
        int updatedRows = -1;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);

            int i = 0;

            statement.setObject(++i, OrderStatusPickFromShop.ORDER_PACKED);
            statement.setObject(++i,orderID);
            statement.setObject(++i, OrderStatusPickFromShop.ORDER_CONFIRMED);




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



    public int setOrderReadyForPickupPFS(int orderID)
    {
        String updateStatement = "UPDATE " + Order.TABLE_NAME
                + " SET " + Order.STATUS_PICK_FROM_SHOP + " = ?"
                + " WHERE " + Order.ORDER_ID + " = ?"
                + " AND "  + Order.STATUS_PICK_FROM_SHOP + " = ? ";


//        + " AND "  + Order.SHOP_ID + " = ? "



        Connection connection = null;
        PreparedStatement statement = null;
        int updatedRows = -1;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);

            int i = 0;

            statement.setObject(++i, OrderStatusPickFromShop.ORDER_READY_FOR_PICKUP);
            statement.setObject(++i,orderID);
            statement.setObject(++i, OrderStatusPickFromShop.ORDER_PACKED);




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



    public int paymentReceivedPFS(int orderID)
    {


        String updateStatement = "UPDATE " + Order.TABLE_NAME

                + " SET " + " " + Order.STATUS_PICK_FROM_SHOP + " = ?"
                + " WHERE " + Order.ORDER_ID + " = ?"
                + " AND "  + Order.STATUS_PICK_FROM_SHOP + " = ? ";





        Connection connection = null;
        PreparedStatement statement = null;
        int updatedRows = -1;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);

            int i = 0;

            statement.setObject(++i, OrderStatusPickFromShop.DELIVERED);
            statement.setObject(++i,orderID);
            statement.setObject(++i, OrderStatusPickFromShop.ORDER_READY_FOR_PICKUP);

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



}
