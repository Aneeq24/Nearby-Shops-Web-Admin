package org.nearbyshops.RESTEndpoints.RESTEndpointsCart;

import org.nearbyshops.DAOs.DAOCartOrder.CartItemService;
import org.nearbyshops.DAOs.DAOCartOrder.CartService;
import org.nearbyshops.Model.Cart;
import org.nearbyshops.Model.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;




@RestController
@RequestMapping("/api/CartItem")
public class CartItemResource {



	@Autowired
	CartItemService cartItemService;

	@Autowired
	CartService cartService;



	@PostMapping
	public ResponseEntity<Object> createCartItem(@RequestBody CartItem cartItem,
												 @RequestParam(value = "EndUserID",defaultValue = "0") int endUserID,
												 @RequestParam(value = "ShopID",defaultValue = "0") int shopID)
	{


//		System.out.println("End User DELIVERY_GUY_SELF_ID : " + endUserID + " ShopID : " + shopID);

		if(endUserID>0 && shopID>0)
		{

			// Check if the Cart exists if not then create one
			int cartID = cartService.getCartID(endUserID,shopID);

			if(cartID==0)
			{
				// cart does not exist so create one

				Cart cart = new Cart();

				cart.setEndUserID(endUserID);
				cart.setShopID(shopID);
				int idOfInsertedCart = cartService.saveCart(cart);

				cartItem.setCartID(idOfInsertedCart);
			}
			else if(cartID>0)
			{
				// cart exists

				cartItem.setCartID(cartID);
			}

		}

		int rowCount = cartItemService.saveCartItem(cartItem);



//
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}


		if(rowCount == 1)
		{


			return ResponseEntity.status(HttpStatus.CREATED)
					.build();
			
		}
		else
		{

			return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
					.build();
		}
	}




	@PutMapping
	public ResponseEntity<Object> updateCartItem(
									@RequestBody CartItem cartItem,
                                   @RequestParam(value = "EndUserID",defaultValue = "0") int endUserID,
                                   @RequestParam(value = "ShopID",defaultValue = "0") int shopID)
	{





		if(endUserID>0 && shopID>0)
		{

			// Check if the Cart exists if not then create one
			int cartID = cartService.getCartID(endUserID,shopID);

			if(cartID==0)
			{
				// cart does not exist so create one

				Cart cart = new Cart();

				cart.setEndUserID(endUserID);
				cart.setShopID(shopID);
				int idOfInsertedCart = cartService.saveCart(cart);

				cartItem.setCartID(idOfInsertedCart);
			}
			else if(cartID>0)
			{
				// cart exists

				cartItem.setCartID(cartID);
			}

		}


		int rowCount = cartItemService.updateCartItem(cartItem);



//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}



		if(rowCount >= 1)
		{

//			System.out.println("Update Cart Success !");

			return ResponseEntity.status(HttpStatus.OK)
					.build();
		}
		else
		{

			return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
					.build();
		}

	}






	@DeleteMapping
	public ResponseEntity<Object> deleteCartItem(@RequestParam(value = "CartID",defaultValue = "0")int cartID,
										 @RequestParam(value = "ItemID",required = false) Integer itemID,
                                   @RequestParam(value = "EndUserID",defaultValue = "0") int endUserID,
                                   @RequestParam(value = "ShopID",defaultValue = "0") int shopID)
	{

		if(endUserID>0 && shopID>0)
		{
//			// Check if the Cart exists if not then create one
			cartID = cartService.getCartID(endUserID,shopID);
		}




		int rowCount = cartItemService.deleteCartItem(itemID,cartID);




//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}


		if(rowCount>=1)
		{

			// if the cart item is the last item then delete the cart also.

			if(!cartItemService.checkItemsInCart(cartID))
			{
				cartService.deleteCart(cartID);
			}


			return ResponseEntity.status(HttpStatus.OK)
					.build();
		}
		else
		{

			return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
					.build();
		}
	}






	@GetMapping
	public ResponseEntity<Object> getCartItem(
								@RequestParam(value = "EndUserID",defaultValue = "0") int endUserID,
                                @RequestParam(value = "ShopID",defaultValue = "0") int shopID,
                                @RequestParam(value = "SortBy",required = false) String sortBy,
                                @RequestParam(value = "Limit",required = false)Integer limit,
								@RequestParam(value = "Offset",defaultValue = "0") int offset)
	{


		List<CartItem> cartList;

		cartList = cartItemService.getCartItem(endUserID, shopID, sortBy,limit,offset);



//		GenericEntity<List<CartItem>> listEntity = new GenericEntity<List<CartItem>>(cartList){
//		};
	
		
		if(cartList.size()<=0)
		{

			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.build();
			
		}else
		{

			return ResponseEntity.status(HttpStatus.OK)
					.body(cartList);
		}


	}




	@GetMapping ("/GetAvailabilityForShopItem/{ShopID}/{EndUserID}/{ItemID}")
	public ResponseEntity<Object> getCartItemForShopItem(@PathVariable("EndUserID") int endUserID,
											@PathVariable("ShopID") int shopID,
										   @PathVariable("ItemID") int itemID)
	{


		CartItem cartItem;

		cartItem = cartItemService.getCartItemAvailability(endUserID, shopID,itemID);



		if(cartItem==null)
		{
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.build();

		}
		else
		{

			return ResponseEntity.status(HttpStatus.OK)
					.body(cartItem);
		}

	}




	@GetMapping ("/GetAvailability/{ShopID}/{EndUserID}")
	public ResponseEntity<Object> getCartItemAvailability(@PathVariable("EndUserID") int endUserID,
											@PathVariable("ShopID") int shopID)
	{


		List<CartItem> cartList;

		cartList = cartItemService
					.getCartItemAvailability(endUserID, shopID);


//		GenericEntity<List<CartItem>> listEntity = new GenericEntity<List<CartItem>>(cartList){
//		};


		if(cartList.size()<=0)
		{
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.build();

		}
		else
		{

			return ResponseEntity.status(HttpStatus.OK)
					.body(cartList);
		}

	}



	@GetMapping ("/GetAvailabilityByItem/{ItemID}/{EndUserID}")
	public ResponseEntity<Object> getCartItemAvailabilityByItem(@PathVariable("EndUserID") int endUserID,
														@PathVariable("ItemID") int itemID)
	{


		List<CartItem> cartList;

		cartList = cartItemService.getCartItemAvailabilityByItem(itemID,endUserID);

//		GenericEntity<List<CartItem>> listEntity = new GenericEntity<List<CartItem>>(cartList){
//		};


		if(cartList.size()<=0)
		{
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.build();

		}
		else
		{

			return ResponseEntity.status(HttpStatus.OK)
					.body(cartList);
		}

	}



}
