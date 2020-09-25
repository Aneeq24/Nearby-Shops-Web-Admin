package org.nearbyshops.RESTEndpoints.RESTEndpointsOrder;

import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.nearbyshops.AppProperties;
import org.nearbyshops.Constants;
import org.nearbyshops.DAOs.DAOOrders.DAOOrderUtility;
import org.nearbyshops.DAOs.DAOOrders.OrderService;
import org.nearbyshops.DAOs.DAOOrders.PlaceOrderDAO;
import org.nearbyshops.DAOs.DAORoles.DAOUserUtility;
import org.nearbyshops.DAOs.DAOSettings.MarketSettingsDAO;
import org.nearbyshops.Model.ModelBilling.RazorPayOrder;
import org.nearbyshops.Model.ModelEndpoint.DeliverySlotEndpoint;
import org.nearbyshops.Model.ModelEndpoint.OrderEndPoint;
import org.nearbyshops.Model.ModelEndpoint.ShopEndPoint;
import org.nearbyshops.Model.ModelEndpoint.UserEndpoint;
import org.nearbyshops.Model.ModelRoles.User;
import org.nearbyshops.Model.ModelSettings.MarketSettings;
import org.nearbyshops.Model.Order;
import org.nearbyshops.Utility.SendEmail;
import org.nearbyshops.Utility.SendPush;
import org.nearbyshops.Utility.SendSMS;
import org.nearbyshops.Utility.UserAuthentication;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.Arrays;


@RestController
@RequestMapping ("/api/Order")
public class OrderResource {


	@Autowired
	AppProperties appProperties;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private UserAuthentication userAuthentication;


	@Autowired
	PlaceOrderDAO placeOrderDAO;


	@Autowired
	OrderService orderService;

	@Autowired
	SendEmail sendEmail;

	@Autowired
	SendSMS sendSMS;


	@Autowired
	MarketSettingsDAO marketSettingsDAO;


	@Autowired
	DAOUserUtility daoUserUtility;


	@Autowired
	DAOOrderUtility daoOrderUtility;




	@PostMapping
	public ResponseEntity<Object> createOrder(@RequestBody Order order,
											  @RequestParam("CartID") int cartID)
	{

		int orderId = placeOrderDAO.placeOrderNew(order, cartID);


		if (orderId != -1) {

			if(order.getPaymentMode()==Order.PAYMENT_MODE_RAZORPAY)
			{
				RazorPayOrder razorPayOrder = order.getRazorPayOrder();

				if(razorPayOrder!=null)
				{
					capturePayment(order.getNetPayable(),razorPayOrder.getRzpPaymentID());
				}
			}



			Order orderResult =  orderService.getOrderDetailsForPush(orderId);



			try {
				// send notification using fcm
				String topic = appProperties.getMarket_id_for_fcm() + Constants.CHANNEL_SHOP_WITH_SHOP_ID + orderResult.getShopID();
//				String topicMarketStaff = Constants.market_id_for_fcm + Constants.CHANNEL_MARKET_STAFF;


				String notificationTitle = "Order Received";
				String notificationMessage = "You have received an order. Please check the order and respond to the customer !";

				SendPush.sendFCMPushNotification(topic, notificationTitle, notificationMessage, Constants.NOTIFICATION_TYPE_ORDER_RECEIVED);
				SendPush.sendFCMPushNotification(appProperties.getMarket_id_for_fcm() + Constants.CHANNEL_MARKET_STAFF, "Staff Notification", notificationMessage, Constants.NOTIFICATION_TYPE_GENERAL);

				if(orderResult.getDeliveryMode()==Order.DELIVERY_MODE_HOME_DELIVERY)
				{
					SendPush.sendFCMPushNotification(appProperties.getMarket_id_for_fcm() + Constants.CHANNEL_DELIVERY_STAFF, "Delivery Notification", notificationMessage, Constants.NOTIFICATION_TYPE_ORDER_RECEIVED);
				}



				User shopAdminProfile = orderResult.getRt_shop_admin_profile();


				if(shopAdminProfile.getEmail()!=null)
				{
					String htmlText = "<h2>You have received an order .</h2>" +
							"<p>Dear shop owner you have received an order with ID " + orderId + " ."
							+ " Please confirm and process the order.<p>";



					MarketSettings marketSettings = marketSettingsDAO.getSettingsInstance();


					Email emailComposed = EmailBuilder.startingBlank()
							.from(marketSettings.getEmailSenderName(),marketSettings.getEmailAddressForSender())
							.to(shopAdminProfile.getName(),shopAdminProfile.getEmail())
							.withSubject("You have received an Order with Order Number :  " + orderId + "")
							.withHTMLText(htmlText)
							.buildEmail();

					sendEmail.getMailerInstance().sendMail(emailComposed,true);
				}



				if(shopAdminProfile.getPhone()!=null)
				{
					sendSMS.sendSMS("Dear shop owner you have received an order with order number " + orderId + ". Please confirm and process the order. ",
							shopAdminProfile.getPhone());
				}



			}
			catch (Exception exception)
			{
				exception.printStackTrace();
			}






			return ResponseEntity.status(HttpStatus.CREATED)
					.build();


		}
		else {

			return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
					.build();
		}

	}





