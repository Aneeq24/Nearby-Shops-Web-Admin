package org.nearbyshops.RESTEndpoints;

import org.nearbyshops.AppProperties;
import org.nearbyshops.Constants;
import org.nearbyshops.DAOs.*;
import org.nearbyshops.DAOs.DAOCartOrder.CartItemService;
import org.nearbyshops.DAOs.DAOImages.ItemImagesDAO;
import org.nearbyshops.DAOs.DAOReviewItem.FavoriteItemDAOPrepared;
import org.nearbyshops.DAOs.DAORoles.DAOShopStaff;
import org.nearbyshops.DAOs.DAORoles.DAOStaff;
import org.nearbyshops.DAOs.DAORoles.DAOUserTokens;
import org.nearbyshops.DAOs.DAORoles.DAOUserUtility;
import org.nearbyshops.Model.CartItem;
import org.nearbyshops.Model.ItemCategory;
import org.nearbyshops.Model.ModelEndpoint.ItemImageEndPoint;
import org.nearbyshops.Model.ModelEndpoint.ShopItemEndPoint;
import org.nearbyshops.Model.ModelImages.ItemImage;
import org.nearbyshops.Model.ModelRoles.ShopStaffPermissions;
import org.nearbyshops.Model.ModelRoles.StaffPermissions;
import org.nearbyshops.Model.ModelRoles.User;
import org.nearbyshops.Model.ShopItem;
import org.nearbyshops.Utility.UserAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;




@RestController
@RequestMapping ("/api/v1/ShopItem")
public class ShopItemResource {



	@Autowired
	private HttpServletRequest request;

	@Autowired
	private UserAuthentication userAuthentication;

	@Autowired
	private AppProperties appProperties;



	@Autowired
	private ShopItemByShopDAO shopItemByShopDAO;

	@Autowired
	private ShopItemByItemDAO shopItemByItemDAO;

	@Autowired
	private ShopItemDAO shopItemDAO;

	@Autowired
	private ItemCategoryDAO itemCategoryDAO;



	@Autowired
	ItemDAO itemDAO;

	@Autowired
	ItemImagesDAO itemImagesDAO;

	@Autowired
	CartItemService cartItemService;

	@Autowired
	DAOUserTokens daoUserTokens;


	@Autowired
	DAOUserUtility daoUserUtility;

	@Autowired
	DAOStaff daoStaff;


	@Autowired
	DAOShopStaff daoShopStaff;


	@Autowired
	FavoriteItemDAOPrepared favoriteItemDAOPrepared;





