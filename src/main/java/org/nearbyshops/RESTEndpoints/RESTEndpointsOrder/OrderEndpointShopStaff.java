package org.nearbyshops.RESTEndpoints.RESTEndpointsOrder;

import org.nearbyshops.AppProperties;
import org.nearbyshops.Constants;
import org.nearbyshops.DAOs.DAOOrders.DAOOrderStaff;
import org.nearbyshops.DAOs.DAOOrders.DAOOrderUtility;
import org.nearbyshops.DAOs.DAOOrders.OrderService;
import org.nearbyshops.DAOs.DAORoles.DAOShopStaff;
import org.nearbyshops.DAOs.DAORoles.DAOUserUtility;
import org.nearbyshops.DAOs.DAOSettings.MarketSettingsDAO;
import org.nearbyshops.Model.ModelEndpoint.UserEndpoint;
import org.nearbyshops.Model.ModelRoles.ShopStaffPermissions;
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
import java.util.Arrays;
import java.util.List;




@RestController
@RequestMapping ("/api/Order/ShopStaff")
public class OrderEndpointShopStaff {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private UserAuthentication userAuthentication;


	@Autowired
	private DAOOrderUtility daoOrderUtility;


	@Autowired
	DAOUserUtility daoUserUtility;


	@Autowired
	DAOShopStaff daoShopStaff;


	@Autowired
	AppProperties appProperties;


	@Autowired
	OrderService orderService;

	@Autowired
	DAOOrderStaff daoOrderStaff;

	@Autowired
	SendSMS sendSMS;


	@Autowired
	SendEmail sendEmail;



	@Autowired
	MarketSettingsDAO marketSettingsDAO;