	void capturePayment(double amount, String paymentID)
	{

		try {

			RazorpayClient razorpayClient = new RazorpayClient(appProperties.getRazorpay_key_id(),appProperties.getRazorpay_key_secret());

			JSONObject captureRequest = new JSONObject();
			captureRequest.put("amount", amount*100);
			captureRequest.put("currency", "INR");

			Payment payment = razorpayClient.Payments.capture(paymentID, captureRequest);

		} catch (RazorpayException e) {
			// Handle Exception
			System.out.println(e.getMessage());
		}
	}




	@GetMapping
	@RolesAllowed({Constants.ROLE_END_USER})
	public ResponseEntity<Object> getOrders(
			@RequestParam(value = "ShopID",required = false)Integer shopID,
			@RequestParam(value = "EndUserID",required = false) Integer endUserID,
			@RequestParam(value = "DeliveryMode",required = false) Integer deliveryMode,
			@RequestParam(value = "DeliveryDate",required = false) Date deliveryDate,
			@RequestParam(value = "DeliverySlotID",required = false)Integer deliverySlotID,
			@RequestParam(value = "StatusHomeDelivery",required = false)Integer homeDeliveryStatus,
			@RequestParam(value = "StatusPickFromShopStatus",required = false)Integer pickFromShopStatus,
			@RequestParam(value = "PendingOrders",required = false) Boolean pendingOrders,

			@RequestParam(value = "GetFilterDeliveryPerson",defaultValue = "false") boolean getFilterDeliveryPerson,
			@RequestParam(value = "GetFilterShops",defaultValue = "false") boolean getFilterShops,
			@RequestParam(value = "GetFilterDeliverySlots",defaultValue = "false") boolean getFilterDeliverySlots,

			@RequestParam(value = "SearchString",required = false) String searchString,
			@RequestParam(value = "SortBy",required = false) String sortBy,
			@RequestParam(value = "Limit",defaultValue = "0")int limit,
			@RequestParam(value = "Offset",defaultValue = "0")int offset,
			@RequestParam(value = "GetRowCount",defaultValue = "false")boolean getRowCount,
			@RequestParam(value = "MetadataOnly",defaultValue = "false")boolean getOnlyMetaData
	)
	{



		User user = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_END_USER));

		if(user==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}



		if(shopID!=null)
		{
			if(user.getRole()== Constants.ROLE_SHOP_ADMIN_CODE)
			{
				shopID = daoUserUtility.getShopIDForShopAdmin(user.getUserID());
			}
			else if(user.getRole()== Constants.ROLE_SHOP_STAFF_CODE)
			{
				shopID = daoUserUtility.getShopIDforShopStaff(user.getUserID());
			}

		}


//		System.out.println("Shop ID : " +  shopID);
//		System.out.println("EndUser ID : " +  endUserID);




		if(endUserID!=null)
		{
			if(user.getRole()==Constants.ROLE_END_USER_CODE)
			{
				endUserID = user.getUserID();
			}
		}



		if(limit >= appProperties.getMax_limit())
		{
			limit = appProperties.getMax_limit();
		}



		OrderEndPoint endpoint  = null;


		endpoint =  orderService.getOrdersList(
				endUserID, shopID,
				deliveryMode,
				homeDeliveryStatus,pickFromShopStatus,
				null,
				pendingOrders,
				searchString,
				sortBy,
				limit,offset,
				getRowCount,getOnlyMetaData
		);




		endpoint.setLimit(limit);
		endpoint.setOffset(offset);
		endpoint.setMax_limit(appProperties.getMax_limit());




