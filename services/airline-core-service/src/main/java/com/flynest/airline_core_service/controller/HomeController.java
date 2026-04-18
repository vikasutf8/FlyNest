package com.flynest.airline_core_service.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/home")
public class HomeController {

    @GetMapping("")
    public String HomeController(){
//        ApiResponse response = new ApiResponse();
//        response.setMessage("Welcome to Location Service API");
//        return response.getMessage();
        return  "this is home controller";
    }
}
