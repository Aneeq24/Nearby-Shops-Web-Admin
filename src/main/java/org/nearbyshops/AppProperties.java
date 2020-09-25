package org.nearbyshops;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;




@Component
@ConfigurationProperties("app")
public class AppProperties {

    String domain_name;

    String admin_email;
    String admin_password;


    String smtp_server_url;
    int smtp_port;
    String smtp_username;
    String smtp_password;

//    String email_sender_name;
//    String email_address_for_sender;


    String market_id_for_fcm;
    String fcm_configuration_file_path;
    String fcm_database_url;

    String msg91_apikey;

    String razorpay_key_id;
    String razorpay_key_secret;

    List<String> trusted_market_aggregators;

    int token_duration_minutes;
    int email_verification_code_expiry_minutes;
    int phone_otp_expiry_minutes;
    int password_reset_code_expiry_minutes;

    int max_limit;



    // getter and Setter methods


    public String getSmtp_server_url() {
        return smtp_server_url;
    }

    public void setSmtp_server_url(String smtp_server_url) {
        this.smtp_server_url = smtp_server_url;
    }

    public int getSmtp_port() {
        return smtp_port;
    }

    public void setSmtp_port(int smtp_port) {
        this.smtp_port = smtp_port;
    }

    public String getSmtp_username() {
        return smtp_username;
    }

    public void setSmtp_username(String smtp_username) {
        this.smtp_username = smtp_username;
    }

    public String getSmtp_password() {
        return smtp_password;
    }

    public void setSmtp_password(String smtp_password) {
        this.smtp_password = smtp_password;
    }


    public String getMarket_id_for_fcm() {
        return market_id_for_fcm;
    }

    public void setMarket_id_for_fcm(String market_id_for_fcm) {
        this.market_id_for_fcm = market_id_for_fcm;
    }

    public String getDomain_name() {
        return domain_name;
    }

    public void setDomain_name(String domain_name) {
        this.domain_name = domain_name;
    }

    public String getFcm_configuration_file_path() {
        return fcm_configuration_file_path;
    }

    public void setFcm_configuration_file_path(String fcm_configuration_file_path) {
        this.fcm_configuration_file_path = fcm_configuration_file_path;
    }

    public String getFcm_database_url() {
        return fcm_database_url;
    }

    public void setFcm_database_url(String fcm_database_url) {
        this.fcm_database_url = fcm_database_url;
    }

    public String getMsg91_apikey() {
        return msg91_apikey;
    }

    public void setMsg91_apikey(String msg91_apikey) {
        this.msg91_apikey = msg91_apikey;
    }

    public String getAdmin_email() {
        return admin_email;
    }

    public void setAdmin_email(String admin_email) {
        this.admin_email = admin_email;
    }

    public String getAdmin_password() {
        return admin_password;
    }

    public void setAdmin_password(String admin_password) {
        this.admin_password = admin_password;
    }

    public String getRazorpay_key_id() {
        return razorpay_key_id;
    }

    public void setRazorpay_key_id(String razorpay_key_id) {
        this.razorpay_key_id = razorpay_key_id;
    }

    public String getRazorpay_key_secret() {
        return razorpay_key_secret;
    }

    public void setRazorpay_key_secret(String razorpay_key_secret) {
        this.razorpay_key_secret = razorpay_key_secret;
    }

    public List<String> getTrusted_market_aggregators() {
        return trusted_market_aggregators;
    }

    public void setTrusted_market_aggregators(List<String> trusted_market_aggregators) {
        this.trusted_market_aggregators = trusted_market_aggregators;
    }

    public int getToken_duration_minutes() {
        return token_duration_minutes;
    }

    public void setToken_duration_minutes(int token_duration_minutes) {
        this.token_duration_minutes = token_duration_minutes;
    }

    public int getEmail_verification_code_expiry_minutes() {
        return email_verification_code_expiry_minutes;
    }

    public void setEmail_verification_code_expiry_minutes(int email_verification_code_expiry_minutes) {
        this.email_verification_code_expiry_minutes = email_verification_code_expiry_minutes;
    }

    public int getPhone_otp_expiry_minutes() {
        return phone_otp_expiry_minutes;
    }

    public void setPhone_otp_expiry_minutes(int phone_otp_expiry_minutes) {
        this.phone_otp_expiry_minutes = phone_otp_expiry_minutes;
    }

    public int getPassword_reset_code_expiry_minutes() {
        return password_reset_code_expiry_minutes;
    }

    public void setPassword_reset_code_expiry_minutes(int password_reset_code_expiry_minutes) {
        this.password_reset_code_expiry_minutes = password_reset_code_expiry_minutes;
    }

    public int getMax_limit() {
        return max_limit;
    }

    public void setMax_limit(int max_limit) {
        this.max_limit = max_limit;
    }
}
