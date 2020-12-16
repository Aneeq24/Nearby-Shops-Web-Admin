package org.nearbyshops.DAOs;



import org.nearbyshops.Constants;
import org.nearbyshops.DAOs.DAOSettings.MarketSettingsDAO;
import org.nearbyshops.DAOs.DAOSettings.ServiceConfigurationDAO;
import org.nearbyshops.Model.*;
import org.nearbyshops.Model.ModelEndpoint.ItemEndPoint;
import org.nearbyshops.Model.ModelItemSpecification.ItemSpecificationItem;
import org.nearbyshops.Model.ModelItemSpecification.ItemSpecificationValue;
import org.nearbyshops.Model.ModelReviewItem.ItemReview;
import org.nearbyshops.Model.ModelStats.ItemStats;
import org.nearbyshops.Utility.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;



@Component
public class ItemDAO {


	@Autowired
	DataSource dataSource;

	@Autowired
	MarketSettingsDAO marketSettingsDAO;


	@Autowired
	ServiceConfigurationDAO serviceConfigurationDAO;


	public int saveItem(Item item, boolean getRowCount)
	{


		Connection connection = null;
		PreparedStatement statement = null;
		int idOfInsertedRow = -1;
		int rowCountItems = 0;

		String insertItemCategory = "INSERT INTO "
				+ Item.TABLE_NAME
				+ "("
				+ Item.ITEM_NAME + ","
				+ Item.ITEM_DESC + ","

				+ Item.ITEM_IMAGE_URL + ","
				+ Item.ITEM_CATEGORY_ID + ","

				+ Item.QUANTITY_UNIT + ","
				+ Item.TIMESTAMP_UPDATED + ","
				+ Item.ITEM_DESCRIPTION_LONG + ","

				+ Item.LIST_PRICE + ","
//				+ Item.DISCOUNTED_PRICE + ","
				+ Item.BARCODE + ","
				+ Item.BARCODE_FORMAT + ","
				+ Item.IMAGE_COPYRIGHTS + ""

				+ ") VALUES(?,? ,?,?, ?,?,?, ?,?,?,?)";

		try {

			connection = dataSource.getConnection();
			statement = connection.prepareStatement(insertItemCategory,PreparedStatement.RETURN_GENERATED_KEYS);

			int i = 0;
			statement.setString(++i,item.getItemName());
			statement.setString(++i,item.getItemDescription());

			statement.setString(++i,item.getItemImageURL());
			statement.setInt(++i,item.getItemCategoryID());

			statement.setString(++i,item.getQuantityUnit());
			statement.setTimestamp(++i,new Timestamp(System.currentTimeMillis()));
			statement.setString(++i,item.getItemDescriptionLong());

			statement.setFloat(++i,item.getListPrice());
//			statement.setFloat(++i,item.getDiscountedPrice());
			statement.setString(++i,item.getBarcode());
			statement.setString(++i,item.getBarcodeFormat());
			statement.setString(++i,item.getImageCopyrights());

//			statement.setObject(++i,item.getGidbItemID());
//			statement.setString(++i,item.getGidbServiceURL());



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



	public int changeParent(Item item)
	{

		String updateStatement = "UPDATE " + Item.TABLE_NAME

				+ " SET "

//				+ " " + Item.ITEM_NAME + " = ?,"
//				+ " " + Item.ITEM_DESC + " = ?,"
//				+ " " + Item.ITEM_IMAGE_URL + " = ?,"

				+ " " + Item.ITEM_CATEGORY_ID + " = ?"
//				+ " " + Item.QUANTITY_UNIT + " = ?,"
//				+ " " + Item.ITEM_DESCRIPTION_LONG + " = ?"

				+ " WHERE " + Item.ITEM_ID + " = ?";


		Connection connection = null;
		PreparedStatement statement = null;

		int rowCountUpdated = 0;

		try {

			connection = dataSource.getConnection();
			statement = connection.prepareStatement(updateStatement);

//			statement.setString(1,item.getItemName());
//			statement.setString(2,item.getItemDescription());
//			statement.setString(3,item.getItemImageURL());

			if(item.getItemCategoryID()!=null && (item.getItemCategoryID()==-1||item.getItemCategoryID()==0))
			{
				item.setItemCategoryID(null);
			}

			statement.setObject(1,item.getItemCategoryID());
//			statement.setString(5,item.getQuantityUnit());
//			statement.setString(6,item.getItemDescriptionLong());
			statement.setInt(2,item.getItemID());

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



	public int changeParentBulk(List<Item> itemList)
	{

		String updateStatement = "UPDATE " + Item.TABLE_NAME

				+ " SET "

//				+ " " + Item.ITEM_NAME + " = ?,"
//				+ " " + Item.ITEM_DESC + " = ?,"
//				+ " " + Item.ITEM_IMAGE_URL + " = ?,"

				+ " " + Item.ITEM_CATEGORY_ID + " = ?"
//				+ " " + Item.QUANTITY_UNIT + " = ?,"
//				+ " " + Item.ITEM_DESCRIPTION_LONG + " = ?"

				+ " WHERE " + Item.ITEM_ID + " = ?";


		Connection connection = null;
		PreparedStatement statement = null;

		int rowCountUpdated = 0;

		try {

			connection = dataSource.getConnection();
			statement = connection.prepareStatement(updateStatement);


			for(Item item : itemList)
			{
//				statement.setString(1,item.getItemName());
//				statement.setString(2,item.getItemDescription());
//				statement.setString(3,item.getItemImageURL());
				if(item.getItemCategoryID()!=null && item.getItemCategoryID()==-1)
				{
					item.setItemCategoryID(null);
				}

				statement.setObject(1,item.getItemCategoryID());
//				statement.setString(5,item.getQuantityUnit());
//				statement.setString(6,item.getItemDescriptionLong());
				statement.setInt(2,item.getItemID());

				statement.addBatch();
			}


			int[] totalsArray = statement.executeBatch();

			for(int i : totalsArray)
			{
				rowCountUpdated = rowCountUpdated + i;
			}

//			System.out.println("Total rows updated: UPDATE BULK " + rowCountUpdated);


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



	public int updateItem(Item item)
	{

		String updateStatement = "UPDATE " + Item.TABLE_NAME

				+ " SET "

				+ Item.ITEM_NAME + "=?,"
				+ Item.ITEM_DESC + "=?,"

				+ Item.ITEM_IMAGE_URL + "=?,"
				+ Item.ITEM_CATEGORY_ID + "=?,"

				+ Item.QUANTITY_UNIT + "=?,"
				+ Item.TIMESTAMP_UPDATED + "=?,"
				+ Item.ITEM_DESCRIPTION_LONG + "=?,"

				+ Item.LIST_PRICE + "=?,"
				+ Item.BARCODE + "=?,"
				+ Item.BARCODE_FORMAT + "=?,"
				+ Item.IMAGE_COPYRIGHTS + "=?"

				+ " WHERE " + Item.ITEM_ID + " = ?";


//						+ Item.DISCOUNTED_PRICE + "=?,"



		Connection connection = null;
		PreparedStatement statement = null;

		int rowCountUpdated = 0;

		try {

			connection = dataSource.getConnection();
			statement = connection.prepareStatement(updateStatement);

			int i = 0;
			statement.setString(++i,item.getItemName());
			statement.setString(++i,item.getItemDescription());

			statement.setString(++i,item.getItemImageURL());
			statement.setInt(++i,item.getItemCategoryID());

			statement.setString(++i,item.getQuantityUnit());
			statement.setTimestamp(++i,new Timestamp(System.currentTimeMillis()));
			statement.setString(++i,item.getItemDescriptionLong());

			statement.setFloat(++i,item.getListPrice());
//			statement.setFloat(++i,item.getDiscountedPrice());
			statement.setString(++i,item.getBarcode());
			statement.setString(++i,item.getBarcodeFormat());
			statement.setString(++i,item.getImageCopyrights());


			statement.setObject(++i,item.getItemID());


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




	public int deleteItem(int itemID)
	{

		String deleteStatement = "DELETE FROM " + Item.TABLE_NAME + " WHERE " + Item.ITEM_ID + " = ?";

		Connection connection= null;
		PreparedStatement statement = null;
		int rowCountDeleted = 0;
		try {
			
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(deleteStatement);
			statement.setInt(1,itemID);

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




	
	public ItemEndPoint getItems(
					Integer itemCategoryID, Integer shopID,
					Double latCenter, Double lonCenter,
					String itemSpecValues,
					Double deliveryRangeMin,Double deliveryRangeMax,
					Double proximity,
					String searchString,
					String sortBy,
					int limit, int offset,
					boolean getRowCount,
					boolean getOnlyMetadata
	)
	{

		String queryCount = "";

//		String query = "";


		String queryJoin = "SELECT "
				+ "min(" + ShopItem.ITEM_PRICE + ") as min_price" + ","
				+ "max(" + ShopItem.ITEM_PRICE + ") as max_price" + ","
				+ "avg(" + ShopItem.ITEM_PRICE + ") as avg_price" + ","
				+ "count( DISTINCT " + ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ID + ") as shop_count" + ","

				+ Item.TABLE_NAME + "." + Item.ITEM_ID + ","
				+ Item.TABLE_NAME + "." + Item.ITEM_NAME + ","
				+ Item.TABLE_NAME + "." + Item.ITEM_IMAGE_URL + ","
				+ Item.TABLE_NAME + "." + Item.ITEM_CATEGORY_ID + ","
				+ Item.TABLE_NAME + "." + Item.QUANTITY_UNIT + ","
				+ Item.TABLE_NAME + "." + Item.LIST_PRICE + ","


//				+ Item.TABLE_NAME + "." + Item.ITEM_DESC + ","

//				+ Item.TABLE_NAME + "." + Item.DATE_TIME_CREATED + ","
//				+ Item.TABLE_NAME + "." + Item.TIMESTAMP_UPDATED + ","
//				+ Item.TABLE_NAME + "." + Item.ITEM_DESCRIPTION_LONG + ","

//				+ Item.TABLE_NAME + "." + Item.BARCODE + ","
//				+ Item.TABLE_NAME + "." + Item.BARCODE_FORMAT + ","
//				+ Item.TABLE_NAME + "." + Item.IMAGE_COPYRIGHTS + ","


				+  "avg(" + ItemReview.TABLE_NAME + "." + ItemReview.RATING + ") as avg_rating" + ","
				+  "count( DISTINCT " + ItemReview.TABLE_NAME + "." + ItemReview.END_USER_ID + ") as rating_count" + ","
				+  "(avg(" + ItemReview.TABLE_NAME + "." + ItemReview.RATING + ")* count( DISTINCT " + ItemReview.TABLE_NAME + "." + ItemReview.END_USER_ID + ") ) as popularity"


				+ " FROM " + ItemCategory.TABLE_NAME
				+ " INNER JOIN " + Item.TABLE_NAME + " ON ( " + Item.TABLE_NAME + "." + Item.ITEM_CATEGORY_ID + " = " + ItemCategory.TABLE_NAME + "." + ItemCategory.ITEM_CATEGORY_ID + " ) "
				+ " INNER JOIN " + ShopItem.TABLE_NAME + " ON ( " + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_ID + "=" + Item.TABLE_NAME + "." + Item.ITEM_ID + " ) "
				+ " INNER JOIN " + Shop.TABLE_NAME + " ON ( " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + "=" + ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ID + " ) "
				+ " LEFT OUTER JOIN " + ItemReview.TABLE_NAME + " ON ( " + ItemReview.TABLE_NAME + "." + ItemReview.ITEM_ID + " = " + Item.TABLE_NAME + "." + Item.ITEM_ID + " ) "
				+ " LEFT OUTER JOIN " + ItemSpecificationItem.TABLE_NAME + " ON ( " + Item.TABLE_NAME + "." + Item.ITEM_ID + " = "  +  ItemSpecificationItem.TABLE_NAME + "." + ItemSpecificationItem.ITEM_ID + " ) "
				+ " LEFT OUTER JOIN " + ItemSpecificationValue.TABLE_NAME + " ON ( " + ItemSpecificationValue.TABLE_NAME + "." + ItemSpecificationValue.ID + " = " + ItemSpecificationItem.TABLE_NAME + "." + ItemSpecificationItem.ITEM_SPECIFICATION_VALUE_ID + " ) "

				+ " WHERE " + Shop.TABLE_NAME + "." + Shop.IS_OPEN + " = TRUE "
				+ " AND " + Shop.TABLE_NAME + "." + Shop.SHOP_ENABLED + " = TRUE "
				+ " AND " + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_PRICE + " > 0 "
				+ " AND " + Shop.TABLE_NAME + "." + Shop.ACCOUNT_BALANCE + ">=" + marketSettingsDAO.getSettingsInstance().getMinAccountBalanceForShopOwner();




//		+ " AND " + ShopItem.TABLE_NAME + "." + ShopItem.AVAILABLE_ITEM_QUANTITY + " > 0 "


		if(itemSpecValues!=null)
		{
			queryJoin = queryJoin + " AND " + ItemSpecificationValue.TABLE_NAME + "." + ItemSpecificationValue.ID + " IN ( " + itemSpecValues + " ) ";
		}






		if(shopID != null)
		{
				queryJoin = queryJoin + " AND "
						+ Shop.TABLE_NAME
						+ "."
						+ Shop.SHOP_ID + " = " + shopID;
			
		}



		if(itemCategoryID != null)
		{
			queryJoin = queryJoin + " AND "
					+ Item.TABLE_NAME
					+ "."
					+ Item.ITEM_CATEGORY_ID + " = " + itemCategoryID;
		}







//		if(Globals.licensingRestrictionsEnabled)
//		{
//			double latMarket = serviceConfigurationDAO.getMarketConfiguration().getLatCenter();
//			double lonMarket = serviceConfigurationDAO.getMarketConfiguration().getLonCenter();
//
//
//			queryJoin = queryJoin + " AND (6371.01 * acos(cos( radians(" + latMarket + ")) * cos( radians(" + Shop.LAT_CENTER
//					+ " )) * cos(radians( " + Shop.LON_CENTER + ") - radians(" + lonMarket + "))"
//					+ " + sin( radians(" + latMarket + ")) * sin(radians(" + Shop.LAT_CENTER + ")))) <= " + Globals.maxMarketRangeInKms ;
//		}






		// Applying filters

		if(latCenter !=null && lonCenter !=null)
		{
			// Applying shop visibility filter. Gives all the shops which are visible at the given location defined by
			// latCenter and lonCenter. For more information see the API documentation.

			String queryPartlatLonCenterNew = "";

			queryPartlatLonCenterNew = queryPartlatLonCenterNew
					+ " (6371.01 * acos(cos( radians(" + latCenter + ")) * cos( radians("
					+ Shop.LAT_CENTER + " )) * cos(radians( "
					+ Shop.LON_CENTER + ") - radians(" + lonCenter + "))"
					+ " + sin( radians(" + latCenter + ")) * sin(radians(" + Shop.LAT_CENTER
					+ ")))) <= " + Shop.DELIVERY_RANGE ;


			queryJoin = queryJoin + " AND " + queryPartlatLonCenterNew;
		}









		if(deliveryRangeMin !=null  ||deliveryRangeMax !=null){

			// apply delivery range filter

			queryJoin = queryJoin
					+ " AND "
					+ Shop.TABLE_NAME
					+ "."
					+ Shop.DELIVERY_RANGE
					+ " BETWEEN " + deliveryRangeMin + " AND " + deliveryRangeMax;

			//+ " <= " + deliveryRange;
		}






		// proximity cannot be greater than the delivery range if the delivery range is supplied. Otherwise this condition is
		// not required.
		if(proximity !=null)
		{

			String queryPartProximity = "";

			// filter using Haversine formula using SQL math functions
			queryPartProximity = queryPartProximity
					+ " (6371.01 * acos(cos( radians("
					+ latCenter
					+ ")) * cos( radians("
					+ Shop.LAT_CENTER
					+ " )) * cos(radians( "
					+ Shop.LON_CENTER
					+ ") - radians("
					+ lonCenter
					+ "))"
					+ " + sin( radians("
					+ latCenter
					+ ")) * sin(radians("
					+ Shop.LAT_CENTER
					+ ")))) <= "
					+ proximity ;


			queryJoin = queryJoin + " AND " + queryPartProximity;
		}




		if(searchString !=null)
		{
			String queryPartSearch = Item.TABLE_NAME + "." + Item.ITEM_NAME +" ilike '%" + searchString + "%'";
			queryJoin = queryJoin + " AND " + queryPartSearch;
		}



		// all the non-aggregate columns which are present in select must be present in group by also.
		queryJoin = queryJoin

				+ " group by "
				+ Item.TABLE_NAME + "." + Item.ITEM_ID ;




		queryCount = queryJoin;



		if(sortBy!=null)
		{
			if(!sortBy.equals(""))
			{
				String queryPartSortBy = " ORDER BY " + sortBy;


				queryJoin = queryJoin + queryPartSortBy;
			}
		}



//		if(limit != null)
//		{
//
//			String queryPartLimitOffset = "";
//
//			if(offset>0)
//			{
//				queryPartLimitOffset = " LIMIT " + limit + " " + " OFFSET " + offset;
//
//			}else
//			{
//				queryPartLimitOffset = " LIMIT " + limit + " " + " OFFSET " + 0;
//			}
//
//
////			queryNormal = queryNormal + queryPartLimitOffset;
//			queryJoin = queryJoin + queryPartLimitOffset;
//		}


		queryJoin  = queryJoin + " LIMIT " + limit + " " + " OFFSET " + offset;




		queryCount = "SELECT COUNT(*) as item_count FROM (" + queryCount + ") AS temp";




		ItemEndPoint endPoint = new ItemEndPoint();

		ArrayList<Item> itemList = new ArrayList<>();
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

//		PreparedStatement statementCount = null;
//		ResultSet resultSetCount = null;


		try {
			
			connection = dataSource.getConnection();

			int i = 0;


			if(!getOnlyMetadata) {


				statement = connection.prepareStatement(queryJoin);




				rs = statement.executeQuery();

				while (rs.next()) {
					Item item = new Item();

					item.setItemID(rs.getInt(Item.ITEM_ID));
					item.setItemName(rs.getString(Item.ITEM_NAME));
					item.setItemImageURL(rs.getString(Item.ITEM_IMAGE_URL));
					item.setItemCategoryID(rs.getInt(Item.ITEM_CATEGORY_ID));
					item.setQuantityUnit(rs.getString(Item.QUANTITY_UNIT));
					item.setListPrice(rs.getFloat(Item.LIST_PRICE));


//					item.setItemDescription(rs.getString(Item.ITEM_DESC));
//					item.setItemDescriptionLong(rs.getString(Item.ITEM_DESCRIPTION_LONG));
//					item.setBarcode(rs.getString(Item.BARCODE));
//					item.setBarcodeFormat(rs.getString(Item.BARCODE_FORMAT));
//					item.setImageCopyrights(rs.getString(Item.IMAGE_COPYRIGHTS));


					//				item.setDateTimeCreated(rs.getTimestamp(Item.DATE_TIME_CREATED));

//				if(isJoinQuery)
//				{
					ItemStats itemStats = new ItemStats();
					itemStats.setMax_price(rs.getDouble("max_price"));
					itemStats.setMin_price(rs.getDouble("min_price"));
					itemStats.setAvg_price(rs.getDouble("avg_price"));
					itemStats.setShopCount(rs.getInt("shop_count"));
					item.setItemStats(itemStats);

					item.setRt_rating_avg(rs.getFloat("avg_rating"));
					item.setRt_rating_count(rs.getFloat("rating_count"));

//				}

					itemList.add(item);
				}

				endPoint.setResults(itemList);
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






	public ItemEndPoint getItemsRecursive(
			Integer itemCategoryID, Integer shopID,
			Double latCenter, Double lonCenter,
			String itemSpecValues,
			Double deliveryRangeMin,Double deliveryRangeMax,
			Double proximity,
			String searchString,
			String sortBy,
			int limit, int offset,
			boolean getRowCount,
			boolean getOnlyMetadata
	)
	{

		String queryCount = "";



		// a recursive CTE (Common table Expression) query. This query is used for retrieving hierarchical / tree set data.

		String withRecursiveStart = "WITH RECURSIVE category_tree("
				+ ItemCategory.ITEM_CATEGORY_ID + ","
				+ ItemCategory.PARENT_CATEGORY_ID
				+ ") AS (";


		String queryJoin = "SELECT "

				+ ItemCategory.TABLE_NAME + "." + ItemCategory.ITEM_CATEGORY_ID + ","
				+ ItemCategory.TABLE_NAME + "." + ItemCategory.PARENT_CATEGORY_ID

				+ " FROM "
				+ ItemCategory.TABLE_NAME

				+ " WHERE "
				+ ItemCategory.ITEM_CATEGORY_ID  + " = " + itemCategoryID;


		String union = " UNION ";

		String querySelect = " SELECT "

				+ "cat." + ItemCategory.ITEM_CATEGORY_ID + ","
				+ "cat." + ItemCategory.PARENT_CATEGORY_ID

				+ " FROM category_tree tempCat," + 	ItemCategory.TABLE_NAME + " cat"
				+ " WHERE cat." + ItemCategory.PARENT_CATEGORY_ID
				+ " = tempcat." + ItemCategory.ITEM_CATEGORY_ID
				+ " )";


		String queryLast = " SELECT "
				+ ItemCategory.ITEM_CATEGORY_ID
				+ " FROM category_tree";



		String queryRecursive = withRecursiveStart + queryJoin + union + querySelect +  queryLast;




		String queryJoinItems = "SELECT "
				+ "min(" + ShopItem.ITEM_PRICE + ") as min_price" + ","
				+ "max(" + ShopItem.ITEM_PRICE + ") as max_price" + ","
				+ "avg(" + ShopItem.ITEM_PRICE + ") as avg_price" + ","
				+ "count( DISTINCT " + ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ID + ") as shop_count" + ","

				+ Item.TABLE_NAME + "." + Item.ITEM_ID + ","
				+ Item.TABLE_NAME + "." + Item.ITEM_NAME + ","
				+ Item.TABLE_NAME + "." + Item.ITEM_IMAGE_URL + ","
				+ Item.TABLE_NAME + "." + Item.ITEM_CATEGORY_ID + ","
				+ Item.TABLE_NAME + "." + Item.QUANTITY_UNIT + ","
				+ Item.TABLE_NAME + "." + Item.LIST_PRICE + ","


//				+ Item.TABLE_NAME + "." + Item.ITEM_DESC + ","

//				+ Item.TABLE_NAME + "." + Item.DATE_TIME_CREATED + ","
//				+ Item.TABLE_NAME + "." + Item.TIMESTAMP_UPDATED + ","
//				+ Item.TABLE_NAME + "." + Item.ITEM_DESCRIPTION_LONG + ","

//				+ Item.TABLE_NAME + "." + Item.BARCODE + ","
//				+ Item.TABLE_NAME + "." + Item.BARCODE_FORMAT + ","
//				+ Item.TABLE_NAME + "." + Item.IMAGE_COPYRIGHTS + ","

				+  "((" + Item.TABLE_NAME + "." + Item.LIST_PRICE + " - " + " avg(" + ShopItem.ITEM_PRICE + ")" + ") / GREATEST (" + Item.TABLE_NAME + "." + Item.LIST_PRICE + ",1) ) * 100 " +" as discount_percent " + ","
				+  "(count( DISTINCT " + (OrderItem.TABLE_NAME + "." + OrderItem.ITEM_ID) + ") * sum ( DISTINCT " + OrderItem.TABLE_NAME + "." + OrderItem.ITEM_QUANTITY + " ) ) as item_sold " + ","
				+  "avg(" + ItemReview.TABLE_NAME + "." + ItemReview.RATING + ") as avg_rating" + ","
				+  "count( DISTINCT " + ItemReview.TABLE_NAME + "." + ItemReview.END_USER_ID + ") as rating_count" + ""


				+ " FROM " + ItemCategory.TABLE_NAME
				+ " INNER JOIN " + Item.TABLE_NAME + " ON ( " + Item.TABLE_NAME + "." + Item.ITEM_CATEGORY_ID + " = " + ItemCategory.TABLE_NAME + "." + ItemCategory.ITEM_CATEGORY_ID + " ) "
				+ " INNER JOIN " + ShopItem.TABLE_NAME + " ON ( " + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_ID + "=" + Item.TABLE_NAME + "." + Item.ITEM_ID + " ) "
				+ " INNER JOIN " + Shop.TABLE_NAME + " ON ( " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + "=" + ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ID + " ) "
				+ " LEFT OUTER JOIN " + ItemReview.TABLE_NAME + " ON ( " + ItemReview.TABLE_NAME + "." + ItemReview.ITEM_ID + " = " + Item.TABLE_NAME + "." + Item.ITEM_ID + " ) "
				+ " LEFT OUTER JOIN " + ItemSpecificationItem.TABLE_NAME + " ON ( " + Item.TABLE_NAME + "." + Item.ITEM_ID + " = "  +  ItemSpecificationItem.TABLE_NAME + "." + ItemSpecificationItem.ITEM_ID + " ) "
				+ " LEFT OUTER JOIN " + ItemSpecificationValue.TABLE_NAME + " ON ( " + ItemSpecificationValue.TABLE_NAME + "." + ItemSpecificationValue.ID + " = " + ItemSpecificationItem.TABLE_NAME + "." + ItemSpecificationItem.ITEM_SPECIFICATION_VALUE_ID + " ) "
				+ " LEFT OUTER JOIN " + OrderItem.TABLE_NAME + " ON (" + OrderItem.TABLE_NAME + "." + OrderItem.ITEM_ID + " = " + Item.TABLE_NAME + "." + Item.ITEM_ID + ")"

				+ " WHERE " + Shop.TABLE_NAME + "." + Shop.IS_OPEN + " = TRUE "
				+ " AND " + Shop.TABLE_NAME + "." + Shop.SHOP_ENABLED + " = TRUE "
				+ " AND " + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_PRICE + " > 0 "
				+ " AND " + Shop.TABLE_NAME + "." + Shop.ACCOUNT_BALANCE + ">=" + marketSettingsDAO.getSettingsInstance().getMinAccountBalanceForShopOwner();




//		+  "(avg(" + ItemReview.TABLE_NAME + "." + ItemReview.RATING + ")* count( DISTINCT " + ItemReview.TABLE_NAME + "." + ItemReview.END_USER_ID + ") ) as popularity "

//		+ " AND " + ShopItem.TABLE_NAME + "." + ShopItem.AVAILABLE_ITEM_QUANTITY + " > 0 "


		if(itemSpecValues!=null)
		{
			queryJoinItems = queryJoinItems + " AND " + ItemSpecificationValue.TABLE_NAME + "." + ItemSpecificationValue.ID + " IN ( " + itemSpecValues + " ) ";
		}






		if(shopID != null)
		{
			queryJoinItems = queryJoinItems + " AND "
					+ Shop.TABLE_NAME
					+ "."
					+ Shop.SHOP_ID + " = " + shopID;

		}




		if(itemCategoryID != null && itemCategoryID!=1)
		{

//			queryJoinItems = queryJoinItems + " AND "
//					+ Item.TABLE_NAME
//					+ "."
//					+ Item.ITEM_CATEGORY_ID + " = " + itemCategoryID;

			queryJoinItems = queryJoinItems
					+ " AND " + ItemCategory.TABLE_NAME + "." + ItemCategory.ITEM_CATEGORY_ID + " IN " + " (" + queryRecursive + ")";

//			System.out.println("Item Cat ID : " + itemCategoryID);
		}






		if(Globals.licensingRestrictionsEnabled)
		{
			double latMarket = serviceConfigurationDAO.getMarketConfiguration().getLatCenter();
			double lonMarket = serviceConfigurationDAO.getMarketConfiguration().getLonCenter();


			queryJoinItems = queryJoinItems + " AND (6371.01 * acos(cos( radians(" + latMarket + ")) * cos( radians(" + Shop.LAT_CENTER
					+ " )) * cos(radians( " + Shop.LON_CENTER + ") - radians(" + lonMarket + "))"
					+ " + sin( radians(" + latMarket + ")) * sin(radians(" + Shop.LAT_CENTER + ")))) <= " + Globals.maxMarketRangeInKms ;
		}






		// Applying filters

		if(latCenter !=null && lonCenter !=null)
		{
			// Applying shop visibility filter. Gives all the shops which are visible at the given location defined by
			// latCenter and lonCenter. For more information see the API documentation.

			String queryPartlatLonCenterNew = "";

			queryPartlatLonCenterNew = queryPartlatLonCenterNew
					+ " (6371.01 * acos(cos( radians(" + latCenter + ")) * cos( radians("
					+ Shop.LAT_CENTER + " )) * cos(radians( "
					+ Shop.LON_CENTER + ") - radians(" + lonCenter + "))"
					+ " + sin( radians(" + latCenter + ")) * sin(radians(" + Shop.LAT_CENTER
					+ ")))) <= " + Shop.DELIVERY_RANGE ;


			queryJoinItems = queryJoinItems + " AND " + queryPartlatLonCenterNew;
		}









		if(deliveryRangeMin !=null  ||deliveryRangeMax !=null){

			// apply delivery range filter

			queryJoinItems = queryJoinItems
					+ " AND "
					+ Shop.TABLE_NAME
					+ "."
					+ Shop.DELIVERY_RANGE
					+ " BETWEEN " + deliveryRangeMin + " AND " + deliveryRangeMax;

			//+ " <= " + deliveryRange;
		}






		// proximity cannot be greater than the delivery range if the delivery range is supplied. Otherwise this condition is
		// not required.
		if(proximity !=null)
		{

			String queryPartProximity = "";

			// filter using Haversine formula using SQL math functions
			queryPartProximity = queryPartProximity
					+ " (6371.01 * acos(cos( radians("
					+ latCenter
					+ ")) * cos( radians("
					+ Shop.LAT_CENTER
					+ " )) * cos(radians( "
					+ Shop.LON_CENTER
					+ ") - radians("
					+ lonCenter
					+ "))"
					+ " + sin( radians("
					+ latCenter
					+ ")) * sin(radians("
					+ Shop.LAT_CENTER
					+ ")))) <= "
					+ proximity ;


			queryJoinItems = queryJoinItems + " AND " + queryPartProximity;
		}




		if(searchString !=null)
		{
			String queryPartSearch = Item.TABLE_NAME + "." + Item.ITEM_NAME +" ilike '%" + searchString + "%'";
			queryJoinItems = queryJoinItems + " AND " + queryPartSearch;
		}



		// all the non-aggregate columns which are present in select must be present in group by also.
		queryJoinItems = queryJoinItems

				+ " group by "
				+ Item.TABLE_NAME + "." + Item.ITEM_ID ;




		queryCount = queryJoinItems;



		if(sortBy!=null)
		{
			if(!sortBy.equals(""))
			{
				String queryPartSortBy = " ORDER BY " + sortBy;


				queryJoinItems = queryJoinItems + queryPartSortBy;
			}
		}




		queryJoinItems  = queryJoinItems + " LIMIT " + limit + " " + " OFFSET " + offset;


		queryCount = "SELECT COUNT(*) as item_count FROM (" + queryCount + ") AS temp";




		ItemEndPoint endPoint = new ItemEndPoint();

		ArrayList<Item> itemList = new ArrayList<>();

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;


		try {

			connection = dataSource.getConnection();

			int i = 0;


			if(!getOnlyMetadata) {


				statement = connection.prepareStatement(queryJoinItems);




				rs = statement.executeQuery();

				while (rs.next()) {
					Item item = new Item();

					item.setItemID(rs.getInt(Item.ITEM_ID));
					item.setItemName(rs.getString(Item.ITEM_NAME));
					item.setItemImageURL(rs.getString(Item.ITEM_IMAGE_URL));
					item.setItemCategoryID(rs.getInt(Item.ITEM_CATEGORY_ID));
					item.setQuantityUnit(rs.getString(Item.QUANTITY_UNIT));
					item.setListPrice(rs.getFloat(Item.LIST_PRICE));


					ItemStats itemStats = new ItemStats();
					itemStats.setMax_price(rs.getDouble("max_price"));
					itemStats.setMin_price(rs.getDouble("min_price"));
					itemStats.setAvg_price(rs.getDouble("avg_price"));
					itemStats.setShopCount(rs.getInt("shop_count"));
					item.setItemStats(itemStats);

					item.setRt_rating_avg(rs.getFloat("avg_rating"));
					item.setRt_rating_count(rs.getFloat("rating_count"));

					itemList.add(item);
				}

				endPoint.setResults(itemList);
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





	public Item getItemDetailsForEditItem(int itemID)
	{


		String queryJoinItems = "SELECT "

				+ Item.TABLE_NAME + "." + Item.ITEM_ID + ","
				+ Item.TABLE_NAME + "." + Item.ITEM_NAME + ","
				+ Item.TABLE_NAME + "." + Item.ITEM_IMAGE_URL + ","
				+ Item.TABLE_NAME + "." + Item.ITEM_CATEGORY_ID + ","
				+ Item.TABLE_NAME + "." + Item.QUANTITY_UNIT + ","
				+ Item.TABLE_NAME + "." + Item.LIST_PRICE + ","


				+ Item.TABLE_NAME + "." + Item.ITEM_DESC + ","

//				+ Item.TABLE_NAME + "." + Item.DATE_TIME_CREATED + ","
//				+ Item.TABLE_NAME + "." + Item.TIMESTAMP_UPDATED + ","
				+ Item.TABLE_NAME + "." + Item.ITEM_DESCRIPTION_LONG + ","

				+ Item.TABLE_NAME + "." + Item.BARCODE + ","
				+ Item.TABLE_NAME + "." + Item.BARCODE_FORMAT + ","
				+ Item.TABLE_NAME + "." + Item.IMAGE_COPYRIGHTS + ""

				+ " FROM " + Item.TABLE_NAME
				+ " WHERE " + Item.TABLE_NAME + "." + Item.ITEM_ID + " = " + itemID;




		// all the non-aggregate columns which are present in select must be present in group by also.
		queryJoinItems = queryJoinItems

				+ " group by "
				+ Item.TABLE_NAME + "." + Item.ITEM_ID ;





		Item item = null;

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;


		try {

			connection = dataSource.getConnection();

			int i = 0;

			statement = connection.prepareStatement(queryJoinItems);


			rs = statement.executeQuery();

			while (rs.next()) {

				item = new Item();

				item.setItemID(rs.getInt(Item.ITEM_ID));
				item.setItemName(rs.getString(Item.ITEM_NAME));
				item.setItemImageURL(rs.getString(Item.ITEM_IMAGE_URL));
				item.setItemCategoryID(rs.getInt(Item.ITEM_CATEGORY_ID));
				item.setQuantityUnit(rs.getString(Item.QUANTITY_UNIT));
				item.setListPrice(rs.getFloat(Item.LIST_PRICE));

				item.setItemDescription(rs.getString(Item.ITEM_DESC));
				item.setItemDescriptionLong(rs.getString(Item.ITEM_DESCRIPTION_LONG));
				item.setBarcode(rs.getString(Item.BARCODE));
				item.setBarcodeFormat(rs.getString(Item.BARCODE_FORMAT));
				item.setImageCopyrights(rs.getString(Item.IMAGE_COPYRIGHTS));

//				item.setDateTimeCreated(rs.getTimestamp(Item.DATE_TIME_CREATED));
//				item.setTimestampUpdated(rs.getTimestamp(Item.TIMESTAMP_UPDATED));
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



		return item;
	}


	public Item getItemPhoto(int itemID)
	{


		String queryJoinItems = "SELECT "

				+ Item.TABLE_NAME + "." + Item.ITEM_ID + ","
				+ Item.TABLE_NAME + "." + Item.ITEM_IMAGE_URL + ","
				+ Item.TABLE_NAME + "." + Item.IMAGE_COPYRIGHTS + ""

				+ " FROM " + Item.TABLE_NAME
				+ " WHERE " + Item.TABLE_NAME + "." + Item.ITEM_ID + " = " + itemID;




		// all the non-aggregate columns which are present in select must be present in group by also.
		queryJoinItems = queryJoinItems

				+ " group by "
				+ Item.TABLE_NAME + "." + Item.ITEM_ID ;





		Item item = null;

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;


		try {

			connection = dataSource.getConnection();

			int i = 0;

			statement = connection.prepareStatement(queryJoinItems);


			rs = statement.executeQuery();

			while (rs.next()) {

				item = new Item();

				item.setItemID(rs.getInt(Item.ITEM_ID));
				item.setItemImageURL(rs.getString(Item.ITEM_IMAGE_URL));
				item.setImageCopyrights(rs.getString(Item.IMAGE_COPYRIGHTS));
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



		return item;
	}




	public Item getItemDetails(int itemID)
	{


		String queryJoinItems = "SELECT "
				+ "min(" + ShopItem.ITEM_PRICE + ") as min_price" + ","
				+ "max(" + ShopItem.ITEM_PRICE + ") as max_price" + ","
				+ "avg(" + ShopItem.ITEM_PRICE + ") as avg_price" + ","
				+ "count( DISTINCT " + ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ID + ") as shop_count" + ","

				+ Item.TABLE_NAME + "." + Item.ITEM_ID + ","
				+ Item.TABLE_NAME + "." + Item.ITEM_NAME + ","
				+ Item.TABLE_NAME + "." + Item.ITEM_IMAGE_URL + ","
				+ Item.TABLE_NAME + "." + Item.ITEM_CATEGORY_ID + ","
				+ Item.TABLE_NAME + "." + Item.QUANTITY_UNIT + ","
				+ Item.TABLE_NAME + "." + Item.LIST_PRICE + ","


				+ Item.TABLE_NAME + "." + Item.ITEM_DESC + ","

//				+ Item.TABLE_NAME + "." + Item.DATE_TIME_CREATED + ","
//				+ Item.TABLE_NAME + "." + Item.TIMESTAMP_UPDATED + ","
				+ Item.TABLE_NAME + "." + Item.ITEM_DESCRIPTION_LONG + ","

				+ Item.TABLE_NAME + "." + Item.BARCODE + ","
				+ Item.TABLE_NAME + "." + Item.BARCODE_FORMAT + ","
				+ Item.TABLE_NAME + "." + Item.IMAGE_COPYRIGHTS + ","

				+  "((" + Item.TABLE_NAME + "." + Item.LIST_PRICE + " - " + " avg(" + ShopItem.ITEM_PRICE + ")" + ") / GREATEST (" + Item.TABLE_NAME + "." + Item.LIST_PRICE + ",1) ) * 100 " +" as discount_percent " + ","
				+  "(count( DISTINCT " + (OrderItem.TABLE_NAME + "." + OrderItem.ITEM_ID) + ") * sum ( DISTINCT " + OrderItem.TABLE_NAME + "." + OrderItem.ITEM_QUANTITY + " ) ) as item_sold " + ","
				+  "avg(" + ItemReview.TABLE_NAME + "." + ItemReview.RATING + ") as avg_rating" + ","
				+  "count( DISTINCT " + ItemReview.TABLE_NAME + "." + ItemReview.END_USER_ID + ") as rating_count" + ""


				+ " FROM " + Item.TABLE_NAME
				+ " INNER JOIN " + ShopItem.TABLE_NAME + " ON ( " + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_ID + "=" + Item.TABLE_NAME + "." + Item.ITEM_ID + " ) "
				+ " LEFT OUTER JOIN " + ItemReview.TABLE_NAME + " ON ( " + ItemReview.TABLE_NAME + "." + ItemReview.ITEM_ID + " = " + Item.TABLE_NAME + "." + Item.ITEM_ID + " ) "
				+ " LEFT OUTER JOIN " + OrderItem.TABLE_NAME + " ON (" + OrderItem.TABLE_NAME + "." + OrderItem.ITEM_ID + " = " + Item.TABLE_NAME + "." + Item.ITEM_ID + ")"
				+ " WHERE " + Item.TABLE_NAME + "." + Item.ITEM_ID + " = " + itemID;




		// all the non-aggregate columns which are present in select must be present in group by also.
		queryJoinItems = queryJoinItems

				+ " group by "
				+ Item.TABLE_NAME + "." + Item.ITEM_ID ;





		Item item = null;

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;


		try {

			connection = dataSource.getConnection();

			int i = 0;

			statement = connection.prepareStatement(queryJoinItems);


			rs = statement.executeQuery();

			while (rs.next()) {

				item = new Item();

				item.setItemID(rs.getInt(Item.ITEM_ID));
				item.setItemName(rs.getString(Item.ITEM_NAME));
				item.setItemImageURL(rs.getString(Item.ITEM_IMAGE_URL));
				item.setItemCategoryID(rs.getInt(Item.ITEM_CATEGORY_ID));
				item.setQuantityUnit(rs.getString(Item.QUANTITY_UNIT));
				item.setListPrice(rs.getFloat(Item.LIST_PRICE));

				item.setItemDescription(rs.getString(Item.ITEM_DESC));
				item.setItemDescriptionLong(rs.getString(Item.ITEM_DESCRIPTION_LONG));
				item.setBarcode(rs.getString(Item.BARCODE));
				item.setBarcodeFormat(rs.getString(Item.BARCODE_FORMAT));
				item.setImageCopyrights(rs.getString(Item.IMAGE_COPYRIGHTS));


//				item.setDateTimeCreated(rs.getTimestamp(Item.DATE_TIME_CREATED));

				ItemStats itemStats = new ItemStats();
				itemStats.setMax_price(rs.getDouble("max_price"));
				itemStats.setMin_price(rs.getDouble("min_price"));
				itemStats.setAvg_price(rs.getDouble("avg_price"));
				itemStats.setShopCount(rs.getInt("shop_count"));
				item.setItemStats(itemStats);

				item.setRt_rating_avg(rs.getFloat("avg_rating"));
				item.setRt_rating_count(rs.getFloat("rating_count"));

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



		return item;
	}




	public Item getShopItemDetails(int itemID, int shopID)
	{


		String queryJoinItems = "SELECT "

				+ Item.TABLE_NAME + "." + Item.ITEM_ID + ","
				+ Item.TABLE_NAME + "." + Item.ITEM_NAME + ","
				+ Item.TABLE_NAME + "." + Item.ITEM_IMAGE_URL + ","
				+ Item.TABLE_NAME + "." + Item.ITEM_CATEGORY_ID + ","
				+ Item.TABLE_NAME + "." + Item.QUANTITY_UNIT + ","
				+ Item.TABLE_NAME + "." + Item.LIST_PRICE + ","

				+ Shop.TABLE_NAME + "." + Shop.SHOP_NAME + ","
				+ ShopItem.TABLE_NAME + "." + ShopItem.ITEM_PRICE + ","
				+ ShopItem.TABLE_NAME + "." + ShopItem.AVAILABLE_ITEM_QUANTITY + ","


//				+ Item.TABLE_NAME + "." + Item.ITEM_DESC + ","
				+ Item.TABLE_NAME + "." + Item.ITEM_DESCRIPTION_LONG + ","

				+ Item.TABLE_NAME + "." + Item.BARCODE + ","
				+ Item.TABLE_NAME + "." + Item.BARCODE_FORMAT + ","
//				+ Item.TABLE_NAME + "." + Item.IMAGE_COPYRIGHTS + ","

				+  "(count( DISTINCT " + (OrderItem.TABLE_NAME + "." + OrderItem.ITEM_ID) + ") * sum ( DISTINCT " + OrderItem.TABLE_NAME + "." + OrderItem.ITEM_QUANTITY + " ) ) as item_sold " + ","
				+  "avg(" + ItemReview.TABLE_NAME + "." + ItemReview.RATING + ") as avg_rating" + ","
				+  "count( DISTINCT " + ItemReview.TABLE_NAME + "." + ItemReview.END_USER_ID + ") as rating_count" + ""


				+ " FROM " + Item.TABLE_NAME
				+ " INNER JOIN " + ShopItem.TABLE_NAME + " ON ( " + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_ID + "=" + Item.TABLE_NAME + "." + Item.ITEM_ID + " ) "
				+ " INNER JOIN " + Shop.TABLE_NAME + " ON ( " + ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ID + "=" + Shop.TABLE_NAME + "." + Shop.SHOP_ID + " ) "
				+ " LEFT OUTER JOIN " + ItemReview.TABLE_NAME + " ON ( " + ItemReview.TABLE_NAME + "." + ItemReview.ITEM_ID + " = " + Item.TABLE_NAME + "." + Item.ITEM_ID + " ) "
				+ " LEFT OUTER JOIN " + OrderItem.TABLE_NAME + " ON (" + OrderItem.TABLE_NAME + "." + OrderItem.ITEM_ID + " = " + Item.TABLE_NAME + "." + Item.ITEM_ID + ")"
				+ " WHERE " + Item.TABLE_NAME + "." + Item.ITEM_ID + " = " + itemID
				+ " AND " + ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ID + " = " + shopID;



//		+  "((" + Item.TABLE_NAME + "." + Item.LIST_PRICE + " - " + " avg(" + ShopItem.ITEM_PRICE + ")" + ") / GREATEST (" + Item.TABLE_NAME + "." + Item.LIST_PRICE + ",1) ) * 100 " +" as discount_percent " + ","

		// all the non-aggregate columns which are present in select must be present in group by also.
		queryJoinItems = queryJoinItems

				+ " group by "
				+ Shop.TABLE_NAME + "." + Shop.SHOP_ID + ","
				+ ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ITEM_ID + ","
				+ Item.TABLE_NAME + "." + Item.ITEM_ID ;





		Item item = null;

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;


		try {

			connection = dataSource.getConnection();

			int i = 0;

			statement = connection.prepareStatement(queryJoinItems);


			rs = statement.executeQuery();

			while (rs.next()) {

				item = new Item();

				item.setItemID(rs.getInt(Item.ITEM_ID));
				item.setItemName(rs.getString(Item.ITEM_NAME));
				item.setItemImageURL(rs.getString(Item.ITEM_IMAGE_URL));
				item.setQuantityUnit(rs.getString(Item.QUANTITY_UNIT));
				item.setListPrice(rs.getFloat(Item.LIST_PRICE));
				item.setItemDescriptionLong(rs.getString(Item.ITEM_DESCRIPTION_LONG));
				item.setBarcode(rs.getString(Item.BARCODE));
				item.setBarcodeFormat(rs.getString(Item.BARCODE_FORMAT));

//				item.setItemDescription(rs.getString(Item.ITEM_DESC));
//				item.setItemCategoryID(rs.getInt(Item.ITEM_CATEGORY_ID));
//				item.setImageCopyrights(rs.getString(Item.IMAGE_COPYRIGHTS));



				item.setRt_rating_avg(rs.getFloat("avg_rating"));
				item.setRt_rating_count(rs.getFloat("rating_count"));


				ShopItem shopItem = new ShopItem();
				shopItem.setItemPrice(rs.getFloat(ShopItem.ITEM_PRICE));
				shopItem.setAvailableItemQuantity(rs.getInt(ShopItem.AVAILABLE_ITEM_QUANTITY));

				Shop shop = new Shop();
				shop.setShopName(rs.getString(Shop.SHOP_NAME));

				shopItem.setShop(shop);
				item.setShopItem(shopItem);

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



		return item;
	}






	public Item getItemImageURL(
			Integer itemID
	) {



		String queryJoin = "SELECT " + Item.TABLE_NAME + "." + Item.ITEM_IMAGE_URL + ""
						+ " FROM " + Item.TABLE_NAME
						+ " WHERE " + Item.ITEM_ID + " = " + itemID;



		Item item = null;
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;

		try {

			connection = dataSource.getConnection();
			statement = connection.createStatement();

			rs = statement.executeQuery(queryJoin);

			while(rs.next())
			{
				item = new Item();

//				item.setItemID(itemID);
				item.setItemImageURL(rs.getString(Item.ITEM_IMAGE_URL));
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

		return item;
	}


}