//		if(getFilterDeliveryPerson)
//		{
//			UserEndpoint userEndpoint = daoOrderUtility.fetchDeliveryGuys(
//					deliveryMode,
//					deliveryDate,
//
//					null,
//					homeDeliveryStatus,
//					shopID,
//					deliverySlotID,
//					"order_count desc",
//					true,false
//			);
//
//
//			endpoint.setDeliveryPersonList(userEndpoint.getResults());
//			endpoint.setDeliveryPersonCount(userEndpoint.getItemCount());
//		}


		if(getFilterShops)
		{
			ShopEndPoint shopEndPoint = daoOrderUtility.fetchShops(
					endUserID,
					deliveryMode,
					deliveryDate,

					true,

					null,
					homeDeliveryStatus,
					0,0,
					false,
					null,
					deliverySlotID,
					"order_count desc",
					true,false
			);

			endpoint.setShopList(shopEndPoint.getResults());
			endpoint.setShopCount(shopEndPoint.getItemCount());
		}




		if(getFilterDeliverySlots)
		{

			DeliverySlotEndpoint deliverySlotEndpoint = daoOrderUtility.fetchDeliverySlots(
					endUserID,
					deliveryMode,
					deliveryDate,
					false,

					null,
					homeDeliveryStatus,
					shopID,
					false,
					null,
					"order_count desc",
					true,false
			);


			endpoint.setDeliverySlotList(deliverySlotEndpoint.getResults());
			endpoint.setDeliverySlotCount(deliverySlotEndpoint.getItemCount());

		}





//
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}

		//Marker

		return ResponseEntity.status(HttpStatus.OK)
				.body(endpoint);
	}





	@GetMapping ("/OrdersListForEndUser")
	@RolesAllowed({Constants.ROLE_END_USER})
	public ResponseEntity<Object> getOrdersForEndUser(
			@RequestParam(value = "ShopID",required = false)Integer shopID,
			@RequestParam(value = "EndUserID",required = false) Integer endUserID,
			@RequestParam(value = "DeliveryMode",required = false) Integer deliveryMode,
			@RequestParam(value = "DeliveryDate",required = false) Date deliveryDate,
			@RequestParam(value = "DeliverySlotID",required = false)Integer deliverySlotID,
			@RequestParam(value = "StatusHomeDelivery",required = false)Integer homeDeliveryStatus,
			@RequestParam(value = "StatusPickFromShopStatus",required = false)Integer pickFromShopStatus,
			@RequestParam(value = "PendingOrders",required = false) Boolean pendingOrders,

			@RequestParam(value = "GetFilterDeliveryPerson",defaultValue = "false") boolean getFilterDeliveryPerson,
			@RequestParam(value = "GetFilterShops",defaultValue = "false") boolean getFilterShops,
			@RequestParam(value = "GetFilterDeliverySlots",defaultValue = "false") boolean getFilterDeliverySlots,

			@RequestParam(value = "SearchString",required = false) String searchString,
			@RequestParam(value = "SortBy",required = false) String sortBy,
			@RequestParam(value = "Limit",defaultValue = "0")int limit,
			@RequestParam(value = "Offset",defaultValue = "0")int offset,
			@RequestParam(value = "GetRowCount",defaultValue = "false")boolean getRowCount,
			@RequestParam(value = "MetadataOnly",defaultValue = "false")boolean getOnlyMetaData
	)
	{



		User user = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_END_USER));

		if(user==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}



		if(shopID!=null)
		{
			if(user.getRole()== Constants.ROLE_SHOP_ADMIN_CODE)
			{
				shopID = daoUserUtility.getShopIDForShopAdmin(user.getUserID());
			}
			else if(user.getRole()== Constants.ROLE_SHOP_STAFF_CODE)
			{
				shopID = daoUserUtility.getShopIDforShopStaff(user.getUserID());
			}

		}





		if(endUserID!=null)
		{
			if(user.getRole()==Constants.ROLE_END_USER_CODE)
			{
				endUserID = user.getUserID();
			}
		}



		if(limit >= appProperties.getMax_limit())
		{
			limit = appProperties.getMax_limit();
		}



		OrderEndPoint endpoint  = null;


		endpoint = orderService.getOrdersListForEndUser(
				endUserID, shopID,
				deliveryMode,
				homeDeliveryStatus,pickFromShopStatus,
				null,
				pendingOrders,
				searchString,
				sortBy,
				limit,offset,
				getRowCount,getOnlyMetaData
		);




		endpoint.setLimit(limit);
		endpoint.setOffset(offset);
		endpoint.setMax_limit(appProperties.getMax_limit());




