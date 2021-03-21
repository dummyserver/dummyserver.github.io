package com.github.ahenteti.dummyserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DummyServerMvcController {

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }
}
