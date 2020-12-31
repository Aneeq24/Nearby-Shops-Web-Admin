package org.nearbyshops.WebControllers;


import org.nearbyshops.AppProperties;
import org.nearbyshops.DAOs.DAORoles.DAOUserNew;
import org.nearbyshops.Model.ModelEndpoint.UserEndpoint;
import org.nearbyshops.Model.ModelRoles.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserController {

	@Autowired
	private HttpSession session;

	@Autowired
	DAOUserNew daoUserNew;

	@Autowired
	private AppProperties appProperties;


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


	@RequestMapping(value = "/editUser/{id}")
	public ModelAndView edit(@PathVariable int id) {

		ModelAndView model = new ModelAndView("page");
		String imageURL = appProperties.getDomain_name() + "/api/v1/User/Image";

		if (session.getAttribute("token") != null) {
			user = daoUserNew.getUserDetails(id);
			model.addObject("userClickEditUser", true);
			model.addObject("imgURL", imageURL);
			model.addObject("user", user);
		} else {
			return new ModelAndView("redirect:/home?error=Session Expired");
		}
		return model;
	}


	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	public ModelAndView editSave(@ModelAttribute("user") User user) {
		ModelAndView model = new ModelAndView("success");
		int response = daoUserNew.updateUserByAdmin(user);

		if (response == 1) {
			model.addObject("typeSucess", true);
			model.addObject("message", "User Updated Successfully");
			model.addObject("url", "/users");
		} else {
			model.addObject("typeSucess", false);
			model.addObject("message", "Something went Wrong");
			model.addObject("url", "/editUser/" + user.getUserID());
		}
		return model;
	}

	@RequestMapping(value = "/delete/user/{id}")
	@ResponseBody
	public ModelAndView deleteUser( @PathVariable("id") int id) {

		ModelAndView model = new ModelAndView("success");
		try {
			int response = daoUserNew.deleteUser(id);
			model.addObject("responseCode", response);
			if (response == 1) {
				model.addObject("typeSucess", true);
				model.addObject("message", "User Deleted Successfully");
				model.addObject("url", "/users");
			} else {
				model.addObject("typeSucess", false);
				model.addObject("message", "Something went Wrong");
				model.addObject("url", "/users");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}

}
