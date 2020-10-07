package org.nearbyshops.RESTEndpoints;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import net.coobird.thumbnailator.Thumbnails;
import org.nearbyshops.AppProperties;
import org.nearbyshops.Constants;
import org.nearbyshops.DAOs.DAOBilling.DAOAddBalance;
import org.nearbyshops.DAOs.DAOImages.BannerImageDAO;
import org.nearbyshops.DAOs.DAOImages.ShopImageDAO;
import org.nearbyshops.DAOs.DAOOrders.OrderService;
import org.nearbyshops.DAOs.DAOReviewShop.FavoriteShopDAOPrepared;
import org.nearbyshops.DAOs.DAOReviewShop.ShopReviewDAOPrepared;
import org.nearbyshops.DAOs.DAORoles.DAOStaff;
import org.nearbyshops.DAOs.DAORoles.DAOUserTokens;
import org.nearbyshops.DAOs.DAORoles.DAOUserUtility;
import org.nearbyshops.DAOs.ItemCategoryDAO;
import org.nearbyshops.DAOs.ShopDAO;
import org.nearbyshops.DAOs.ShopItemByShopDAO;
import org.nearbyshops.DAOs.ShopItemDAO;
import org.nearbyshops.Model.Image;
import org.nearbyshops.Model.ItemCategory;
import org.nearbyshops.Model.ModelEndpoint.ShopEndPoint;
import org.nearbyshops.Model.ModelEndpoint.ShopImageEndPoint;
import org.nearbyshops.Model.ModelImages.BannerImage;
import org.nearbyshops.Model.ModelImages.ShopImage;
import org.nearbyshops.Model.ModelOrderStatus.OrderStatusHomeDelivery;
import org.nearbyshops.Model.ModelOrderStatus.OrderStatusPickFromShop;
import org.nearbyshops.Model.ModelReviewShop.ShopReview;
import org.nearbyshops.Model.ModelRoles.StaffPermissions;
import org.nearbyshops.Model.ModelRoles.User;
import org.nearbyshops.Model.ModelStats.ShopStats;
import org.nearbyshops.Model.Order;
import org.nearbyshops.Model.Shop;
import org.nearbyshops.Utility.UserAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;




@RestController
@RequestMapping("/api/v1/Shop")
public class ShopResource {


	@Autowired
	HttpServletRequest request;

	@Autowired
	UserAuthentication userAuthentication;

	@Autowired
	AppProperties appProperties;


	@Autowired
	DAOStaff daoStaff;

	@Autowired
	ShopDAO shopDAO;

	@Autowired
	ShopImageDAO shopImageDAO;

	@Autowired
	DAOAddBalance addBalanceDAO;

	@Autowired
	DAOUserUtility daoUserUtility;

	@Autowired
	DAOUserTokens daoUserTokens;

	@Autowired
	ShopItemDAO shopItemDAO;

	@Autowired
	ShopItemByShopDAO shopItemByShopDAO;

	@Autowired
	OrderService orderService;



	@Autowired
	ItemCategoryDAO itemCategoryDAO;

	@Autowired
	BannerImageDAO bannerImageDAO;


	@Autowired
	ShopReviewDAOPrepared shopReviewDAOPrepared;

	@Autowired
	FavoriteShopDAOPrepared favoriteShopDAOPrepared;





