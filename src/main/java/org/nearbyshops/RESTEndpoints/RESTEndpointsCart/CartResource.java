package org.nearbyshops.RESTEndpoints.RESTEndpointsCart;

import org.nearbyshops.DAOs.DAOCartOrder.CartService;
import org.nearbyshops.Model.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/Cart")
public class CartResource {



	@Autowired
	CartService cartService;


	@PostMapping
	public ResponseEntity<Object> createCart(@RequestBody Cart cart)
	{

		int idOfInsertedRow = cartService.saveCart(cart);

		cart.setCartID(idOfInsertedRow);

		if(idOfInsertedRow >=1)
		{


			return ResponseEntity.status(HttpStatus.CREATED)
					.body(cart);

			
		}
		else
			{

			return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
					.build();
		}

	}



	@PutMapping ("/{CartID}")
	public ResponseEntity<Object> updateCart(@PathVariable("CartID")int cartID,
							   @RequestBody Cart cart)
	{

		cart.setCartID(cartID);

		int rowCount = cartService.updateCart(cart);


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




	@DeleteMapping ("/{CartID}")
	public ResponseEntity<Object> deleteCart(@PathVariable("CartID")int cartID)
	{

		int rowCount = cartService.deleteCart(cartID);
		


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



//
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response getCarts(@QueryParam("EndUserID")int endUserID,
//                             @QueryParam("ShopID")int shopID)
//
//	{
//
//		List<Cart> cartList = Globals.cartService.readCarts(endUserID,shopID);
//
//		GenericEntity<List<Cart>> listEntity = new GenericEntity<List<Cart>>(cartList){
//		};
//
//
//		if(cartList.size()<=0)
//		{
//
//			return Response.status(Status.NO_CONTENT)
//					.entity(listEntity)
//					.build();
//
//		}else
//		{
//
//			return Response.status(Status.OK)
//					.entity(listEntity)
//					.build();
//		}
//
//	}



	
	@GetMapping ("/{CartID}")
	public ResponseEntity<Object> getCart(@PathVariable("CartID")int cartID)
	{

		Cart cart = cartService.readCart(cartID);
		
		if(cart != null)
		{

			return ResponseEntity.status(HttpStatus.OK)
					.body(cart);
			
		}
		else
		{
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.build();
		}
	}


}
