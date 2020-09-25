package org.nearbyshops.DAOs;

import org.nearbyshops.DAOs.DAOSettings.MarketSettingsDAO;
import org.nearbyshops.Model.*;
import org.nearbyshops.Model.ModelEndpoint.ShopItemEndPoint;
import org.nearbyshops.Model.ModelReviewItem.ItemReview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


@Component
public class ShopItemByShopDAO {



	@Autowired
	DataSource dataSource;


	@Autowired
	MarketSettingsDAO marketSettingsDAO;




//	Integer endUserID, Boolean isFilledCart,
	public ShopItemEndPoint getShopItems(Integer itemCategoryID,
											boolean filterByCategoryRecursively,
											Integer shopID,
											Double latCenter, Double lonCenter,
											Double deliveryRangeMin, Double deliveryRangeMax,
											Double proximity,
											Boolean isOutOfStock, Boolean priceEqualsZero,
											Boolean shopEnabled,
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


		String queryJoin = "SELECT DISTINCT "

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



		String queryJoinShopItem = "SELECT "

					+ ShopItem.TABLE_NAME + "." + ShopItem.ITEM_ID + ","
					+ ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ID + ","
					+ ShopItem.TABLE_NAME + "." + ShopItem.ITEM_PRICE + ","
					+ ShopItem.TABLE_NAME + "." + ShopItem.AVAILABLE_ITEM_QUANTITY + ","
					+ ShopItem.TABLE_NAME + "." + ShopItem.ALLOW_QUARTER_QUANTITY + ","
					+ ShopItem.TABLE_NAME + "." + ShopItem.ALLOW_HALF_QUANTITY + ","



//					+ ShopItem.TABLE_NAME + "." + ShopItem.EXTRA_DELIVERY_CHARGE + ","
//					+ ShopItem.TABLE_NAME + "." + ShopItem.DATE_TIME_ADDED + ","
//					+ ShopItem.TABLE_NAME + "." + ShopItem.LAST_UPDATE_DATE_TIME + ","


//					+ Item.TABLE_NAME + "." + Item.ITEM_ID + ","
					+ Item.TABLE_NAME + "." + Item.ITEM_IMAGE_URL + ","
					+ Item.TABLE_NAME + "." + Item.ITEM_NAME + ","
					+ Item.TABLE_NAME + "." + Item.QUANTITY_UNIT + ","
					+ Item.TABLE_NAME + "." + Item.LIST_PRICE + ","

//					+ Item.TABLE_NAME + "." + Item.ITEM_CATEGORY_ID + ","
//					+ Item.TABLE_NAME + "." + Item.ITEM_DESC + ","
//					+ Item.TABLE_NAME + "." + Item.DATE_TIME_CREATED + ","
//					+ Item.TABLE_NAME + "." + Item.ITEM_DESCRIPTION_LONG + ","


					+  "((" + Item.TABLE_NAME + "." + Item.LIST_PRICE + " - " + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_PRICE + ") / GREATEST (" + Item.TABLE_NAME + "." + Item.LIST_PRICE + ",1) ) * 100 " +" as discount_percent " + ","
					+  "(count( DISTINCT " + (OrderItem.TABLE_NAME + "." + OrderItem.ITEM_ID) + ") * sum ( DISTINCT " + OrderItem.TABLE_NAME + "." + OrderItem.ITEM_QUANTITY + " ) ) as item_sold " + ","
					+  "avg(" + ItemReview.TABLE_NAME + "." + ItemReview.RATING + ") as avg_rating" + ","
					+  "count( DISTINCT " + ItemReview.TABLE_NAME + "." + ItemReview.END_USER_ID + ") as rating_count" + ""

					+ " FROM " + Shop.TABLE_NAME
					+ " INNER JOIN " + ShopItem.TABLE_NAME + " ON ( " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + "=" + ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ID + ") "
					+ " INNER JOIN " + Item.TABLE_NAME + " ON ( " + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_ID + "=" + Item.TABLE_NAME + "." + Item.ITEM_ID + ") "
					+ " LEFT OUTER JOIN " + ItemCategory.TABLE_NAME + " ON ( " + Item.TABLE_NAME + "." + Item.ITEM_CATEGORY_ID + "=" + ItemCategory.TABLE_NAME + "." + ItemCategory.ITEM_CATEGORY_ID + ") "
					+ " LEFT OUTER JOIN " + ItemReview.TABLE_NAME + " ON (" + ItemReview.TABLE_NAME + "." + ItemReview.ITEM_ID + " = " + Item.TABLE_NAME + "." + Item.ITEM_ID + ")"
					+ " LEFT OUTER JOIN " + OrderItem.TABLE_NAME + " ON (" + OrderItem.TABLE_NAME + "." + OrderItem.ITEM_ID + " = " + Item.TABLE_NAME + "." + Item.ITEM_ID + ")"
					+ " WHERE TRUE " ;




		if(shopEnabled!=null && shopEnabled)
		{
			queryJoinShopItem = queryJoinShopItem + " AND " + Shop.TABLE_NAME + "." + Shop.IS_OPEN + " = TRUE "
						+ " AND " + Shop.TABLE_NAME + "." + Shop.SHOP_ENABLED + " = TRUE "
						+ " AND " + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_PRICE + " > 0 "
						+ " AND " + Shop.TABLE_NAME + "." + Shop.ACCOUNT_BALANCE + ">=" + marketSettingsDAO.getSettingsInstance().getMinAccountBalanceForShopOwner();
		}



//		if(endUserID!=null)
//		{
//
//			if(isFilledCart!=null)
//			{
//				if(isFilledCart)
//				{
//					queryJoinShopItem = queryJoinShopItem + " AND "
//							+ ShopItem.TABLE_NAME
//							+ "."
//							+ ShopItem.SHOP_ID + " IN "
//							+ " (SELECT " + Cart.SHOP_ID + " FROM " + Cart.TABLE_NAME + " WHERE "
//							+ Cart.END_USER_ID + " = " + endUserID + ")";
//				}else
//				{
//					queryJoinShopItem = queryJoinShopItem + " AND "
//							+ ShopItem.TABLE_NAME
//							+ "."
//							+ ShopItem.SHOP_ID + " NOT IN "
//							+ " (SELECT " + Cart.SHOP_ID + " FROM " + Cart.TABLE_NAME + " WHERE "
//							+ Cart.END_USER_ID + " = " + endUserID + ")";
//
//				}
//
//			}
//		}




		if(shopID !=null)
		{
				queryJoinShopItem = queryJoinShopItem + " AND " + ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ID + " = " + shopID;
		}
		else
		{

			if(latCenter != null && lonCenter != null)
			{
				// Applying shop visibility filter. Gives all the shops which are visible at the given location defined by
				// latCenter and lonCenter. For more information see the API documentation.


				queryJoinShopItem = queryJoinShopItem + " AND " + " (6371.01 * acos(cos( radians("
						+ latCenter + ")) * cos( radians(" + Shop.LAT_CENTER
						+ " )) * cos(radians( " + Shop.LON_CENTER + ") - radians("
						+ lonCenter + "))" + " + sin( radians(" + latCenter
						+ ")) * sin(radians(" + Shop.LAT_CENTER + ")))) <= " + Shop.DELIVERY_RANGE;
			}




			if(deliveryRangeMin !=null && deliveryRangeMax!=null){

				// apply delivery range filter
				queryJoinShopItem = queryJoinShopItem + " AND " + Shop.TABLE_NAME + "." + Shop.DELIVERY_RANGE
						+ " BETWEEN " + deliveryRangeMin + " AND " + deliveryRangeMax;
			}





			// proximity cannot be greater than the delivery range if the delivery range is supplied.
			if(proximity !=null)
			{
				// filter using Haversine formula
				queryJoinShopItem = queryJoinShopItem + " AND " + " (6371.01 * acos(cos( radians("
						+ latCenter + ")) * cos( radians(" + Shop.LAT_CENTER
						+ " )) * cos(radians( " + Shop.LON_CENTER + ") - radians(" + lonCenter
						+ "))" + " + sin( radians(" + latCenter + ")) * sin(radians("
						+ Shop.LAT_CENTER + ")))) <= " + proximity ;
			}

		}



		if(searchString !=null)
		{
			String queryPartSearch = " ( " + Item.TABLE_NAME + "." + Item.ITEM_DESC +" ilike '%" + searchString + "%'"
					+ " or " + Item.TABLE_NAME + "." + Item.ITEM_DESCRIPTION_LONG + " ilike '%" + searchString + "%'"
					+ " or " + Item.TABLE_NAME + "." + Item.ITEM_NAME + " ilike '%" + searchString + "%'" + ") ";

			queryJoinShopItem = queryJoinShopItem + " AND " + queryPartSearch;
		}



//		if(itemCategoryID !=null)
//		{
//
//		}




		if(itemCategoryID!=null)
		{

			if(filterByCategoryRecursively)
			{

				if(itemCategoryID!=1)
				{
					queryJoinShopItem = queryJoinShopItem
							+ " AND " + ItemCategory.TABLE_NAME + "." + ItemCategory.ITEM_CATEGORY_ID
							+ " IN " + " (" + queryRecursive + ")";
				}
			}
			else
			{
				queryJoinShopItem = queryJoinShopItem + " AND "
						+ ItemCategory.TABLE_NAME
						+ "."
						+ ItemCategory.ITEM_CATEGORY_ID + " = " + itemCategoryID;
			}


//			System.out.println("Item Cat ID : " + itemCategoryID);
		}






		if(priceEqualsZero!=null && priceEqualsZero)
		{
			queryJoinShopItem = queryJoinShopItem + " AND "
					+ ShopItem.TABLE_NAME  + "." + ShopItem.ITEM_PRICE + " = " + 0;
		}




		if(isOutOfStock!=null && isOutOfStock)
		{
			queryJoinShopItem = queryJoinShopItem + " AND "
					+ ShopItem.TABLE_NAME  + "." + ShopItem.AVAILABLE_ITEM_QUANTITY + " = " + 0;
		}



		/*
				Applying Filters
		 */






		queryJoinShopItem = queryJoinShopItem

				+ " group by "
				+ ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ITEM_ID + ","
				+ ShopItem.TABLE_NAME + "." + ShopItem.ITEM_ID + ","
				+ ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ID + ","
				+ Item.TABLE_NAME + "." + Item.ITEM_ID ;


		queryCount = queryJoinShopItem;



		if(sortBy!=null)
		{
			if(!sortBy.equals(""))
			{
				String queryPartSortBy = " ORDER BY " + sortBy;
				queryJoinShopItem = queryJoinShopItem + queryPartSortBy;
			}
		}





		queryJoinShopItem = queryJoinShopItem + " LIMIT " + limit + " " + " OFFSET " + offset;

		/*
				Applying Filters Ends
		 */




		queryCount = "SELECT COUNT(*) as item_count FROM (" + queryCount + ") AS temp";


		ShopItemEndPoint endPoint = new ShopItemEndPoint();





		ArrayList<ShopItem> shopItemList = new ArrayList<>();


		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;

		try {


			connection = dataSource.getConnection();


			if(!getOnlyMetadata) {


				statement = connection.createStatement();
				rs = statement.executeQuery(queryJoinShopItem);

				while(rs.next())
				{

					ShopItem shopItem = new ShopItem();
					shopItem.setShopID(rs.getInt(ShopItem.SHOP_ID));
					shopItem.setItemID(rs.getInt(ShopItem.ITEM_ID));
					shopItem.setAvailableItemQuantity(rs.getInt(ShopItem.AVAILABLE_ITEM_QUANTITY));

					shopItem.setAllowQuarterQuantity(rs.getBoolean(ShopItem.ALLOW_QUARTER_QUANTITY));
					shopItem.setAllowHalfQuantity(rs.getBoolean(ShopItem.ALLOW_HALF_QUANTITY));

					shopItem.setItemPrice(rs.getDouble(ShopItem.ITEM_PRICE));

//					shopItem.setDateTimeAdded(rs.getTimestamp(ShopItem.DATE_TIME_ADDED));
//					shopItem.setLastUpdateDateTime(rs.getTimestamp(ShopItem.LAST_UPDATE_DATE_TIME));
//					shopItem.setExtraDeliveryCharge(rs.getInt(ShopItem.EXTRA_DELIVERY_CHARGE));

					Item item = new Item();

//					item.setItemID(rs.getInt(Item.ITEM_ID));
					item.setItemID(shopItem.getItemID());
					item.setItemName(rs.getString(Item.ITEM_NAME));
					item.setItemImageURL(rs.getString(Item.ITEM_IMAGE_URL));
					item.setQuantityUnit(rs.getString(Item.QUANTITY_UNIT));
					item.setListPrice(rs.getFloat(Item.LIST_PRICE));

//					item.setItemCategoryID(rs.getInt(Item.ITEM_CATEGORY_ID));
//					item.setItemDescriptionLong(rs.getString(Item.ITEM_DESCRIPTION_LONG));
//					item.setItemDescription(rs.getString(Item.ITEM_DESC));
//					item.setDateTimeCreated(rs.getTimestamp(Item.DATE_TIME_CREATED));

					item.setRt_rating_avg(rs.getFloat("avg_rating"));
					item.setRt_rating_count(rs.getFloat("rating_count"));



//					System.out.println("Item ID : " + item.getItemID() + " Item Sold : " + rs.getInt("item_sold"));

					shopItem.setItem(item);
					shopItemList.add(shopItem);

				}


				endPoint.setResults(shopItemList);
			}




			if(getRowCount)
			{
				statement = connection.createStatement();
				rs = statement.executeQuery(queryCount);

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


}
