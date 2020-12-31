package org.nearbyshops.WebControllers;


import org.nearbyshops.AppProperties;
import org.nearbyshops.Constants;
import org.nearbyshops.DAOs.ShopDAO;
import org.nearbyshops.Model.ModelEndpoint.ShopEndPoint;
import org.nearbyshops.Model.ModelRoles.User;
import org.nearbyshops.Model.Shop;
import org.nearbyshops.Utility.UserAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;




@Controller
public class ShopController {



	@Autowired
	private HttpServletRequest request;


	@Autowired
	private HttpSession session;

	@Autowired
	ShopDAO shopDAO;


	@Autowired
	private AppProperties appProperties;


	@Autowired
	UserAuthentication userAuthentication;



	Logger logger = LoggerFactory.getLogger(ShopController.class);


	private ShopEndPoint sep;
	private Shop shop;


	@RequestMapping("/shops")
	public ModelAndView showShops() {

//		logger.info(session.getAttribute("username").toString() + " | " + session.getAttribute("token").toString());

		ModelAndView model = new ModelAndView("page");
		if (session.getAttribute("token") != null) {

		    ShopEndPoint endPoint = shopDAO.getShopsListQuerySimple(
                    null,
                    null,null,
                    null,null,
                    null, null,
                    100,0, false,false
            );

			model.addObject("userClickShops", true);
			model.addObject("shopList", endPoint.getResults());

		} else {
			return new ModelAndView("redirect:/home?error=Session Expired");
		}


		return model;
	}




	@RequestMapping(value = "/editShop/{id}")
	public ModelAndView edit(@PathVariable int id) {


		User user = userAuthentication.isUserAllowed(
				session.getAttribute("username").toString(),
				session.getAttribute("token").toString(),
				Arrays.asList(Constants.ROLE_STAFF));


		if(user==null)
		{

			logger.info("Authentication failed !");
			return null;
		}



		ModelAndView model = new ModelAndView("page");


		if (session.getAttribute("token") != null) {

            Shop shop = shopDAO.getShopDetails(id,null,null);

//            String imageURL = "http://localhost:" + request.getLocalPort() +"/api/v1/Shop/Image";

			String imageURL = appProperties.getDomain_name() + "/api/v1/Shop/Image";

			model.addObject("userClickEditShops", true);
			model.addObject("imgURL", imageURL);
			model.addObject("Shop", shop);


		} else {
			return new ModelAndView("redirect:/home?error=Session Expired");
		}
		return model;
	}






	// Image MEthods
	private static final java.nio.file.Path BASE_DIR = Paths.get("./data/images/Shop");
	private static final double MAX_IMAGE_SIZE_MB = 2;




	@RequestMapping(value = "/updateShop", method = RequestMethod.POST)
	public ModelAndView editsave(@ModelAttribute("Shop") Shop shop, @RequestParam("file") MultipartFile file) {


//		logger.info(session.getAttribute("username").toString() + " | " + session.getAttribute("token").toString());


		User user = userAuthentication.isUserAllowed(
				session.getAttribute("username").toString(),
				session.getAttribute("token").toString(),
				Arrays.asList(Constants.ROLE_STAFF));


		if(user==null)
		{

			logger.info("Authentication failed !");
			return null;
		}



		ModelAndView model = new ModelAndView("success");

		if (!file.isEmpty()) {

			try {


				if(shop.getLogoImagePath()!=null)
				{
					Files.deleteIfExists(BASE_DIR.resolve(shop.getLogoImagePath()));
					Files.deleteIfExists(BASE_DIR.resolve("three_hundred_" + shop.getLogoImagePath() + ".jpg"));
					Files.deleteIfExists(BASE_DIR.resolve("five_hundred_" + shop.getLogoImagePath() + ".jpg"));
				}


				// Get the file and save it somewhere
				byte[] bytes = file.getBytes();



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
				long filesize = Files.copy(file.getInputStream(), BASE_DIR.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

				
				if(filesize > MAX_IMAGE_SIZE_MB * 1048 * 1024)
				{
					// delete file if it exceeds the file size limit
					Files.deleteIfExists(BASE_DIR.resolve(fileName));
				}



				shop.setLogoImagePath(fileName);




			} catch (IOException e) {
				e.printStackTrace();
			}

		}



		int rowCount = shopDAO.updateShopByAdmin(shop);

		if (rowCount==1) {
			model.addObject("typeSucess", true);
			model.addObject("message", "Shop Updated Successfully");
			model.addObject("url", "/shops");
		} else {
			model.addObject("typeSucess", false);
			model.addObject("message", "Something went Wrong");
			model.addObject("url", "/editShop/" + shop.getShopID());
		}
		return model;
	}


}
