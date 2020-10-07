package org.nearbyshops.RESTEndpoints.RESTEndpointSettings;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import net.coobird.thumbnailator.Thumbnails;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.nearbyshops.AppProperties;
import org.nearbyshops.Constants;
import org.nearbyshops.DAOs.DAOSettings.MarketSettingsDAO;
import org.nearbyshops.DAOs.DAOSettings.ServiceConfigurationDAO;
import org.nearbyshops.Model.Image;
import org.nearbyshops.Model.ModelRoles.User;
import org.nearbyshops.Model.ModelSettings.Market;
import org.nearbyshops.Model.ModelUtility.PushNotificationData;
import org.nearbyshops.Utility.Globals;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;


@RestController
@RequestMapping("/api/ServiceConfiguration")
public class ServiceConfigurationResource {



	@Autowired
	private ServiceConfigurationDAO serviceConfigurationDAO;


	@Autowired
	AppProperties appProperties;

	@Autowired
	MarketSettingsDAO marketSettingsDAO;




	@Autowired
	private HttpServletRequest request;

	@Autowired
	private UserAuthentication userAuthentication;





	@GetMapping
	@CrossOrigin
	public ResponseEntity<Object> getService(@RequestParam(value = "latCenter",required = false)Double latCenter,
									 @RequestParam(value = "lonCenter",required = false)Double lonCenter)
	{
//		@PathParam("ServiceID")int service

		Market market = serviceConfigurationDAO.getServiceConfigurationFromTable(latCenter,lonCenter);

		if(market != null)
		{
			market.setRt_login_using_otp_enabled(marketSettingsDAO.getSettingsInstance().isLoginUsingOTPEnabled());
			market.setRt_market_id_for_fcm(appProperties.getMarket_id_for_fcm());

			return ResponseEntity.status(HttpStatus.OK)
					.body(market);

		}
		else
		{
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.build();
		}

	}





	@PostMapping ("/SendPushNotification")
	@RolesAllowed({Constants.ROLE_STAFF})
	public ResponseEntity<Object> sendPushNotification(@RequestBody PushNotificationData pushNotificationData)
	{

		User user = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_STAFF));

		if(user==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}


		try {

			for(String topic : pushNotificationData.getTopicList())
			{


//				Globals.sendFCMPushNotification(
//						topic,
//						pushNotificationData.getTitle(),
//						pushNotificationData.getDescription(),
//						Constants.NOTIFICATION_TYPE_GENERAL
//				);

				// See documentation on defining a message payload.
				Message messageEndUser = Message.builder()
						.putData("notification_type", Constants.NOTIFICATION_TYPE_GENERAL)
						.putData("notification_title", pushNotificationData.getTitle())
						.putData("notification_message", pushNotificationData.getDescription())
						.setNotification(new Notification(pushNotificationData.getTitle(),
								pushNotificationData.getDescription(),
								pushNotificationData.getImageURL()))
						.setTopic(topic)
						.build();


//            System.out.println(topic);

				FirebaseMessaging.getInstance().sendAsync(messageEndUser);
			}


		}
		catch (Exception e)
		{
			e.printStackTrace();
		}


		return ResponseEntity.status(HttpStatus.OK)
				.build();
	}




	@PutMapping
	@RolesAllowed({Constants.ROLE_ADMIN})
	public ResponseEntity<Object> updateService(@RequestBody Market market)
	{


		User user = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_ADMIN));

		if(user==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}


		market.setServiceID(1);
		int rowCount =	serviceConfigurationDAO.updateService(market);




		if(rowCount >= 1)
		{
			updateMarketEntryAtSDS();

			serviceConfigurationDAO.setMarket(null);

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








	// Image MEthods
	private static final java.nio.file.Path BASE_DIR = Paths.get("./data/images/ServiceConfiguration");
	private static final double MAX_IMAGE_SIZE_MB = 2;


	@PostMapping ("/Image")
	@RolesAllowed({Constants.ROLE_ADMIN})
	public ResponseEntity<Object> uploadImage(@RequestParam(name = "img") MultipartFile img,
											  @RequestParam(value = "PreviousImageName",required = false) String previousImageName
	) throws Exception
	{




		if(previousImageName!=null)
		{
			Files.deleteIfExists(BASE_DIR.resolve(previousImageName));
			Files.deleteIfExists(BASE_DIR.resolve("three_hundred_" + previousImageName + ".jpg"));
			Files.deleteIfExists(BASE_DIR.resolve("five_hundred_" + previousImageName + ".jpg"));
		}


		File theDir = new File(BASE_DIR.toString());

		// if the directory does not exist, create it
		if (!theDir.exists()) {

			System.out.println("Creating directory: " + BASE_DIR.toString());

			boolean result = false;

			try{
				theDir.mkdir();
				result = true;
			}
			catch(Exception se){
				//handle it
			}
			if(result) {
				System.out.println("DIR created");
			}
		}



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



	@GetMapping ("/Image/{name}")
	public ResponseEntity<?> getImage(@PathVariable("name") String fileName,
									  HttpServletResponse response) {

		//fileName += ".jpg";
		java.nio.file.Path dest = BASE_DIR.resolve(fileName);

		if (!Files.exists(dest)) {

			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.build();
		}


		try {

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
	@RolesAllowed({Constants.ROLE_ADMIN})
	public ResponseEntity<Object> deleteImageFile(@PathVariable("name")String fileName)
	{

		User user = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_ADMIN));

		if(user==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}



		boolean deleteStatus = false;

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

		}else
		{
			return ResponseEntity.status(HttpStatus.OK)
					.build();
		}


	}






	// utility methods
	void updateMarketEntryAtSDS()
	{
		for(String url : appProperties.getTrusted_market_aggregators())
		{
			// for each url send a ping
			updateSDSEntry(url);
		}
	}




	private static final OkHttpClient client = new OkHttpClient();

	public void updateSDSEntry(String sdsURL)
	{

//        String credentials = Credentials.basic(username, password);


		String url = "";
		url = sdsURL + "/api/v1/Markets/UpdateService?ServiceURL=" + appProperties.getDomain_name();


//        System.out.println("Ping URL" + url);


		Request request = new Request.Builder()
				.url(url)
				.build();


		client.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

//				System.out.println("SDS Entry Updated Failed");

			}

			@Override
			public void onResponse(Call call, okhttp3.Response response) throws IOException {

//				System.out.println("SDS Entry Updated Code : " + response.code());

			}
		});
	}



}
