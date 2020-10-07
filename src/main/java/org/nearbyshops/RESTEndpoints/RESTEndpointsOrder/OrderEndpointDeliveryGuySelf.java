package org.nearbyshops.RESTEndpoints.RESTEndpointsOrder;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.nearbyshops.AppProperties;
import org.nearbyshops.Constants;
import org.nearbyshops.DAOs.DAOOrders.DAOOrderDeliveryGuy;
import org.nearbyshops.DAOs.DAOOrders.DAOOrderUtility;
import org.nearbyshops.DAOs.DAOOrders.OrderService;
import org.nearbyshops.DAOs.DAOSettings.MarketSettingsDAO;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;


@RestController
@RequestMapping("/api/Order/DeliveryGuySelf")
public class OrderEndpointDeliveryGuySelf {



	@Autowired
	private HttpServletRequest request;

	@Autowired
	private UserAuthentication userAuthentication;

	@Autowired
	DAOOrderDeliveryGuy daoOrderDeliveryGuy;

	@Autowired
	DAOOrderUtility daoOrderUtility;

	@Autowired
	OrderService orderService;


	@Autowired
	AppProperties appProperties;


	@Autowired
	SendEmail sendEmail;

	@Autowired
	SendSMS sendSMS;


	@Autowired
	MarketSettingsDAO marketSettingsDAO;





	@PutMapping ("/StartPickup/{OrderID}")
	@RolesAllowed({Constants.ROLE_DELIVERY_GUY_SELF, Constants.ROLE_DELIVERY_GUY})
	public ResponseEntity<Object> startPickup(@PathVariable("OrderID")int orderID)
	{


		User userAuthenticated = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_DELIVERY_GUY_SELF,Constants.ROLE_DELIVERY_GUY));

		if(userAuthenticated==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}


		int rowCount = 0;


		int deliveryGuyID = userAuthenticated.getUserID();


		rowCount = daoOrderDeliveryGuy.pickupOrder(orderID,deliveryGuyID);


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




	@PutMapping ("/AcceptOrder/{OrderID}")
	@RolesAllowed({Constants.ROLE_DELIVERY_GUY_SELF, Constants.ROLE_DELIVERY_GUY})
	public ResponseEntity<Object> acceptOrder(@PathVariable("OrderID")int orderID)
	{


		User userAuthenticated = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_DELIVERY_GUY_SELF,Constants.ROLE_DELIVERY_GUY));

		if(userAuthenticated==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}



		int deliveryGuyID = userAuthenticated.getUserID();
		int rowCount = daoOrderDeliveryGuy.acceptOrder(orderID,deliveryGuyID);



		if(rowCount >= 1)
		{


			Order orderResult = orderService.getOrderDetailsForPush(orderID);


			// See documentation on defining a message payload.
			Message messageEndUser = Message.builder()
					.setNotification(new Notification("Out For Delivery", "Order number " + String.valueOf(orderID) + " is out for Delivery !"))
					.setTopic("end_user_" + orderResult.getEndUserID())
					.build();


			System.out.println("Topic : " + "end_user_" + orderResult.getEndUserID());


			try {


				String responseEndUser = FirebaseMessaging.getInstance().send(messageEndUser);
				System.out.println("Sent Notification to EndUser: " + responseEndUser);


			} catch (FirebaseMessagingException e) {
				e.printStackTrace();
			}


			return ResponseEntity.status(HttpStatus.OK)
					.build();
		}


		return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
				.build();
	}





	@PutMapping("/DeclineOrder/{OrderID}")
	@RolesAllowed({Constants.ROLE_DELIVERY_GUY_SELF, Constants.ROLE_DELIVERY_GUY})
	public ResponseEntity<Object> declineOrder(@PathVariable("OrderID")int orderID)
	{


		User userAuthenticated = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_DELIVERY_GUY_SELF,Constants.ROLE_DELIVERY_GUY));

		if(userAuthenticated==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}



		int deliveryGuyID = userAuthenticated.getUserID();
		int rowCount = daoOrderDeliveryGuy.declineOrder(orderID,deliveryGuyID);

		if(rowCount >= 1)
		{
			return ResponseEntity.status(HttpStatus.OK)
					.build();
		}


		return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
				.build();
	}





	@PutMapping ("/ReturnPackage/{OrderID}")
	@RolesAllowed({Constants.ROLE_DELIVERY_GUY_SELF, Constants.ROLE_DELIVERY_GUY})
	public ResponseEntity<Object> returnOrderPackage(@PathVariable("OrderID")int orderID)
	{


		User userAuthenticated = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_DELIVERY_GUY_SELF,Constants.ROLE_DELIVERY_GUY));

		if(userAuthenticated==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}


		int rowCount = daoOrderDeliveryGuy.returnOrder(orderID);


		if(rowCount >= 1)
		{
			return ResponseEntity.status(HttpStatus.OK)
					.build();
		}



		return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
				.build();


	}





	@PutMapping("/HandoverToUser/{OrderID}/{DeliveryOTP}")
	@RolesAllowed({Constants.ROLE_DELIVERY_GUY_SELF, Constants.ROLE_DELIVERY_GUY})
	public ResponseEntity<Object> deliverOrder(@PathVariable("OrderID")int orderID,
											   @PathVariable("DeliveryOTP")int deliveryOTP)
	{


		User userAuthenticated = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_DELIVERY_GUY_SELF,Constants.ROLE_DELIVERY_GUY));

		if(userAuthenticated==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}



		int paymentMode = daoOrderUtility.getOrderPaymentMode(orderID);
		int rowCount = daoOrderDeliveryGuy.deliverOrder(orderID,deliveryOTP,userAuthenticated.getRole(),paymentMode);

//
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}

		if (rowCount >= 1) {


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


				MarketSettings settings = marketSettingsDAO.getSettingsInstance();

				Email emailComposed = EmailBuilder.startingBlank()
						.from(settings.getEmailSenderName(),appProperties.getEmail_address_for_sender())
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




}
