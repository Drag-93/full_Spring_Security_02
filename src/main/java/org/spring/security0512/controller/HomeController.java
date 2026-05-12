package org.spring.security0512.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class HomeController {
    @GetMapping({"","/","index"})
    public String index(){
        return "index";
    }

}
