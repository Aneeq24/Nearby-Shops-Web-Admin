package org.nearbyshops.RESTEndpoints;

import com.google.gson.Gson;
import net.coobird.thumbnailator.Thumbnails;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.nearbyshops.Constants;
import org.nearbyshops.DAOs.DAORoles.DAOStaff;
import org.nearbyshops.DAOs.ItemCategoryDAO;
import org.nearbyshops.Model.Image;
import org.nearbyshops.Model.ItemCategory;
import org.nearbyshops.Model.ModelEndpoint.ItemCategoryEndPoint;
import org.nearbyshops.Model.ModelRoles.StaffPermissions;
import org.nearbyshops.Model.ModelRoles.User;
import org.nearbyshops.Utility.UserAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;




@RestController
@RequestMapping("/api/v1/ItemCategory")
public class ItemCategoryREST {



	Logger logger = LoggerFactory.getLogger(ItemCategoryREST.class);

	@Autowired
	ItemCategoryDAO itemCategoryDAO;

	@Autowired
	DAOStaff daoStaff;


    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserAuthentication userAuthentication;

    @Autowired
    Gson gson;




	@PostMapping
	@RolesAllowed({Constants.ROLE_ADMIN, Constants.ROLE_STAFF})
	public ResponseEntity<?> saveItemCategory(@RequestBody ItemCategory itemCategory)
	{

	    User user = userAuthentication.isUserAllowed(request,
				Arrays.asList(Constants.ROLE_STAFF));

        if(user==null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }



        if(user.getRole()== Constants.ROLE_STAFF_CODE) {

			StaffPermissions permissions = daoStaff.getStaffPermissions(user.getUserID());

			if (!permissions.isPermitCreateUpdateItemCat())
			{
				// the staff member doesn't have permission to post Item Category
				return ResponseEntity
						.status(HttpStatus.FORBIDDEN)
						.build();
			}
		}





		int idOfInsertedRow = itemCategoryDAO.saveItemCategory(itemCategory,false);
		itemCategory.setItemCategoryID(idOfInsertedRow);



		if(idOfInsertedRow >=1)
		{
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(itemCategory);
		}
		else
		{
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.build();
		}


	}





