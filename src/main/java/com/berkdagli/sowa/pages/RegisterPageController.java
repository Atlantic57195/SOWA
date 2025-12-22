package com.berkdagli.sowa.pages;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterPageController {
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }
}
