package org.nearbyshops.RESTEndpoints.RESTEndpointsOrder;


import org.nearbyshops.AppProperties;
import org.nearbyshops.Constants;
import org.nearbyshops.DAOs.DAOOrders.DAOOrderUtility;
import org.nearbyshops.DAOs.DAOOrders.OrderItemService;
import org.nearbyshops.DAOs.ShopDAO;
import org.nearbyshops.Model.ModelEndpoint.OrderItemEndPoint;
import org.nearbyshops.Model.ModelRoles.User;
import org.nearbyshops.Model.Order;
import org.nearbyshops.Utility.UserAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;


@RestController
@RequestMapping ("/api/OrderItem")
public class OrderItemResource {



	@Autowired
	AppProperties appProperties;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private UserAuthentication userAuthentication;

	@Autowired
	OrderItemService orderItemService;

	@Autowired
	DAOOrderUtility daoOrderUtility;

	@Autowired
	ShopDAO shopDAO;




	@GetMapping
	@RolesAllowed({Constants.ROLE_SHOP_ADMIN, Constants.ROLE_SHOP_STAFF, Constants.ROLE_END_USER})
	public ResponseEntity<Object> getOrderItem(@RequestParam(value = "OrderID",required = false)Integer orderID,
											   @RequestParam(value = "ItemID",required = false)Integer itemID,
											   @RequestParam(value = "latCenter",defaultValue = "0")double latCenter,
											   @RequestParam(value = "lonCenter",defaultValue = "0")double lonCenter,
											   @RequestParam(value = "SearchString",required = false)String searchString,
											   @RequestParam(value = "SortBy",required = false) String sortBy,
											   @RequestParam(value = "Limit",required = false)Integer limit,
											   @RequestParam(value = "Offset",defaultValue = "0")int offset)
	{

			//	@QueryParam("ShopID")Integer shopID,


		User userAuthenticated = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_END_USER));

		if(userAuthenticated==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}




		if(limit!=null && limit >= appProperties.getMax_limit())
		{
			limit = appProperties.getMax_limit();
		}



		 OrderItemEndPoint endPoint = orderItemService.getOrderItem(
													 orderID,itemID,
													 searchString,sortBy,limit,offset
											 );

		Order orderDetails = daoOrderUtility.getOrderDetails(orderID);

		// delivery otp should be given to only the user who has placed the order
		if(userAuthenticated.getUserID()!=orderDetails.getEndUserID())
		{
			orderDetails.setDeliveryOTP(0);
		}

		endPoint.setOrderDetails(orderDetails);

		endPoint.setShopDetails(shopDAO.getShopDetailsForOrder(orderDetails.getShopID(),latCenter,lonCenter));


		if(limit!=null)
		{
			endPoint.setLimit(limit);
			endPoint.setOffset(offset);
			endPoint.setMax_limit(appProperties.getMax_limit());
		}



		//Marker
		return ResponseEntity.status(HttpStatus.OK)
				.body(endPoint);
	}


}
