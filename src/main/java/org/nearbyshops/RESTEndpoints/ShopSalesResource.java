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
import org.nearbyshops.DAOs.DAOSalesReport;
import org.nearbyshops.DAOs.ItemCategoryDAO;
import org.nearbyshops.DAOs.ItemDAO;
import org.nearbyshops.DAOs.ItemDAOJoinOuter;
import org.nearbyshops.Model.Image;
import org.nearbyshops.Model.Item;
import org.nearbyshops.Model.ItemCategory;
import org.nearbyshops.Model.ModelEndpoint.ItemCategoryEndPoint;
import org.nearbyshops.Model.ModelEndpoint.ItemEndPoint;
import org.nearbyshops.Model.ModelEndpoint.ItemImageEndPoint;
import org.nearbyshops.Model.ModelEndpoint.ShopEndPoint;
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
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/api/v1/ShopSales")
public class ShopSalesResource {



	@Autowired
	private HttpServletRequest request;

	@Autowired
	private UserAuthentication userAuthentication;

	@Autowired
	private AppProperties appProperties;


	@Autowired
	private DAOSalesReport daoSalesReport;



	@GetMapping
	public ResponseEntity<?> getItems(
			@RequestParam(value = "StartDate",required = false) Date startDate,
			@RequestParam(value = "EndDate",required = false) Date endDate,
			@RequestParam(value = "SearchString",required = false)String searchString,
			@RequestParam(value = "SortBy",required = false) String sortBy,
			@RequestParam(value = "Limit",defaultValue = "0")int limit,
			@RequestParam(value = "Offset",defaultValue = "0")int offset,
			@RequestParam(value = "GetRowCount",defaultValue = "false")boolean getRowCount,
			@RequestParam(value = "MetadataOnly",defaultValue = "false")boolean getOnlyMetaData)
	{



		if(limit >= appProperties.getMax_limit())
		{
			limit = appProperties.getMax_limit();
		}

		

		 ShopEndPoint endPoint  = daoSalesReport.getShopSales(
											startDate,
				 							endDate,
											searchString,
											sortBy,limit,offset,
											getRowCount,getOnlyMetaData
									);




		endPoint.setLimit(limit);
		endPoint.setOffset(offset);
		endPoint.setMax_limit(appProperties.getMax_limit());



		//Marker
		return ResponseEntity.status(HttpStatus.OK)
                .body(endPoint);
	}



}
