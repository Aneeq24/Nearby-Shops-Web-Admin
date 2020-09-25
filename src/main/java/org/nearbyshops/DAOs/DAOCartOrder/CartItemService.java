package org.nearbyshops.DAOs.DAOCartOrder;

import com.zaxxer.hikari.HikariDataSource;
import org.nearbyshops.Model.Cart;
import org.nearbyshops.Model.CartItem;
import org.nearbyshops.Model.Item;
import org.nearbyshops.Model.ShopItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;


@Component
public class CartItemService {


	@Autowired
	DataSource dataSource;


	public int saveCartItem(CartItem cartItem)
	{	
		
		Connection connection = null;
		PreparedStatement statement = null;
		int rowCount = -1;

		String insertEndUser = "INSERT INTO "
				+ CartItem.TABLE_NAME
				+ "("  
				+ CartItem.CART_ID + ","
				+ CartItem.ITEM_ID + ","
				+ CartItem.ITEM_QUANTITY + ""
				+ ") VALUES(?,?,?)";



		try {
			
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(insertEndUser,Statement.RETURN_GENERATED_KEYS);

			statement.setObject(1,cartItem.getCartID());
			statement.setObject(2,cartItem.getItemID());
			statement.setObject(3,cartItem.getItemQuantity());


			rowCount = statement.executeUpdate();
			
//			ResultSet rs = statement.getGeneratedKeys();

			
			
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

		
		return rowCount;
	}



	public int updateCartItem(CartItem cartItem)
	{	
		String updateStatement = "UPDATE " + CartItem.TABLE_NAME
				+ " SET " + CartItem.ITEM_ID + " = ?,"
							+ CartItem.CART_ID + " = ?,"
							+ CartItem.ITEM_QUANTITY + " = ? "

				+ " WHERE " + CartItem.CART_ID + " = ?"
				+ " AND " + CartItem.ITEM_ID + " = ?";



		Connection connection = null;
		PreparedStatement statement = null;
		int updatedRows = -1;
		
		try {
			
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(updateStatement);

			statement.setObject(1,cartItem.getItemID());
			statement.setObject(2,cartItem.getCartID());
			statement.setObject(3,cartItem.getItemQuantity());

			statement.setObject(4,cartItem.getCartID());
			statement.setObject(5,cartItem.getItemID());

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



	public int deleteCartItem(Integer itemID,Integer cartID)
	{

		String deleteStatement = "DELETE FROM " + CartItem.TABLE_NAME + " WHERE TRUE ";


		if(itemID !=null)
		{
			deleteStatement = deleteStatement + " AND " + CartItem.ITEM_ID + " = " + itemID;
		}

		if(cartID !=null)
		{
			deleteStatement = deleteStatement + " AND " + CartItem.CART_ID + " = " + cartID;
		}


		Connection connection= null;
		Statement statement = null;
		int rowsCountDeleted = 0;
		try {
			
			connection = dataSource.getConnection();
			statement = connection.createStatement();


			rowsCountDeleted = statement.executeUpdate(deleteStatement);
			
			connection.close();
			
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
	
		
		return rowsCountDeleted;
	}






	public ArrayList<CartItem> getCartItem(Integer endUserID, Integer shopID,
										   String sortBy,
										   Integer limit, int offset)
	{

		String queryCount = "";


		String query = "SELECT "
				+ Cart.TABLE_NAME + "." + Cart.CART_ID + ","
				+ CartItem.TABLE_NAME + "." + CartItem.ITEM_ID + ","
				+ CartItem.TABLE_NAME + "." + CartItem.ITEM_QUANTITY + ","

				+ ShopItem.AVAILABLE_ITEM_QUANTITY + ","
				+ ShopItem.ITEM_PRICE + ","


				+ Item.TABLE_NAME + "." + Item.ITEM_ID + ","
				+ Item.TABLE_NAME + "." + Item.ITEM_IMAGE_URL + ","
				+ Item.TABLE_NAME + "." + Item.ITEM_NAME + ","
				+ Item.TABLE_NAME + "." + Item.LIST_PRICE + ","
				+ Item.TABLE_NAME + "." + Item.QUANTITY_UNIT + ""

				+ " FROM " + CartItem.TABLE_NAME
				+ " LEFT OUTER JOIN " + Cart.TABLE_NAME + " ON(" + CartItem.TABLE_NAME + "." + CartItem.CART_ID + " = " + Cart.TABLE_NAME + "." + Cart.CART_ID + ")"
				+ " LEFT OUTER JOIN " + Item.TABLE_NAME + " ON(" + Item.TABLE_NAME + "." + Item.ITEM_ID + " = " + CartItem.TABLE_NAME + "." + CartItem.ITEM_ID + "),"
				+ ShopItem.TABLE_NAME
				+ " Where " + ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ID + " = " + Cart.TABLE_NAME + "." + Cart.SHOP_ID
				+ " and " + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_ID + " = " + CartItem.TABLE_NAME + "." + CartItem.ITEM_ID;





		if(endUserID!=null)
		{
			query = query + " and " + Cart.END_USER_ID + " = " + endUserID;
		}


		if(shopID !=null)
		{
			query = query + " and " + Cart.TABLE_NAME + "." + Cart.SHOP_ID + " = " + shopID;
		}




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
			query = query + " LIMIT " + limit + " " + " OFFSET " + offset;;
		}



		queryCount = "SELECT COUNT(*) as item_count FROM (" + queryCount + ") AS temp";





		ArrayList<CartItem> cartItemList = new ArrayList<CartItem>();

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;

		try {

			connection = dataSource.getConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(query);

			while(rs.next())
			{
				CartItem cartItem = new CartItem();

				cartItem.setCartID(rs.getInt(CartItem.CART_ID));
				cartItem.setItemID(rs.getInt(CartItem.ITEM_ID));
				cartItem.setItemQuantity(rs.getDouble(CartItem.ITEM_QUANTITY));

				cartItem.setRt_availableItemQuantity(rs.getInt(ShopItem.AVAILABLE_ITEM_QUANTITY));
				cartItem.setRt_itemPrice(rs.getDouble(ShopItem.ITEM_PRICE));

				Item item = new Item();

				item.setItemID(rs.getInt(Item.ITEM_ID));
				item.setItemImageURL(rs.getString(Item.ITEM_IMAGE_URL));
				item.setItemName(rs.getString(Item.ITEM_NAME));
				item.setListPrice(rs.getFloat(Item.LIST_PRICE));
				item.setQuantityUnit(rs.getString(Item.QUANTITY_UNIT));


				cartItem.setItem(item);

				cartItemList.add(cartItem);
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


		return cartItemList;
	}





	public boolean checkItemsInCart(int cartID)
	{


		String query = "SELECT " + CartItem.TABLE_NAME + "." + CartItem.CART_ID + ""
					+ " FROM " + CartItem.TABLE_NAME
					+ " WHERE " + CartItem.CART_ID + " = " + cartID;;



		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;

		try {

			connection = dataSource.getConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(query);

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




	public CartItem getCartItemAvailability(int endUserID, int shopID, int itemID)
	{



		String query = " SELECT " + CartItem.TABLE_NAME + "." + CartItem.ITEM_ID + ","
							+ CartItem.TABLE_NAME + "." + CartItem.ITEM_QUANTITY + ""

					+ " FROM " + CartItem.TABLE_NAME
					+ " INNER JOIN " + Cart.TABLE_NAME + " ON ( " + CartItem.TABLE_NAME + "." + CartItem.CART_ID + " = " + Cart.TABLE_NAME + "." + Cart.CART_ID + ")"
					+ " WHERE " + Cart.END_USER_ID + " = " + endUserID
					+ " AND " + Cart.SHOP_ID + " = " + shopID
					+ " AND " + CartItem.ITEM_ID + " = " + itemID;



		CartItem cartItem = null;

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;

		try {

			connection = dataSource.getConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(query);

			while(rs.next())
			{
				cartItem = new CartItem();
				cartItem.setItemID(rs.getInt(CartItem.ITEM_ID));
				cartItem.setItemQuantity(rs.getDouble(CartItem.ITEM_QUANTITY));
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


		return cartItem;
	}



	public ArrayList<CartItem> getCartItemAvailability(Integer endUserID, Integer shopID)
	{


		String query = "SELECT " + CartItem.TABLE_NAME + "." + CartItem.ITEM_ID + ","
									+ CartItem.TABLE_NAME + "." + CartItem.ITEM_QUANTITY + ""

					+ " FROM " + CartItem.TABLE_NAME
					+ " INNER JOIN " + Cart.TABLE_NAME + " ON ( " + CartItem.TABLE_NAME + "." + CartItem.CART_ID + " = " + Cart.TABLE_NAME + "." + Cart.CART_ID + ")"
					+ " WHERE " + Cart.END_USER_ID + " = " + endUserID
				    + " AND " + Cart.SHOP_ID + " = " + shopID;



//				+ " INNER JOIN " + ShopItem.TABLE_NAME + " ON ( " + ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ID + " = " + Cart.TABLE_NAME + "." + Cart.SHOP_ID + " ) "
//				+ " and " + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_ID + " = " + CartItem.TABLE_NAME + "." + CartItem.ITEM_ID;





		ArrayList<CartItem> cartItemList = new ArrayList<CartItem>();

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;

		try {

			connection = dataSource.getConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(query);

			while(rs.next())
			{
				CartItem cartItem = new CartItem();
				cartItem.setItemID(rs.getInt(CartItem.ITEM_ID));
				cartItem.setItemQuantity(rs.getDouble(CartItem.ITEM_QUANTITY));

				cartItemList.add(cartItem);
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


		return cartItemList;
	}


	public ArrayList<CartItem> getCartItemAvailabilityByItem(int itemID, int endUserID)
	{

		String query = "SELECT " + CartItem.TABLE_NAME + "." + CartItem.CART_ID + ","
				+ CartItem.TABLE_NAME + "." + CartItem.ITEM_ID + ","
				+ Cart.TABLE_NAME + "." + Cart.SHOP_ID + ","
				+ CartItem.TABLE_NAME + "." + CartItem.ITEM_QUANTITY + ""
				+ " FROM " + CartItem.TABLE_NAME + "," + Cart.TABLE_NAME
				+ " WHERE " + CartItem.TABLE_NAME + "."+ CartItem.CART_ID + " = " + Cart.TABLE_NAME + "." + Cart.CART_ID
				+ " AND " + Cart.END_USER_ID + " = " + endUserID
				+ " AND " + CartItem.ITEM_ID + " = " + itemID;;



		ArrayList<CartItem> cartItemList = new ArrayList<>();



		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;

		try {

			connection = dataSource.getConnection();
			statement = connection.createStatement();

			rs = statement.executeQuery(query);

			while(rs.next())
			{
				CartItem cartItem = new CartItem();

				cartItem.setCartID(rs.getInt(CartItem.CART_ID));
				cartItem.setItemID(rs.getInt(CartItem.ITEM_ID));
				cartItem.setItemQuantity(rs.getDouble(CartItem.ITEM_QUANTITY));

				Cart cart = new Cart();
				cart.setShopID(rs.getInt(Cart.SHOP_ID));

				cartItem.setCart(cart);

				cartItemList.add(cartItem);
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

		return cartItemList;
	}


}