	@GetMapping ("/FetchDeliveryGuys")
	@RolesAllowed({Constants.ROLE_SHOP_ADMIN, Constants.ROLE_SHOP_STAFF})
	public ResponseEntity<Object> fetchDeliveryGuys(
			@RequestParam(value = "StatusHomeDelivery",required = false)Integer homeDeliveryStatus,
			@RequestParam(value = "SortBy",required = false) String sortBy,
			@RequestParam(value = "Limit",required = false)Integer limit,
			@RequestParam(value = "Offset",required = false)Integer offset,
			@RequestParam(value = "GetRowCount",required = false)boolean getRowCount,
			@RequestParam(value = "MetadataOnly",required = false)boolean getOnlyMetaData

	)
	{


		User userAuthenticated = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_SHOP_STAFF));

		if(userAuthenticated==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}



		int shopID = 0;

		// *********************** second Implementation



		if(userAuthenticated.getRole()== Constants.ROLE_SHOP_ADMIN_CODE)
		{
			shopID = daoUserUtility.getShopIDForShopAdmin(userAuthenticated.getUserID());
		}
		else if(userAuthenticated.getRole()== Constants.ROLE_SHOP_STAFF_CODE)
		{
			shopID = daoUserUtility.getShopIDforShopStaff(userAuthenticated.getUserID());
		}




		if(limit!=null)
		{
			if(limit >= appProperties.getMax_limit())
			{
				limit = appProperties.getMax_limit();
			}

			if(offset==null)
			{
				offset = 0;
			}
		}




		getRowCount=true;


		UserEndpoint endpoint = daoOrderUtility.fetchDeliveryGuysBackup(
				shopID,
				homeDeliveryStatus,
				sortBy,limit,offset,
				true,getOnlyMetaData
		);




		if(limit!=null)
		{
			endpoint.setLimit(limit);
			endpoint.setOffset(offset);
			endpoint.setMax_limit(appProperties.getMax_limit());
		}


		return ResponseEntity.status(HttpStatus.OK)
				.body(endpoint);

	}





	
	@PutMapping ("/SetConfirmed/{OrderID}")
	@RolesAllowed({Constants.ROLE_SHOP_ADMIN, Constants.ROLE_SHOP_STAFF})
	public ResponseEntity<Object> confirmOrder(@PathVariable("OrderID")int orderID)
	{

		User userAuthenticated = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_SHOP_STAFF));


		if(userAuthenticated==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}



 		if(userAuthenticated.getRole()== Constants.ROLE_SHOP_STAFF_CODE)
		{
			ShopStaffPermissions permissions = daoShopStaff.getShopStaffPermissions(userAuthenticated.getUserID());

			if(!permissions.isPermitConfirmOrders())
			{

				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.build();
			}

		}




		int rowCount = daoOrderStaff.confirmOrder(orderID);

		if(rowCount >= 1)
		{

			Order orderResult = orderService.getOrderDetailsForPush(orderID);


			String topic = appProperties.getMarket_id_for_fcm() + "end_user_" + orderResult.getEndUserID();

			String notificationTitle = "Order Confirmed";
			String notificationMessage = "Order number " + String.valueOf(orderID) + " has been Confirmed !";
			SendPush.sendFCMPushNotification(topic,notificationTitle,notificationMessage,Constants.NOTIFICATION_TYPE_ORDER_UPDATES);


			return ResponseEntity.status(HttpStatus.OK)
					.build();
		}


		return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
				.build();
	}






	@PutMapping ("/SetOrderPacked/{OrderID}")
	@RolesAllowed({Constants.ROLE_SHOP_ADMIN, Constants.ROLE_SHOP_STAFF})
	public ResponseEntity<Object> setOrderPacked(@PathVariable ("OrderID")int orderID)
	{

		User userAuthenticated = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_SHOP_STAFF));

		if(userAuthenticated==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}


		if(userAuthenticated.getRole()== Constants.ROLE_SHOP_STAFF_CODE)
		{
			ShopStaffPermissions permissions = daoShopStaff.getShopStaffPermissions(userAuthenticated.getUserID());

			if(!permissions.isPermitSetOrdersPacked())
			{
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.build();
			}
		}


			int rowCount = daoOrderStaff.setOrderPacked(orderID);


			if(rowCount >= 1)
			{

				Order orderResult = orderService.getOrderDetailsForPush(orderID);


				String topic = appProperties.getMarket_id_for_fcm() + "end_user_" + orderResult.getEndUserID();

				String notificationTitle = "Order Packed";
				String notificationMessage = "Order number " + String.valueOf(orderID) + " has been Packed !";
				SendPush.sendFCMPushNotification(topic,notificationTitle,notificationMessage,Constants.NOTIFICATION_TYPE_ORDER_UPDATES);



				return ResponseEntity.status(HttpStatus.OK)
						.build();
			}


		return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
				.build();

	}



	@PutMapping ("/SetOutForDelivery/{OrderID}")
	@RolesAllowed({Constants.ROLE_SHOP_ADMIN, Constants.ROLE_SHOP_STAFF})
	public ResponseEntity<Object> deliverBySelf(@PathVariable("OrderID")int orderID)
	{


		User userAuthenticated = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_SHOP_STAFF));

		if(userAuthenticated==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}


		if(userAuthenticated.getRole()== Constants.ROLE_SHOP_STAFF_CODE)
		{
			ShopStaffPermissions permissions =  daoShopStaff.getShopStaffPermissions(userAuthenticated.getUserID());

			if(!permissions.isPermitSetOrdersPacked())
			{
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.build();
			}
		}


		int rowCount =  daoOrderStaff.deliverBySelf(orderID);


		if(rowCount >= 1)
		{

			Order orderResult = orderService.getOrderDetailsForPush(orderID);



			String topic = appProperties.getMarket_id_for_fcm() + "end_user_" + orderResult.getEndUserID();
			String notificationTitle = "Order Delivered";
			String notificationMessage = "Order number " + String.valueOf(orderID) + " has been delivered and amount paid !";

			SendPush.sendFCMPushNotification(topic,notificationTitle,notificationMessage,Constants.NOTIFICATION_TYPE_ORDER_UPDATES);



			User endUserProfile = orderResult.getRt_end_user_profile();


			if(endUserProfile.getEmail()!=null)
			{
				String htmlText = "<h2>Your Order with Order Number : " + orderID + " is Delivered.</h2>" +
						"<p>How was your experience ? Having any issues ... please let us know !" +
						" For support and feedback please call market helpline provided in the market details section !";


				MarketSettings marketSettings = marketSettingsDAO.getSettingsInstance();

				Email emailComposed = EmailBuilder.startingBlank()
						.from(marketSettings.getEmailSenderName(),appProperties.getEmail_address_for_sender())
						.to(orderResult.getRt_end_user_profile().getName(),orderResult.getRt_end_user_profile().getEmail())
						.withSubject("Order No. " + orderID + " is Delivered")
						.withHTMLText(htmlText)
						.buildEmail();


				sendEmail.getMailerInstance().sendMail(emailComposed,true);
			}





			if(endUserProfile.getPhone()!=null)
			{
				sendSMS.sendSMS("Order No. " + orderID + " is Delivered to you ! If you have any issues with the order feel free to contact market administrator !",
						endUserProfile.getPhone());
			}



			return ResponseEntity.status(HttpStatus.OK)
					.build();
		}


		return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
				.build();
	}






	@PutMapping ("/DeliverOrder/{OrderID}")
	@RolesAllowed({Constants.ROLE_SHOP_ADMIN, Constants.ROLE_SHOP_STAFF})
	public ResponseEntity<Object> deliverOrder(@PathVariable("OrderID")int orderID)
	{


		User userAuthenticated = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_SHOP_STAFF));

		if(userAuthenticated==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}




		if(userAuthenticated.getRole()== Constants.ROLE_SHOP_STAFF_CODE)
		{
			ShopStaffPermissions permissions = daoShopStaff.getShopStaffPermissions(userAuthenticated.getUserID());

			if(!permissions.isPermitSetOrdersPacked())
			{
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.build();
			}
		}


		int rowCount = daoOrderStaff.deliverOrder(orderID);


		if(rowCount >= 1)
		{

			return ResponseEntity.status(HttpStatus.OK)
					.build();
		}


		return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
				.build();
	}






	@PutMapping ("/ReturnOrder/{OrderID}")
	@RolesAllowed({Constants.ROLE_SHOP_ADMIN, Constants.ROLE_SHOP_STAFF})
	public ResponseEntity<Object> returnOrder(@PathVariable("OrderID")int orderID)
	{


		User userAuthenticated = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_SHOP_STAFF));

		if(userAuthenticated==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}


		if(userAuthenticated.getRole()== Constants.ROLE_SHOP_STAFF_CODE)
		{
			ShopStaffPermissions permissions = daoShopStaff.getShopStaffPermissions(userAuthenticated.getUserID());

			if(!permissions.isPermitSetOrdersPacked())
			{
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.build();
			}
		}



		int rowCount = daoOrderStaff.returnOrder(orderID);


		if(rowCount >= 1)
		{
			return ResponseEntity.status(HttpStatus.OK)
					.build();
		}


		return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
				.build();
	}






	@PutMapping ("/HandoverToDelivery/{DeliveryGuySelfID}")
	@RolesAllowed({Constants.ROLE_SHOP_ADMIN, Constants.ROLE_SHOP_STAFF})
	ResponseEntity<Object> handoverToDelivery(@PathVariable ("DeliveryGuySelfID")int deliveryGuyID,
									   @RequestBody List<Order> ordersList)
	{


		User userAuthenticated = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_SHOP_STAFF));

		if(userAuthenticated==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}


		int rowCount = 0;
