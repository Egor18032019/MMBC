package com.mmbc.demo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class MainFrontController {

    @RequestMapping({"/"})
    public String loadUI() {

        return "forward:/form.html";
    }

}
