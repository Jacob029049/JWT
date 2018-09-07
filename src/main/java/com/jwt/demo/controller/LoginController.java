package com.jwt.demo.controller;


import com.jwt.demo.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @Value("${com.jwt.username}")
    private  String JWT_USER;

    @Value("${com.jwt.password}")
    private  String JWT_PASSWORD;

    @Autowired
    private JWTUtil jwtUtil;

    //login get token
    @RequestMapping("/login")
    public String Login(@RequestParam String username,@RequestParam String password){

        if (JWT_USER.equals(username) && JWT_PASSWORD.equals(password)){

            return jwtUtil.creatJwtToken();
        }
        return "NOK";
    }
    //url过滤/home/*，调用会执行filter
    @RequestMapping(value = "/home/welcome",method = RequestMethod.GET)
    @ResponseBody
    public String homePage(){
        return "Welcome to home page.";
    }


}