//		int shopID = 0;


		if(userAuthenticated.getRole()== Constants.ROLE_SHOP_STAFF_CODE)
		{
			ShopStaffPermissions permissions = daoShopStaff.getShopStaffPermissions(userAuthenticated.getUserID());

			if(!permissions.isPermitHandoverToDelivery())
			{
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.build();
			}
		}




		for(Order orderReceived : ordersList)
		{
			rowCount = daoOrderStaff.handoverToDelivery(orderReceived.getOrderID(),deliveryGuyID) + rowCount;
		}





		if(rowCount==ordersList.size())
		{
			return ResponseEntity.status(HttpStatus.OK)
					.build();

		}
		else if (rowCount>0 && rowCount<ordersList.size())
		{
			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
					.build();

		}
		else
		{
			return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
					.build();
		}
	}







	@PutMapping ("/UndoHandover/{OrderID}")
	@RolesAllowed({Constants.ROLE_SHOP_ADMIN, Constants.ROLE_SHOP_STAFF})
	public ResponseEntity<Object> undoHandover(@PathVariable("OrderID")int orderID)
	{



		User userAuthenticated = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_SHOP_STAFF));

		if(userAuthenticated==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}


		if(userAuthenticated.getRole()== Constants.ROLE_SHOP_STAFF_CODE)
		{
			ShopStaffPermissions permissions =  daoShopStaff.getShopStaffPermissions(userAuthenticated.getUserID());

			if(!permissions.isPermitHandoverToDelivery())
			{
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.build();
			}
		}


		int rowCount = daoOrderStaff.undoHandover(orderID);



		if(rowCount >= 1)
		{

			return ResponseEntity.status(HttpStatus.OK)
					.build();
		}


		return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
				.build();
	}




	@PutMapping ("/AcceptReturn/{OrderID}")
	@RolesAllowed({Constants.ROLE_SHOP_ADMIN, Constants.ROLE_SHOP_STAFF})
	public ResponseEntity<Object> acceptReturn(@PathVariable("OrderID")int orderID)
	{


		User userAuthenticated = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_SHOP_STAFF));

		if(userAuthenticated==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}


		if(userAuthenticated.getRole()== Constants.ROLE_SHOP_STAFF_CODE)
		{
			ShopStaffPermissions permissions = daoShopStaff.getShopStaffPermissions(userAuthenticated.getUserID());

			if(!permissions.isPermitAcceptReturns())
			{
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.build();
			}

		}


		int rowCount = daoOrderStaff.acceptReturn(orderID);


		if(rowCount >= 1)
		{
			Order orderResult = orderService.getOrderDetailsForPush(orderID);



			String topic = appProperties.getMarket_id_for_fcm() + "end_user_" + orderResult.getEndUserID();

			String notificationTitle = "Order Returned";
			String notificationMessage = "Order number " + String.valueOf(orderID) + " has been Returned !";
			SendPush.sendFCMPushNotification(topic,notificationTitle,notificationMessage,Constants.NOTIFICATION_TYPE_ORDER_UPDATES);

			return ResponseEntity.status(HttpStatus.OK)
					.build();
		}



		return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
				.build();
	}






	@PutMapping ("/UnpackOrder/{OrderID}")
	@RolesAllowed({Constants.ROLE_SHOP_ADMIN, Constants.ROLE_SHOP_STAFF})
	public ResponseEntity<Object> unpackOrder(@PathVariable("OrderID")int orderID)
	{

		User userAuthenticated = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_SHOP_STAFF));

		if(userAuthenticated==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}



		if(userAuthenticated.getRole()== Constants.ROLE_SHOP_STAFF_CODE)
		{
			ShopStaffPermissions permissions = daoShopStaff.getShopStaffPermissions(userAuthenticated.getUserID());

			if(!permissions.isPermitAcceptReturns())
			{
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.build();
			}

		}


		int rowCount = daoOrderStaff.unpackOrder_delete(orderID);



		if(rowCount >= 1)
		{

			return ResponseEntity.status(HttpStatus.OK)
					.build();
		}


		return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
				.build();

