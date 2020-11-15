package org.nearbyshops.Utility;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.nearbyshops.AppProperties;
import org.nearbyshops.Constants;
import org.nearbyshops.DAOs.DAOSettings.MarketSettingsDAO;
import org.nearbyshops.DAOs.DAOSettings.ServiceConfigurationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.Logger;



/**
 * Created by sumeet on 15/8/17.
 */


@Component
public class SendSMS {


    @Autowired
    MarketSettingsDAO marketSettingsDAO;

    @Autowired
    AppProperties appProperties;


    @Autowired
    ServiceConfigurationDAO serviceConfigurationDAO;


    Logger logger = Logger.getLogger(SendSMS.class.getName());



    public void sendOTP(String otp, String phone)
    {
//        GsonBuilder gsonBuilder = new GsonBuilder();
//        Gson gson = gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
//        String json = gson.toJson(firebaseNotification);

        String urlOTP = "https://control.msg91.com/api/sendhttp.php?authkey=" + appProperties.getMsg91_apikey() +
                "&mobiles=" + phone +
                "&message=Your one time password (OTP) for " + serviceConfigurationDAO.getMarketConfiguration().getServiceName() + " is " + otp +
                "&sender=" + appProperties.getSender_id_for_sms() +
                "&route=4&country=0";

//        + GlobalConstants.default_country_code_value




//        String urlOTP = "https://control.msg91.com/api/sendotp.php?authkey=" +
//                GlobalConstants.MSG91_SMS_SERVICE_API_KEY +
//                "&mobile=" + GlobalConstants.default_country_code_value +
//                phone +
//                "&message=Your one time password (OTP) for " + GlobalConstants.service_name_for_sms_value + " is " +
//                otp +
//                "&sender=" + GlobalConstants.sender_id_for_sms_value  + "&otp=" +
//                otp;




        final Request request = new Request.Builder()
                .url(urlOTP)
                .get()
                .build();



//        logger.info(json + "\n");


        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {


                logger.info("Sending notification failed !");
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {


                if(response.isSuccessful())
                {
                    logger.info(response.body().string() + "\n");
//                    FirebaseResponse firebaseResponse = gson.fromJson(response.body().string(),FirebaseResponse.class);
                }
                else
                {
                    logger.info(response.toString()+ "\n");
                }

            }
        });
    }



    public void sendSMS(String message, String phone)
    {

//        GsonBuilder gsonBuilder = new GsonBuilder();
//        Gson gson = gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
//        String json = gson.toJson(firebaseNotification);



        String urlMessage = "http://api.msg91.com/api/sendhttp.php?authkey=" +  appProperties.getMsg91_apikey()
                + "&mobiles=" + phone
                + "&message=" + message
                + "&sender=" + appProperties.getSender_id_for_sms()
                + "&route=4&country=0";

//        + GlobalConstants.default_country_code_value





//        String urlMessage = "http://api.msg91.com/api/sendhttp.php?authkey=" + GlobalConstants.MSG91_SMS_SERVICE_API_KEY
//                + "&mobiles=" + GlobalConstants.default_country_code_value + phone
//                + "&message=" + message
//                + "&sender=" + GlobalConstants.sender_id_for_sms_value
//                + "&route=4&country=" + GlobalConstants.default_country_code_value;




        final Request request = new Request.Builder()
                .url(urlMessage)
                .get()
                .build();



//        logger.info(json + "\n");


        OkHttpClient client = new OkHttpClient();



        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {


                logger.info("Sending notification failed !");
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {


                if(response.isSuccessful())
                {
                    logger.info(response.body().string() + "\n");
//                    FirebaseResponse firebaseResponse = gson.fromJson(response.body().string(),FirebaseResponse.class);
                }
                else
                {
                    logger.info(response.toString()+ "\n");
                }

            }
        });
    }


}