	@DeleteMapping("/{ItemCategoryID}")
	@RolesAllowed({Constants.ROLE_ADMIN, Constants.ROLE_STAFF})
	public ResponseEntity<?> deleteItemCategory(@PathVariable("ItemCategoryID")int itemCategoryID)
	{

        User user = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_STAFF));

        if(user==null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }


		if(user.getRole()== Constants.ROLE_STAFF_CODE) {

			StaffPermissions permissions = daoStaff.getStaffPermissions(user.getUserID());

			if (!permissions.isPermitCreateUpdateItemCat())
			{
                // the staff member doesn't have permission to post Item Category
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .build();
			}
		}




		ItemCategory itemCategory = itemCategoryDAO.getItemCatImageURL(itemCategoryID);
		int rowCount = itemCategoryDAO.deleteItemCategory(itemCategoryID);


		if(itemCategory!=null && rowCount>=1)
		{

			try {


				if(itemCategory.getImagePath()!=null)
				{
					deleteImageFileInternal(itemCategory.getImagePath());
				}

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}




		if(rowCount>=1)
		{
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .build();
		}
		else if(rowCount == 0)
		{
            return ResponseEntity
                    .status(HttpStatus.NOT_MODIFIED)
                    .build();
		}

		return null;
	}





	@PutMapping("/ChangeParent/{ItemCategoryID}")
	@RolesAllowed({Constants.ROLE_ADMIN, Constants.ROLE_STAFF})
	public ResponseEntity<?> changeParent(@PathVariable("ItemCategoryID")int itemCategoryID,
                                          @RequestBody ItemCategory itemCategory)
	{


        User user = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_STAFF));

        if(user==null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }


		if(user.getRole()== Constants.ROLE_STAFF_CODE) {

			StaffPermissions permissions = daoStaff.getStaffPermissions(user.getUserID());

			if (!permissions.isPermitCreateUpdateItemCat())
			{
				// the staff member doesnt have persmission to post Item Category
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .build();
			}
		}




		itemCategory.setItemCategoryID(itemCategoryID);
		int rowCount = itemCategoryDAO.changeParent(itemCategory);



		if(rowCount >= 1)
		{

			return ResponseEntity.status(HttpStatus.OK)
					.build();
		}
		else if(rowCount == 0)
		{

			return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
					.build();
		}

		return null;
	}






	@PutMapping("/ChangeParent")
	@RolesAllowed({Constants.ROLE_ADMIN, Constants.ROLE_STAFF})
	public ResponseEntity<Object> changeParentBulk(@RequestBody List<ItemCategory> itemCategoryList)
	{

        User user = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_STAFF));

        if(user==null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }



		if(user.getRole()== Constants.ROLE_STAFF_CODE) {

			StaffPermissions permissions = daoStaff.getStaffPermissions(user.getUserID());

			if (!permissions.isPermitCreateUpdateItemCat())
			{
				// the staff member doesnt have persmission to post Item Category
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .build();
			}
		}


		int rowCountSum = 0;

		rowCountSum = itemCategoryDAO.changeParentBulk(itemCategoryList);



		if(rowCountSum ==  itemCategoryList.size())
		{

			return ResponseEntity.status(HttpStatus.OK)
					.build();
		}
		else if( rowCountSum < itemCategoryList.size() && rowCountSum > 0)
		{

			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
					.build();
		}
		else if(rowCountSum == 0 ) {

			return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
					.build();
		}

		return null;
	}






	@PutMapping("/{ItemCategoryID}")
	@RolesAllowed({Constants.ROLE_ADMIN, Constants.ROLE_STAFF})
	public ResponseEntity<Object> updateItemCategory(@PathVariable("ItemCategoryID")int itemCategoryID,
													 @RequestBody ItemCategory itemCategory
	)
	{


        User user = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_STAFF));

        if(user==null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }



		if(user.getRole()== Constants.ROLE_STAFF_CODE) {

			StaffPermissions permissions = daoStaff.getStaffPermissions(user.getUserID());

			if (!permissions.isPermitCreateUpdateItemCat())
			{
				// the staff member doesnt have persmission to post Item Category
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .build();
			}
		}



		itemCategory.setItemCategoryID(itemCategoryID);


		int rowCount = itemCategoryDAO.updateItemCategory(itemCategory);




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






	@PutMapping
	@RolesAllowed({Constants.ROLE_ADMIN, Constants.ROLE_STAFF})
	public ResponseEntity<?> updateItemCategoryBulk(@RequestBody List<ItemCategory> itemCategoryList)
	{


        User user = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_STAFF));

        if(user==null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }


		if(user.getRole()== Constants.ROLE_STAFF_CODE) {

			StaffPermissions permissions = daoStaff.getStaffPermissions(user.getUserID());

			if (!permissions.isPermitCreateUpdateItemCat())
			{
                // the staff member doesnt have persmission to post Item Category
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .build();
			}
		}


		int rowCountSum = 0;

		for(ItemCategory itemCategory : itemCategoryList)
		{
			rowCountSum = rowCountSum + itemCategoryDAO.updateItemCategory(itemCategory);
		}

		if(rowCountSum ==  itemCategoryList.size())
		{

			return ResponseEntity.status(HttpStatus.OK)
					.build();
		}
		else if( rowCountSum < itemCategoryList.size() && rowCountSum > 0)
		{

			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
					.build();
		}
		else if(rowCountSum == 0 ) {

			return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
					.build();
		}

		return null;
	}







	@GetMapping("/QuerySimple")
	public ResponseEntity<?> getItemCategoriesQuerySimple(
            @RequestParam(value = "ParentID",required = false)Integer parentID,
            @RequestParam(value = "IsDetached",required = false)Boolean parentIsNull,
            @RequestParam(value = "SearchString",required = false) String searchString,
            @RequestParam(value = "SortBy",required = false) String sortBy,
            @RequestParam(value = "Limit",required = false) Integer limit,
			@RequestParam(value = "Offset",defaultValue = "0")int offset,
            @RequestParam(value = "metadata_only",required = false)Boolean metaonly
	)
	{

		final int max_limit = 100;

		if(limit!=null)
		{
			if(limit>=max_limit)
			{
				limit = max_limit;
			}

		}




		ItemCategoryEndPoint endPoint = itemCategoryDAO
				.getItemCategoriesSimplePrepared(
						parentID,
						parentIsNull,
						searchString,
						sortBy,limit,offset);


		endPoint.setLimit(limit);
		endPoint.setMax_limit(max_limit);
		endPoint.setOffset(offset);


		//Marker
		return ResponseEntity.status(HttpStatus.OK)
				.body(endPoint);
	}







	@GetMapping
	public ResponseEntity<?> getItemCategories(
            @RequestParam(value = "ShopID",required = false)Integer shopID,
            @RequestParam(value = "ParentID",required = false)Integer parentID,
			@RequestParam(value = "IsDetached",required = false)Boolean parentIsNull,
            @RequestParam(value = "latCenter",required = false)Double latCenter,
			@RequestParam(value = "lonCenter",required = false)Double lonCenter,
            @RequestParam(value = "deliveryRangeMax",required = false)Double deliveryRangeMax,
            @RequestParam(value = "deliveryRangeMin",required = false)Double deliveryRangeMin,
            @RequestParam(value = "proximity",required = false)Double proximity,
            @RequestParam(value = "ShopEnabled",required = false)Boolean shopEnabled,
            @RequestParam(value = "SearchString",required = false) String searchString,
            @RequestParam(value = "SortBy",required = false) String sortBy,
            @RequestParam(value = "Limit",required = false) Integer limit,
			@RequestParam(value = "Offset",defaultValue = "0") int offset,
            @RequestParam(value = "metadata_only",required = false)Boolean metaonly)
	{



		final int max_limit = 100;

		if(limit!=null)
		{
			if(limit>=max_limit)
			{
				limit = max_limit;
			}
		}



		ItemCategoryEndPoint endPoint = new ItemCategoryEndPoint();


		ArrayList<ItemCategory> list = null;



		if(metaonly==null || (!metaonly)) {
			list = itemCategoryDAO.getItemCategoriesJoinRecursive(
					shopID, parentID, parentIsNull,
					latCenter, lonCenter,
					shopEnabled,
					searchString,
					sortBy,
					limit, offset);

			endPoint.setResults(list);
		}




		endPoint.setLimit(limit);
		endPoint.setMax_limit(max_limit);
		endPoint.setOffset(offset);



		return ResponseEntity.status(HttpStatus.OK)
                .body(endPoint);

	}






	@GetMapping ("/GetItemCategoryDetails")
	public ResponseEntity<?> getItemCategoryDetails(
			@RequestParam("ItemCategoryID")int itemCategoryID
	)
	{


//		logger.info("Sample Boolean " + sample + " | Sample Int : " + sampleInt);


		ItemCategory itemCategory = itemCategoryDAO.getItemCategoryDetails(itemCategoryID);

		if(itemCategory!=null)
		{
			return ResponseEntity.status(HttpStatus.OK)
					.body(itemCategory);
		}
		else
		{
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.build();
		}

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
			serviceURL = serviceURL + "/api/v1/ItemCategory/Image/" + imageID;

			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder()
					.url(serviceURL)
					.build();

			okhttp3.Response response = null;
			response = client.newCall(request).execute();
			response.body().byteStream();
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








	// Image Methods

	private static final java.nio.file.Path BASE_DIR = Paths.get("./data/images/ItemCategory");
	private static final double MAX_IMAGE_SIZE_MB = 10;



	@PostMapping ("/Image")
	@RolesAllowed({Constants.ROLE_ADMIN, Constants.ROLE_STAFF})
	public ResponseEntity<?> uploadImageREST(@RequestParam(name = "img") MultipartFile img,
											 @RequestParam(value = "PreviousImageName",required = false) String previousImageName
	) throws Exception
	{


		User user = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_STAFF));

		if(user==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}


		if(user.getRole()== Constants.ROLE_STAFF_CODE) {

			StaffPermissions permissions = daoStaff.getStaffPermissions(user.getUserID());

			if (!permissions.isPermitCreateUpdateItemCat())
			{
				// the staff member doesnt have persmission to post Item Category
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.build();
			}
		}