//		if(getFilterDeliveryPerson)
//		{
//			UserEndpoint userEndpoint = daoOrderUtility.fetchDeliveryGuys(
//					deliveryMode,
//					deliveryDate,
//
//					null,
//					homeDeliveryStatus,
//					shopID,
//					deliverySlotID,
//					"order_count desc",
//					true,false
//			);
//
//
//			endpoint.setDeliveryPersonList(userEndpoint.getResults());
//			endpoint.setDeliveryPersonCount(userEndpoint.getItemCount());
//		}


		if(getFilterShops)
		{
			ShopEndPoint shopEndPoint = daoOrderUtility.fetchShops(
					endUserID,
					deliveryMode,
					deliveryDate,

					true,

					null,
					homeDeliveryStatus,
					0,0,
					false,
					null,
					deliverySlotID,
					"order_count desc",
					true,false
			);

			endpoint.setShopList(shopEndPoint.getResults());
			endpoint.setShopCount(shopEndPoint.getItemCount());
		}




		if(getFilterDeliverySlots)
		{

			DeliverySlotEndpoint deliverySlotEndpoint = daoOrderUtility.fetchDeliverySlots(
					endUserID,
					deliveryMode,
					deliveryDate,
					false,

					null,
					homeDeliveryStatus,
					shopID,
					false,
					null,
					"order_count desc",
					true,false
			);


			endpoint.setDeliverySlotList(deliverySlotEndpoint.getResults());
			endpoint.setDeliverySlotCount(deliverySlotEndpoint.getItemCount());

		}





//
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}

		//Marker

		return ResponseEntity.status(HttpStatus.OK)
				.body(endpoint);
	}








	@GetMapping ("/OrdersListForDelivery")
	@RolesAllowed({Constants.ROLE_END_USER})
	public ResponseEntity<Object> getOrdersForDelivery(
			@RequestParam(value = "DeliveryDate",required = false) Date deliveryDate,
			@RequestParam(value = "isASAPDelivery",defaultValue = "false") boolean isASAPDelivery,
			@RequestParam(value = "DeliverySlotID",required = false)Integer deliverySlotID,
			@RequestParam(value = "ShopID",required = false)Integer shopID,
			@RequestParam(value = "DeliveryGuyID",required = false)Integer deliveryGuyID,
			@RequestParam(value = "DeliveryGuyNull",defaultValue = "false")boolean deliveryGuyNull,
			@RequestParam(value = "DeliveryMode",required = false) Integer deliveryMode,
			@RequestParam(value = "StatusHDLessThan",required = false)Integer statusHDLessThan,
			@RequestParam(value = "StatusHomeDelivery",required = false)Integer homeDeliveryStatus,
			@RequestParam(value = "latCenter",required = false)Double latCenter,
			@RequestParam(value = "lonCenter",required = false)Double lonCenter,

			@RequestParam(value = "GetFilterDeliveryPerson",defaultValue = "false") boolean getFilterDeliveryPerson,
			@RequestParam(value = "GetFilterShops",defaultValue = "false") boolean getFilterShops,
			@RequestParam(value = "GetFilterDeliverySlots",defaultValue = "false") boolean getFilterDeliverySlots,

			@RequestParam(value = "SearchString",required = false) String searchString,
			@RequestParam(value = "SortBy",required = false) String sortBy,
			@RequestParam(value = "Limit",defaultValue = "0")int limit,
			@RequestParam(value = "Offset",defaultValue = "0")int offset,
			@RequestParam(value = "GetRowCount",defaultValue = "false")boolean getRowCount,
			@RequestParam(value = "MetadataOnly",defaultValue = "false")boolean getOnlyMetaData
	)
	{



		User user = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_END_USER));

		if(user==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}



		if(shopID!=null)
		{
			if(user.getRole()== Constants.ROLE_SHOP_ADMIN_CODE)
			{
				shopID = daoUserUtility.getShopIDForShopAdmin(user.getUserID());
			}
			else if(user.getRole()== Constants.ROLE_SHOP_STAFF_CODE)
			{
				shopID = daoUserUtility.getShopIDforShopStaff(user.getUserID());
			}

		}



		if(deliveryGuyID!=null)
		{
			if(user.getRole()==Constants.ROLE_DELIVERY_GUY_CODE || user.getRole()==Constants.ROLE_DELIVERY_GUY_SELF_CODE)
			{
				deliveryGuyID = user.getUserID();
			}
		}




		if(user.getRole()==Constants.ROLE_DELIVERY_GUY_SELF_CODE)
		{
			shopID= daoUserUtility.getShopIDforDeliveryPerson(user.getUserID());
		}




		if(limit >= appProperties.getMax_limit())
		{
			limit = appProperties.getMax_limit();
		}



		OrderEndPoint endpoint  = null;




		endpoint = orderService.getOrdersListForDelivery(
				deliveryDate,
				isASAPDelivery,
				deliverySlotID,
				shopID,

				deliveryGuyID,
				deliveryGuyNull,

				deliveryMode,

				statusHDLessThan,
				homeDeliveryStatus,

				latCenter,lonCenter,
				searchString, sortBy,
				limit,offset,
				getRowCount,getOnlyMetaData
		);



		endpoint.setLimit(limit);
		endpoint.setOffset(offset);
		endpoint.setMax_limit(appProperties.getMax_limit());




		if(getFilterDeliveryPerson && !deliveryGuyNull)
		{
			UserEndpoint userEndpoint = daoOrderUtility.fetchDeliveryGuys(
					deliveryMode,
					deliveryDate,
					statusHDLessThan,
					homeDeliveryStatus,
					shopID,
					deliverySlotID,
					"order_count desc",
					true,false
			);


			endpoint.setDeliveryPersonList(userEndpoint.getResults());
			endpoint.setDeliveryPersonCount(userEndpoint.getItemCount());
		}




		if(getFilterShops)
		{
			ShopEndPoint shopEndPoint = daoOrderUtility.fetchShops(
					null,
					deliveryMode,
					deliveryDate,

					true,

					statusHDLessThan,
					homeDeliveryStatus,
					latCenter,lonCenter,

					deliveryGuyNull,
					deliveryGuyID,
					deliverySlotID,
					"order_count desc",
					true,false
			);

			endpoint.setShopList(shopEndPoint.getResults());
			endpoint.setShopCount(shopEndPoint.getItemCount());
		}





		if(getFilterDeliverySlots)
		{

			DeliverySlotEndpoint deliverySlotEndpoint = daoOrderUtility.fetchDeliverySlots(
					null,
					deliveryMode,
					deliveryDate,
					true,
					statusHDLessThan,
					homeDeliveryStatus,
					shopID,

					deliveryGuyNull,
					deliveryGuyID,
					"order_count desc",
					true,false
			);



			endpoint.setDeliverySlotList(deliverySlotEndpoint.getResults());
			endpoint.setDeliverySlotCount(deliverySlotEndpoint.getItemCount());

		}





