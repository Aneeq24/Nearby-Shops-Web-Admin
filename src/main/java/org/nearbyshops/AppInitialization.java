package org.nearbyshops;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.nearbyshops.DAOs.DAORoles.DAOUserUtility;
import org.nearbyshops.DAOs.DAOSettings.MarketSettingsDAO;
import org.nearbyshops.DAOs.DAOSettings.ServiceConfigurationDAO;
import org.nearbyshops.DAOs.ItemCategoryDAO;
import org.nearbyshops.Model.*;
import org.nearbyshops.Model.ModelAnalytics.ItemAnalytics;
import org.nearbyshops.Model.ModelAnalytics.ShopAnalytics;
import org.nearbyshops.Model.ModelBilling.RazorPayOrder;
import org.nearbyshops.Model.ModelBilling.Transaction;
import org.nearbyshops.Model.ModelDelivery.DeliveryAddress;
import org.nearbyshops.Model.ModelImages.BannerImage;
import org.nearbyshops.Model.ModelImages.ItemImage;
import org.nearbyshops.Model.ModelImages.ShopImage;
import org.nearbyshops.Model.ModelItemSpecification.ItemSpecificationItem;
import org.nearbyshops.Model.ModelItemSpecification.ItemSpecificationName;
import org.nearbyshops.Model.ModelItemSpecification.ItemSpecificationValue;
import org.nearbyshops.Model.ModelOneSignal.OneSignalIDs;
import org.nearbyshops.Model.ModelReviewItem.FavouriteItem;
import org.nearbyshops.Model.ModelReviewItem.ItemReview;
import org.nearbyshops.Model.ModelReviewItem.ItemReviewThanks;
import org.nearbyshops.Model.ModelReviewShop.FavouriteShop;
import org.nearbyshops.Model.ModelReviewShop.ShopReview;
import org.nearbyshops.Model.ModelReviewShop.ShopReviewThanks;
import org.nearbyshops.Model.ModelRoles.*;
import org.nearbyshops.Model.ModelSettings.Market;
import org.nearbyshops.Model.ModelSettings.MarketSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;


@Component
class AppInitialization implements InitializingBean {



    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;

    @Autowired
    AppProperties appProperties;

    @Autowired
    MarketSettingsDAO marketSettingsDAO;

    @Autowired
    ServiceConfigurationDAO serviceConfigDAO;

    @Autowired
    ItemCategoryDAO itemCategoryDAO;

    @Autowired
    DAOUserUtility daoUserUtility;


    private final OkHttpClient client = new OkHttpClient();




    Logger logger = LoggerFactory.getLogger(AppInitialization.class);





    @Override
    public void afterPropertiesSet() throws Exception {


        createTables();
        upgradeTables();


        createDataDirectory();
        createImagesDirectory();
        insertRootCategory();
        createAdminUser();

        setupPing();
        setupFirebaseAdminSDK();

        loadDefaultMarketConfiguration();
        loadDefaultMarketSettings();


//        logger.info("App Properties : " + appProperties.toString());
    }







    private void loadDefaultMarketSettings()
    {
        // load default settings into table if it does not exist
        if(marketSettingsDAO.getMarketSettingsFromTable()==null)
        {
            marketSettingsDAO.saveSettings(marketSettingsDAO.getDefaultSettings());
        }
    }


    private void loadDefaultMarketConfiguration()
    {
        // load or create default market configuration
        if(serviceConfigDAO.getServiceConfigurationFromTable(null,null)==null)
        {
            serviceConfigDAO.saveService(serviceConfigDAO.getDefaultConfiguration());
        }
    }



