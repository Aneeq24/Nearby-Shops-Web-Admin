package org.nearbyshops.DAOs.DAOSettings;


import org.nearbyshops.Model.ModelSettings.MarketSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;



@Component
public class MarketSettingsDAO {


	@Autowired
	DataSource dataSource;


	private MarketSettings marketSettings;



	public MarketSettings getSettingsInstance()
	{
		if(marketSettings ==null)
		{
			marketSettings = getMarketSettingsFromTable();
		}

		return marketSettings;
	}



	public int saveSettings(MarketSettings market)
	{

		Connection connection = null;
		PreparedStatement statement = null;
		int rowIdOfInsertedRow = -1;

		String insertItemCategory = "INSERT INTO "
				+ MarketSettings.TABLE_NAME
				+ "("

//				+ MarketSettings.SMTP_SERVER_URL + ","
//				+ MarketSettings.SMTP_PORT + ","
//				+ MarketSettings.SMTP_USERNAME + ","
//				+ MarketSettings.SMTP_PASSWORD + ","

				+ MarketSettings.EMAIL_SENDER_NAME + ","
//				+ MarketSettings.EMAIL_ADDRESS_FOR_SENDER + ","

				+ MarketSettings.COD_ENABLED + ","
				+ MarketSettings.POD_ENABLED + ","
				+ MarketSettings.RAZOR_PAY_ENABLED + ","
//				+ MarketSettings.RAZOR_PAY_KEY_ID + ","
//				+ MarketSettings.RAZOR_PAY_KEY_SECRET + ","

//				+ MarketSettings.MSG_91_API_KEY + ","
				+ MarketSettings.SENDER_ID_FOR_SMS + ","
				+ MarketSettings.SERVICE_NAME_FOR_SMS + ","

				+ MarketSettings.DEFAULT_COUNTRY_CODE + ","
				+ MarketSettings.LOGIN_USING_OTP_ENABLED + ","

				+ MarketSettings.MARKET_FEE_PICKUP_FROM_SHOP + ","
				+ MarketSettings.MARKET_FEE_HOME_DELIVERY + ","
				+ MarketSettings.ADD_MARKET_FEE_TO_BILL + ","
				+ MarketSettings.USE_STANDARD_DELIVERY_FEE + ","
				+ MarketSettings.MARKET_DELIVERY_FEE_PER_ORDER + ","

				+ MarketSettings.BOOTSTRAP_MODE_ENABLED + ","
				+ MarketSettings.DEMO_MODE_ENABLED + ","
				+ MarketSettings.MIN_ACCOUNT_BALANCE_FOR_SHOP_OWNER + ","
				+ MarketSettings.UPDATED + ","

				+ MarketSettings.SETTING_ID + ""

				+ " ) VALUES (?,? ,?,?,? ,?,? ,?,? ,?,?,?,?,? ,?,?,?,? ,?)";
		
		try {
			
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(insertItemCategory,Statement.RETURN_GENERATED_KEYS);

			int i = 0;



//			statement.setString(++i, market.getSmtpServerURL());
//			statement.setString(++i, market.getSmtpPort());
//			statement.setString(++i, market.getSmtpUsername());
//			statement.setString(++i, market.getSmtpPassword());

			statement.setString(++i, market.getEmailSenderName());
//			statement.setString(++i, market.getEmailAddressForSender());

			statement.setBoolean(++i, market.isCodEnabled());
			statement.setBoolean(++i, market.isPodEnabled());
			statement.setBoolean(++i, market.isRazorPayEnabled());
//			statement.setString(++i, market.getRazorPayKeyID());
//			statement.setString(++i, market.getRazorPayKeySecret());

//			statement.setString(++i, market.getMsg91APIKey());
			statement.setString(++i, market.getSenderIDForSMS());
			statement.setString(++i, market.getServiceNameForSMS());

			statement.setObject(++i, market.getDefaultCountryCode());
			statement.setObject(++i, market.isLoginUsingOTPEnabled());

			statement.setObject(++i, market.getMarketFeePickupFromShop());
			statement.setObject(++i, market.getMarketFeeHomeDelivery());
			statement.setObject(++i, market.isAddMarketFeeToBill());
			statement.setObject(++i, market.isUseStandardDeliveryFee());
			statement.setObject(++i, market.getMarketDeliveryFeePerOrder());

			statement.setObject(++i, market.isBootstrapModeEnabled());
			statement.setObject(++i, market.isDemoModeEnabled());
			statement.setObject(++i, market.getMinAccountBalanceForShopOwner());
			statement.setTimestamp(++i,new Timestamp(System.currentTimeMillis()));


			statement.setObject(++i,1);




			rowIdOfInsertedRow = statement.executeUpdate();

			// invalidate the settings
			this.marketSettings= null;

			ResultSet rs = statement.getGeneratedKeys();

			if(rs.next())
			{
				rowIdOfInsertedRow = rs.getInt(1);
			}



			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			
			try {
			
				if(statement!=null)
				{statement.close();}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				
				if(connection!=null)
				{connection.close();}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
		return rowIdOfInsertedRow;
	}



	public int updateSettings(MarketSettings market)
	{

		String updateStatement = "UPDATE " + MarketSettings.TABLE_NAME

				+ " SET "

//				+ MarketSettings.SMTP_SERVER_URL + " = ?,"
//				+ MarketSettings.SMTP_PORT + " = ?,"
//				+ MarketSettings.SMTP_USERNAME + " = ?,"
//				+ MarketSettings.SMTP_PASSWORD + " = ?,"

				+ MarketSettings.EMAIL_SENDER_NAME + " = ?,"
//				+ MarketSettings.EMAIL_ADDRESS_FOR_SENDER + " = ?,"

				+ MarketSettings.COD_ENABLED + " = ?,"
				+ MarketSettings.POD_ENABLED + " = ?,"
				+ MarketSettings.RAZOR_PAY_ENABLED + " = ?,"
//				+ MarketSettings.RAZOR_PAY_KEY_ID + " = ?,"
//				+ MarketSettings.RAZOR_PAY_KEY_SECRET + " = ?,"

//				+ MarketSettings.MSG_91_API_KEY + " = ?,"
				+ MarketSettings.SENDER_ID_FOR_SMS + " = ?,"
				+ MarketSettings.SERVICE_NAME_FOR_SMS + " = ?,"

				+ MarketSettings.DEFAULT_COUNTRY_CODE + " = ?,"
				+ MarketSettings.LOGIN_USING_OTP_ENABLED + " = ?,"

				+ MarketSettings.MARKET_FEE_PICKUP_FROM_SHOP + " = ?,"
				+ MarketSettings.MARKET_FEE_HOME_DELIVERY + " = ?,"
				+ MarketSettings.ADD_MARKET_FEE_TO_BILL + " = ?,"
				+ MarketSettings.USE_STANDARD_DELIVERY_FEE + " = ?,"
				+ MarketSettings.MARKET_DELIVERY_FEE_PER_ORDER + " = ?,"

				+ MarketSettings.BOOTSTRAP_MODE_ENABLED + " = ?,"
				+ MarketSettings.DEMO_MODE_ENABLED + " = ?,"
				+ MarketSettings.MIN_ACCOUNT_BALANCE_FOR_SHOP_OWNER + " = ?,"
				+ MarketSettings.UPDATED + " = ?"

				+ " WHERE "
				+ MarketSettings.SETTING_ID + " = ?";


		Connection connection = null;
		PreparedStatement statement = null;
		int updatedRows = -1;
		
		try {
			
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(updateStatement);

			int i = 0;
//			statement.setString(++i, market.getSmtpServerURL());
//			statement.setString(++i, market.getSmtpPort());
//			statement.setString(++i, market.getSmtpUsername());
//			statement.setString(++i, market.getSmtpPassword());

			statement.setString(++i, market.getEmailSenderName());
//			statement.setString(++i, market.getEmailAddressForSender());

			statement.setBoolean(++i, market.isCodEnabled());
			statement.setBoolean(++i, market.isPodEnabled());
			statement.setBoolean(++i, market.isRazorPayEnabled());
//			statement.setString(++i, market.getRazorPayKeyID());
//			statement.setString(++i, market.getRazorPayKeySecret());
//
//			statement.setString(++i, market.getMsg91APIKey());
			statement.setString(++i, market.getSenderIDForSMS());
			statement.setString(++i, market.getServiceNameForSMS());

			statement.setObject(++i, market.getDefaultCountryCode());
			statement.setObject(++i, market.isLoginUsingOTPEnabled());

			statement.setObject(++i, market.getMarketFeePickupFromShop());
			statement.setObject(++i, market.getMarketFeeHomeDelivery());
			statement.setObject(++i, market.isAddMarketFeeToBill());
			statement.setObject(++i, market.isUseStandardDeliveryFee());
			statement.setObject(++i, market.getMarketDeliveryFeePerOrder());

			statement.setObject(++i, market.isBootstrapModeEnabled());
			statement.setObject(++i, market.isDemoModeEnabled());
			statement.setObject(++i, market.getMinAccountBalanceForShopOwner());
			statement.setTimestamp(++i,new Timestamp(System.currentTimeMillis()));

			statement.setObject(++i,1);


			updatedRows = statement.executeUpdate();


			// invalidate the existing settings
			marketSettings = null;


			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		
		{
			
			try {
			
				if(statement!=null)
				{statement.close();}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				
				if(connection!=null)
				{connection.close();}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return updatedRows;
		
	}



	public int deleteSettings(int settingID)
	{
		
		String deleteStatement = "DELETE FROM " + MarketSettings.TABLE_NAME
				+ " WHERE " + MarketSettings.SETTING_ID + " = ?";
		
		
		Connection connection= null;
		PreparedStatement statement = null;
		int rowsCountDeleted = 0;
		try {
			
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(deleteStatement);

			statement.setInt(1,settingID);
			rowsCountDeleted = statement.executeUpdate();


			connection.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		finally
		
		{
			
			try {
			
				if(statement!=null)
				{statement.close();}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				
				if(connection!=null)
				{connection.close();}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		
		return rowsCountDeleted;
	}
	


	public MarketSettings getMarketSettingsFromTable()
	{
		
		String query = " SELECT "
						+ MarketSettings.SETTING_ID + ","

//						+ MarketSettings.SMTP_SERVER_URL + ","
//						+ MarketSettings.SMTP_PORT + ","
//						+ MarketSettings.SMTP_USERNAME + ","
//						+ MarketSettings.SMTP_PASSWORD + ","

						+ MarketSettings.EMAIL_SENDER_NAME + ","
						+ MarketSettings.EMAIL_ADDRESS_FOR_SENDER + ","

						+ MarketSettings.COD_ENABLED + ","
						+ MarketSettings.POD_ENABLED + ","
						+ MarketSettings.RAZOR_PAY_ENABLED + ","
//						+ MarketSettings.RAZOR_PAY_KEY_ID + ","
//						+ MarketSettings.RAZOR_PAY_KEY_SECRET + ","

//						+ MarketSettings.MSG_91_API_KEY + ","
						+ MarketSettings.SENDER_ID_FOR_SMS + ","
						+ MarketSettings.SERVICE_NAME_FOR_SMS + ","

						+ MarketSettings.DEFAULT_COUNTRY_CODE + ","
						+ MarketSettings.LOGIN_USING_OTP_ENABLED + ","

						+ MarketSettings.MARKET_FEE_PICKUP_FROM_SHOP + ","
						+ MarketSettings.MARKET_FEE_HOME_DELIVERY + ","
						+ MarketSettings.ADD_MARKET_FEE_TO_BILL + ","
						+ MarketSettings.USE_STANDARD_DELIVERY_FEE + ","
						+ MarketSettings.MARKET_DELIVERY_FEE_PER_ORDER + ","

						+ MarketSettings.BOOTSTRAP_MODE_ENABLED + ","
						+ MarketSettings.DEMO_MODE_ENABLED + ","
						+ MarketSettings.MIN_ACCOUNT_BALANCE_FOR_SHOP_OWNER + ","

						+ MarketSettings.UPDATED + ","
						+ MarketSettings.CREATED + ""

						+ " FROM " + MarketSettings.TABLE_NAME
						+ " WHERE " + MarketSettings.SETTING_ID + " = " + 1;



		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;

		MarketSettings market = null;

		try {
			
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			
			while(rs.next())
			{
				market = new MarketSettings();

				market.setSettingID(rs.getInt(MarketSettings.SETTING_ID));

//				market.setSmtpServerURL(rs.getString(MarketSettings.SMTP_SERVER_URL));
//				market.setSmtpPort(rs.getString(MarketSettings.SMTP_PORT));
//				market.setSmtpUsername(rs.getString(MarketSettings.SMTP_USERNAME));
//				market.setSmtpPassword(rs.getString(MarketSettings.SMTP_PASSWORD));

				market.setEmailSenderName(rs.getString(MarketSettings.EMAIL_SENDER_NAME));
//				market.setEmailAddressForSender(rs.getString(MarketSettings.EMAIL_ADDRESS_FOR_SENDER));

				market.setCodEnabled(rs.getBoolean(MarketSettings.COD_ENABLED));
				market.setPodEnabled(rs.getBoolean(MarketSettings.POD_ENABLED));
				market.setRazorPayEnabled(rs.getBoolean(MarketSettings.RAZOR_PAY_ENABLED));
//				market.setRazorPayKeyID(rs.getString(MarketSettings.RAZOR_PAY_KEY_ID));
//				market.setRazorPayKeySecret(rs.getString(MarketSettings.RAZOR_PAY_KEY_SECRET));

//				market.setMsg91APIKey(rs.getString(MarketSettings.MSG_91_API_KEY));
				market.setSenderIDForSMS(rs.getString(MarketSettings.SENDER_ID_FOR_SMS));
				market.setServiceNameForSMS(rs.getString(MarketSettings.SERVICE_NAME_FOR_SMS));

				market.setDefaultCountryCode(rs.getInt(MarketSettings.DEFAULT_COUNTRY_CODE));
				market.setLoginUsingOTPEnabled(rs.getBoolean(MarketSettings.LOGIN_USING_OTP_ENABLED));


				market.setMarketFeePickupFromShop(rs.getFloat(MarketSettings.MARKET_FEE_PICKUP_FROM_SHOP));
				market.setMarketFeeHomeDelivery(rs.getFloat(MarketSettings.MARKET_FEE_HOME_DELIVERY));
				market.setAddMarketFeeToBill(rs.getBoolean(MarketSettings.ADD_MARKET_FEE_TO_BILL));
				market.setUseStandardDeliveryFee(rs.getBoolean(MarketSettings.USE_STANDARD_DELIVERY_FEE));
				market.setMarketDeliveryFeePerOrder(rs.getFloat(MarketSettings.MARKET_DELIVERY_FEE_PER_ORDER));

				market.setBootstrapModeEnabled(rs.getBoolean(MarketSettings.BOOTSTRAP_MODE_ENABLED));
				market.setDemoModeEnabled(rs.getBoolean(MarketSettings.DEMO_MODE_ENABLED));
				market.setMinAccountBalanceForShopOwner(rs.getFloat(MarketSettings.MIN_ACCOUNT_BALANCE_FOR_SHOP_OWNER));

				market.setUpdated(rs.getTimestamp(MarketSettings.UPDATED));
				market.setCreated(rs.getTimestamp(MarketSettings.CREATED));

			}


			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		
		{
			
			try {
					if(rs!=null)
					{rs.close();}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			try {
			
				if(statement!=null)
				{statement.close();}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				
				if(connection!=null)
				{connection.close();}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		return market;
	}




	public MarketSettings getDefaultSettings()
	{
		MarketSettings settings = new MarketSettings();

		settings.setSettingID(1);
//		settings.setEmailAddressForSender("abcde@example.org");
		settings.setEmailSenderName("Example Sender");
		settings.setCodEnabled(true);
		settings.setPodEnabled(true);
		settings.setRazorPayEnabled(false);
		settings.setSenderIDForSMS("ABCDEF");
		settings.setServiceNameForSMS("Nearby Shops");
		settings.setDefaultCountryCode(91);
		settings.setLoginUsingOTPEnabled(false);
		settings.setMarketFeePickupFromShop(5);
		settings.setMarketFeeHomeDelivery(5);
		settings.setAddMarketFeeToBill(false);
		settings.setUseStandardDeliveryFee(true);
		settings.setMarketDeliveryFeePerOrder(25);
		settings.setBootstrapModeEnabled(false);
		settings.setDemoModeEnabled(false);
		settings.setMinAccountBalanceForShopOwner(0);


		settings.setMinAccountBalanceForShopOwner(0);

		return settings;
	}

}
