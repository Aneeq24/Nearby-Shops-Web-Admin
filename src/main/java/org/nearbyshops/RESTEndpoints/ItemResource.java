package org.nearbyshops.RESTEndpoints;

import net.coobird.thumbnailator.Thumbnails;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.nearbyshops.AppProperties;
import org.nearbyshops.Constants;
import org.nearbyshops.DAOs.DAOImages.ItemImagesDAO;
import org.nearbyshops.DAOs.DAOReviewItem.FavoriteItemDAOPrepared;
import org.nearbyshops.DAOs.DAORoles.DAOStaff;
import org.nearbyshops.DAOs.DAORoles.DAOUserUtility;
import org.nearbyshops.DAOs.ItemCategoryDAO;
import org.nearbyshops.DAOs.ItemDAO;
import org.nearbyshops.DAOs.ItemDAOJoinOuter;
import org.nearbyshops.Model.Image;
import org.nearbyshops.Model.Item;
import org.nearbyshops.Model.ItemCategory;
import org.nearbyshops.Model.ModelEndpoint.ItemCategoryEndPoint;
import org.nearbyshops.Model.ModelEndpoint.ItemEndPoint;
import org.nearbyshops.Model.ModelEndpoint.ItemImageEndPoint;
import org.nearbyshops.Model.ModelImages.ItemImage;
import org.nearbyshops.Model.ModelRoles.StaffPermissions;
import org.nearbyshops.Model.ModelRoles.User;
import org.nearbyshops.Model.Shop;
import org.nearbyshops.Utility.UserAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/api/v1/Item")
public class ItemResource {



	@Autowired
	private HttpServletRequest request;

	@Autowired
	private UserAuthentication userAuthentication;

	@Autowired
	private AppProperties appProperties;


	@Autowired
	private ItemDAO itemDAO;

	@Autowired
	private ItemDAOJoinOuter itemDAOJoinOuter;

	@Autowired
	private ItemCategoryDAO itemCategoryDAO;

	@Autowired
	DAOUserUtility daoUserUtility;

	@Autowired
	DAOStaff daoStaff;


	@Autowired
	ItemImagesDAO itemImagesDAO;



	@Autowired
	FavoriteItemDAOPrepared favoriteItemDAOPrepared;





	@PostMapping
	@RolesAllowed({Constants.ROLE_STAFF, Constants.ROLE_SHOP_ADMIN})
	public ResponseEntity<?> createItem(@RequestBody Item item)
	{


		User user = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_STAFF,Constants.ROLE_SHOP_ADMIN));


		if(user==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}


		if(user.getRole()== Constants.ROLE_STAFF_CODE) {

			StaffPermissions permissions = daoStaff.getStaffPermissions(user.getUserID());

			if (!permissions.isPermitCreateUpdateItems())
			{
				// the staff member doesnt have persmission to post Item Category
				return ResponseEntity
						.status(HttpStatus.FORBIDDEN)
						.build();
			}


			Shop shop = daoUserUtility.getShopForShopStaff(user.getUserID());

			if(!(shop!=null && shop.getShopEnabled() && shop.isItemUpdatePermitted()))
			{
				return ResponseEntity
						.status(HttpStatus.FORBIDDEN)
						.build();
			}
		}
		else if (user.getRole()== Constants.ROLE_SHOP_ADMIN_CODE)
		{

			Shop shop = daoUserUtility.getShopForShopAdmin(user.getUserID());

			if(!(shop!=null && shop.getShopEnabled() && shop.isItemUpdatePermitted()))
			{
				return ResponseEntity
						.status(HttpStatus.FORBIDDEN)
						.build();
			}
		}




		int idOfInsertedRow = itemDAO.saveItem(item,false);

		item.setItemID(idOfInsertedRow);

		if(idOfInsertedRow >=1)
		{

			return ResponseEntity.status(HttpStatus.CREATED)
					.body(item);

		}else {

			return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
					.build();

		}
	}




	@PutMapping("/ChangeParent/{ItemID}")
	@RolesAllowed({Constants.ROLE_ADMIN, Constants.ROLE_STAFF})
	public ResponseEntity<?> changeParent(@RequestBody Item item, @PathVariable("ItemID")int itemID)
	{

		User user = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_STAFF,Constants.ROLE_ADMIN));

		if(user==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}


		if(user.getRole()== Constants.ROLE_STAFF_CODE) {

			StaffPermissions permissions = daoStaff.getStaffPermissions(user.getUserID());

			if (!permissions.isPermitCreateUpdateItems())
			{
				// the staff member doesnt have persmission to post Item Category
				return ResponseEntity
						.status(HttpStatus.FORBIDDEN)
						.build();
			}
		}



		item.setItemID(itemID);

		//System.out.println("ItemCategoryID: " + itemCategoryID + " " + itemCategory.getCategoryName()
		//+ " " + itemCategory.getCategoryDescription());

		int rowCount = itemDAO.changeParent(item);



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





	@PutMapping ("/ChangeParent")
	@RolesAllowed({Constants.ROLE_ADMIN, Constants.ROLE_STAFF})
	public ResponseEntity<?> changeParentBulk(@RequestBody List<Item> itemList)
	{


		User user = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_STAFF,Constants.ROLE_ADMIN));

		if(user==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}


		if(user.getRole()== Constants.ROLE_STAFF_CODE) {

			StaffPermissions permissions = daoStaff.getStaffPermissions(user.getUserID());

			if (!permissions.isPermitCreateUpdateItems())
			{
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.build();
			}
		}




		int rowCountSum = 0;