//
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}

		//Marker

		return ResponseEntity.status(HttpStatus.OK)
				.body(endpoint);
	}







	@PutMapping("/CancelByUser/{OrderID}/{EndUserID}")
	@RolesAllowed({Constants.ROLE_END_USER})
	public ResponseEntity<Object> cancelOrderByEndUser(@PathVariable("OrderID")int orderID,
										 @PathVariable("EndUserID")int endUserID)
	{


		User user = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_END_USER));

		if(user==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}




		int rowCount = orderService.cancelOrderByEndUser(orderID,endUserID);


		if(rowCount >= 1)
		{
			return ResponseEntity.status(HttpStatus.OK)
					.build();
		}



		return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
				.build();

	}





	@PutMapping ("/CancelByShop/{OrderID}/{ShopID}")
	@RolesAllowed({Constants.ROLE_SHOP_STAFF,Constants.ROLE_STAFF})
	public ResponseEntity<Object> cancelOrderByShop(@PathVariable("OrderID")int orderID,
													@PathVariable("ShopID")int shopID)
	{

		User user = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_SHOP_STAFF,Constants.ROLE_STAFF));

		if(user==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}


		if(user.getRole()== Constants.ROLE_SHOP_ADMIN_CODE)
		{
			shopID = daoUserUtility.getShopIDForShopAdmin(user.getUserID());
		}
		else if(user.getRole()== Constants.ROLE_SHOP_STAFF_CODE)
		{
			shopID = daoUserUtility.getShopIDforShopStaff(user.getUserID());
		}



		int rowCount = orderService.cancelOrderByShop(orderID,shopID);


		if(rowCount >= 1)
		{
			return ResponseEntity.status(HttpStatus.OK)
					.build();
		}

		return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
				.build();
	}


}