//		order.setOrderID(orderID);
//		int rowCount = Globals.orderService.updateOrder(order);

	}






	@PutMapping ("/PaymentReceived/{OrderID}")
	@RolesAllowed({Constants.ROLE_SHOP_ADMIN, Constants.ROLE_SHOP_STAFF})
	public ResponseEntity<Object> paymentReceived(@PathVariable("OrderID")int orderID)
	{
//		Order order = Globals.orderService.readStatusHomeDelivery(orderID);


		User userAuthenticated = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_SHOP_STAFF));

		if(userAuthenticated==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}


		if(userAuthenticated.getRole()== Constants.ROLE_SHOP_STAFF_CODE)
		{
			ShopStaffPermissions permissions = daoShopStaff.getShopStaffPermissions(userAuthenticated.getUserID());

			if(!permissions.isPermitAcceptPaymentsFromDelivery())
			{
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.build();
			}

		}


		int rowCount = daoOrderStaff.paymentReceived(orderID);


		if(rowCount >= 1)
		{

			return ResponseEntity.status(HttpStatus.OK)
					.build();
		}


		return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
				.build();
	}









	@PutMapping ("/SetConfirmedPFS/{OrderID}")
	@RolesAllowed({Constants.ROLE_SHOP_ADMIN, Constants.ROLE_SHOP_STAFF})
	public ResponseEntity<Object> confirmOrderPFS(@PathVariable("OrderID")int orderID)
	{

//		Order order = Globals.orderService.readStatusHomeDelivery(orderID);


		User userAuthenticated = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_SHOP_STAFF));

		if(userAuthenticated==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}



		if(userAuthenticated.getRole()== Constants.ROLE_SHOP_STAFF_CODE)
		{
			ShopStaffPermissions permissions = daoShopStaff.getShopStaffPermissions(userAuthenticated.getUserID());

			if(!permissions.isPermitConfirmOrders())
			{
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.build();
			}

		}







		int rowCount = daoOrderStaff.confirmOrderPFS(orderID);

		if(rowCount >= 1)
		{
			Order orderResult = orderService.getOrderDetailsForPush(orderID);


			String topic = appProperties.getMarket_id_for_fcm() + "end_user_" + orderResult.getEndUserID();
			String notificationTitle = "Order Confirmed";
			String notificationMessage = "Order number " + String.valueOf(orderID) + " has been Confirmed!";
			SendPush.sendFCMPushNotification(topic,notificationTitle,notificationMessage,Constants.NOTIFICATION_TYPE_ORDER_UPDATES);


			User endUserProfile = orderResult.getRt_end_user_profile();


			if(endUserProfile.getEmail()!=null)
			{
				String htmlText = "<h2>Your Order with Order Number : " + orderID + " is confirmed by the seller.</h2>" +
						"<p>We will let you know when its packed and ready for pickup. <p>";


				MarketSettings marketSettings = marketSettingsDAO.getSettingsInstance();

				Email emailComposed = EmailBuilder.startingBlank()
						.from(marketSettings.getEmailSenderName(),appProperties.getEmail_address_for_sender())
						.to(orderResult.getRt_end_user_profile().getName(),orderResult.getRt_end_user_profile().getEmail())
						.withSubject("Order No. " + orderID + " Confirmed")
						.withHTMLText(htmlText)
						.buildEmail();

//				getMailerInstance().sendMail(emailComposed,true);
			}


			return ResponseEntity.status(HttpStatus.OK)
					.build();
		}


		return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
				.build();
	}




	@PutMapping ("/SetOrderPackedPFS/{OrderID}")
	@RolesAllowed({Constants.ROLE_SHOP_ADMIN, Constants.ROLE_SHOP_STAFF})
	public ResponseEntity<Object> setOrderPackedPFS(@PathVariable("OrderID")int orderID)
	{
//		Order order = Globals.orderService.readStatusHomeDelivery(orderID);


		User userAuthenticated = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_SHOP_STAFF));

		if(userAuthenticated==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}



		if(userAuthenticated.getRole()== Constants.ROLE_SHOP_STAFF_CODE)
		{
			ShopStaffPermissions permissions = daoShopStaff.getShopStaffPermissions(userAuthenticated.getUserID());

			if(!permissions.isPermitSetOrdersPacked())
			{
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.build();
			}
		}



