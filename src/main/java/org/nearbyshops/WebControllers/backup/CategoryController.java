//package org.nearbyshops.WebControllers;
//
//import javax.servlet.http.HttpSession;
//
//import org.nearbyshops.Model.ItemCategory;
//import org.nearbyshops.Model.ModelEndpoint.ItemEndPoint;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.ModelAndView;
//
//
//@Controller
//public class CategoryController {
//
//
//	@Autowired
//	private HttpSession session;
//
//
//	private ItemCategory category;
//	private ItemEndPoint iep;
//
//
//
//
//	@RequestMapping("/getCategory/{id}")
//	@ResponseBody
//	public ModelAndView getCategories(@PathVariable("id") int id) {
//
//
//		//		if (session.getAttribute("token") != null) {
////
////
////			model.addObject("userClickItems", true);
////			model.addObject("categoryID", id);
////			model.addObject("itemList", iep.getResults());
////			model.addObject("SubCategoriesList", iep.getSubcategories());
////		}
////		else {
////			return new ModelAndView("redirect:/home?error=Session Expired");
////}
//
//
//
//		return new ModelAndView("page");
//	}
//
//
//
//}
