package com.sleepypoem.commerceapp.config.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Enumeration;

@Slf4j
public class MyRequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Enumeration<String> headerNames = request.getHeaderNames();
        log.info("Request: {} {} {}", request.getMethod(), request.getRequestURI(), request.getProtocol());
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            log.trace("Header: {} = {}", headerName, headerValue);
        }

        return true;
    }

    // Implementa los métodos restantes de la interfaz HandlerInterceptor...
}