	@PostMapping
	@RolesAllowed({Constants.ROLE_END_USER})
	public ResponseEntity<Object> createShop(@RequestBody Shop shop)
	{

		User user = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_END_USER));

		if(user==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}



		int idOfInsertedRow = shopDAO.insertShop(shop,user.getUserID());
		shop.setShopID(idOfInsertedRow);




		if(idOfInsertedRow >=1)
		{

			String notificationTitle = "Shop Registered";
			String notificationMessage = "A new Shop is Registered !";


			Message messageEndUser = Message.builder()
					.putData("notification_type", Constants.NOTIFICATION_TYPE_SHOP_CREATED)
					.putData("notification_title", notificationTitle)
					.putData("notification_message", notificationMessage)
					.setTopic(appProperties.getMarket_id_for_fcm() + Constants.CHANNEL_MARKET_STAFF)
					.build();



			FirebaseMessaging.getInstance().sendAsync(messageEndUser);


			return ResponseEntity.status(HttpStatus.CREATED)
					.body(shop);
		}
		else {


			return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
					.build();
		}
	}




	@PostMapping ("/CreateShopByAdmin/{UserID}")
	@RolesAllowed({Constants.ROLE_STAFF})
	public ResponseEntity<Object> createShopByStaff(@RequestBody Shop shop,
													@PathVariable("UserID")int userID)
	{


		User user = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_STAFF));

		if(user==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}




		int idOfInsertedRow = shopDAO.insertShop(shop,userID);
		shop.setShopID(idOfInsertedRow);


		if(idOfInsertedRow >=1)
		{

			return ResponseEntity.status(HttpStatus.CREATED)
					.body(shop);
		}
		else {


			return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
					.build();
		}
	}





	@PutMapping ("/UpdateByAdmin/{ShopID}")
	@RolesAllowed({Constants.ROLE_STAFF})
	public ResponseEntity<Object> updateShopByAdmin(@RequestBody Shop shop,
													@PathVariable("ShopID") int ShopID)
	{



		User user = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_STAFF));

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


		shop.setShopID(ShopID);
		int rowCount = shopDAO.updateShopByAdmin(shop);


		if(rowCount >= 1)
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





	//, @PathParam("ShopID")int ShopID

	@PutMapping ("/UpdateBySelf")
	@RolesAllowed({Constants.ROLE_SHOP_ADMIN})
	public ResponseEntity<Object> updateShopByOwner(@RequestBody Shop shop)
	{

		User userAuthenticated = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_SHOP_ADMIN));

		if(userAuthenticated==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}




		shop.setShopAdminID(userAuthenticated.getUserID());


		int rowCount = shopDAO.updateShopBySelf(shop);

		if(rowCount >= 1)
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





	@PutMapping ("/SetShopOpen")
	@RolesAllowed({Constants.ROLE_SHOP_ADMIN,Constants.ROLE_ADMIN})
	public ResponseEntity<Object> setShopOpen()
	{

		User userAuthenticated = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_SHOP_ADMIN,Constants.ROLE_ADMIN));

		if(userAuthenticated==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}


		int shopAdminID = userAuthenticated.getUserID();

		int rowCount = shopDAO.setShopOpen(true,shopAdminID);


		if(rowCount >= 1)
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





	@PutMapping ("/SetShopClosed")
	@RolesAllowed({Constants.ROLE_SHOP_ADMIN,Constants.ROLE_ADMIN})
	public ResponseEntity<Object> setShopClosed()
	{

		User userAuthenticated = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_SHOP_ADMIN,Constants.ROLE_ADMIN));

		if(userAuthenticated==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}



		int shopAdminID = userAuthenticated.getUserID();

		int rowCount = shopDAO.setShopOpen(false,shopAdminID);


		if(rowCount >= 1)
		{
			return ResponseEntity.status(HttpStatus.OK)
					.build();
		}

		return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
				.build();

	}






	@DeleteMapping ("/{ShopID}")
	@RolesAllowed({Constants.ROLE_SHOP_ADMIN, Constants.ROLE_ADMIN, Constants.ROLE_STAFF})
	public ResponseEntity<Object> deleteShop(@PathVariable("ShopID") int shopID)
	{



		User userAuthenticated = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_SHOP_ADMIN,Constants.ROLE_STAFF));

		if(userAuthenticated==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}



		if(userAuthenticated.getRole()== Constants.ROLE_SHOP_ADMIN_CODE)
		{
			shopID = daoUserUtility.getShopIDForShopAdmin(userAuthenticated.getUserID());
		}
		else if(userAuthenticated.getRole()== Constants.ROLE_STAFF_CODE)
		{

			StaffPermissions permissions = daoStaff.getStaffPermissions(userAuthenticated.getUserID());

			if(!permissions.isPermitApproveShops())
			{
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.build();
			}

		}





		int rowCount = shopDAO.deleteShop(shopID);


		if(rowCount>=1)
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




	@GetMapping ("/GetShopDetails/{ShopID}")
	public ResponseEntity<Object> getShopDetails(@PathVariable("ShopID") int shopID,
										 @RequestParam(value = "latCenter",defaultValue = "0") double latCenter,
										 @RequestParam(value = "lonCenter",defaultValue = "0")double lonCenter )
	{


		Shop shop = shopDAO.getShopDetails(shopID,latCenter,lonCenter);


		if(shop!= null)
		{
			return ResponseEntity.status(HttpStatus.OK)
					.body(shop);

		}
		else
		{

			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.build();

		}
	}






	@GetMapping("/GetShopDetailsForDetailScreen")
	public ResponseEntity<Object> getShopDetailsForDetailScreen(
			@RequestParam(value = "GetShopDetails",defaultValue = "false")boolean getShopDetails,
			@RequestParam(value = "ShopID",required = false)Integer shopID,
			@RequestParam(value = "SortBy",required = false) String sortBy,
			@RequestParam(value = "Limit",required = false)Integer limit,
			@RequestParam(value = "Offset",defaultValue = "0")int offset)
	{


		try
		{


			User user = userAuthentication.isUserAllowed(request,
					Arrays.asList(Constants.ROLE_END_USER));



			if(limit!=null && limit >= appProperties.getMax_limit())
			{
				limit = appProperties.getMax_limit();
			}




			ShopImageEndPoint endPoint = new ShopImageEndPoint();


			List<ShopImage> list =
					shopImageDAO.getShopImagesForEndUser(
							shopID,
							sortBy
					);

			endPoint.setResults(list);


//        System.out.println("Shop ID : " + shopID + " | Size : " + endPoint.getResults().size());


			if(getShopDetails)
			{
				endPoint.setShopDetails(shopDAO.getShopDetails(shopID,0.00,0.00));


				if(user!=null)
				{
					List<ShopReview> shopReviewList = shopReviewDAOPrepared.getShopReviews(shopID,user.getUserID(),null,1,0);

					if(shopReviewList.size()==1)
					{
						endPoint.setShopReview(shopReviewList.get(0));
					}
				}

			}



			if(user!=null)
			{

				boolean isFavourite = favoriteShopDAOPrepared.checkFavourite(
						shopID,user.getUserID()
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
		catch (Exception e)
		{
			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.build();

		}
	}






	@GetMapping ("/GetShopIDForShopAdmin/{ShopAdminID}")
	public ResponseEntity<Object> getShopIDForShopAdmin(@PathVariable ("ShopAdminID") int shopAdminID)
	{

		int shopID = daoUserUtility.getShopIDForShopAdmin(shopAdminID);

		Shop shop = new Shop();
		shop.setShopID(shopID);



		if(shopID>0)
		{
			return ResponseEntity.status(HttpStatus.OK)
					.body(shop);
		}
		else
		{
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.build();
		}
	}



	@GetMapping ("/GetShopForShopAdmin")
	@RolesAllowed({Constants.ROLE_SHOP_ADMIN,Constants.ROLE_ADMIN})
	public ResponseEntity<Object> getShopForShopAdmin(@RequestParam(value = "GetStats",defaultValue = "false") boolean getStats)
	{

		User userAuthenticated = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_SHOP_ADMIN,Constants.ROLE_ADMIN));

		if(userAuthenticated==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}


		Shop shop = daoUserUtility.getShopDetailsForShopAdmin(userAuthenticated.getUserID());



		if(getStats)
		{
			int itemInShopCount = shopItemDAO.getShopItemAvailability(
					null,shop.getShopID(),null,
					null,
					null,null,null,
					true,
					true
			).getItemCount();



			int outOfStockCount = shopItemByShopDAO.getShopItems(
					null,false,shop.getShopID(),
					null,null,
					null,null,null,
					true,null,
					null,
					null,null,
					10,0,
					true,true
			).getItemCount();




			int priceNotSetCount = shopItemByShopDAO.getShopItems(
					null,false,shop.getShopID(),
					null,null,
					null,null,null,
					null,true,
					null,
					null,null,
					10,0,
					true,true
			).getItemCount();



			int ordersNotConfirmedPFS = orderService.getOrdersList(
					null,shop.getShopID(),
					Order.DELIVERY_MODE_PICKUP_FROM_SHOP,null,
					OrderStatusPickFromShop.ORDER_PLACED,null,null,
					null,null,
					10,0,
					true,true
			).getItemCount();


			int ordersNotConfirmedHD = orderService.getOrdersList(
					null,shop.getShopID(),
					Order.DELIVERY_MODE_HOME_DELIVERY,OrderStatusHomeDelivery.ORDER_PLACED,
					null,null,null,
					null,null,
					10,0,
					true,true
			).getItemCount();



			ShopStats stats  = new ShopStats();
			stats.setOrdersNotConfirmedHD(ordersNotConfirmedHD);
			stats.setOrdersNotConfirmedPFS(ordersNotConfirmedPFS);
			stats.setItemsInShopCount(itemInShopCount);
			stats.setPriceNotSetCount(priceNotSetCount);
			stats.setOutOfStockCount(outOfStockCount);

			shop.setShopStats(stats);



//			int ordersNotConfirmed = ordersNotConfirmedHD + ordersNotConfirmedPFS;
//
//			shop.setRt_orders_not_confirmed_count(ordersNotConfirmed);
//			shop.setRt_stats_items_in_shop_count(itemInShopCount);
//			shop.setRt_stats_price_not_set_count(priceNotSetCount);
//			shop.setRt_stats_out_of_stock_count(outOfStockCount);
		}




		// shop status
		// 1. Check items added to shop
		// 2. Check prices set or not

		if(shop!=null)
		{
			return ResponseEntity.status(HttpStatus.OK)
					.body(shop);
		}
		else
		{

			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.build();
		}


	}





	@GetMapping ("/GetShopForShopStaff")
	@RolesAllowed({Constants.ROLE_SHOP_STAFF})
	public ResponseEntity<Object> getShopForShopStaff()
	{

		User userAuthenticated = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_SHOP_STAFF));

		if(userAuthenticated==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}



		Shop shop = daoUserUtility.getShopForShopStaff(userAuthenticated.getUserID());


		if(shop!= null)
		{
			return ResponseEntity.status(HttpStatus.OK)
					.body(shop);

		} else
		{

			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.build();
		}


	}







	//	@PUT
