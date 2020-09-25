package org.nearbyshops.RESTEndpoints.RESTEndpointSettings;

import org.nearbyshops.Constants;
import org.nearbyshops.DAOs.DAOSettings.MarketSettingsDAO;
import org.nearbyshops.Model.ModelRoles.User;
import org.nearbyshops.Model.ModelSettings.MarketSettings;
import org.nearbyshops.Utility.UserAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;


@RestController
@RequestMapping("/api/MarketSettings")
public class MarketSettingsResource {


	@Autowired
	private MarketSettingsDAO marketSettingsDAO;



	@Autowired
	private HttpServletRequest request;

	@Autowired
	private UserAuthentication userAuthentication;




	@GetMapping
	public ResponseEntity<Object> getSettings()
	{

		MarketSettings marketSettings = marketSettingsDAO.getSettingsInstance();

		if(marketSettings != null)
		{

			return ResponseEntity.status(HttpStatus.OK)
					.body(marketSettings);

		} else
		{

			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.build();
		}

	}




	@PutMapping
	@RolesAllowed({Constants.ROLE_ADMIN})
	public ResponseEntity<Object> updateSettings(@RequestBody MarketSettings market)
	{


		User user = userAuthentication.isUserAllowed(request, Arrays.asList(Constants.ROLE_ADMIN));

		if(user==null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.build();
		}


		market.setSettingID(1);
		int rowCount =	marketSettingsDAO.updateSettings(market);


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


}
