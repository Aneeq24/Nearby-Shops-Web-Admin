package org.nearbyshops.DAOs.DAOOrders;



import org.nearbyshops.AppProperties;
import org.nearbyshops.DAOs.DAOCartOrder.CartService;
import org.nearbyshops.DAOs.DAOCartOrder.CartStatsDAO;
import org.nearbyshops.DAOs.DAOSettings.MarketSettingsDAO;
import org.nearbyshops.Model.*;
import org.nearbyshops.Model.ModelBilling.RazorPayOrder;
import org.nearbyshops.Model.ModelBilling.Transaction;
import org.nearbyshops.Model.ModelRoles.User;
import org.nearbyshops.Model.ModelStats.CartStats;
import org.nearbyshops.Utility.UtilityMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


/**
 * Created by sumeet on 7/6/16.
 */



@Component
public class PlaceOrderDAO {



    @Autowired
    DataSource dataSource;

    @Autowired
    DAOOrderUtility daoOrderUtility;

    @Autowired
    CartService cartService;



    @Autowired
    CartStatsDAO cartStatsDAO;

    @Autowired
    AppProperties appProperties;

    @Autowired
    MarketSettingsDAO marketSettingsDAO;



    public int placeOrderNew(Order order, int cartID) {

        Connection connection = null;
        PreparedStatement statement = null;



        Cart cart = cartService.readCart(cartID);
        List<CartStats> cartStats = cartStatsDAO.getCartStats(cart.getEndUserID(),cartID,cart.getShopID());
        Shop shop = daoOrderUtility.getShopDetailsForCreateOrder(cart.getShopID());


        int orderID = -1;
        int copiedItemsCount = -1;
        int updatedItemsCount = -1;


        String copyCartToOrder = " insert into " + Order.TABLE_NAME
                + " ( "
                + Order.END_USER_ID + ","
                + Order.SHOP_ID + ","

                + " " + Order.STATUS_HOME_DELIVERY + ","
                + " " + Order.STATUS_PICK_FROM_SHOP + ","

//                + " " + Order.PAYMENT_RECEIVED + ","
//                + " " + Order.DELIVERY_RECEIVED + ","

                + " " + Order.ITEM_COUNT + ","
                + " " + Order.ITEM_TOTAL + ","
                + " " + Order.APP_SERVICE_CHARGE + ","
                + " " + Order.DELIVERY_CHARGES + ","
                + " " + Order.SAVINGS_OVER_MRP + ","
                + " " + Order.NET_PAYABLE + ","
                + " " + Order.VENDOR_PAYOUT + ","


                + " " + Order.DELIVERY_ADDRESS_ID + ","

                + " " + Order.DELIVERY_OTP + ","
                + " " + Order.DELIVERY_DATE + ","
//                + " " + Order.DELIVERY_SLOT + ","
                + " " + Order.DELIVERY_MODE + ","
                + " " + Order.PAYMENT_MODE + ""

                + " ) " +
                " select "
                + Cart.END_USER_ID + ","
                + Cart.SHOP_ID + ","
                + " 1 " + ","
                + " 1 " + ","

//                + " false " + ","
//                + " false " + ","

                + " ? " + ","
                + " ? " + ","
                + " ? " + ","
                + " ? " + ","
                + " ? " + ","
                + " ? " + ","
                + " ? " + ","

                + " ? " + ","

                + " ? " + ","
                + " ? " + ","
//                + " ? " + ","
                + " ? " + ","
                + " ? " + ""

                + " from " + Cart.TABLE_NAME
                + " where " + Cart.CART_ID + " = ?";




        String copyCartItemToOrderItem =

                "insert into " + OrderItem.TABLE_NAME +
                        " ("
                        + OrderItem.ORDER_ID  + ","
                        + OrderItem.ITEM_ID + ","
                        + OrderItem.ITEM_PRICE_AT_ORDER + ","
                        + OrderItem.LIST_PRICE_AT_ORDER + ","
                        + OrderItem.ITEM_QUANTITY + ") " +


                        " select " + " ? " + ","
                        + ShopItem.TABLE_NAME+ "." + ShopItem.ITEM_ID + ","
                        + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_PRICE + ","
                        + Item.TABLE_NAME + "." + Item.LIST_PRICE + ","
                        + CartItem.TABLE_NAME + "." + CartItem.ITEM_QUANTITY
                        + " from "
                        + CartItem.TABLE_NAME + ","
                        + Cart.TABLE_NAME + ","
                        + ShopItem.TABLE_NAME  + ","
                        + Item.TABLE_NAME  +
                        " where "
                        + Cart.TABLE_NAME + "." + Cart.CART_ID + " = " + CartItem.TABLE_NAME + "." + CartItem.CART_ID +
                        " and "
                        + ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ID + " = " + Cart.TABLE_NAME + "." + Cart.SHOP_ID +
                        " and "
                        + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_ID + " = " + CartItem.TABLE_NAME + "." + CartItem.ITEM_ID +
                        " and "
                        + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_ID + " = " + Item.TABLE_NAME + "." + Item.ITEM_ID +
                        " and "
                        + Cart.TABLE_NAME + "." + Cart.CART_ID + " = ? ";




        // reduce the item available quantity from the inventory
        String updateQuantity =
                        " Update " + ShopItem.TABLE_NAME +
                        " SET " +  ShopItem.AVAILABLE_ITEM_QUANTITY + " = " +  ShopItem.AVAILABLE_ITEM_QUANTITY + " - " +  OrderItem.ITEM_QUANTITY +
                        " from " +  OrderItem.TABLE_NAME + "," + Order.TABLE_NAME +
                        " where " + OrderItem.TABLE_NAME + "." + OrderItem.ITEM_ID + " = " + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_ID +
                        " and " + Order.TABLE_NAME+ "." + Order.ORDER_ID + " = " + OrderItem.TABLE_NAME+ "."  + OrderItem.ORDER_ID +
                        " and " + ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ID + " = " + Order.TABLE_NAME + "." + Order.SHOP_ID +
                        " and " + Order.TABLE_NAME + "." + Order.ORDER_ID + " = ?";



        String deleteCartItems = " DELETE FROM " + CartItem.TABLE_NAME +
                            " WHERE " + Cart.CART_ID + " = ?";


        String deleteCart = " DELETE FROM " + Cart.TABLE_NAME +
                                 " WHERE " + Cart.CART_ID + " = ?";





        String updateAccountBalance = "UPDATE " + Shop.TABLE_NAME

                + " SET " + " " + Shop.ACCOUNT_BALANCE + " = " + Shop.ACCOUNT_BALANCE + " + ? "
                + " WHERE " + Shop.SHOP_ID
                + " = ( SELECT " + Order.SHOP_ID + " FROM " + Order.TABLE_NAME + " WHERE " + Order.ORDER_ID + " = ? )";




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

                + " 'Payout : ' ,"
                + " 'Payout for Order ID '" +  " || " + Order.TABLE_NAME + "." +  Order.ORDER_ID + "::text " + ","

                + Transaction.TRANSACTION_TYPE_APP_FEE + ","
                + " ? ,"

                + " false " + ","
                + Shop.TABLE_NAME + "." + Shop.ACCOUNT_BALANCE + ""

                + " FROM " + User.TABLE_NAME
                + " INNER JOIN " + Shop.TABLE_NAME + " ON ( " + Shop.TABLE_NAME + "." +  Shop.SHOP_ADMIN_ID + " = " + User.TABLE_NAME + "." + User.USER_ID + " ) "
                + " INNER JOIN " + Order.TABLE_NAME + " ON ( " + Order.TABLE_NAME + "." + Order.SHOP_ID + " = " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + " ) "
                + " WHERE " + Order.TABLE_NAME + "." + Order.ORDER_ID + " = ? ";



        String saveRazorPayInfo = " INSERT INTO " + RazorPayOrder.TABLE_NAME
                + " ( "
                + RazorPayOrder.LOCAL_ORDER_ID + ","
                + RazorPayOrder.RZP_PAYMENT_ID + ","
                + RazorPayOrder.RZP_ORDER_ID + ","
                + RazorPayOrder.PAID_AMOUNT + ","
                + RazorPayOrder.SHOP_OWNER_PAYOUT + ""

                + " )  VALUES(?,?, ?,?,?) ";




        try {

            connection = dataSource.getConnection();
            connection.setAutoCommit(false);

            int itemCount = 0;
            double itemTotal = 0;
            double marketFee = 0;
            double deliveryCharges = 0;
            double savingsOverMRP = 0;
            double netPayable = 0;

            double vendorPayout = 0;



            // calculate and set different kind of charges
            if(cartStats.size()==1)
            {

                itemCount = cartStats.get(0).getItemsInCart();
                itemTotal = cartStats.get(0).getCart_Total();
                savingsOverMRP = cartStats.get(0).getSavingsOverMRP();
//                    appServiceCharge = 10;


                if(order.getDeliveryMode()==Order.DELIVERY_MODE_PICKUP_FROM_SHOP)
                {
                    deliveryCharges = 0;
                }
                else
                {

                    if(marketSettingsDAO.getSettingsInstance().isUseStandardDeliveryFee())
                    {
                        deliveryCharges = marketSettingsDAO.getSettingsInstance().getMarketDeliveryFeePerOrder();
                    }
                    else
                    {
                        if(cartStats.get(0).getCart_Total() < shop.getBillAmountForFreeDelivery())
                        {
                            deliveryCharges = shop.getDeliveryCharges();
                        }
                        else
                        {
                            deliveryCharges = 0; // delivery free above this amount
                        }
                    }

                }




                if(order.getDeliveryMode()==Order.DELIVERY_MODE_PICKUP_FROM_SHOP)
                {
                    marketFee = marketSettingsDAO.getSettingsInstance().getMarketFeePickupFromShop();
                }
                else
                {
                    marketFee = marketSettingsDAO.getSettingsInstance().getMarketFeeHomeDelivery();
                }



                if(marketSettingsDAO.getSettingsInstance().isAddMarketFeeToBill())
                {
                    netPayable = itemTotal + marketFee + deliveryCharges;
                }
                else
                {
                    netPayable = itemTotal  + deliveryCharges;
                }




                if(order.getPaymentMode()==Order.PAYMENT_MODE_CASH_ON_DELIVERY)
                {
                    vendorPayout = 0 - marketFee;
                }
                else if(order.getPaymentMode()==Order.PAYMENT_MODE_PAY_ONLINE_ON_DELIVERY)
                {
                    vendorPayout = 0 - marketFee;
                }
                else if(order.getPaymentMode()==Order.PAYMENT_MODE_RAZORPAY)
                {
                    vendorPayout = netPayable - marketFee;
                }



                if(!marketSettingsDAO.getSettingsInstance().isAddMarketFeeToBill())
                {
                    marketFee = 0;
                }

            }





            statement = connection.prepareStatement(copyCartToOrder,PreparedStatement.RETURN_GENERATED_KEYS);

            int i = 0;

            statement.setInt(++i,itemCount); // item count
            statement.setDouble(++i,itemTotal); // item total
            statement.setDouble(++i,marketFee); // app service charge
            statement.setDouble(++i,deliveryCharges); // delivery charge
            statement.setDouble(++i,savingsOverMRP); // Savings Over MRP
            statement.setDouble(++i,netPayable); // net payable
            statement.setDouble(++i,vendorPayout); // vendor payout

            statement.setInt(++i,order.getDeliveryAddressID());
//            statement.setBoolean(8,order.isPickFromShop());



            statement.setInt(++i,Integer.parseInt(String.valueOf(UtilityMethods.generateOTP(4))));
            statement.setDate(++i,order.getDeliveryDate());


//            if(order.getDeliverySlotID()==-1 || order.getDeliverySlotID()==0)
//            {
//                statement.setObject(++i,null);
//            }
//            else
//            {
//                statement.setObject(++i,order.getDeliverySlotID());
//            }



            statement.setObject(++i,order.getDeliveryMode());
            statement.setObject(++i,order.getPaymentMode());


            statement.setInt(++i,cartID);

            statement.executeUpdate();




            ResultSet rsCopyCartToOrder = statement.getGeneratedKeys();

            if(rsCopyCartToOrder.next())
            {
                orderID = rsCopyCartToOrder.getInt(1);
            }


            statement = connection.prepareStatement(copyCartItemToOrderItem,PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1,orderID);
            statement.setInt(2,cartID);
            copiedItemsCount = statement.executeUpdate();


            statement = connection.prepareStatement(updateQuantity,PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1,orderID);
            updatedItemsCount = statement.executeUpdate();



            statement = connection.prepareStatement(deleteCart,PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1,cartID);
            statement.executeUpdate();


            statement = connection.prepareStatement(deleteCartItems,PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1,cartID);
            statement.executeUpdate();





            statement = connection.prepareStatement(updateAccountBalance);

            i = 0;

            statement.setObject(++i, vendorPayout);
            statement.setObject(++i,orderID);

            statement.executeUpdate();




            statement = connection.prepareStatement(createTransactionRecord);

            i = 0;

            statement.setObject(++i, vendorPayout);
            statement.setObject(++i,orderID);

            statement.executeUpdate();



            if(order.getPaymentMode()==Order.PAYMENT_MODE_RAZORPAY)
            {
                statement = connection.prepareStatement(saveRazorPayInfo);

                i = 0;

                RazorPayOrder razorPayOrder = order.getRazorPayOrder();

                statement.setObject(++i, orderID);
                statement.setObject(++i,razorPayOrder.getRzpPaymentID());
                statement.setObject(++i,razorPayOrder.getRzpOrderID());
                statement.setObject(++i,netPayable);
                statement.setObject(++i,vendorPayout);

                statement.executeUpdate();
            }



            connection.commit();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            if (connection != null) {
                try {

//                    rowIdUserID = -1;
                    orderID = -1;
                    connection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }

        } finally {



            if (statement != null) {
                try {

                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }




            try {

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }



        return orderID;
    }


}