//	@Path("/BecomeASeller")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@RolesAllowed({GlobalConstants.ROLE_END_USER})
//	public ResponseEntity becomeASeller()
//	{
//		// this is deprecated and no longer required ... create shop method has replaced it
//
//		User user = (User) Globals.accountApproved;
//
//		int rowCount = Globals.daoShopStaff.becomeASeller(user.getUserID());
//
//
//		if(rowCount >= 1)
//		{
//			return ResponseEntity.status(ResponseEntity.Status.OK)
//					.build();
//		}
//		else {
//
//			return ResponseEntity.status(Status.NOT_MODIFIED)
//					.build();
//		}
//	}




	@PutMapping ("/AddBalance/{ShopAdminID}/{AmountToAdd}")
	@RolesAllowed({Constants.ROLE_ADMIN, Constants.ROLE_STAFF})
	public ResponseEntity<Object> addBalance(@PathVariable("ShopAdminID") int shopAdminID,
											 @PathVariable("AmountToAdd") double amountToAdd)
	{

		User user = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_STAFF));

		if(user==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}


		StaffPermissions permissions = daoStaff.getStaffPermissions(user.getUserID());

		// check staff permissions
		if(user.getRole()!= Constants.ROLE_ADMIN_CODE)
		{
			if(permissions==null || !permissions.isPermitApproveShops())
			{
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.build();
			}
		}




		int rowCount = addBalanceDAO.add_balance_to_shop(shopAdminID,amountToAdd);


		if(rowCount >= 1)
		{
			return ResponseEntity.status(HttpStatus.OK)
					.build();
		}
		else {

			return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
					.build();
		}


	}






	@GetMapping ("/QuerySimple")
	public ResponseEntity<Object> getShopListSimple(
			@RequestParam(value = "UnderReview",required = false)Boolean underReview,
            @RequestParam(value = "Enabled",required = false)Boolean enabled,
			@RequestParam(value = "Waitlisted",required = false) Boolean waitlisted,
            @RequestParam(value = "latCenter",required = false)Double latCenter,
			@RequestParam(value = "lonCenter",required = false)Double lonCenter,
            @RequestParam(value = "SearchString",required = false) String searchString,
            @RequestParam(value = "SortBy",required = false) String sortBy,
            @RequestParam(value = "Limit",defaultValue = "0") int limit,
			@RequestParam(value = "Offset",defaultValue = "0") int offset,
			@RequestParam(value = "GetRowCount",defaultValue = "false")boolean getRowCount,
			@RequestParam(value = "MetadataOnly",defaultValue = "false")boolean getOnlyMetaData
	)
	{



		if(limit >= appProperties.getMax_limit())
		{
			limit = appProperties.getMax_limit();
		}



		ShopEndPoint endPoint = shopDAO.getShopsListQuerySimple(
									underReview,
									enabled,waitlisted,
									latCenter,lonCenter,
									searchString, sortBy,
									limit,offset, getRowCount,getOnlyMetaData
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







	@GetMapping
	public ResponseEntity<Object> getShops(
            @RequestParam("LeafNodeItemCategoryID")Integer itemCategoryID,
            @RequestParam("latCenter")Double latCenter, @RequestParam("lonCenter")Double lonCenter,
            @RequestParam("deliveryRangeMax")Double deliveryRangeMax,
            @RequestParam("deliveryRangeMin")Double deliveryRangeMin,
            @RequestParam("proximity")Double proximity,
            @RequestParam("SearchString") String searchString,
            @RequestParam("SortBy") String sortBy,
            @RequestParam("Limit") int limit, @RequestParam("Offset") int offset,
			@RequestParam("GetRowCount")boolean getRowCount,
			@RequestParam("MetadataOnly")boolean getOnlyMetaData
	)
	{



		if(limit >= appProperties.getMax_limit())
		{
			limit = appProperties.getMax_limit();
		}



		ShopEndPoint endPoint = shopDAO.getShopListQueryJoin(itemCategoryID,
				latCenter,lonCenter,
				deliveryRangeMin,deliveryRangeMax,proximity,
				searchString,sortBy,
				limit,offset,
				getRowCount,getOnlyMetaData);



		endPoint.setLimit(limit);
		endPoint.setOffset(offset);
		endPoint.setMax_limit(appProperties.getMax_limit());



		//Marker
		return ResponseEntity.status(HttpStatus.OK)
				.body(endPoint);
	}







	@GetMapping ("/FilterByItemCat")
	public ResponseEntity<Object> filterShopsByItemCategory(
            @RequestParam(value = "ItemCategoryID",required = false) Integer itemCategoryID,
            @RequestParam(value = "latCenter",required = false)Double latCenter,
			@RequestParam(value = "lonCenter",required = false)Double lonCenter,
            @RequestParam(value = "proximity",required = false)Double proximity,
			@RequestParam(value = "GetSubcategories",defaultValue = "false")boolean getSubcategories,
			@RequestParam(value = "GetBannerImages", defaultValue = "false")boolean getBannerImages,
			@RequestParam(value = "SearchString",required = false) String searchString,
            @RequestParam(value = "SortBy",required = false) String sortBy,
            @RequestParam(value = "Limit",defaultValue = "0") int limit,
			@RequestParam(value = "Offset",defaultValue = "0") int offset,
			@RequestParam(value = "GetRowCount", defaultValue = "false")boolean getRowCount,
			@RequestParam(value = "MetadataOnly",defaultValue = "false")boolean getOnlyMetaData
	)
	{


		List<ItemCategory> subcategories;



		if(limit >= appProperties.getMax_limit())
		{
			limit = appProperties.getMax_limit();
		}


		ShopEndPoint endPoint = shopDAO.filterShopsByItemCategory(
				itemCategoryID,
				latCenter,lonCenter,
				null,null,
				proximity,
				searchString,
				sortBy,
				limit,offset,
				getRowCount,getOnlyMetaData
		);




		if(getSubcategories)
		{
			if(itemCategoryID==null)
			{
				itemCategoryID=1;
			}

			subcategories = itemCategoryDAO.getItemCategoriesJoinRecursive(
					null, 1, null,
					latCenter, lonCenter,
					true,
					null,
					ItemCategory.CATEGORY_ORDER,
					null,null
			);



			endPoint.setSubcategories(subcategories);
		}




		if(getBannerImages)
		{
			List<BannerImage> bannerImages = bannerImageDAO.getBannerImages(
					null,BannerImage.SORT_ORDER,
					false,false
			);


			endPoint.setBannerImages(bannerImages);
		}



		endPoint.setLimit(limit);
		endPoint.setOffset(offset);
		endPoint.setMax_limit(appProperties.getMax_limit());


/*
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/


		//Marker
		return ResponseEntity.status(HttpStatus.OK)
				.body(endPoint);

	}






	// Image MEthods

	private static final java.nio.file.Path BASE_DIR = Paths.get("./data/images/Shop");
	private static final double MAX_IMAGE_SIZE_MB = 2;





	@PostMapping ("/Image")
	@RolesAllowed({Constants.ROLE_END_USER})
	public ResponseEntity<Object> uploadImage(@RequestParam(name = "img") MultipartFile img,
											  @RequestParam(name = "PreviousImageName",required = false) String previousImageName
	) throws Exception
	{


		if(previousImageName!=null)
		{
			Files.deleteIfExists(BASE_DIR.resolve(previousImageName));
			Files.deleteIfExists(BASE_DIR.resolve("three_hundred_" + previousImageName + ".jpg"));
			Files.deleteIfExists(BASE_DIR.resolve("five_hundred_" + previousImageName + ".jpg"));
		}


		File theDir = new File(BASE_DIR.toString());

		// if the directory does not exist, create it
		if (!theDir.exists()) {

//			System.out.println("Creating directory: " + BASE_DIR.toString());

			boolean result = false;

			try{
				theDir.mkdir();
				result = true;
			}
			catch(Exception se){
				//handle it
			}
			if(result) {
//				System.out.println("DIR created");
			}
		}



		String fileName = "" + System.currentTimeMillis();

		// Copy the file to its location.
		long filesize = Files.copy(img.getInputStream(), BASE_DIR.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

		if(filesize > MAX_IMAGE_SIZE_MB * 1048 * 1024)
		{
			// delete file if it exceeds the file size limit
			Files.deleteIfExists(BASE_DIR.resolve(fileName));

			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
					.build();
		}


		createThumbnails(fileName);


		Image image = new Image();
		image.setPath(fileName);

		// Return a 201 Created response with the appropriate Location header.

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(image);
	}






	private void createThumbnails(String filename)
	{
		try {

			Thumbnails.of(BASE_DIR.toString() + "/" + filename)
					.size(300,300)
					.outputFormat("jpg")
					.toFile(new File(BASE_DIR.toString() + "/" + "three_hundred_" + filename));

			//.toFile(new File("five-" + filename + ".jpg"));

			//.toFiles(Rename.PREFIX_DOT_THUMBNAIL);


			Thumbnails.of(BASE_DIR.toString() + "/" + filename)
					.size(500,500)
					.outputFormat("jpg")
					.toFile(new File(BASE_DIR.toString() + "/" + "five_hundred_" + filename));



		} catch (IOException e) {
			e.printStackTrace();
		}
	}





	@GetMapping ("/Image/{name}")
	public ResponseEntity<Object> getImage(@PathVariable("name") String fileName,
										   HttpServletResponse response) {



		try {

			//fileName += ".jpg";
			java.nio.file.Path dest = BASE_DIR.resolve(fileName);

			if (!Files.exists(dest)) {

				return ResponseEntity.status(HttpStatus.NO_CONTENT)
						.build();
			}


			StreamUtils.copy(Files.newInputStream(dest), response.getOutputStream());

			return ResponseEntity.status(HttpStatus.OK)
					.build();


		} catch (IOException e) {
			e.printStackTrace();
		}


		return ResponseEntity.status(HttpStatus.NO_CONTENT)
				.build();
	}




	@DeleteMapping ("/Image/{name}")
	@RolesAllowed({Constants.ROLE_SHOP_ADMIN, Constants.ROLE_ADMIN})
	public ResponseEntity<Object> deleteImageFile(@PathVariable("name")String fileName)
	{

		boolean deleteStatus = false;



		try {


			//Files.delete(BASE_DIR.resolve(fileName));
			deleteStatus = Files.deleteIfExists(BASE_DIR.resolve(fileName));

			// delete thumbnails
			Files.deleteIfExists(BASE_DIR.resolve("three_hundred_" + fileName + ".jpg"));
			Files.deleteIfExists(BASE_DIR.resolve("five_hundred_" + fileName + ".jpg"));


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		if(!deleteStatus)
		{
			return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
					.build();

		}
		else
		{
			return ResponseEntity.status(HttpStatus.OK)
					.build();
		}

	}

}
