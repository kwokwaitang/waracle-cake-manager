package com.waracle.cake_manager.service;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.logging.Logger;

@Service
@PropertySource("classpath:application.properties")
public class CakeDataService implements Supplier<String> {

    private static final Logger LOGGER = Logger.getGlobal();

    @Value("${cake.data.url}")
    private String cakeUrl;

    private final HttpClient httpClient;

    public CakeDataService(HttpClient httpClient) {
        this.httpClient = Objects.requireNonNull(httpClient, () -> "Unavailable HTTP client");
    }

    @Override
    public String get() {
        try {
            HttpGet request = new HttpGet(cakeUrl);
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity);
            }
        } catch (IOException exception) {
            LOGGER.warning(() -> String.format("Problem encountered when fetching cake data [%s]",
                    ExceptionUtils.getStackTrace(exception)));

            return "{}";
        }

        return "{}";
    }
}