    private void createTables()
    {

        jdbcTemplate.execute(User.createTable);
        jdbcTemplate.execute(UserTokens.createTable);
        jdbcTemplate.execute(UserMarkets.createTable);
        jdbcTemplate.execute(OneSignalIDs.createTable);
        jdbcTemplate.execute(StaffPermissions.createTablePostgres);
        jdbcTemplate.execute(EmailVerificationCode.createTablePostgres);
        jdbcTemplate.execute(PhoneVerificationCode.createTablePostgres);

        jdbcTemplate.execute(Transaction.createTablePostgres);


        jdbcTemplate.execute(ItemCategory.createTablePostgres);
        jdbcTemplate.execute(Item.createTableItemPostgres);
        jdbcTemplate.execute(Shop.createTableShopPostgres);
        jdbcTemplate.execute(ShopItem.createTableShopItemPostgres);

        jdbcTemplate.execute(ShopImage.createTablePostgres);
        jdbcTemplate.execute(BannerImage.createTable);

        jdbcTemplate.execute(ShopStaffPermissions.createTablePostgres);
        jdbcTemplate.execute(DeliveryGuyData.createTablePostgres);

        jdbcTemplate.execute(Cart.createTable);
        jdbcTemplate.execute(CartItem.createTable);
        jdbcTemplate.execute(DeliveryAddress.createTable);
//        jdbcTemplate.execute(DeliverySlot.createTable);

        jdbcTemplate.execute(Order.createTable);
        jdbcTemplate.execute(RazorPayOrder.createTable);

        jdbcTemplate.execute(OrderItem.createTable);



        // tables for shop reviews
        jdbcTemplate.execute(ShopReview.createTableShopReviewPostgres);
        jdbcTemplate.execute(FavouriteShop.createTableFavouriteBookPostgres);
        jdbcTemplate.execute(ShopReviewThanks.createTableShopReviewThanksPostgres);

        // tables for Item reviews
        jdbcTemplate.execute(ItemReview.createTableItemReviewPostgres);
        jdbcTemplate.execute(FavouriteItem.createTableFavouriteItemPostgres);
        jdbcTemplate.execute(ItemReviewThanks.createTableItemReviewThanksPostgres);



        jdbcTemplate.execute(ItemImage.createTableItemImagesPostgres);


        jdbcTemplate.execute(ItemSpecificationName.createTableItemSpecNamePostgres);
        jdbcTemplate.execute(ItemSpecificationValue.createTableItemSpecificationValuePostgres);
        jdbcTemplate.execute(ItemSpecificationItem.createTableItemSpecificationItemPostgres);


        jdbcTemplate.execute(Market.createTablePostgres);
        jdbcTemplate.execute(MarketSettings.createTablePostgres);


        // tables for storing analytics data
        jdbcTemplate.execute(ItemAnalytics.createTable);
        jdbcTemplate.execute(ShopAnalytics.createTable);


        logger.info("Tables Created !");
    }



