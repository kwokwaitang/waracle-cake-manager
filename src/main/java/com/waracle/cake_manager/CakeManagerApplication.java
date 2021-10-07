package com.waracle.cake_manager;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class CakeManagerApplication {

    /**
     * Timeout in 30 seconds
     */
    private static final int TIMEOUT = 30 * 1000;

    public static void main(String[] args) {
        SpringApplication.run(CakeManagerApplication.class, args);
    }

    /**
     * Set up the password encoder to use BCRYPT (for making sure the password isn't plain text in the database)
     *
     * @return The password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Set up an HTTP Client
     *
     * @return An HTTP client
     */
    @Bean
    public HttpClient httpClient() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(TIMEOUT)
                .build();

        return HttpClientBuilder.create().
                setDefaultRequestConfig(requestConfig)
                .build();
    }

    /**
     * Set up a model mapper for object mapping i.e. copy fields from source to destination object
     *
     * @return The object mapper
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                // To allow ModelMapper to compare private fields in the mapping classes (objects)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);

        return modelMapper;
    }
}
