//package org.nearbyshops.Experimental.Config;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.converter.json.GsonHttpMessageConverter;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//import java.util.List;
//
//
//@Configuration
//@EnableSwagger2
//public class ApplicationConfig implements WebMvcConfigurer {
//
//
//
//    Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
//
//
//
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.add(customGsonHttpMessageConverter());
//    }
//
//
//
//
//    private GsonHttpMessageConverter customGsonHttpMessageConverter() {
//
////        logger.info("GSON Message Converter : ");
//
//        Gson gson = new GsonBuilder()
//                .excludeFieldsWithoutExposeAnnotation()
//                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
//                .create();
//
//
//        GsonHttpMessageConverter gsonMessageConverter = new GsonHttpMessageConverter();
//        gsonMessageConverter.setGson(gson);
//
//        return gsonMessageConverter;
//    }
//
//}
