package com.jwt.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomePageController {


    @RequestMapping(value = "/hello/welcome",method = RequestMethod.GET)
    @ResponseBody
    public String homePage(){
        return "Welcome to home page.";
    }
}
