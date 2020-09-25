package org.nearbyshops.RESTEndpoints.RESTEndpointsCart;


import org.nearbyshops.DAOs.DAOCartOrder.DeliveryAddressService;
import org.nearbyshops.Model.ModelDelivery.DeliveryAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/DeliveryAddress")
public class DeliveryAddressResource {


	@Autowired
	DeliveryAddressService deliveryAddressService;


	@PostMapping
	public ResponseEntity<Object> createAddress(@RequestBody DeliveryAddress address)
	{

		int idOfInsertedRow = deliveryAddressService.saveAddress(address);

		address.setId(idOfInsertedRow);


		if(idOfInsertedRow >=1)
		{


			return ResponseEntity.status(HttpStatus.CREATED)
					.body(address);
			
		}
		else
			{


			return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
					.build();
		}

	}





	
	@PutMapping ("/{DeliveryAddressID}")
	public ResponseEntity<Object> updateCart(@PathVariable("DeliveryAddressID")int addressID,
									 @RequestBody DeliveryAddress deliveryAddress)
	{

		deliveryAddress.setId(addressID);

		int rowCount = deliveryAddressService.updateAddress(deliveryAddress);


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





	@DeleteMapping ("/{id}")
	public ResponseEntity<Object> deleteCart(@PathVariable("id")int addressID)
	{

		int rowCount = deliveryAddressService.deleteAddress(addressID);
		
		
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



	
	@GetMapping
	public ResponseEntity<Object> getDeliveryAddresses(@RequestParam("EndUserID")int endUserID)
	{


		List<DeliveryAddress> addressesList = deliveryAddressService.readAddresses(endUserID);

//		GenericEntity<List<DeliveryAddress>> listEntity = new GenericEntity<List<DeliveryAddress>>(addressesList){
//
//		};

		
		if(addressesList.size()<=0)
		{
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.build();
		}
		else
		{
			return ResponseEntity.status(HttpStatus.OK)
					.body(addressesList);
		}
		
	}



	
	@GetMapping ("/{DeliveryAddressID}")
	public ResponseEntity<Object> getDeliveryAddress(@PathVariable("DeliveryAddressID")int addressID)
	{

		DeliveryAddress deliveryAddress = deliveryAddressService.readAddress(addressID);
		
		if(deliveryAddress != null)
		{

			return ResponseEntity.status(HttpStatus.OK)
					.body(deliveryAddress);
			
		}
		else
		{
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.build();
		}
		
	}


}
