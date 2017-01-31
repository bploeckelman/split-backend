package com.example;

import com.example.filters.JwtFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendApplication {

    /**
     * Register the JWTFilter and set its associated URL patterns
     *
     * @return the FilterRegistrationBean that Spring uses to configure the JWTFilter bean
     */
    @Bean
    public FilterRegistrationBean jwtFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new JwtFilter());
        registrationBean.addUrlPatterns("/api/secure/*");
        return registrationBean;
    }

    /**
     * Entry point
     *
     * @param args program arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}