//		logger.info("Previous Image Filename " + previousImageName);



		if(previousImageName!=null)
		{
			Files.deleteIfExists(BASE_DIR.resolve(previousImageName));
			Files.deleteIfExists(BASE_DIR.resolve("three_hundred_" + previousImageName + ".jpg"));
			Files.deleteIfExists(BASE_DIR.resolve("five_hundred_" + previousImageName + ".jpg"));
		}


		createDirectoryIfNotExist();


		String fileName = "" + System.currentTimeMillis();

		// Copy the file to its location.
		long filesize = Files.copy(img.getInputStream(), BASE_DIR.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

		if(filesize > MAX_IMAGE_SIZE_MB * 1048 * 1024)
		{
			// delete file if it exceeds the file size limit
			Files.deleteIfExists(BASE_DIR.resolve(fileName));

			return ResponseEntity
					.status(HttpStatus.EXPECTATION_FAILED)
					.build();
		}


		createThumbnails(fileName);


		Image image = new Image();
		image.setPath(fileName);



		// Return a 201 Created response with the appropriate Location header.
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(image);
	}





	String uploadImagePlain(InputStream in, String previousImageName) throws IOException {


		if(previousImageName!=null)
		{
			Files.deleteIfExists(BASE_DIR.resolve(previousImageName));
			Files.deleteIfExists(BASE_DIR.resolve("three_hundred_" + previousImageName + ".jpg"));
			Files.deleteIfExists(BASE_DIR.resolve("five_hundred_" + previousImageName + ".jpg"));
		}


		createDirectoryIfNotExist();


		String fileName = "" + System.currentTimeMillis();

		Files.copy(in, BASE_DIR.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);


		// Copy the file to its location.
//		long filesize = Files.copy(in, BASE_DIR.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

//		if(filesize > MAX_IMAGE_SIZE_MB * 1048 * 1024)
//		{
//			// delete file if it exceeds the file size limit
//			Files.deleteIfExists(BASE_DIR.resolve(fileName));
//			return null;
//		}


		createThumbnails(fileName);

		return fileName;
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



		} catch (IOException e) {
			e.printStackTrace();
		}
	}





	private void createDirectoryIfNotExist()
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


//			response.setContentType(MediaType.IMAGE_JPEG_VALUE);
			StreamUtils.copy(Files.newInputStream(dest), response.getOutputStream());

			return ResponseEntity.status(HttpStatus.OK)
					.build();


		}
		catch (Exception e) {
			e.printStackTrace();
		}



		return ResponseEntity.status(HttpStatus.NO_CONTENT)
				.build();
	}






	@DeleteMapping ("/Image/{name}")
	@RolesAllowed({Constants.ROLE_ADMIN, Constants.ROLE_STAFF})
	ResponseEntity<Object> deleteImageFile(@PathVariable("name")String fileName)
	{


		User user = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_STAFF));

		if(user==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}



		if(user.getRole()== Constants.ROLE_STAFF_CODE) {

			StaffPermissions permissions = daoStaff.getStaffPermissions(user.getUserID());

			if (!permissions.isPermitCreateUpdateItemCat())
			{
				// the staff member doesnt have persmission to post Item Category
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.build();
			}
		}



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
