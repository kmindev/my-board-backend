package com.back.config;

import com.back.config.interceptor.RestClientLoggingInterceptor;
import java.net.http.HttpClient;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient defaultRestClient() {
        return RestClient.builder()
                .requestFactory(jdkClientHttpRequestFactory())
                .requestInterceptor(new RestClientLoggingInterceptor())
                .build();
    }

    @Bean
    public ClientHttpRequestFactory jdkClientHttpRequestFactory() {
        HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(Duration.ofSeconds(5));
        return requestFactory;
    }

}
