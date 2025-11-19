package com.example.signusbackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import io.swagger.v3.oas.annotations.Hidden;

@RestController
public class SwaggerController {
    @Hidden
	@GetMapping("/")
    public RedirectView redirectToSwagger() {
        return new RedirectView("/api/auth/login-admin");
    }
}
