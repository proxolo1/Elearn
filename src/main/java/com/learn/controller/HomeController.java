package com.learn.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomeController {
    @GetMapping("/*")
    public ModelAndView Home(){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("./show.html");
        return modelAndView;
    }

}
