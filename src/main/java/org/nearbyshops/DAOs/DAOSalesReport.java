package org.nearbyshops.DAOs;


import com.google.gson.Gson;
import org.nearbyshops.Model.ModelEndpoint.ShopEndPoint;
import org.nearbyshops.Model.ModelRoles.User;
import org.nearbyshops.Model.ModelSalesReport.OrderSalesStats;
import org.nearbyshops.Model.Order;
import org.nearbyshops.Model.Shop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;


@Component
public class DAOSalesReport {


    @Autowired
    DataSource dataSource;

    Logger logger = LoggerFactory.getLogger(DAOSalesReport.class);

    @Autowired
    Gson gson;



    public ShopEndPoint getShopSales(
            Date startDate,
            Date endDate,
            String searchString,
            String sortBy,
            int limit, int offset,
            boolean getRowCount,
            boolean getOnlyMetadata
    ) {


        String queryCount = "";
        String queryJoin = "SELECT "

                + Shop.TABLE_NAME + "." + Shop.SHOP_ID + ","
                + Shop.TABLE_NAME + "." + Shop.SHOP_NAME + ","

                + "min(" + Order.TABLE_NAME + "." + Order.NET_PAYABLE + ") as min_order_price " + ","
                + "max(" + Order.TABLE_NAME + "." + Order.NET_PAYABLE + ") as max_order_price " + ","
                + "avg(" + Order.TABLE_NAME + "." + Order.NET_PAYABLE + ") as avg_order_price " + ","
//                + "count( DISTINCT " + Order.TABLE_NAME + "." + Order.SHOP_ID + ") as shop_count" + ","
                + "count( DISTINCT " + Order.TABLE_NAME + "." + Order.ORDER_ID + ") as order_count " + ","
                + "sum(" + Order.TABLE_NAME + "." + Order.NET_PAYABLE + ") as total_order_sales " + ","
                + "sum("  + Order.TABLE_NAME + "." + Order.VENDOR_PAYOUT + ") as total_vendor_payout " + ""

                + " FROM " + Shop.TABLE_NAME
                + " INNER JOIN " + Order.TABLE_NAME + " ON (" + Order.TABLE_NAME + "." + Order.SHOP_ID + " = " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + ") "
                + " WHERE TRUE ";





        if(startDate != null && endDate != null)
        {
            queryJoin = queryJoin + " AND " + Order.TABLE_NAME + "." + Order.DATE_TIME_PLACED + " >= ? ";
            queryJoin = queryJoin + " AND " + Order.TABLE_NAME + "." + Order.DATE_TIME_PLACED + " < ? ";
        }





        if(searchString !=null)
        {
//            String queryPartSearch = " ( " + Item.TABLE_NAME + "." + Item.ITEM_DESC +" ilike '%" + searchString + "%'"
//                    + " or " + Item.TABLE_NAME + "." + Item.ITEM_DESCRIPTION_LONG + " ilike '%" + searchString + "%'"
//                    + " or " + Item.TABLE_NAME + "." + Item.ITEM_NAME + " ilike '%" + searchString + "%'" + ") ";
//
//            queryJoin = queryJoin + " AND " + queryPartSearch;
        }





        // all the non-aggregate columns which are present in select must be present in group by also.
        queryJoin = queryJoin + " group by " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + "";



        queryCount = queryJoin;


        if(sortBy!=null && !sortBy.equals(""))
        {
            queryJoin = queryJoin +  " ORDER BY " + sortBy;
        }



        queryJoin = queryJoin + " LIMIT " + limit + " OFFSET " + offset;



		/* Applying filters Ends */

        queryCount = "SELECT COUNT(*) as item_count FROM (" + queryCount + ") AS temp";


        ShopEndPoint endPoint = new ShopEndPoint();
        ArrayList<OrderSalesStats> salesList = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;


        int i = 0;


        try {

            connection = dataSource.getConnection();


            if(!getOnlyMetadata)
            {

                statement = connection.prepareStatement(queryJoin);



                if(startDate != null && endDate != null)
                {
                    statement.setObject(++i,startDate);
                    statement.setObject(++i,endDate);
                }



                rs = statement.executeQuery();

                while(rs.next())
                {
                    OrderSalesStats orderStats = new OrderSalesStats();

                    orderStats.setMinOrderPrice(rs.getDouble("min_order_price"));
                    orderStats.setMaxOrderPrice(rs.getDouble("max_order_price"));
                    orderStats.setAvgOrderPrice(rs.getDouble("avg_order_price"));
                    orderStats.setOrderCount(rs.getInt("order_count"));
//
                    orderStats.setTotalOrderSales(rs.getDouble("total_order_sales"));
                    orderStats.setTotalVendorPayout(rs.getDouble("total_vendor_payout"));


                    orderStats.setShopID(rs.getInt(Shop.SHOP_ID));
                    orderStats.setShopName(rs.getString(Shop.SHOP_NAME));

//                    orderStats.setShop(shop);

                    salesList.add(orderStats);


//                    logger.info(gson.toJson(orderStats));
                }



                endPoint.setOrderSalesStatsList(salesList);

            }






            if(getRowCount)
            {
                statement = connection.prepareStatement(queryCount);

                i = 0;


                if(startDate != null && endDate != null)
                {
                    statement.setObject(++i,startDate);
                    statement.setObject(++i,endDate);
                }


                rs = statement.executeQuery();

                while(rs.next())
                {
                    endPoint.setItemCount(rs.getInt("item_count"));
                }
            }






//			System.out.println("Item By CategoryID " + itemList.size());

        }
        catch (SQLException e) {
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



//+ "count( DISTINCT " + Order.TABLE_NAME + "." + Order.ORDER_ID + ") as order_count " + ","

    public double getTotalSales()
    {

        String queryJoin = "SELECT " + "sum(" + Order.TABLE_NAME + "." + Order.NET_PAYABLE + ") as total_order_sales " + ""
                        + " FROM " + Order.TABLE_NAME + " WHERE TRUE ";


        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;


        int i = 0;


        try {

            connection = dataSource.getConnection();

            statement = connection.prepareStatement(queryJoin);



            rs = statement.executeQuery();

            while(rs.next())
            {
                return rs.getDouble("total_order_sales");
            }


        }
        catch (SQLException e) {
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


        return 0;
    }


    public int getOrderCount()
    {

        String queryJoin = "SELECT " + "count( DISTINCT " + Order.TABLE_NAME + "." + Order.ORDER_ID + ") as order_count " + ""
                        + " FROM " + Order.TABLE_NAME + " WHERE TRUE ";


        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;


        int i = 0;


        try {

            connection = dataSource.getConnection();

            statement = connection.prepareStatement(queryJoin);



            rs = statement.executeQuery();

            while(rs.next())
            {
                return rs.getInt("order_count");
            }


        }
        catch (SQLException e) {
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


        return 0;
    }



    public int getVendorCount()
    {

        String queryJoin = "SELECT " + "count( DISTINCT " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + ") as shop_count " + ""
                + " FROM " + Shop.TABLE_NAME + " WHERE TRUE ";


        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;


        int i = 0;


        try {

            connection = dataSource.getConnection();

            statement = connection.prepareStatement(queryJoin);



            rs = statement.executeQuery();

            while(rs.next())
            {
                return rs.getInt("shop_count");
            }


        }
        catch (SQLException e) {
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


        return 0;
    }



    public int getUserCount()
    {

        String queryJoin = "SELECT " + "count( DISTINCT " + User.TABLE_NAME + "." + User.USER_ID + ") as user_count " + ""
                + " FROM " + User.TABLE_NAME + " WHERE TRUE ";


        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;


        int i = 0;


        try {

            connection = dataSource.getConnection();

            statement = connection.prepareStatement(queryJoin);



            rs = statement.executeQuery();

            while(rs.next())
            {
                return rs.getInt("user_count");
            }


        }
        catch (SQLException e) {
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


        return 0;
    }




}
