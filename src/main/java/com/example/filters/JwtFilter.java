package com.example.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Brian Ploeckelman on 1/30/2017
 */
public class JwtFilter extends GenericFilterBean {

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
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;

        // Validate Authorization header
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ServletException("Missing or invalid Authorization header.");
        }

        // Strip 'Bearer ' prefix off of web token
        final String token = authHeader.substring(7);

        // Validate token and parse claims
        try {
            final JwtParser jwtParser = Jwts.parser().setSigningKey("secret");
            final Claims claims = jwtParser.parseClaimsJws(token).getBody();
            request.setAttribute("claims", claims);
        }
        catch (final SignatureException e) {
            throw new ServletException("Invalid token");
        }

        // Pass request and response to next filter in chain
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