//		for(Item item : itemList)
//		{
//			rowCountSum = rowCountSum + itemDAO.updateItem(item);
//		}

		rowCountSum = itemDAO.changeParentBulk(itemList);

		if(rowCountSum ==  itemList.size())
		{

			return ResponseEntity.status(HttpStatus.OK)
					.build();
		}
		else if( rowCountSum < itemList.size() && rowCountSum > 0)
		{

			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
					.build();
		}
		else {

			return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
					.build();
		}
	}






	@PutMapping ("/{ItemID}")
	@RolesAllowed({Constants.ROLE_ADMIN, Constants.ROLE_STAFF, Constants.ROLE_SHOP_ADMIN})
	public ResponseEntity<?> updateItem(@RequestBody Item item, @PathVariable("ItemID")int itemID) {

		try {


			User user = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_STAFF,Constants.ROLE_SHOP_ADMIN));

			if(user==null)
			{
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.build();
			}


			if (user.getRole() == Constants.ROLE_STAFF_CODE) {

				StaffPermissions permissions = daoStaff.getStaffPermissions(user.getUserID());

				if (!permissions.isPermitCreateUpdateItems()) {

					// the staff member doesnt have persmission to post Item Category
					return ResponseEntity.status(HttpStatus.FORBIDDEN)
							.build();
				}
			} else if (user.getRole() == Constants.ROLE_SHOP_ADMIN_CODE) {

				Shop shop = daoUserUtility.getShopForShopAdmin(user.getUserID());

				if (!(shop != null && shop.getShopEnabled() && shop.isItemUpdatePermitted())) {

					return ResponseEntity.status(HttpStatus.FORBIDDEN)
							.build();
				}
			}


			item.setItemID(itemID);

			//System.out.println("ItemCategoryID: " + itemCategoryID + " " + itemCategory.getCategoryName()
			//+ " " + itemCategory.getCategoryDescription());

			int rowCount = itemDAO.updateItem(item);

			if (rowCount >= 1) {

				return ResponseEntity.status(HttpStatus.OK)
						.build();
			} else if (rowCount == 0) {

				return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
						.build();
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}



		return null;

	}






	
	@DeleteMapping ("/{ItemID}")
	@RolesAllowed({Constants.ROLE_ADMIN, Constants.ROLE_STAFF})
	public ResponseEntity<?> deleteItem(@PathVariable("ItemID")int itemID)
	{

		User user = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_STAFF,Constants.ROLE_SHOP_ADMIN));

		if(user==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}


		if(user.getRole()== Constants.ROLE_STAFF_CODE) {

			StaffPermissions permissions = daoStaff.getStaffPermissions(user.getUserID());

			if (!permissions.isPermitCreateUpdateItems())
			{
				// the staff member doesnt have persmission to post Item Category
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.build();
			}
		}




		Item item = itemDAO.getItemImageURL(itemID);
		int rowCount = itemDAO.deleteItem(itemID);


		if(item!=null && rowCount>=1) {


			try {

				if(item.getItemImageURL()!=null)
				{
					deleteImageFileInternal(item.getItemImageURL());
				}

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}



		if(rowCount>=1)
		{

			return ResponseEntity.status(HttpStatus.OK)
					.build();
		}
		
		if(rowCount == 0)
		{

			return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
					.build();
		}
		
		return null;
	}




