package org.nearbyshops.RESTEndpoints.RESTEndpointsCart;

import org.nearbyshops.DAOs.DAOCartOrder.CartStatsDAO;
import org.nearbyshops.DAOs.DAOSettings.MarketSettingsDAO;
import org.nearbyshops.DAOs.ShopDAO;
import org.nearbyshops.Model.ModelSettings.MarketSettings;
import org.nearbyshops.Model.ModelStats.CartStats;
import org.nearbyshops.Model.ModelUtility.DeliveryConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/CartStats")
public class CartStatsResource {

	@Autowired
	private ShopDAO shopDAO;

	@Autowired
	private CartStatsDAO cartStatsDAO;

	@Autowired
	MarketSettingsDAO marketSettingsDAO;



	@GetMapping("/{EndUserID}")
	public ResponseEntity<Object> getCartStatList(@PathVariable("EndUserID")int endUserID,
										  @RequestParam(value = "CartID",required = false) Integer cartID,
										  @RequestParam(value = "ShopID",required = false) Integer shopID,
										  @RequestParam(value = "GetShopDetails",required = false) Boolean getShopDetails,
										  @RequestParam(value = "latCenter",defaultValue = "0")double latCenter,
												  @RequestParam(value = "lonCenter",defaultValue = "0")double lonCenter)
	{

		List<CartStats> cartStats = cartStatsDAO.getCartStats(endUserID,cartID, shopID);


		if(getShopDetails!=null && getShopDetails)
		{
			for(CartStats cartStatsItem: cartStats)
			{
				cartStatsItem.setShop(shopDAO.getShopDetailsForCartsList(cartStatsItem.getShopID(),latCenter,lonCenter));
			}

		}



//		GenericEntity<List<CartStats>> listEntity = new GenericEntity<List<CartStats>>(cartStats){
//		};



//
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}


		if(cartStats.size()<=0)
		{

			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.build();
			
		}else
		{

			return ResponseEntity.status(HttpStatus.OK)
					.body(cartStats);
		}

	}





//	@RequestParam("CartID") Integer cartID,

	@GetMapping("/{EndUserID}/{ShopID}")
	public ResponseEntity<Object> getCartStats(@PathVariable("EndUserID")int endUserID,
									   @PathVariable("ShopID") int shopID,
								 @RequestParam(value = "GetShopDetails",defaultValue = "false") boolean getShopDetails,
								 @RequestParam(value = "GetDeliveryConfig",defaultValue = "false") boolean getDeliveryConfig)
	{

		List<CartStats> cartStatsList = cartStatsDAO.getCartStats(endUserID,null, shopID);

		CartStats cartStats = null;

		if(cartStatsList.size()==1)
		{
			cartStats = cartStatsList.get(0);
		}


		if(getShopDetails && cartStats!=null)
		{
			cartStats.setShop(shopDAO.getShopDetailsForPlaceOrderPage(shopID,0.0,0.0));
		}



		if(cartStats!=null && getDeliveryConfig)
		{
			MarketSettings marketSettings = marketSettingsDAO.getSettingsInstance();



			DeliveryConfig deliveryConfig = new DeliveryConfig();

			deliveryConfig.setUseStandardDeliveryCharge(marketSettings.isUseStandardDeliveryFee());

			deliveryConfig.setMarketDeliveryCharge(marketSettings.getMarketDeliveryFeePerOrder());
			deliveryConfig.setMarketFeeAddedToBill(marketSettings.isAddMarketFeeToBill());

//			if(deliveryConfig.isMarketFeeAddedToBill())
//			{
//				deliveryConfig.setMarketFeeForPickup(Constants.market_fee_pick_for_shop_value);
//				deliveryConfig.setMarketFeeForDelivery(Constants.market_fee_home_delivery_value);
//			}

			deliveryConfig.setMarketFeeForPickup(marketSettings.getMarketFeePickupFromShop());
			deliveryConfig.setMarketFeeForDelivery(marketSettings.getMarketFeeHomeDelivery());


			cartStats.setDeliveryConfig(deliveryConfig);
		}





		if(cartStats!=null)
		{
			return ResponseEntity.status(HttpStatus.OK)
					.body(cartStats);

		}
		else
		{
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.build();
		}
	}



}