//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}


//			order.setStatusHomeDelivery(OrderStatusHomeDelivery.ORDER_PACKED);

		int rowCount = daoOrderStaff.setOrderPackedPFS(orderID);


		if(rowCount >= 1)
		{
			Order orderResult = orderService.getOrderDetailsForPush(orderID);


			String topic = appProperties.getMarket_id_for_fcm() + "end_user_" + orderResult.getEndUserID();
			String notificationTitle = "Order Packed";
			String notificationMessage = "Order number " + String.valueOf(orderID) + " has been Packed !";
			SendPush.sendFCMPushNotification(topic,notificationTitle,notificationMessage,Constants.NOTIFICATION_TYPE_ORDER_UPDATES);


			User endUserProfile = orderResult.getRt_end_user_profile();


			if(endUserProfile.getEmail()!=null)
			{
				String htmlText = "<h2>Your Order with Order Number : " + orderID + " is packed by the seller.</h2>" +
						"<p>We will let you know when its ready for pickup. <p>";


				MarketSettings marketSettings = marketSettingsDAO.getSettingsInstance();


				Email emailComposed = EmailBuilder.startingBlank()
						.from(marketSettings.getEmailSenderName(),appProperties.getEmail_address_for_sender())
						.to(orderResult.getRt_end_user_profile().getName(),orderResult.getRt_end_user_profile().getEmail())
						.withSubject("Order No. " + orderID + " Packed")
						.withHTMLText(htmlText)
						.buildEmail();




//				getMailerInstance().sendMail(emailComposed,true);
			}



			return ResponseEntity.status(HttpStatus.OK)
					.build();
		}

		return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
				.build();

	}




	@PutMapping ("/SetOrderReadyForPickupPFS/{OrderID}")
	@RolesAllowed({Constants.ROLE_SHOP_ADMIN, Constants.ROLE_SHOP_STAFF})
	public ResponseEntity<Object> setOrderReadyForPickupPFS(@PathVariable ("OrderID")int orderID)
	{
//		Order order = Globals.orderService.readStatusHomeDelivery(orderID);


		User userAuthenticated = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_SHOP_STAFF));

		if(userAuthenticated==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}




		if(userAuthenticated.getRole()== Constants.ROLE_SHOP_STAFF_CODE)
		{
			ShopStaffPermissions permissions = daoShopStaff.getShopStaffPermissions(userAuthenticated.getUserID());

			if(!permissions.isPermitSetOrdersPacked())
			{
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.build();
			}
		}



