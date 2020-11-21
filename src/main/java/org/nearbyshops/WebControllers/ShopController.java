package org.nearbyshops.WebControllers;

import com.google.gson.Gson;
import org.json.JSONObject;
import org.nearbyshops.DAOs.ShopDAO;
import org.nearbyshops.Model.ModelEndpoint.ShopEndPoint;
import org.nearbyshops.Model.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ShopController {

	@Autowired
	private HttpSession session;

	@Autowired
	ShopDAO shopDAO;


	private ShopEndPoint sep;
	private Shop shop;


	@RequestMapping("/shops")
	public ModelAndView showShops() {

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

		ModelAndView model = new ModelAndView("page");

		if (session.getAttribute("token") != null) {
//			JSONObject obj = jsonObject.getObjectWithToken(apiurl + "/Shop/GetShopDetails/" + id, "GET",
//					session.getAttribute("username").toString(), session.getAttribute("token").toString());
//			shop = gson.fromJson(obj.toString(), Shop.class);

            Shop shop = shopDAO.getShopDetails(id,null,null);

			model.addObject("userClickEditShops", true);
//			model.addObject("imgURL", apiurl + "/Shop/Image");
			model.addObject("Shop", shop);
		} else {
			return new ModelAndView("redirect:/home?error=Session Expired");
		}
		return model;
	}

//
//
//	@RequestMapping(value = "/updateShop", method = RequestMethod.POST)
//	public ModelAndView editsave(@ModelAttribute("Shop") Shop shop, @RequestParam("file") MultipartFile file) {
//		ModelAndView model = new ModelAndView("success");
//
//		if (!file.isEmpty()) {
//
//			try {
//
//				// Get the file and save it somewhere
//				byte[] bytes = file.getBytes();
//				Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
//				Files.write(path, bytes);
//				Map<String, String> headers = new HashMap<>();
//				headers.put("User-Agent",
//						"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36");
//				HttpPostMultipart multipart = new HttpPostMultipart(apiurl + "/Shop/Image", "utf-8", session.getAttribute("email").toString(), session.getAttribute("token").toString());
//				multipart.addFilePart("img", new File(UPLOADED_FOLDER + file.getOriginalFilename()));
//				// Print result
//				String response = multipart.finish();
//				JSONObject myResponse = new JSONObject(response.toString());
//				shop.setLogoImagePath(myResponse.getString("path"));
//				Files.deleteIfExists(Paths.get(UPLOADED_FOLDER + file.getOriginalFilename()));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//		}
//
//		String jsonString = gson.toJson(shop).toString();
//		int response = jsonObject.putUpdateRequest(apiurl + "/Shop/UpdateByAdmin/" + shop.getShopID(), jsonString,
//				"PUT", session.getAttribute("email").toString(), session.getAttribute("token").toString());
//		model.addObject("response", response);
//		if (response == 200) {
//			model.addObject("typeSucess", true);
//			model.addObject("message", "Shop Updated Successfully");
//			model.addObject("url", "/shops");
//		} else {
//			model.addObject("typeSucess", false);
//			model.addObject("message", "Something went Wrong");
//			model.addObject("url", "/editShop/" + shop.getShopID());
//		}
//		return model;
//	}

}