//	@RequestParam("deliveryRangeMax")Double deliveryRangeMax,
//	@RequestParam("deliveryRangeMin")Double deliveryRangeMin,
//	@RequestParam("proximity")Double proximity,



	@GetMapping
	public ResponseEntity<?> getItems(
            @RequestParam(value = "ItemCategoryID",required = false) Integer itemCategoryID,
            @RequestParam(value = "ShopID", required = false) Integer shopID,
			@RequestParam(value = "GetSubcategories",defaultValue = "false") boolean getSubcategories,
            @RequestParam(value = "latCenter",required = false) Double latCenter,
			@RequestParam(value = "lonCenter",required = false) Double lonCenter,
            @RequestParam(value = "ItemSpecValues",required = false) String itemSpecValues,
            @RequestParam(value = "SearchString",required = false)String searchString,
            @RequestParam(value = "SortBy",required = false) String sortBy,
            @RequestParam(value = "Limit",defaultValue = "0")int limit,
			@RequestParam(value = "Offset",defaultValue = "0")int offset,
			@RequestParam(value = "GetRowCount",defaultValue = "false")boolean getRowCount,
			@RequestParam(value = "MetadataOnly",defaultValue = "false")boolean getOnlyMetaData)
	{


		List<ItemCategory> subcategories;


		if(limit >= appProperties.getMax_limit())
		{
			limit = appProperties.getMax_limit();
		}




		ItemEndPoint endPoint  = itemDAO.getItemsRecursive(
											itemCategoryID,
											shopID,
											latCenter, lonCenter,
											itemSpecValues,
											null, null, null,
											searchString,
											sortBy,limit,offset,
											getRowCount,getOnlyMetaData
									);





			if(getSubcategories)
			{
				subcategories = itemCategoryDAO.getItemCategoriesJoinRecursive(
						shopID, itemCategoryID, null,
						latCenter, lonCenter,
						true,
						searchString,
						ItemCategory.CATEGORY_ORDER,
						null,null
				);



				endPoint.setSubcategories(subcategories);
			}





		endPoint.setLimit(limit);
		endPoint.setOffset(offset);
		endPoint.setMax_limit(appProperties.getMax_limit());



		//Marker
		return ResponseEntity.status(HttpStatus.OK)
                .body(endPoint);
	}






	@GetMapping ("/OuterJoin")
	public ResponseEntity<?> getItems(
            @RequestParam(value = "ItemCategoryID",required = false)Integer itemCategoryID,
			@RequestParam(value = "GetSubcategories",defaultValue = "false")boolean getSubcategories,
            @RequestParam(value = "IsDetached",required = false)Boolean parentIsNull,
            @RequestParam(value = "SearchString",required = false) String searchString,
            @RequestParam(value = "SortBy",required = false) String sortBy,
            @RequestParam(value = "Limit",defaultValue = "0") int limit,
			@RequestParam(value = "Offset",defaultValue = "0") int offset,
			@RequestParam(value = "GetRowCount",defaultValue = "false")boolean getRowCount,
			@RequestParam(value = "MetadataOnly",defaultValue = "false")boolean getOnlyMetaData)
	{




		if (limit >= appProperties.getMax_limit()) {

			limit = appProperties.getMax_limit();
		}




		ItemEndPoint endPoint = itemDAOJoinOuter.getItems(
				itemCategoryID,
				parentIsNull,searchString,
				sortBy,limit,offset,
				getRowCount,getOnlyMetaData
		);






		if(getSubcategories)
		{
			ItemCategoryEndPoint endPointCat = itemCategoryDAO
					.getItemCategoriesSimplePrepared(
							itemCategoryID,
							parentIsNull,
							searchString,
							ItemCategory.CATEGORY_ORDER,null,null);

			endPoint.setSubcategories(endPointCat.getResults());
		}




		endPoint.setLimit(limit);
		endPoint.setMax_limit(appProperties.getMax_limit());
		endPoint.setOffset(offset);





//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}


		//Marker
		return ResponseEntity.status(HttpStatus.OK)
				.body(endPoint);
	}








	@GetMapping ("/GetItemDetails")
	public ResponseEntity<Object> getItemDetails(@RequestParam("ItemID")Integer itemID)
	{
		Item item = itemDAO.getItemDetailsForEditItem(itemID);

		//Marker
		return ResponseEntity.status(HttpStatus.OK)
				.body(item);
	}







	@GetMapping ("/ItemDetailsForItemDetailScreen")
	public ResponseEntity<Object> getItemDetailsForDetailScreen(
			@RequestParam(value = "GetItemDetails", defaultValue = "false")boolean getItemDetails,
			@RequestParam(value = "ItemID",required = false)Integer itemID,
			@RequestParam(value = "SortBy",required = false) String sortBy,
			@RequestParam(value = "Limit",required = false)Integer limit,
			@RequestParam(value = "Offset",defaultValue = "0")int offset)
	{



		User user = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_END_USER));


		if(limit!=null && limit >= appProperties.getMax_limit())
		{
			limit = appProperties.getMax_limit();
		}




		ItemImageEndPoint endPoint = new ItemImageEndPoint();


		List<ItemImage> list = null;


		list =
				itemImagesDAO.getItemImagesForEndUser(
						itemID,
						sortBy
				);

		endPoint.setResults(list);


		if(getItemDetails)
		{
			endPoint.setItemDetails(itemDAO.getItemDetails(itemID));
		}





		if(user!=null)
		{
			boolean isFavourite = favoriteItemDAOPrepared.checkFavourite(
					itemID,user.getUserID()
			);

			endPoint.setFavourite(isFavourite);
		}




		if(limit!=null)
		{
			endPoint.setLimit(limit);
			endPoint.setOffset(offset);
			endPoint.setMax_limit(appProperties.getMax_limit());
		}



