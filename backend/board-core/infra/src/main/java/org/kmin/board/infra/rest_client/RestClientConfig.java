package org.kmin.board.infra.rest_client;

import java.net.http.HttpClient;
import java.time.Duration;
import org.kmin.board.infra.MyBoardConfig;
import org.kmin.board.infra.rest_client.interceptor.RestClientLoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

public class RestClientConfig implements MyBoardConfig {

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
