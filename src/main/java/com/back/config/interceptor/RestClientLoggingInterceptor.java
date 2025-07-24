package com.back.config.interceptor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

@Slf4j
public class RestClientLoggingInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        logRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        logResponse(response);
        return response;
    }

    private void logRequest(HttpRequest request, byte[] body) {
        log.debug("[Rest Client] Request URI: {}", request.getURI());
        log.debug("[Rest Client] Request Method: {}", request.getMethod());
        log.debug("[Rest Client] Request Headers: {}", request.getHeaders());
        log.debug("[Rest Client] Request body: {}", new String(body, StandardCharsets.UTF_8));
    }

    private void logResponse(ClientHttpResponse response) throws IOException {
        log.debug("[Rest Client] Response Status code: {}", response.getStatusCode());
        log.debug("[Rest Client] Response Status text: {}", response.getStatusText());
        log.debug("[Rest Client] Response Headers: {}", response.getHeaders());
        // body 는 한 번만 읽을 수 있으므로 보통 생략하거나 buffering 처리 필요
    }

}
