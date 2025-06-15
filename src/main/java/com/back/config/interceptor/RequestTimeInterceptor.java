package com.back.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class RequestTimeInterceptor implements HandlerInterceptor {

    private static final Long LONG_TIME_MS = 3000L;
    private static final String START_TIME_ATTRIBUTE = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        long startTime = System.currentTimeMillis();
        request.setAttribute(START_TIME_ATTRIBUTE, startTime);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        long startTime = (Long) request.getAttribute(START_TIME_ATTRIBUTE);
        long endTime = System.currentTimeMillis();
        long executeTime = endTime - startTime;

        String uri = request.getRequestURI();
        String method = request.getMethod();

        if (executeTime > LONG_TIME_MS) {
            log.warn("[{} {}] Slow Request! execute time : {}ms", method, uri, executeTime);
        } else {
            log.info("[{} {}] execute time : {}ms", method, uri, executeTime);
        }
    }

}
