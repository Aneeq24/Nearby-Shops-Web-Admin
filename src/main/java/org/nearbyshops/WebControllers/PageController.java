package org.nearbyshops.WebControllers;

import org.nearbyshops.AppProperties;
import org.nearbyshops.DAOs.DAORoles.DAOUserNew;
import org.nearbyshops.DAOs.DAORoles.DAOUserTokens;
import org.nearbyshops.Model.ModelEndpoint.ShopEndPoint;
import org.nearbyshops.Model.ModelRoles.User;
import org.nearbyshops.Model.ModelSettings.Market;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;



@Controller
public class PageController {

	@Autowired
	private HttpSession session;

	private Market market;

	private ShopEndPoint sep;


	@Autowired
	private AppProperties appProperties;


	@Autowired
	private DAOUserNew daoUserNew;

	@Autowired
	private DAOUserTokens daoUserTokens;




	@RequestMapping({ "/", "/home", "/index" })
	public ModelAndView firstPage(@RequestParam(name = "error", required = false) String error,
			@RequestParam(name = "logout", required = false) String logout) {
		ModelAndView model = new ModelAndView("index");
		if (error != null) {
			model.addObject("message", error);
		}
		if (logout != null) {
			model.addObject("logout", "You have logged out successfully!");
		}
		return model;
	}



	@RequestMapping("/dashboard")
	public ModelAndView login() {
		ModelAndView model = new ModelAndView("page");
		if (session.getAttribute("token") != null) {
			model.addObject("username", session.getAttribute("username"));
			model.addObject("token", session.getAttribute("token"));
			model.addObject("userClickDashboard", true);
		} else {
			return new ModelAndView("redirect:/home?error=Session Expired");
		}
		return model;
	}




	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView recoverPass(@RequestParam("username") String username,
			@RequestParam("password") String password, HttpServletRequest request) {

		ModelAndView model = new ModelAndView("index");

		session = request.getSession(true);
		if (session.getAttribute("token") == null) {




			User userProfile = daoUserTokens.verifyUserSimplePassword(username,password);

			if (userProfile!=null) {

				// user exists

				User userFetched = daoUserNew.checkTokenAndGetProfile(username);


				session.setAttribute("username", username);
				session.setAttribute("token", userFetched.getToken());
				model.addObject("name", userFetched.getName());

				return new ModelAndView("redirect:/sales");

			} else {

				return new ModelAndView("redirect:/home?error=Username and Password is invalid!");
			}



		} else {
			return new ModelAndView("redirect:/sales");
		}
	}



	@RequestMapping("/logout")
	public ModelAndView logout(HttpServletRequest request) {
		session = request.getSession(false);
		session.removeAttribute("username");
		session.removeAttribute("email");
		session.removeAttribute("token");
		session.getMaxInactiveInterval();
		return new ModelAndView("redirect:/home?logout");
	}




	@RequestMapping("/underConstruction")
	public ModelAndView underConstruction() {
		ModelAndView model = new ModelAndView("page");
		model.addObject("userPageUnderConstruction", true);
		return model;
	}

}
