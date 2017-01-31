package com.example.filters;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Brian Ploeckelman on 1/30/2017
 */
@Component
// Filter must run before JwtFilter to capture OPTIONS pre-flight request
// so that secured request don't fail before Authorization header arrives
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter extends GenericFilterBean {

    /**
     * The <code>doFilter</code> method of the Filter is called by the container
     * each time a request/response pair is passed through the chain due to a
     * client request for a resource at the end of the chain. The FilterChain
     * passed in to this method allows the Filter to pass on the request and
     * response to the next entity in the chain.
     *
     * @param servletRequest  The request to process
     * @param servletResponse The response associated with the request
     * @param filterChain     Provides access to the next filter in the chain for this
     *                        filter to pass the request and response to for further
     *                        processing
     *
     * @throws IOException if an I/O error occurs during this filter's
     *                     processing of the request
     * @throws ServletException if the processing fails for any other reason
     */
    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        // Set Access-Control headers
        final HttpServletRequest  request  = (HttpServletRequest)  servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, authorization, content-type");

        // Pass-through pre-flight OPTIONS requests
        // so Authorization header arrives at JwtFilter
        // with main request
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            // Pass request and response to next filter in chain
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

}
