package com.junshock.jpatest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model){
        model.addAttribute("data","hello!!!");

        // spring boot thymeleaf 가 resources:templates/ + {ViewName} + html 로 찾아서 렌더링.
        return "hello";
    }
}