    private void upgradeTables()
    {

        Connection connection = null;
        Statement statement = null;

        try {

            connection = dataSource.getConnection();

            statement = connection.createStatement();

            statement.executeUpdate(Order.addColumns);
            statement.executeUpdate(Order.addColumnsPaymentMode);
            statement.executeUpdate(Order.dropNull);

            statement.executeUpdate(User.upgradeTableSchema);
            statement.executeUpdate(User.removeNotNullforPassword);
            statement.executeUpdate(ShopItem.addColumns);

            statement.executeUpdate(OrderItem.addColumns);
            statement.executeUpdate(Item.dropColumn);

//            statement.executeUpdate(ItemCategory.dropColumns);

            statement.executeUpdate(Shop.addColumnsMinOrder);


            logger.info("Tables Upgrade Complete ... !");


        } catch (SQLException e) {

            e.printStackTrace();
        }
        finally{


            // close the connection and statement accountApproved

            if(statement !=null)
            {

                try {
                    statement.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }


            if(connection!=null)
            {
                try {
                    connection.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }



    private void createDataDirectory()
    {
        // create directory images
        final java.nio.file.Path BASE_DIR = Paths.get("./data");

        File theDir = new File(BASE_DIR.toString());

        // if the directory does not exist, create it
        if (!theDir.exists()) {

            logger.info("Creating directory: " + BASE_DIR.toString());

            boolean result = false;

            try{
                theDir.mkdir();
                result = true;
            }
            catch(Exception se){
                //handle it
            }

            if(result) {
                logger.info("DIR created");
            }
        }

    }


    private void createImagesDirectory()
    {
        // create directory images
        final java.nio.file.Path BASE_DIR = Paths.get("./data/images");

        File theDir = new File(BASE_DIR.toString());

        // if the directory does not exist, create it
        if (!theDir.exists()) {

            logger.info("Creating directory: " + BASE_DIR.toString());

            boolean result = false;

            try{
                theDir.mkdir();
                result = true;
            }
            catch(Exception se){
                //handle it
            }

            if(result) {
                logger.info("DIR created");
            }
        }

    }



    private void insertRootCategory()
    {

        // Insert the root category whose ID is 1
        String insertItemCategory = "";


        // The root ItemCategory has id 1. If the root category does not exist then insert it.
        if(itemCategoryDAO.checkRoot(1) == null)
        {

            insertItemCategory = "INSERT INTO "
                    + ItemCategory.TABLE_NAME
                    + "("
                    + ItemCategory.ITEM_CATEGORY_ID + ","
                    + ItemCategory.ITEM_CATEGORY_NAME + ","
                    + ItemCategory.PARENT_CATEGORY_ID + ","
                    + ItemCategory.ITEM_CATEGORY_DESCRIPTION + ""
                    + " ) VALUES( "
                    + "" + "1"	+ ","
                    + "'" + "ROOT"	+ "',"
                    + "" + "NULL" + ","
                    + "'" + "This is the root Category. Do not modify it." + "'"
                    + ")";


            jdbcTemplate.execute(insertItemCategory);
        }

    }



    private void createAdminUser()
    {

        // Create admin account with given username and password if it does not exit | or update in case admin account exist

        User admin = new User();
        admin.setEmail(appProperties.getAdmin_email());
        admin.setRole(Constants.ROLE_ADMIN_CODE);
        admin.setPassword(appProperties.getAdmin_password());

//        logger.info("Admin Username : " + appProperties.getAdmin_email() + " | " + " Admin Password : " + appProperties.getAdmin_password());

        int userID = daoUserUtility.getUserID(appProperties.getAdmin_email());

        if(userID==-1)
        {
            // user does not exist
            daoUserUtility.createAdminUsingEmail(admin,true);
        }
        else
        {
            // user exists so upgrade the user role to admin
            daoUserUtility.updateUserRole(userID);
        }

    }



    private void setupFirebaseAdminSDK() {

        FileInputStream serviceAccount = null;


        try {

            serviceAccount = new FileInputStream(appProperties.getFcm_configuration_file_path());


            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(appProperties.getFcm_database_url())
                    .build();


            FirebaseApp.initializeApp(options);



        } catch (Exception e) {

            e.printStackTrace();
        }


    }





    private void setupPing()
    {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                for(String url : appProperties.getTrusted_market_aggregators())
                {
                    // for each url send a ping
                    sendPing(url);
                }
            }
        };


        Timer timer = new Timer();

        // send ping to the sds server at every 3 hours
        timer.scheduleAtFixedRate(timerTask,0,3*60*60*1000);
    }



    void sendPing(String sdsURL)
    {

//        String credentials = Credentials.basic(username, password);

//        logger.info("Send ping URL : " + sdsURL);


        String url = "";
        url = sdsURL + "/api/v1/Markets/Ping?ServiceURL=" + appProperties.getDomain_name();


//        System.out.println("Ping URL" + url);


        Request request = new Request.Builder()
                .url(url)
                .build();



        try (okhttp3.Response response = client.newCall(request).execute()) {


//            if (!response.isSuccessful())
//            {
//            }

//            Headers responseHeaders = response.headers();
//            for (int i = 0; i < responseHeaders.size(); i++) {
//                System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//            }



            logger.info("Ping Response Code : " + response.code());


        } catch (IOException e) {
//            e.printStackTrace();
        }


    }



}
