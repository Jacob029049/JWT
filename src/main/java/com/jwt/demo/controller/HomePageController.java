package com.jwt.demo.controller;

import com.jwt.demo.facade.SearchFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomePageController {

    @Autowired
    private SearchFacade searchFacade;

    @RequestMapping(value = "/hello/welcome",method = RequestMethod.GET)
    @ResponseBody
    public String homePage(){
        searchFacade.query();
        return "Welcome to home page.";
    }
}
