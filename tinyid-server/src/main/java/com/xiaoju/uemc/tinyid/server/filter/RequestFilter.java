package com.xiaoju.uemc.tinyid.server.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @Author du_imba
 */
@Component
@ServletComponentScan
@WebFilter(urlPatterns = "/*", filterName = "requestFilter")
public class RequestFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String params = "";
        Map<String, String[]> paramsMap = request.getParameterMap();
        if (paramsMap != null && !paramsMap.isEmpty()) {
            for (Map.Entry<String, String[]> entry : paramsMap.entrySet()) {
                params += entry.getKey() + ":" + StringUtils.arrayToDelimitedString(entry.getValue(), ",") + ";";
            }
        }
        long start = System.currentTimeMillis();
        try {
            filterChain.doFilter(request, response);
        } catch (Throwable e) {
            throw e;
        } finally {
            long cost = System.currentTimeMillis() - start;
            logger.info("request filter path={}, cost={}, params={}", request.getServletPath(), cost, params);
        }
    }

    @Override
    public void destroy() {

    }
}
