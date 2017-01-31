package com.example.controllers;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import java.util.*;

/**
 * Created by Brian Ploeckelman on 1/30/2017
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private static class UserLogin {
        public String username;
        public String password;
    }

    private static class LoginResponse {
        public String token;
        public LoginResponse(final String token) {
            this.token = token;
        }
    }


    // Quick and dirty in-memory 'database'
    private final Map<String, List<String>> userDb = new HashMap<>();

    public UserController() {
        // Add some test users
        userDb.put("tom", Arrays.asList("user"));
        userDb.put("sally", Arrays.asList("user", "admin"));
    }

    /**
     * Validate the specified user login information and generate web token
     *
     * @param login user information for the user attempting to login
     *
     * @return a LoginResponse for the user that contains their role(s) and a signed web token
     *
     * @throws ServletException if the supplied user information is invalid
     *                          or doesn't exist
     */
    @PostMapping("login")
    public LoginResponse login(@RequestBody final UserLogin login) throws ServletException {
        if (login.username == null || !userDb.containsKey(login.username)) {
            throw new ServletException("Invalid login");
        }

        return new LoginResponse(Jwts.builder()
                .setSubject(login.username)
                .claim("roles", userDb.get(login.username))
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "secret")
                .compact()
        );
    }

    /**
     * Validate the supplied user account information and create a new user if appropriate
     *
     * @param login user information for the user that attempting to create a new account
     *
     * @return a LoginReponse for the new user that contains their role(s) and a signed web token
     *
     * @throws ServletException if the user account information is invalid
     *                          or the specified user already exists
     */
    @PostMapping("signup")
    public LoginResponse signup(@RequestBody final UserLogin login) throws ServletException {
        if (login.username == null) {
            throw new ServletException("Missing username");
        }
        if (login.password == null) {
            throw new ServletException("Missing password");
        }
        if (userDb.containsKey(login.username)) {
            throw new ServletException("User already signed up");
        }

        userDb.put(login.username, Arrays.asList("user"));

        return new LoginResponse(Jwts.builder()
                .setSubject(login.username)
                .claim("roles", userDb.get(login.username))
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "secret")
                .compact()
        );
    }

}
