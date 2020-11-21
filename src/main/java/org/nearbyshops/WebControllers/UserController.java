package org.nearbyshops.WebControllers;


import org.nearbyshops.DAOs.DAORoles.DAOUserNew;
import org.nearbyshops.Model.ModelEndpoint.UserEndpoint;
import org.nearbyshops.Model.ModelRoles.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserController {

	@Autowired
	private HttpSession session;

	@Autowired
	DAOUserNew daoUserNew;


	private UserEndpoint uep;
	private User user;
	private List<User> userList;


	@RequestMapping("/users")
	public ModelAndView showUsers() {

		ModelAndView model = new ModelAndView("page");
		if (session.getAttribute("token") != null) {


			uep = daoUserNew.getUsers(
					null,
					null, null,
					null,
					null,
					100,0,
					false,false
			);


			model.addObject("userClickUsers", true);
			model.addObject("userList", uep.getResults());
		} else {
			return new ModelAndView("redirect:/home?error=Session Expired");
		}
		return model;
	}



//
//	@RequestMapping(value = "/editUser/{id}")
//	public ModelAndView edit(@PathVariable int id) {
//
//		ModelAndView model = new ModelAndView("page");
//
//		if (session.getAttribute("token") != null) {
//			JSONObject obj = jsonObject.getObjectWithToken(apiurl + "/User/GetUserDetails/" + id, "GET",
//					session.getAttribute("email").toString(), session.getAttribute("token").toString());
//			user = gson.fromJson(obj.toString(), User.class);
//			model.addObject("userClickEditUser", true);
//			model.addObject("imgURL", apiurl);
//			model.addObject("user", user);
//		} else {
//			return new ModelAndView("redirect:/home?error=Session Expired");
//		}
//		return model;
//	}
//
//	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
//	public ModelAndView editsave(@ModelAttribute("user") User user) {
//		ModelAndView model = new ModelAndView("success");
//		String jsonString = gson.toJson(user).toString();
//		int response = jsonObject.putUpdateRequest(apiurl + "/User/UpdateProfileByAdmin", jsonString, "PUT",
//				session.getAttribute("email").toString(), session.getAttribute("token").toString());
//		model.addObject("response", response);
//		if (response == 200) {
//			model.addObject("typeSucess", true);
//			model.addObject("message", "User Updated Successfully");
//			model.addObject("url", "/users");
//		} else {
//			model.addObject("typeSucess", false);
//			model.addObject("message", "Something went Wrong");
//			model.addObject("url", "/editUser/" + user.getUserID());
//		}
//		return model;
//	}
//
//	@RequestMapping(value = "/newUser", method = RequestMethod.GET)
//	public ModelAndView setupForm() {
//		ModelAndView model = new ModelAndView("page");
//		User user = new User();
//		model.addObject("userClickAddUser", true);
//		model.addObject("user", user);
//		return model;
//	}
//
//	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
//	public ModelAndView addShop(@ModelAttribute("Shop") Shop shop) {
//		ModelAndView model = new ModelAndView("success");
//		String jsonString = gson.toJson(shop).toString();
//		int response = jsonObject.putUpdateRequest(apiurl + "Shop/UpdateByAdmin/" + shop.getShopID(), jsonString,
//				"POST", session.getAttribute("email").toString(), session.getAttribute("token").toString());
//		model.addObject("response", response);
//		if (response == 201) {
//			model.addObject("typeSucess", true);
//			model.addObject("message", "Record Inserted Successfully");
//			model.addObject("url", "/users");
//		} else {
//			model.addObject("typeSucess", false);
//			model.addObject("message", "Something went Wrong");
//			model.addObject("url", "/users");
//		}
//		return model;
//	}

}
