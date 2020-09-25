//package org.nearbyshops.RESTEndpointsSB;
//
//import com.google.firebase.messaging.FirebaseMessaging;
//import com.google.firebase.messaging.Message;
//import net.coobird.thumbnailator.Thumbnails;
//import org.nearbyshops.AppProperties;
//import org.nearbyshops.Constants;
//import org.nearbyshops.DAOs.*;
//import org.nearbyshops.DAOs.DAOBilling.DAOAddBalance;
//import org.nearbyshops.DAOs.DAOImages.BannerImageDAO;
//import org.nearbyshops.DAOs.DAOImages.ShopImageDAO;
//import org.nearbyshops.DAOs.DAOOrders.OrderService;
//import org.nearbyshops.DAOs.DAOReviewShop.FavoriteShopDAOPrepared;
//import org.nearbyshops.DAOs.DAOReviewShop.ShopReviewDAOPrepared;
//import org.nearbyshops.DAOs.DAORoles.DAOStaff;
//import org.nearbyshops.DAOs.DAORoles.DAOUserTokens;
//import org.nearbyshops.DAOs.DAORoles.DAOUserUtility;
//import org.nearbyshops.Model.Image;
//import org.nearbyshops.Model.ItemCategory;
//import org.nearbyshops.Model.ModelEndpoint.ShopEndPoint;
//import org.nearbyshops.Model.ModelEndpoint.ShopImageEndPoint;
//import org.nearbyshops.Model.ModelImages.BannerImage;
//import org.nearbyshops.Model.ModelImages.ShopImage;
//import org.nearbyshops.Model.ModelOrderStatus.OrderStatusHomeDelivery;
//import org.nearbyshops.Model.ModelOrderStatus.OrderStatusPickFromShop;
//import org.nearbyshops.Model.ModelReviewShop.ShopReview;
//import org.nearbyshops.Model.ModelRoles.StaffPermissions;
//import org.nearbyshops.Model.ModelRoles.User;
//import org.nearbyshops.Model.ModelStats.ShopStats;
//import org.nearbyshops.Model.Order;
//import org.nearbyshops.Model.Shop;
//import org.nearbyshops.Utility.UserAuthentication;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.util.StreamUtils;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.annotation.security.RolesAllowed;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.ws.rs.*;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URI;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.util.Arrays;
//import java.util.List;
//
//
////@RestController
////@RequestMapping("/api/v1/Shop")
//@Path("/api/v1/Shop")
//public class ShopResource {
//
//
//	@Autowired
//	HttpServletRequest request;
//
//	@Autowired
//	UserAuthentication userAuthentication;
//
//	@Autowired
//	AppProperties appProperties;
//
//
//	@Autowired
//	DAOStaff daoStaff;
//
//	@Autowired
//	ShopDAO shopDAO;
//
//	@Autowired
//	ShopImageDAO shopImageDAO;
//
//	@Autowired
//	DAOAddBalance addBalanceDAO;
//
//	@Autowired
//	DAOUserUtility daoUserUtility;
//
//	@Autowired
//	DAOUserTokens daoUserTokens;
//
//	@Autowired
//	ShopItemDAO shopItemDAO;
//
//	@Autowired
//	ShopItemByShopDAO shopItemByShopDAO;
//
//	@Autowired
//	OrderService orderService;
//
//
//
//	@Autowired
//	ItemCategoryDAO itemCategoryDAO;
//
//	@Autowired
//	BannerImageDAO bannerImageDAO;
//
//
//	@Autowired
//	ShopReviewDAOPrepared shopReviewDAOPrepared;
//
//	@Autowired
//	FavoriteShopDAOPrepared favoriteShopDAOPrepared;
//
//
//
//
//
////	@PostMapping
//	@POST
//	@RolesAllowed({Constants.ROLE_END_USER})
//	public Response createShop(Shop shop)
//	{
//
//		User user = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_END_USER));
//
//		if(user==null)
//		{
//			return Response.status(Response.Status.FORBIDDEN)
//					.build();
//		}
//
//
//
//		int idOfInsertedRow = shopDAO.insertShop(shop,user.getUserID());
//		shop.setShopID(idOfInsertedRow);
//
//
//
//
//		if(idOfInsertedRow >=1)
//		{
//
//			String notificationTitle = "Shop Registered";
//			String notificationMessage = "A new Shop is Registered !";
//
//
//			Message messageEndUser = Message.builder()
//					.putData("notification_type", Constants.NOTIFICATION_TYPE_SHOP_CREATED)
//					.putData("notification_title", notificationTitle)
//					.putData("notification_message", notificationMessage)
//					.setTopic(appProperties.getMarket_id_for_fcm() + Constants.CHANNEL_MARKET_STAFF)
//					.build();
//
//
//
//			FirebaseMessaging.getInstance().sendAsync(messageEndUser);
//
//
//			return Response.status(Response.Status.CREATED)
//					.entity(shop)
//					.build();
//		}
//		else {
//
//
//			return Response.status(Response.Status.NOT_MODIFIED)
//					.build();
//		}
//	}
//
//
//
//
//	@POST
//	@Path("/CreateShopByAdmin/{UserID}")
//	@RolesAllowed({Constants.ROLE_STAFF})
//	public Response createShopByStaff(Shop shop, @PathParam("UserID") int userID)
//	{
//
//
//		User user = userAuthentication.isUserAllowed(request,
//				Arrays.asList(Constants.ROLE_STAFF));
//
//
//
//		if(user==null)
//		{
//			return Response.status(Response.Status.FORBIDDEN)
//					.build();
//		}
//
//
//
//
//		int idOfInsertedRow = shopDAO.insertShop(shop,userID);
//		shop.setShopID(idOfInsertedRow);
//
//
//		if(idOfInsertedRow >=1)
//		{
//
//			return Response.status(Response.Status.CREATED)
//					.entity(shop)
//					.build();
//		}
//		else {
//
//
//			return Response.status(Response.Status.NOT_MODIFIED)
//					.build();
//		}
//	}
//
//
//
//
//
//	@PUT
//	@Path("/UpdateByAdmin/{ShopID}")
//	@RolesAllowed({Constants.ROLE_STAFF})
//	public Response updateShopByAdmin(Shop shop, @PathParam("ShopID") int ShopID)
//	{
//
//
//
//		User user = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_STAFF));
//
//		if(user==null)
//		{
//			return Response.status(Response.Status.FORBIDDEN)
//					.build();
//		}
//
//
//
//		if(user.getRole()== Constants.ROLE_STAFF_CODE) {
//
//			StaffPermissions permissions = daoStaff.getStaffPermissions(user.getUserID());
//
//			if (!permissions.isPermitApproveShops())
//			{
//				// the staff member doesnt have persmission to update shop
//				return Response.status(Response.Status.FORBIDDEN)
//						.build();
//			}
//		}
//
//
//		shop.setShopID(ShopID);
//		int rowCount = shopDAO.updateShopByAdmin(shop);
//
//
//		if(rowCount >= 1)
//		{
//			return Response.status(Response.Status.OK)
//					.build();
//		}
//		else
//		{
//			return Response.status(Response.Status.NOT_MODIFIED)
//					.build();
//		}
//
//	}
//
//
//
//
//
//	//, @PathParam("ShopID")int ShopID
//
//	@PUT
//	@Path("/UpdateBySelf")
//	@RolesAllowed({Constants.ROLE_SHOP_ADMIN})
//	public Response updateShopByOwner(Shop shop)
//	{
//
//		User userAuthenticated = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_SHOP_ADMIN));
//
//		if(userAuthenticated==null)
//		{
//			return Response.status(Response.Status.FORBIDDEN)
//					.build();
//		}
//
//
//
//
//		shop.setShopAdminID(userAuthenticated.getUserID());
//
//
//		int rowCount = shopDAO.updateShopBySelf(shop);
//
//		if(rowCount >= 1)
//		{
//			return Response.status(Response.Status.OK)
//					.build();
//		}
//		else
//		{
//			return Response.status(Response.Status.NOT_MODIFIED)
//					.build();
//		}
//	}
//
//
//
//
//
//	@PUT
//	@Path("/SetShopOpen")
//	@RolesAllowed({Constants.ROLE_SHOP_ADMIN,Constants.ROLE_ADMIN})
//	public Response setShopOpen()
//	{
//
//		User userAuthenticated = userAuthentication.isUserAllowed(request,
//				Arrays.asList(Constants.ROLE_SHOP_ADMIN,Constants.ROLE_ADMIN));
//
//		if(userAuthenticated==null)
//		{
//			return Response.status(Response.Status.FORBIDDEN)
//					.build();
//		}
//
//
//		int shopAdminID = userAuthenticated.getUserID();
//
//		int rowCount = shopDAO.setShopOpen(true,shopAdminID);
//
//
//		if(rowCount >= 1)
//		{
//			return Response.status(Response.Status.OK)
//					.build();
//		}
//		else
//		{
//			return Response.status(Response.Status.NOT_MODIFIED)
//					.build();
//		}
//	}
//
//
//
//
//
//	@PUT
//	@Path("/SetShopClosed")
//	@RolesAllowed({Constants.ROLE_SHOP_ADMIN,Constants.ROLE_ADMIN})
//	public Response setShopClosed()
//	{
//
//		User userAuthenticated = userAuthentication.isUserAllowed(request,
//				Arrays.asList(Constants.ROLE_SHOP_ADMIN,Constants.ROLE_ADMIN));
//
//		if(userAuthenticated==null)
//		{
//			return Response.status(Response.Status.FORBIDDEN)
//					.build();
//		}
//
//
//
//		int shopAdminID = userAuthenticated.getUserID();
//
//		int rowCount = shopDAO.setShopOpen(false,shopAdminID);
//
//
//		if(rowCount >= 1)
//		{
//			return Response.status(Response.Status.OK)
//					.build();
//		}
//
//		return Response.status(Response.Status.NOT_MODIFIED)
//				.build();
//
//	}
//
//
//
//
//
//
//	@DELETE
//	@Path("/{ShopID}")
//	@RolesAllowed({Constants.ROLE_SHOP_ADMIN, Constants.ROLE_ADMIN, Constants.ROLE_STAFF})
//	public Response deleteShop(@PathParam("ShopID") int shopID)
//	{
//
//
//
//		User userAuthenticated = userAuthentication.isUserAllowed(request,
//				Arrays.asList(Constants.ROLE_SHOP_ADMIN,Constants.ROLE_STAFF));
//
//		if(userAuthenticated==null)
//		{
//			return Response.status(Response.Status.FORBIDDEN)
//					.build();
//		}
//
//
//
//		if(userAuthenticated.getRole()== Constants.ROLE_SHOP_ADMIN_CODE)
//		{
//			shopID = daoUserUtility.getShopIDForShopAdmin(userAuthenticated.getUserID());
//		}
//		else if(userAuthenticated.getRole()== Constants.ROLE_STAFF_CODE)
//		{
//
//			StaffPermissions permissions = daoStaff.getStaffPermissions(userAuthenticated.getUserID());
//
//			if(!permissions.isPermitApproveShops())
//			{
//				return Response.status(Response.Status.BAD_REQUEST)
//						.build();
//			}
//
//		}
//
//
//
//
//
//		int rowCount = shopDAO.deleteShop(shopID);
//
//
//		if(rowCount>=1)
//		{
//			return Response.status(Response.Status.OK)
//					.build();
//		}
//		else
//		{
//
//			return Response.status(Response.Status.NOT_MODIFIED)
//					.build();
//		}
//
//	}
//
//
//
//
//	@GET
//	@Path("/GetShopDetails/{ShopID}")
//	public Response getShopDetails(@PathParam("ShopID") int shopID,
//								   @QueryParam("latCenter")double latCenter, @QueryParam("lonCenter")double lonCenter )
//	{
//		Shop shop = shopDAO.getShopDetails(shopID,latCenter,lonCenter);
//
//
//		if(shop!= null)
//		{
//			return Response.status(Response.Status.OK)
//					.entity(shop)
//					.build();
//
//		}
//		else
//		{
//
//			return Response.status(Response.Status.NO_CONTENT)
//					.build();
//
//		}
//	}
//
//
//
//
//
//
//	@GET
//	@Path("/GetShopDetailsForDetailScreen")
//	public Response getShopDetailsForDetailScreen(
//			@QueryParam("GetShopDetails")boolean getShopDetails,
//			@QueryParam("ShopID")Integer shopID,
//			@QueryParam("SortBy") String sortBy,
//			@QueryParam("Limit")Integer limit, @QueryParam("Offset")int offset)
//	{
//
//
//		try
//		{
//
//
//			User user = userAuthentication.isUserAllowed(request,
//					Arrays.asList(Constants.ROLE_END_USER));
//
//
//
//			if(limit!=null && limit >= appProperties.getMax_limit())
//			{
//				limit = appProperties.getMax_limit();
//			}
//
//
//
//
//			ShopImageEndPoint endPoint = new ShopImageEndPoint();
//
//
//			List<ShopImage> list =
//					shopImageDAO.getShopImagesForEndUser(
//							shopID,
//							sortBy
//					);
//
//			endPoint.setResults(list);
//
//
////        System.out.println("Shop ID : " + shopID + " | Size : " + endPoint.getResults().size());
//
//
//			if(getShopDetails)
//			{
//				endPoint.setShopDetails(shopDAO.getShopDetails(shopID,0.00,0.00));
//
//
//				if(user!=null)
//				{
//					List<ShopReview> shopReviewList = shopReviewDAOPrepared.getShopReviews(shopID,user.getUserID(),null,1,0);
//
//					if(shopReviewList.size()==1)
//					{
//						endPoint.setShopReview(shopReviewList.get(0));
//					}
//				}
//
//			}
//
//
//
//			if(user!=null)
//			{
//
//				boolean isFavourite = favoriteShopDAOPrepared.checkFavourite(
//						shopID,user.getUserID()
//				);
//
//				endPoint.setFavourite(isFavourite);
//			}
//
//
//
//
//			if(limit!=null)
//			{
//				endPoint.setLimit(limit);
//				endPoint.setOffset(offset);
//				endPoint.setMax_limit(appProperties.getMax_limit());
//			}
//
//
//
////		try {
////			Thread.sleep(2000);
////		} catch (InterruptedException e) {
////			e.printStackTrace();
////		}
//
//
//
//			//Marker
//
//			return Response.status(Response.Status.OK)
//					.entity(endPoint)
//					.build();
//
//
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//
//			return Response.status(Response.Status.NO_CONTENT)
//					.build();
//
//		}
//	}
//
//
//
//
//
//
//	@GET
//	@Path("/GetShopIDForShopAdmin/{ShopAdminID}")
//	public Response getShopIDForShopAdmin(@PathParam("ShopAdminID") int shopAdminID)
//	{
//
//		int shopID = daoUserUtility.getShopIDForShopAdmin(shopAdminID);
//
//		Shop shop = new Shop();
//		shop.setShopID(shopID);
//
//
//
//		if(shopID>0)
//		{
//			return Response.status(Response.Status.OK)
//					.entity(shop)
//					.build();
//		}
//		else
//		{
//			return Response.status(Response.Status.NO_CONTENT)
//					.build();
//		}
//	}
//
//
//
//	@GET
//	@Path("/GetShopForShopAdmin")
//	@RolesAllowed({Constants.ROLE_SHOP_ADMIN,Constants.ROLE_ADMIN})
//	public Response getShopForShopAdmin(@QueryParam("GetStats") boolean getStats)
//	{
//
//		User userAuthenticated = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_SHOP_ADMIN,Constants.ROLE_ADMIN));
//
//		if(userAuthenticated==null)
//		{
//			return Response.status(Response.Status.FORBIDDEN)
//					.build();
//		}
//
//
//		Shop shop = daoUserUtility.getShopDetailsForShopAdmin(userAuthenticated.getUserID());
//
//
//
//		if(getStats)
//		{
//			int itemInShopCount = shopItemDAO.getShopItemAvailability(
//					null,shop.getShopID(),null,
//					null,
//					null,null,null,
//					true,
//					true
//			).getItemCount();
//
//
//
//			int outOfStockCount = shopItemByShopDAO.getShopItems(
//					null,false,shop.getShopID(),
//					null,null,
//					null,null,null,
//					null,null,
//					true,null,
//					null,
//					null,null,
//					10,0,
//					true,true
//			).getItemCount();
//
//
//
//
//			int priceNotSetCount = shopItemByShopDAO.getShopItems(
//					null,false,shop.getShopID(),
//					null,null,
//					null,null,null,
//					null,null,
//					null,true,
//					null,
//					null,null,
//					10,0,
//					true,true
//			).getItemCount();
//
//
//
//			int ordersNotConfirmedPFS = orderService.getOrdersList(
//					null,shop.getShopID(),
//					Order.DELIVERY_MODE_PICKUP_FROM_SHOP,null,
//					OrderStatusPickFromShop.ORDER_PLACED,null,null,
//					null,null,
//					10,0,
//					true,true
//			).getItemCount();
//
//
//			int ordersNotConfirmedHD = orderService.getOrdersList(
//					null,shop.getShopID(),
//					Order.DELIVERY_MODE_HOME_DELIVERY,OrderStatusHomeDelivery.ORDER_PLACED,
//					null,null,null,
//					null,null,
//					10,0,
//					true,true
//			).getItemCount();
//
//
//
//			ShopStats stats  = new ShopStats();
//			stats.setOrdersNotConfirmedHD(ordersNotConfirmedHD);
//			stats.setOrdersNotConfirmedPFS(ordersNotConfirmedPFS);
//			stats.setItemsInShopCount(itemInShopCount);
//			stats.setPriceNotSetCount(priceNotSetCount);
//			stats.setOutOfStockCount(outOfStockCount);
//
//			shop.setShopStats(stats);
//
//
//
////			int ordersNotConfirmed = ordersNotConfirmedHD + ordersNotConfirmedPFS;
////
////			shop.setRt_orders_not_confirmed_count(ordersNotConfirmed);
////			shop.setRt_stats_items_in_shop_count(itemInShopCount);
////			shop.setRt_stats_price_not_set_count(priceNotSetCount);
////			shop.setRt_stats_out_of_stock_count(outOfStockCount);
//		}
//
//
//
//
//		// shop status
//		// 1. Check items added to shop
//		// 2. Check prices set or not
//
//		if(shop!=null)
//		{
//			return Response.status(Response.Status.OK)
//					.entity(shop)
//					.build();
//		}
//		else
//		{
//
//			return Response.status(Response.Status.NO_CONTENT)
//					.build();
//		}
//
//
//	}
//
//
//
//
//
//	@GET
//	@Path("/GetShopForShopStaff")
//	@RolesAllowed({Constants.ROLE_SHOP_STAFF})
//	public Response getShopForShopStaff()
//	{
//
//		User userAuthenticated = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_SHOP_STAFF));
//
//		if(userAuthenticated==null)
//		{
//			return Response.status(Response.Status.FORBIDDEN)
//					.build();
//		}
//
//
//
//		Shop shop = daoUserUtility.getShopForShopStaff(userAuthenticated.getUserID());
//
//
//		if(shop!= null)
//		{
//			return Response.status(Response.Status.OK)
//					.entity(shop)
//					.build();
//
//		} else
//		{
//
//			return Response.status(Response.Status.NO_CONTENT)
//					.build();
//		}
//
//
//	}
//
//
//
//
//
//
//
//	//	@PUT
////	@Path("/BecomeASeller")
////	@Consumes(MediaType.APPLICATION_JSON)
////	@RolesAllowed({GlobalConstants.ROLE_END_USER})
////	public Response becomeASeller()
////	{
////		// this is deprecated and no longer required ... create shop method has replaced it
////
////		User user = (User) Globals.accountApproved;
////
////		int rowCount = Globals.daoShopStaff.becomeASeller(user.getUserID());
////
////
////		if(rowCount >= 1)
////		{
////			return Response.status(Response.Status.OK)
////					.build();
////		}
////		else {
////
////			return Response.status(Status.NOT_MODIFIED)
////					.build();
////		}
////	}
//
//
//
//
//	@PUT
//	@Path("/AddBalance/{ShopAdminID}/{AmountToAdd}")
//	@RolesAllowed({Constants.ROLE_ADMIN, Constants.ROLE_STAFF})
//	public Response addBalance(@PathParam("ShopAdminID") int shopAdminID,
//											 @PathParam("AmountToAdd") double amountToAdd)
//	{
//
//		User user = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_END_USER));
//
//		if(user==null)
//		{
//			return Response.status(Response.Status.FORBIDDEN)
//					.build();
//		}
//
//
//		StaffPermissions permissions = daoStaff.getStaffPermissions(user.getUserID());
//
//		// check staff permissions
//		if(user.getRole()!= Constants.ROLE_ADMIN_CODE)
//		{
//			if(permissions==null || !permissions.isPermitApproveShops())
//			{
//				return Response.status(Response.Status.FORBIDDEN)
//						.build();
//			}
//		}
//
//
//
//
//		int rowCount = addBalanceDAO.add_balance_to_shop(shopAdminID,amountToAdd);
//
//
//		if(rowCount >= 1)
//		{
//			return Response.status(Response.Status.OK)
//					.build();
//		}
//		else {
//
//			return Response.status(Response.Status.NOT_MODIFIED)
//					.build();
//		}
//
//
//	}
//
//
//
//
//
//
//	@GET
//	@Path("/QuerySimple")
//	public Response getShopListSimple(
//			@QueryParam("UnderReview")Boolean underReview,
//			@QueryParam("Enabled")Boolean enabled, @QueryParam("Waitlisted") Boolean waitlisted,
//			@QueryParam("latCenter")Double latCenter, @QueryParam("lonCenter")Double lonCenter,
//			@QueryParam("SearchString") String searchString,
//			@QueryParam("SortBy") String sortBy,
//			@QueryParam("Limit") int limit, @QueryParam("Offset") int offset,
//			@QueryParam("GetRowCount")boolean getRowCount,
//			@QueryParam("MetadataOnly")boolean getOnlyMetaData
//	)
//	{
//
//
//
//		if(limit >= appProperties.getMax_limit())
//		{
//			limit = appProperties.getMax_limit();
//		}
//
//
//
//		ShopEndPoint endPoint = shopDAO.getShopsListQuerySimple(
//									underReview,
//									enabled,waitlisted,
//									latCenter,lonCenter,
//									searchString, sortBy,
//									limit,offset, getRowCount,getOnlyMetaData
//		);
//
//
//		endPoint.setLimit(limit);
//		endPoint.setMax_limit(appProperties.getMax_limit());
//		endPoint.setOffset(offset);
//
//
//		/*try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}*/
//
//
//
//
//		//Marker
//		return Response.status(Response.Status.OK)
//				.entity(endPoint)
//				.build();
//	}
//
//
//
//
//
//
//
//	@GET
//	public Response getShops(
//			@QueryParam("LeafNodeItemCategoryID")Integer itemCategoryID,
//			@QueryParam("latCenter")Double latCenter, @QueryParam("lonCenter")Double lonCenter,
//			@QueryParam("deliveryRangeMax")Double deliveryRangeMax,
//			@QueryParam("deliveryRangeMin")Double deliveryRangeMin,
//			@QueryParam("proximity")Double proximity,
//			@QueryParam("SearchString") String searchString,
//			@QueryParam("SortBy") String sortBy,
//			@QueryParam("Limit") int limit, @QueryParam("Offset") int offset,
//			@QueryParam("GetRowCount")boolean getRowCount,
//			@QueryParam("MetadataOnly")boolean getOnlyMetaData
//	)
//	{
//
//
//
//		if(limit >= appProperties.getMax_limit())
//		{
//			limit = appProperties.getMax_limit();
//		}
//
//
//
//		ShopEndPoint endPoint = shopDAO.getShopListQueryJoin(itemCategoryID,
//				latCenter,lonCenter,
//				deliveryRangeMin,deliveryRangeMax,proximity,
//				searchString,sortBy,
//				limit,offset,
//				getRowCount,getOnlyMetaData);
//
//
//
//		endPoint.setLimit(limit);
//		endPoint.setOffset(offset);
//		endPoint.setMax_limit(appProperties.getMax_limit());
//
//
//
//		//Marker
//		return Response.status(Response.Status.OK)
//				.entity(endPoint)
//				.build();
//	}
//
//
//
//
//
//
//
//	@GET
//	@Path("/FilterByItemCat")
//	public Response filterShopsByItemCategory(
//			@QueryParam("ItemCategoryID") Integer itemCategoryID,
//			@QueryParam("latCenter")Double latCenter, @QueryParam("lonCenter")Double lonCenter,
//			@QueryParam("proximity")Double proximity,
//			@QueryParam("GetSubcategories")boolean getSubcategories,
//			@QueryParam("GetBannerImages")boolean getBannerImages,
//			@QueryParam("SearchString") String searchString,
//			@QueryParam("SortBy") String sortBy,
//			@QueryParam("Limit") int limit, @QueryParam("Offset") int offset,
//			@QueryParam("GetRowCount")boolean getRowCount,
//			@QueryParam("MetadataOnly")boolean getOnlyMetaData
//	)
//	{
//
//
//		List<ItemCategory> subcategories;
//
//
//
//		if(limit >= appProperties.getMax_limit())
//		{
//			limit = appProperties.getMax_limit();
//		}
//
//
//		ShopEndPoint endPoint = shopDAO.filterShopsByItemCategory(
//				itemCategoryID,
//				latCenter,lonCenter,
//				null,null,
//				proximity,
//				searchString,
//				sortBy,
//				limit,offset,
//				getRowCount,getOnlyMetaData
//		);
//
//
//
//
//		if(getSubcategories)
//		{
//			if(itemCategoryID==null)
//			{
//				itemCategoryID=1;
//			}
//
//			subcategories = itemCategoryDAO.getItemCategoriesJoinRecursive(
//					null, 1, null,
//					latCenter, lonCenter,
//					true,
//					null,
//					ItemCategory.CATEGORY_ORDER,
//					null,null
//			);
//
//
//
//			endPoint.setSubcategories(subcategories);
//		}
//
//
//		if(getBannerImages)
//		{
//			List<BannerImage> bannerImages = bannerImageDAO.getBannerImages(
//					null,BannerImage.SORT_ORDER,
//					true,false
//			);
//
//
//			endPoint.setBannerImages(bannerImages);
//		}
//
//
//
//		endPoint.setLimit(limit);
//		endPoint.setOffset(offset);
//		endPoint.setMax_limit(appProperties.getMax_limit());
//
//
///*
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}*/
//
//
//		//Marker
//		return Response.status(Response.Status.OK)
//				.entity(endPoint)
//				.build();
//
//	}
//
//
//
//
//
//
//
//
//
//	// Image MEthods
//
//	private static final java.nio.file.Path BASE_DIR = Paths.get("./data/images/Shop");
//	private static final double MAX_IMAGE_SIZE_MB = 2;
//
//
//
//
//
//
//	@POST
//	@Path("/Image")
//	@Consumes({MediaType.APPLICATION_OCTET_STREAM})
//	@RolesAllowed({Constants.ROLE_END_USER})
//	public Response uploadImage(InputStream in, @HeaderParam("Content-Length") long fileSize,
//								@QueryParam("PreviousImageName") String previousImageName
//	) throws Exception
//	{
//
//
//		if(previousImageName!=null)
//		{
//			Files.deleteIfExists(BASE_DIR.resolve(previousImageName));
//			Files.deleteIfExists(BASE_DIR.resolve("three_hundred_" + previousImageName + ".jpg"));
//			Files.deleteIfExists(BASE_DIR.resolve("five_hundred_" + previousImageName + ".jpg"));
//		}
//
//
//		File theDir = new File(BASE_DIR.toString());
//
//		// if the directory does not exist, create it
//		if (!theDir.exists()) {
//
////			System.out.println("Creating directory: " + BASE_DIR.toString());
//
//			boolean result = false;
//
//			try{
//				theDir.mkdir();
//				result = true;
//			}
//			catch(Exception se){
//				//handle it
//			}
//			if(result) {
////				System.out.println("DIR created");
//			}
//		}
//
//
//
//		String fileName = "" + System.currentTimeMillis();
//
//		// Copy the file to its location.
//		long filesize = Files.copy(in, BASE_DIR.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
//
//		if(filesize > MAX_IMAGE_SIZE_MB * 1048 * 1024)
//		{
//			// delete file if it exceeds the file size limit
//			Files.deleteIfExists(BASE_DIR.resolve(fileName));
//
//			return Response.status(Response.Status.EXPECTATION_FAILED).build();
//		}
//
//
//		createThumbnails(fileName);
//
//
//		Image image = new Image();
//		image.setPath(fileName);
//
//		// Return a 201 Created response with the appropriate Location header.
//
//		return Response.status(Response.Status.CREATED).location(URI.create("/api/Images/" + fileName)).entity(image).build();
//	}
//
//
//
//
//
//
//	private void createThumbnails(String filename)
//	{
//		try {
//
//			Thumbnails.of(BASE_DIR.toString() + "/" + filename)
//					.size(300,300)
//					.outputFormat("jpg")
//					.toFile(new File(BASE_DIR.toString() + "/" + "three_hundred_" + filename));
//
//			//.toFile(new File("five-" + filename + ".jpg"));
//
//			//.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
//
//
//			Thumbnails.of(BASE_DIR.toString() + "/" + filename)
//					.size(500,500)
//					.outputFormat("jpg")
//					.toFile(new File(BASE_DIR.toString() + "/" + "five_hundred_" + filename));
//
//
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//
//
//
//
//	@GET
//	@Path("/Image/{name}")
//	@Produces("image/jpeg")
//	public InputStream getImage(@PathParam("name") String fileName) {
//
//		//fileName += ".jpg";
//		java.nio.file.Path dest = BASE_DIR.resolve(fileName);
//
//		if (!Files.exists(dest)) {
//			throw new WebApplicationException(Response.Status.NOT_FOUND);
//		}
//
//
//		try {
//			return Files.newInputStream(dest);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		return null;
//	}
//
//
//
//
//	@DELETE
//	@Path("/Image/{name}")
//	@RolesAllowed({Constants.ROLE_SHOP_ADMIN, Constants.ROLE_ADMIN})
//	public Response deleteImageFile(@PathParam("name")String fileName)
//	{
//
//		boolean deleteStatus = false;
//
//		Response response;
//
////		System.out.println("Filename: " + fileName);
//
//
//		try {
//
//
//			//Files.delete(BASE_DIR.resolve(fileName));
//			deleteStatus = Files.deleteIfExists(BASE_DIR.resolve(fileName));
//
//			// delete thumbnails
//			Files.deleteIfExists(BASE_DIR.resolve("three_hundred_" + fileName + ".jpg"));
//			Files.deleteIfExists(BASE_DIR.resolve("five_hundred_" + fileName + ".jpg"));
//
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//
//		if(!deleteStatus)
//		{
//			response = Response.status(Response.Status.NOT_MODIFIED).build();
//
//		}else
//		{
//			response = Response.status(Response.Status.OK).build();
//		}
//
//		return response;
//	}
//
//}
