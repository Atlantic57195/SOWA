package com.berkdagli.sowa.pages;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginPageController {
    @GetMapping( "/login")
    public String loginPage() {
        return "login";
    }
}
