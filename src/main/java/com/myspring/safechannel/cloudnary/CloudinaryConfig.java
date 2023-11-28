package com.myspring.safechannel.cloudnary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class CloudinaryConfig {
	final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${cloudinary.properties.cloudname}")
    private String cloudName;

    @Value("${cloudinary.properties.apikey}")
    private String apiKey;

    @Value("${cloudinary.properties.apisecret}")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary() {
        log.info("Cloud Name: {}", cloudName);
        log.info("API Key: {}", apiKey);
        log.info("API Secret: {}", apiSecret);
        
        
     return  new   Cloudinary(ObjectUtils.asMap(
        		"cloud_name", cloudName,
        		"api_key", apiKey,
        		"api_secret", apiSecret,
        		"secure", true));
        
       
        

//        return new Cloudinary(String.format("cloudinary://api_key:%s@api_secret:%s@cloud_name:%s", apiKey, apiSecret, cloudName));
    }

}
