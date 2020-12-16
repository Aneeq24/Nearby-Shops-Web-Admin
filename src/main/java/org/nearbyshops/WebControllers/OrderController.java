package org.nearbyshops.WebControllers;

import com.google.gson.Gson;
import org.json.JSONObject;
import org.nearbyshops.DAOs.DAOOrders.OrderService;
import org.nearbyshops.Model.ModelEndpoint.OrderEndPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class OrderController {

	@Autowired
	private HttpSession session;

	private OrderEndPoint oep;

	@Autowired
	OrderService orderService;



	@RequestMapping("/orders")
	public ModelAndView showShops() {

		ModelAndView model = new ModelAndView("page");
		if (session.getAttribute("token") != null) {

			oep = orderService.getOrdersListForEndUser(
					null,null,null,null,null,
					null,null,
					null,null,
					100,0,
					false,false
			);


			model.addObject("userClickOrders", true);
			model.addObject("orderList", oep.getResults());
		} else {
			return new ModelAndView("redirect:/home?error=Session Expired");
		}
		return model;
	}

}
