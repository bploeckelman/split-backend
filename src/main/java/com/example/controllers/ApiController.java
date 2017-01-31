package com.example.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Brian Ploeckelman on 1/30/2017
 */
@RestController
@RequestMapping("/api")
public class ApiController {

//    @SuppressWarnings("unchecked")
//    @GetMapping("role/{role}")
//    public boolean login(@PathVariable final String role,
//                         final HttpServletRequest request) throws ServletException {
//        final Claims claims = (Claims) request.getAttribute("claims");
//        return ((List<String>) claims.get("roles")).contains(role);
//    }

    @GetMapping("test")
    public String anon() {
        return "successful anonymous access";
    }

    @GetMapping("secure/test")
    public String secure() {
        return "successful secure access";
    }

}