	@PostMapping("/CreateBulk/{ShopID}")
	@RolesAllowed({Constants.ROLE_SHOP_ADMIN, Constants.ROLE_SHOP_STAFF,Constants.ROLE_STAFF})
	public ResponseEntity<Object> createShopItemBulk(@PathVariable("ShopID") int shopID,
											 @RequestBody List<ShopItem> itemList)
	{
		int rowCountSum = 0;


		User user = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_STAFF,Constants.ROLE_SHOP_ADMIN));

		if(user==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}


		if(user.getRole()== Constants.ROLE_STAFF_CODE) {

			StaffPermissions permissions = daoStaff.getStaffPermissions(user.getUserID());

			if (!permissions.isPermitApproveShops())
			{
				// the staff member doesnt have persmission to update shop
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.build();
			}

		}
		else if(user.getRole()== Constants.ROLE_SHOP_STAFF_CODE) {

			shopID = daoUserUtility.getShopIDforShopStaff(user.getUserID());
			ShopStaffPermissions permissions = daoShopStaff.getShopStaffPermissions(user.getUserID());


			if (!permissions.isPermitAddRemoveItems()) {
				// staff member do not have this permission
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.build();
			}

		}
		else if(user.getRole()== Constants.ROLE_SHOP_ADMIN_CODE)
		{
			shopID = daoUserUtility.getShopIDForShopAdmin(user.getUserID());
		}




		for(ShopItem shopItem : itemList)
		{
			shopItem.setShopID(shopID);
			rowCountSum = rowCountSum + shopItemDAO.insertShopItem(shopItem);
		}






		if(rowCountSum ==  itemList.size())
		{

			return ResponseEntity.status(HttpStatus.OK)
					.build();
		}
		else if( (rowCountSum < itemList.size()) && (rowCountSum > 0))
		{

			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
					.build();
		}
		else if(rowCountSum == 0 ) {

			return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
					.build();
		}

		return null;
	}





	@PostMapping("/DeleteBulk/{ShopID}")
	@RolesAllowed({Constants.ROLE_SHOP_ADMIN, Constants.ROLE_SHOP_STAFF,Constants.ROLE_STAFF})
	public ResponseEntity<Object> deleteShopItemBulk(@PathVariable("ShopID") int shopID,
													 @RequestBody List<ShopItem> itemList)
	{
		int rowCountSum = 0;



		User user = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_STAFF,Constants.ROLE_SHOP_STAFF));

		if(user==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}




		if(user.getRole()== Constants.ROLE_STAFF_CODE) {

			StaffPermissions permissions = daoStaff.getStaffPermissions(user.getUserID());

			if (!permissions.isPermitApproveShops())
			{
				// the staff member doesnt have persmission to update shop
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.build();
			}

		}
		else if(user.getRole()== Constants.ROLE_SHOP_ADMIN_CODE)
		{
			shopID = daoUserUtility.getShopIDForShopAdmin(user.getUserID());

		}
		else if (user.getRole()== Constants.ROLE_SHOP_STAFF_CODE)
		{

			ShopStaffPermissions permissions = daoShopStaff.getShopStaffPermissions(user.getUserID());


			if(!permissions.isPermitAddRemoveItems())
			{
				// staff member do not have permission
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.build();
			}


			shopID = daoUserUtility.getShopIDforShopStaff(user.getUserID());
		}




		for(ShopItem shopItem : itemList)
		{
			shopItem.setShopID(shopID);

			rowCountSum = rowCountSum + shopItemDAO
					.deleteShopItem(shopItem.getShopID(),shopItem.getItemID());
		}




		if(rowCountSum <=  itemList.size())
		{

			return ResponseEntity.status(HttpStatus.OK)
					.build();
		}
		else
		{

			return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
					.build();
		}
	}





	@PutMapping ("/Update/{ShopID}")
	@RolesAllowed({Constants.ROLE_SHOP_ADMIN, Constants.ROLE_SHOP_STAFF,Constants.ROLE_STAFF})
	public ResponseEntity<Object> updateShopItem(@PathVariable("ShopID") int shopID,
												 @RequestBody ShopItem shopItem)
	{


		User userAuthenticated = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_STAFF,Constants.ROLE_SHOP_ADMIN));

		if(userAuthenticated==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}



		int rowCount = 0;



		if(userAuthenticated.getRole()== Constants.ROLE_STAFF_CODE) {

			StaffPermissions permissions = daoStaff.getStaffPermissions(userAuthenticated.getUserID());

			if (!permissions.isPermitApproveShops())
			{
				// the staff member doesnt have persmission to update shop
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.build();
			}

		}
		else if(userAuthenticated.getRole()== Constants.ROLE_SHOP_ADMIN_CODE)
		{
			shopID = daoUserUtility.getShopIDForShopAdmin(userAuthenticated.getUserID());
		}
		else if (userAuthenticated.getRole()== Constants.ROLE_SHOP_STAFF_CODE)
		{

			shopID = daoUserUtility.getShopIDforShopStaff(userAuthenticated.getUserID());
			ShopStaffPermissions permissions = daoShopStaff.getShopStaffPermissions(userAuthenticated.getUserID());


			if (!permissions.isPermitUpdateItemsInShop()) {
				// staff member do not have this permission
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.build();
			}
		}



		shopItem.setShopID(shopID);
		rowCount = shopItemDAO.updateShopItem(shopItem);

		
		if(rowCount == 1)
		{

			return ResponseEntity.status(HttpStatus.OK)
								.build();
			
		}
		else
		{

			return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
					.build();
		}
	}






	@DeleteMapping ("/Delete")
	@RolesAllowed({Constants.ROLE_SHOP_STAFF,Constants.ROLE_STAFF})
	public ResponseEntity<Object> deleteShopItem(@RequestParam("ShopID")int shopID,
												 @RequestParam("ItemID") int itemID)
	{
		int rowCount = 0;
//		int shopID = 0;


		User userAuthenticated = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_STAFF,Constants.ROLE_SHOP_STAFF));


		if(userAuthenticated==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}




		if(userAuthenticated.getRole()== Constants.ROLE_STAFF_CODE) {

			StaffPermissions permissions = daoStaff.getStaffPermissions(userAuthenticated.getUserID());

			if (!permissions.isPermitApproveShops())
			{
				// the staff member doesnt have persmission to update shop
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.build();
			}

		}
		else if(userAuthenticated.getRole()== Constants.ROLE_SHOP_ADMIN_CODE)
		{
			shopID = daoUserUtility.getShopIDForShopAdmin(userAuthenticated.getUserID());
		}
		else if (userAuthenticated.getRole()== Constants.ROLE_SHOP_STAFF_CODE)
		{

			shopID = daoUserUtility.getShopIDforShopStaff(userAuthenticated.getUserID());
			ShopStaffPermissions permissions = daoShopStaff.getShopStaffPermissions(userAuthenticated.getUserID());

			if (!permissions.isPermitUpdateItemsInShop()) {

				// staff member do not have this permission
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.build();
			}
		}





		rowCount =	shopItemDAO.deleteShopItem(shopID, itemID);

		
		if(rowCount == 1)
		{

			return ResponseEntity.status(HttpStatus.OK)
					.build();
			
		}
		else
		{

			return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
					.build();
		}
	}








	@GetMapping("/ForShop")
	public ResponseEntity<Object> getShopItemAvailability(
            @RequestParam(value = "ItemCategoryID",required = false)Integer ItemCategoryID,
            @RequestParam(value = "ShopID",required = false)Integer ShopID,
			@RequestParam(value = "ItemID",required = false) Integer itemID,
            @RequestParam(value = "SearchString",required = false) String searchString,
            @RequestParam(value = "SortBy",required = false) String sortBy,
            @RequestParam(value = "Limit",required = false) Integer limit,
			@RequestParam(value = "Offset",required = false)Integer offset,
			@RequestParam(value = "GetRowCount",defaultValue = "false")boolean getRowCount,
			@RequestParam(value = "MetadataOnly",defaultValue = "false")boolean getOnlyMetaData
	)
	{



		ShopItemEndPoint endPoint = shopItemDAO.getShopItemAvailability(
				ItemCategoryID,ShopID,itemID,
				searchString,
				sortBy,limit,offset,
				getRowCount,
				getOnlyMetaData
		);




		endPoint.setLimit(limit);
		endPoint.setMax_limit(appProperties.getMax_limit());
		endPoint.setOffset(offset);


		/*try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/

		//Marker
		return ResponseEntity.status(HttpStatus.OK)
				.body(endPoint);
	}







	/*Deprecated Endpoint*/
//	@GetMapping
	public ResponseEntity<Object> getShopItems(
            @RequestParam("ItemCategoryID")Integer ItemCategoryID,
			@RequestParam("FilterCategoriesRecursively")boolean filterCategoriesRecursively,
			@RequestParam("GetSubcategories")boolean getSubcategories,
            @RequestParam("ShopID")Integer ShopID, @RequestParam("ItemID") Integer itemID,
            @RequestParam("latCenter")Double latCenter, @RequestParam("lonCenter")Double lonCenter,
            @RequestParam("deliveryRangeMax")Double deliveryRangeMax,
            @RequestParam("deliveryRangeMin")Double deliveryRangeMin,
            @RequestParam("proximity")Double proximity,
            @RequestParam("EndUserID") Integer endUserID, @RequestParam("IsFilledCart") Boolean isFilledCart,
            @RequestParam("IsOutOfStock") Boolean isOutOfStock, @RequestParam("PriceEqualsZero")Boolean priceEqualsZero,
            @RequestParam("MinPrice")Integer minPrice, @RequestParam("MaxPrice")Integer maxPrice,
            @RequestParam("SearchString") String searchString,
            @RequestParam("ShopEnabled")Boolean shopEnabled,
            @RequestParam("SortBy") String sortBy,
			@RequestParam("Limit") int limit, @RequestParam("Offset") int offset,
			@RequestParam("GetRowCount")boolean getRowCount,
			@RequestParam("MetadataOnly")boolean getOnlyMetaData
	)
	{
		/* To be Deprecated ... This endpoint is Deprecated please use the endpoints given below */

		ShopItemEndPoint endPoint = null;


		if(ShopID!=null && itemID == null)
		{


			endPoint = shopItemByShopDAO.getShopItems(
					ItemCategoryID,
					filterCategoriesRecursively,
					ShopID,
					latCenter, lonCenter,
					deliveryRangeMin,deliveryRangeMax,
					proximity,
					isOutOfStock,
					priceEqualsZero,
					shopEnabled,
					searchString,
					sortBy,limit,offset,
					getRowCount,getOnlyMetaData
			);


		}
		else if(itemID !=null && ShopID==null)
		{


			endPoint = shopItemByItemDAO.getShopItems(
					ItemCategoryID,
					itemID,
					latCenter, lonCenter,
					deliveryRangeMin,deliveryRangeMax,
					proximity, endUserID,
					isFilledCart,
					isOutOfStock,
					priceEqualsZero,
					sortBy,
					limit,offset,
					getRowCount,getOnlyMetaData
			);

		}
		else
		{

			endPoint = shopItemDAO.getShopItems(
					ItemCategoryID,
					ShopID, itemID,
					latCenter, lonCenter,
					deliveryRangeMin,deliveryRangeMax,
					proximity, endUserID,
					isFilledCart,
					isOutOfStock,
					priceEqualsZero,
					searchString,
					sortBy,
					limit,offset,
					getRowCount,getOnlyMetaData
			);

		}







		endPoint.setLimit(limit);
		endPoint.setMax_limit(appProperties.getMax_limit());
		endPoint.setOffset(offset);



//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}






		List<ItemCategory> subcategories;

		if(getSubcategories)
		{
			subcategories = itemCategoryDAO.getItemCategoriesJoinRecursive(
					ShopID, ItemCategoryID, null,
					latCenter, lonCenter,
					true,
					searchString,
					ItemCategory.CATEGORY_ORDER,
					null,null
			);



			endPoint.setSubcategories(subcategories);
		}




		//Marker
		return ResponseEntity.status(HttpStatus.OK)
				.body(endPoint);
	}








	@GetMapping("/ShopItemsByShop")
	public ResponseEntity<Object> getShopItemsByShop(
			@RequestParam(value = "ItemCategoryID",required = false)Integer ItemCategoryID,
			@RequestParam(value = "FilterCategoriesRecursively",defaultValue = "false")boolean filterByCategoryRecursively,
			@RequestParam(value = "GetSubcategories",defaultValue = "false")boolean getSubcategories,
			@RequestParam(value = "ShopID",required = false)Integer ShopID,
			@RequestParam(value = "ItemID",required = false) Integer itemID,
			@RequestParam(value = "latCenter",required = false)Double latCenter,
			@RequestParam(value = "lonCenter",required = false)Double lonCenter,
			@RequestParam(value = "IsOutOfStock",required = false) Boolean isOutOfStock,
			@RequestParam(value = "PriceEqualsZero",required = false)Boolean priceEqualsZero,
			@RequestParam(value = "MinPrice",required = false)Integer minPrice,
			@RequestParam(value = "MaxPrice",required = false)Integer maxPrice,
			@RequestParam(value = "SearchString",required = false) String searchString,
			@RequestParam(value = "ShopEnabled",required = false)Boolean shopEnabled,
			@RequestParam(value = "SortBy",required = false) String sortBy,
			@RequestParam(value = "Limit",defaultValue = "0") int limit,
			@RequestParam(value = "Offset",defaultValue = "0") int offset,
			@RequestParam(value = "GetRowCount",defaultValue = "false")boolean getRowCount,
			@RequestParam(value = "MetadataOnly",defaultValue = "false")boolean getOnlyMetaData
	)
	{

//		@RequestParam(value = "EndUserID",required = false) Integer endUserID,
//		@RequestParam(value = "IsFilledCart",required = false) Boolean isFilledCart,



			ShopItemEndPoint endPoint = null;



			endPoint = shopItemByShopDAO.getShopItems(
					ItemCategoryID,
					filterByCategoryRecursively,
					ShopID,
					latCenter, lonCenter,
					null,null,
					null,
					isOutOfStock,
					priceEqualsZero,
					shopEnabled,
					searchString,
					sortBy,limit,offset,
					getRowCount,getOnlyMetaData
			);








		endPoint.setLimit(limit);
		endPoint.setMax_limit(appProperties.getMax_limit());
		endPoint.setOffset(offset);



//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}






		List<ItemCategory> subcategories;

		if(getSubcategories)
		{
			subcategories = itemCategoryDAO.getItemCategoriesJoinRecursive(
					ShopID, ItemCategoryID, null,
					latCenter, lonCenter,
					true,
					searchString,
					ItemCategory.CATEGORY_ORDER,
					null,null
			);



			endPoint.setSubcategories(subcategories);
		}




		//Marker
		return ResponseEntity.status(HttpStatus.OK)
				.body(endPoint);
	}






	@GetMapping ("/ShopItemsByItem")
	public ResponseEntity<Object> getShopItemsByItem(
			@RequestParam(value = "ItemCategoryID",required = false)Integer ItemCategoryID,
			@RequestParam(value = "GetSubcategories",defaultValue = "false")boolean getSubcategories,
			@RequestParam(value = "ShopID",required = false)Integer ShopID,
			@RequestParam(value = "ItemID",required = false) Integer itemID,
			@RequestParam(value = "latCenter",required = false)Double latCenter,
			@RequestParam(value = "lonCenter",required = false)Double lonCenter,
			@RequestParam(value = "EndUserID",required = false) Integer endUserID,
			@RequestParam(value = "IsFilledCart",required = false) Boolean isFilledCart,
			@RequestParam(value = "IsOutOfStock",required = false) Boolean isOutOfStock,
			@RequestParam(value = "PriceEqualsZero",required = false)Boolean priceEqualsZero,
			@RequestParam(value = "MinPrice",required = false)Integer minPrice,
			@RequestParam(value = "MaxPrice",required = false)Integer maxPrice,
			@RequestParam(value = "SearchString",required = false) String searchString,
			@RequestParam(value = "ShopEnabled",required = false)Boolean shopEnabled,
			@RequestParam(value = "SortBy",required = false) String sortBy,
			@RequestParam(value = "Limit", defaultValue = "0") int limit,
			@RequestParam(value = "Offset",defaultValue = "0") int offset,
			@RequestParam(value = "GetRowCount",defaultValue = "false")boolean getRowCount,
			@RequestParam(value = "MetadataOnly",defaultValue = "false")boolean getOnlyMetaData
	)
	{



		ShopItemEndPoint endPoint = null;


		endPoint = shopItemByItemDAO.getShopItems(
				ItemCategoryID,
				itemID,
				latCenter, lonCenter,
				null,null,
				null, endUserID,
				isFilledCart,
				isOutOfStock,
				priceEqualsZero,
				sortBy,
				limit,offset,
				getRowCount,getOnlyMetaData
		);




		endPoint.setLimit(limit);
		endPoint.setMax_limit(appProperties.getMax_limit());
		endPoint.setOffset(offset);



//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}






		List<ItemCategory> subcategories;

		if(getSubcategories)
		{
			subcategories = itemCategoryDAO.getItemCategoriesJoinRecursive(
					ShopID, ItemCategoryID, null,
					latCenter, lonCenter,
					true,
					searchString,
					ItemCategory.CATEGORY_ORDER,
					null,null
			);



			endPoint.setSubcategories(subcategories);
		}




		//Marker
		return ResponseEntity.status(HttpStatus.OK)
				.body(endPoint);
	}






	@GetMapping ("/GetAvailableShops")
	public ResponseEntity<Object> getAvailableShops(
			@RequestParam(value = "ItemID",required = false) Integer itemID,
			@RequestParam(value = "latCenter",required = false)Double latCenter,
			@RequestParam(value = "lonCenter",required = false)Double lonCenter,
			@RequestParam(value = "EndUserID",required = false) Integer endUserID,
			@RequestParam(value = "GetFilledCart",defaultValue = "false") boolean GetFilledCart,
			@RequestParam(value = "SearchString",required = false) String searchString,
			@RequestParam(value = "ShopEnabled",defaultValue = "false")Boolean shopEnabled,
			@RequestParam(value = "SortBy",required = false) String sortBy,
			@RequestParam(value = "Limit",defaultValue = "0") int limit,
			@RequestParam(value = "Offset",defaultValue = "0") int offset,
			@RequestParam(value = "GetRowCount",defaultValue = "false")boolean getRowCount,
			@RequestParam(value = "MetadataOnly",defaultValue = "false")boolean getOnlyMetaData)
	{



		ShopItemEndPoint endPoint = null;

//		System.out.println("End User ID " + endUserID);


		if(endUserID==null)
		{

			endPoint = shopItemByItemDAO.getShopItems(
					null,
					itemID,
					latCenter, lonCenter,
					null,null,
					null, endUserID,
					null,
					false,
					false,
					sortBy,
					limit,offset,
					getRowCount,getOnlyMetaData
			);
		}
		else
		{



			endPoint = shopItemByItemDAO.getShopItems(
					null,
					itemID,
					latCenter, lonCenter,
					null,null,
					null,
					endUserID, false,
					false,
					false,
					sortBy,
					limit,offset,
					getRowCount,getOnlyMetaData
			);





			if(GetFilledCart)
			{

				endPoint.getResults().addAll(0,shopItemByItemDAO.getShopItems(
						null,
						itemID,
						latCenter, lonCenter,
						null,null,
						null,
						endUserID, true,
						false,
						false,
						sortBy,
						100,0,
						getRowCount,getOnlyMetaData
				).getResults());
			}
		}



//		endPoint.setItemDetails(Globals.itemDAO.getItemDetails(itemID));

		endPoint.setLimit(limit);
		endPoint.setMax_limit(appProperties.getMax_limit());
		endPoint.setOffset(offset);


		//Marker
		return ResponseEntity.status(HttpStatus.OK)
				.body(endPoint);
	}






	@GetMapping ("/GetShopItemDetails")
	public ResponseEntity<Object> getShopItemDetails(
			@RequestParam(value = "GetShopItemDetails",defaultValue = "false")boolean getShopItemDetails,
			@RequestParam(value = "ItemID",required = false)Integer itemID,
			@RequestParam(value = "ShopID",required = false)Integer shopID,
			@RequestParam(value = "SortBy",required = false) String sortBy,
			@RequestParam(value = "Limit",required = false)Integer limit,
			@RequestParam(value = "Offset",defaultValue = "0")int offset)
	{


		User user = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_END_USER));





		if(limit!=null && limit >= appProperties.getMax_limit())
		{
			limit = appProperties.getMax_limit();
		}




		ItemImageEndPoint endPoint = new ItemImageEndPoint();


		List<ItemImage> list = null;


		list =
				itemImagesDAO.getItemImagesForEndUser(
						itemID,
						sortBy
				);

		endPoint.setResults(list);



		if(getShopItemDetails)
		{
			endPoint.setItemDetails(itemDAO.getShopItemDetails(itemID,shopID));


			if(user!=null)
			{

				CartItem cartItem;
				cartItem = cartItemService
						.getCartItemAvailability(user.getUserID(), shopID,itemID);

				endPoint.setCartItem(cartItem);

			}


		}



		if(user!=null)
		{
			boolean isFavourite = favoriteItemDAOPrepared.checkFavourite(
					itemID,user.getUserID()
			);

			endPoint.setFavourite(isFavourite);
		}





		if(limit!=null)
		{
			endPoint.setLimit(limit);
			endPoint.setOffset(offset);
			endPoint.setMax_limit(appProperties.getMax_limit());
		}



//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}


		//Marker

		return ResponseEntity.status(HttpStatus.OK)
				.body(endPoint);
	}



}
