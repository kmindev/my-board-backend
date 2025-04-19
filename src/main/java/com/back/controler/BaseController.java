package com.back.controler;


import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;

public class BaseController {

    protected void requestLog(Logger logger, HttpServletRequest request) {
        logger.info("Request {} {} ", request.getMethod(), getFullUri(request));
    }

    protected <T> void requestLog(Logger logger, HttpServletRequest request, T body) {
        logger.info("Request {} {}: {}", request.getMethod(), getFullUri(request), body);
    }

    protected <T> void responseLog(Logger logger, HttpServletRequest request, T body) {
        logger.info("Response {} {}: {}", request.getMethod(), getFullUri(request), body);
    }

    protected void responseLog(Logger logger, HttpServletRequest request) {
        logger.info("Response {} {}", request.getMethod(), getFullUri(request));
    }

    private String getFullUri(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String query = request.getQueryString();
        return query != null ? uri + "?" + query : uri;
    }

}
