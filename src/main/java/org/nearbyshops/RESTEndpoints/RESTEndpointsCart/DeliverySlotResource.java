//package org.nearbyshops.RESTEndpoints.RESTEndpointsCart;
//
//import org.nearbyshops.AppProperties;
//import org.nearbyshops.DAOs.DAOCartOrder.DAODeliverySlot;
//import org.nearbyshops.Model.ModelEndpoint.DeliverySlotEndpoint;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.sql.Date;
//
//
//@RestController
//@RequestMapping("/api/DeliverySlot")
//public class DeliverySlotResource {
//
//
//	@Autowired
//	AppProperties appProperties;
//
//	@Autowired
//	DAODeliverySlot daoDeliverySlot;
//
//
//	@PostMapping
//	public ResponseEntity<Object> createSlot(@RequestBody DeliverySlot address)
//	{
//
//		int idOfInsertedRow = daoDeliverySlot.saveDeliverySlot(address,false);
//		address.setSlotID(idOfInsertedRow);
//
//
//		if(idOfInsertedRow >=1)
//		{
//			return ResponseEntity.status(HttpStatus.CREATED)
//					.body(address);
//		}
//		else {
//
//			return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
//					.build();
//		}
//
//	}
//
//
//
//	@PutMapping ("/{DeliverySlotID}")
//	public ResponseEntity<Object> updateSlot(@PathVariable("DeliverySlotID")int slotID,
//											 @RequestBody DeliverySlot slot)
//	{
//
//		slot.setSlotID(slotID);
//
//		int rowCount = daoDeliverySlot.updateDeliverySlot(slot);
//
//
//		if(rowCount >= 1)
//		{
//
//			return ResponseEntity.status(HttpStatus.OK)
//					.build();
//		}
//		else
//		{
//
//			return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
//					.build();
//		}
//
//	}
//
//
//
//
//	@PutMapping("/EnableSlot/{DeliverySlotID}/{Enabled}")
//	public ResponseEntity<Object> enableSlot(
//			@PathVariable("DeliverySlotID")int slotID,
//			@PathVariable("Enabled") boolean isEnabled)
//	{
//
//		int rowCount = daoDeliverySlot.enableSlot(slotID,isEnabled);
//
//		if(rowCount >= 1)
//		{
//			return ResponseEntity.status(HttpStatus.OK)
//					.build();
//		}
//		else
//		{
//
//			return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
//					.build();
//		}
//	}
//
//
//
//
//
//
//	@DeleteMapping ("/{SlotID}")
//	public ResponseEntity<Object> deleteSlot(@PathVariable("SlotID")int slotID)
//	{
//
//		int rowCount = daoDeliverySlot.deleteItem(slotID);
//
//
//		if(rowCount>=1)
//		{
//
//			return ResponseEntity.status(HttpStatus.OK)
//					.build();
//		}
//		else
//		{
//			return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
//					.build();
//		}
//	}
//
//
//
//
//
//
//
//	@GetMapping
//	public ResponseEntity<Object> getItems(
//			@RequestParam("ShopID")Integer shopID,
//			@RequestParam("IsShopNull")boolean isShopNull,
//			@RequestParam("SearchString") String searchString,
//			@RequestParam("SortBy") String sortBy,
//			@RequestParam("Limit")Integer limit, @RequestParam("Offset")int offset,
//			@RequestParam("GetRowCount")boolean getRowCount,
//			@RequestParam("MetadataOnly")boolean getOnlyMetaData)
//	{
//
//
//
//
//		try
//		{
//			if (limit!=null && limit >= appProperties.getMax_limit()) {
//
//				limit = appProperties.getMax_limit();
//			}
//
//
//
//
//			DeliverySlotEndpoint endPoint = daoDeliverySlot.getDeliverySlots(
//					shopID,
//					isShopNull,
//					searchString,
//					sortBy,
//					limit,offset,
//					getRowCount,getOnlyMetaData
//			);
//
//
//
////			System.out.println("Shop ID :  " + shopID);
////			System.out.println("Row Count Delivery Slot : " + endPoint.getResults().size());
//
//
//
//
//			endPoint.setLimit(limit);
//			endPoint.setMax_limit(appProperties.getMax_limit());
//			endPoint.setOffset(offset);
//
//
//			//Marker
//			return ResponseEntity.status(HttpStatus.OK)
//					.body(endPoint);
//
//		}
//		catch (Exception exception)
//		{
//			exception.printStackTrace();
//		}
//
//
//
//
//		//Marker
//		return ResponseEntity.status(HttpStatus.NO_CONTENT)
//				.build();
//
//
//
////		try {
////			Thread.sleep(1000);
////		} catch (InterruptedException e) {
////			e.printStackTrace();
////		}
//
//
//	}
//
//
//
//
//
//
//	@GetMapping ("/GetAvailableSlots")
//	ResponseEntity<Object> getAvailableSlots(
//			@RequestParam("ShopID")Integer shopID,
//			@RequestParam("DeliveryDate") Date deliveryDate,
//			@RequestParam("IsPickupSlot") boolean isPickupSlot,
//			@RequestParam("IsDeliverySlot") boolean isDeliverySlot,
//			@RequestParam("SortBy") String sortBy,
//			@RequestParam("GetRowCount")boolean getRowCount,
//			@RequestParam("MetadataOnly")boolean getOnlyMetaData)
//	{
//
//
//
//		DeliverySlotEndpoint endPoint = daoDeliverySlot.getAvailableSlotsBackup(
//				shopID,
//				isPickupSlot,isDeliverySlot,
//				deliveryDate,
//				DeliverySlot.SLOT_START_TIME,
//				getRowCount,getOnlyMetaData
//		);
//
//
//		//Marker
//		return ResponseEntity.status(HttpStatus.OK)
//				.body(endPoint);
//
//	}
//
//
//
//
//}