//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}


//			order.setStatusHomeDelivery(OrderStatusHomeDelivery.ORDER_PACKED);

		int rowCount = daoOrderStaff.setOrderReadyForPickupPFS(orderID);


		if(rowCount >= 1)
		{
			Order orderResult = orderService.getOrderDetailsForPush(orderID);


			String topic = appProperties.getMarket_id_for_fcm() + "end_user_" + orderResult.getEndUserID();
			String notificationTitle = "Order Ready for Pickup";
			String notificationMessage = "Order number " + String.valueOf(orderID) + " is Ready for Pickup !";
			SendPush.sendFCMPushNotification(topic,notificationTitle,notificationMessage,Constants.NOTIFICATION_TYPE_ORDER_UPDATES);



			User endUserProfile = orderResult.getRt_end_user_profile();


			if(endUserProfile.getEmail()!=null)
			{
				String htmlText = "<h2>Your Order with Order Number : " + orderID + " is Ready for Pickup.</h2>" +
						"<p>You can collect the items from the shop premises. The address for shop is provided in the shop details.  " +
						" We advice you to call the shop and confirm timing and availability for pickup before you reach the shop.<p>";



				MarketSettings marketSettings = marketSettingsDAO.getSettingsInstance();



				Email emailComposed = EmailBuilder.startingBlank()
						.from(marketSettings.getEmailSenderName(),appProperties.getEmail_address_for_sender())
						.to(orderResult.getRt_end_user_profile().getName(),orderResult.getRt_end_user_profile().getEmail())
						.withSubject("Order No. " + orderID + " is Ready for Pickup")
						.withHTMLText(htmlText)
						.buildEmail();

				sendEmail.getMailerInstance().sendMail(emailComposed,true);
			}



			if(endUserProfile.getPhone()!=null)
			{
				sendSMS.sendSMS("Order No. " + orderID + " is Ready for Pickup. You can collect the order from the Shop !",
						endUserProfile.getPhone());
			}



			return ResponseEntity.status(HttpStatus.OK)
					.build();
		}


		return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
				.build();

	}





	@PutMapping ("/PaymentReceivedPFS/{OrderID}")
	@RolesAllowed({Constants.ROLE_SHOP_ADMIN, Constants.ROLE_SHOP_STAFF})
	public ResponseEntity<Object> paymentReceivedPFS(@PathVariable("OrderID")int orderID)
	{
//		Order order = Globals.orderService.readStatusHomeDelivery(orderID);


		User userAuthenticated = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_SHOP_STAFF));

		if(userAuthenticated==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}



		if(userAuthenticated.getRole()== Constants.ROLE_SHOP_STAFF_CODE)
		{
			ShopStaffPermissions permissions = daoShopStaff.getShopStaffPermissions(userAuthenticated.getUserID());

			if(!permissions.isPermitAcceptPaymentsFromDelivery())
			{
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.build();
			}

		}




		int rowCount = daoOrderStaff.paymentReceivedPFS(orderID);


		if(rowCount >= 1)
		{
			Order orderResult = orderService.getOrderDetailsForPush(orderID);


			String topic = appProperties.getMarket_id_for_fcm() + "end_user_" + orderResult.getEndUserID();
			String notificationTitle = "Order Delivered";
			String notificationMessage = "Order number " + String.valueOf(orderID) + " has been delivered and amount paid !";

			SendPush.sendFCMPushNotification(topic,notificationTitle,notificationMessage,Constants.NOTIFICATION_TYPE_ORDER_UPDATES);



			User endUserProfile = orderResult.getRt_end_user_profile();


			if(endUserProfile.getEmail()!=null)
			{
				String htmlText = "<h2>Your Order with Order Number : " + orderID + " is Delivered.</h2>" +
						"<p>How was your experience ? Having any issues ... please let us know !" +
						" For support and feedback please call market helpline provided in the market details section !";



				MarketSettings marketSettings = marketSettingsDAO.getSettingsInstance();


				Email emailComposed = EmailBuilder.startingBlank()
						.from(marketSettings.getEmailSenderName(),appProperties.getEmail_address_for_sender())
						.to(orderResult.getRt_end_user_profile().getName(),orderResult.getRt_end_user_profile().getEmail())
						.withSubject("Order No. " + orderID + " is Delivered")
						.withHTMLText(htmlText)
						.buildEmail();


				sendEmail.getMailerInstance().sendMail(emailComposed,true);
			}





			if(endUserProfile.getPhone()!=null)
			{
				sendSMS.sendSMS("Order No. " + orderID + " is Delivered to you ! If you have any issues with the order feel free to contact market administrator !",
						endUserProfile.getPhone());
			}


			return ResponseEntity.status(HttpStatus.OK)
					.build();
		}




		return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
				.build();

//		order.setOrderID(orderID);
//		int rowCount = Globals.orderService.updateOrder(order);

	}




	// Permissions : General
	// Submit Item Categories
	// Submit Items
	// Add / Remove Items From Shop
	// Update Stock

	// Permissions : Home Delivery Inventory
	// 0. Cancel OrderPFS's
	// 1. Confirm OrderPFS's
	// 2. Set OrderPFS's Packed
	// 3. Handover to Delivery
	// 4. Mark OrderPFS Delivered
	// 5. Payment Received | Collect Payments from Delivery Guy
	// 6. Accept Return's | Cancelled By Shop

	// 7. Accept Return | Returned by Delivery Guy | Not required

}