//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}


		//Marker

		return ResponseEntity.status(HttpStatus.OK)
				.body(endPoint);
	}






	// Image Utility Methods

	private void deleteImageFileInternal(String fileName)
	{
		boolean deleteStatus = false;

//		System.out.println("Filename: " + fileName);

		try {

			//Files.delete(BASE_DIR.resolve(fileName));
			deleteStatus = Files.deleteIfExists(BASE_DIR.resolve(fileName));

			// delete thumbnails
			Files.deleteIfExists(BASE_DIR.resolve("three_hundred_" + fileName + ".jpg"));
			Files.deleteIfExists(BASE_DIR.resolve("five_hundred_" + fileName + ".jpg"));


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}




	private String saveNewImage(String serviceURL,String imageID)
	{
		try
		{
			serviceURL = serviceURL + "/api/v1/Item/Image/" + imageID;

			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder()
					.url(serviceURL)
					.build();

			okhttp3.Response response = null;
			response = client.newCall(request).execute();
//			response.body().byteStream();
//			System.out.println();

			return uploadNewImage(response.body().byteStream());

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return null;
	}




	private String uploadNewImage(InputStream in)
	{

		File theDir = new File(BASE_DIR.toString());

		// if the directory does not exist, create it
		if (!theDir.exists()) {

//			System.out.println("Creating directory: " + BASE_DIR.toString());

			boolean result = false;

			try{
				theDir.mkdir();
				result = true;
			}
			catch(Exception se){
				//handle it
			}
			if(result) {
//				System.out.println("DIR created");
			}
		}



		String fileName = "" + System.currentTimeMillis();


		try {

			// Copy the file to its location.
			long filesize = 0;

			filesize = Files.copy(in, BASE_DIR.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

			if(filesize > MAX_IMAGE_SIZE_MB * 1048 * 1024)
			{
				// delete file if it exceeds the file size limit
				Files.deleteIfExists(BASE_DIR.resolve(fileName));
				return null;
			}

			createThumbnails(fileName);

			Image image = new Image();
			image.setPath(fileName);

			// Return a 201 Created response with the appropriate Location header.

		}
		catch (IOException e) {
			e.printStackTrace();

			return null;
		}

		return fileName;
	}






	// Image MEthods

	private static final java.nio.file.Path BASE_DIR = Paths.get("./data/images/Item");
	private static final double MAX_IMAGE_SIZE_MB = 10;





	@PostMapping("/Image")
	@RolesAllowed({Constants.ROLE_ADMIN, Constants.ROLE_STAFF, Constants.ROLE_SHOP_ADMIN})
	public ResponseEntity<Object> uploadImage(@RequestParam(name = "img") MultipartFile img,
											  @RequestParam(value = "PreviousImageName",required = false) String previousImageName
	) throws Exception
	{


		User user = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_STAFF,Constants.ROLE_SHOP_ADMIN));

		if(user==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}


		if(user.getRole()== Constants.ROLE_STAFF_CODE) {

			StaffPermissions permissions = daoStaff.getStaffPermissions(user.getUserID());

			if (!permissions.isPermitCreateUpdateItems())
			{
				// the staff member doesnt have persmission to post Item Category
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.build();
			}
		}


		if(previousImageName!=null)
		{
			Files.deleteIfExists(BASE_DIR.resolve(previousImageName));
			Files.deleteIfExists(BASE_DIR.resolve("three_hundred_" + previousImageName + ".jpg"));
			Files.deleteIfExists(BASE_DIR.resolve("five_hundred_" + previousImageName + ".jpg"));
			Files.deleteIfExists(BASE_DIR.resolve("seven_hundred_" + previousImageName + ".jpg"));
			Files.deleteIfExists(BASE_DIR.resolve("nine_hundred_" + previousImageName + ".jpg"));
		}


		File theDir = new File(BASE_DIR.toString());

		// if the directory does not exist, create it
		if (!theDir.exists()) {

//			System.out.println("Creating directory: " + BASE_DIR.toString());

			boolean result = false;

			try{
				theDir.mkdir();
				result = true;
			}
			catch(Exception se){
				//handle it
			}
			if(result) {
//				System.out.println("DIR created");
			}
		}



		String fileName = "" + System.currentTimeMillis();

		// Copy the file to its location.
		long filesize = Files.copy(img.getInputStream(), BASE_DIR.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

		if(filesize > MAX_IMAGE_SIZE_MB * 1048 * 1024)
		{
			// delete file if it exceeds the file size limit
			Files.deleteIfExists(BASE_DIR.resolve(fileName));

			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
					.build();
		}


		createThumbnails(fileName);


		Image image = new Image();
		image.setPath(fileName);


		// Return a 201 Created response with the appropriate Location header.
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(image);
	}






	private void createThumbnails(String filename)
	{
		try {

			Thumbnails.of(BASE_DIR.toString() + "/" + filename)
					.size(300,300)
					.outputFormat("jpg")
					.toFile(new File(BASE_DIR.toString() + "/" + "three_hundred_" + filename));

			//.toFile(new File("five-" + filename + ".jpg"));

			//.toFiles(Rename.PREFIX_DOT_THUMBNAIL);


			Thumbnails.of(BASE_DIR.toString() + "/" + filename)
					.size(500,500)
					.outputFormat("jpg")
					.toFile(new File(BASE_DIR.toString() + "/" + "five_hundred_" + filename));



			Thumbnails.of(BASE_DIR.toString() + "/" + filename)
					.size(700,700)
					.outputFormat("jpg")
					.toFile(new File(BASE_DIR.toString() + "/" + "seven_hundred_" + filename));



			Thumbnails.of(BASE_DIR.toString() + "/" + filename)
					.size(900,900)
					.outputFormat("jpg")
					.toFile(new File(BASE_DIR.toString() + "/" + "nine_hundred_" + filename));



		} catch (IOException e) {
			e.printStackTrace();
		}
	}




	@GetMapping ("/Image/{name}")
	public ResponseEntity<?> getImage(@PathVariable("name") String fileName, HttpServletResponse response) {


		try {

			//fileName += ".jpg";
			java.nio.file.Path dest = BASE_DIR.resolve(fileName);

			if (!Files.exists(dest)) {

				return ResponseEntity.status(HttpStatus.NO_CONTENT)
						.build();
			}


			StreamUtils.copy(Files.newInputStream(dest), response.getOutputStream());

			return ResponseEntity.status(HttpStatus.OK)
					.build();



		} catch (IOException e) {
			e.printStackTrace();
		}

		return ResponseEntity.status(HttpStatus.NO_CONTENT)
				.build();
	}




	@DeleteMapping ("/Image/{name}")
	@RolesAllowed({Constants.ROLE_ADMIN, Constants.ROLE_STAFF})
	public ResponseEntity<Object> deleteImageFile(@PathVariable("name")String fileName)
	{


		boolean deleteStatus = false;

		try {


			//Files.delete(BASE_DIR.resolve(fileName));
			deleteStatus = Files.deleteIfExists(BASE_DIR.resolve(fileName));

			// delete thumbnails
			Files.deleteIfExists(BASE_DIR.resolve("three_hundred_" + fileName + ".jpg"));
			Files.deleteIfExists(BASE_DIR.resolve("five_hundred_" + fileName + ".jpg"));
			Files.deleteIfExists(BASE_DIR.resolve("seven_hundred_" + fileName + ".jpg"));
			Files.deleteIfExists(BASE_DIR.resolve("nine_hundred_" + fileName + ".jpg"));


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}




		if(!deleteStatus)
		{
			return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
					.build();

		}
		else
		{
			return ResponseEntity.status(HttpStatus.OK)
					.build();
		}


	}

}
