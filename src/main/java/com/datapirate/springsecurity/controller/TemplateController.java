package com.datapirate.springsecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Akash on 20-Jun-20
 */

@Controller
@RequestMapping("/")
public class TemplateController {

    @GetMapping("login")
    public String getLoginView() {
        return "/login";
    }

    @GetMapping("courses")
    public String getCourses() {
        return "/courses";
    }

}
