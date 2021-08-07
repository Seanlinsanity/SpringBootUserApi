package com.seanlindev.springframework.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class IndexController {
    @GetMapping({"/", "/index"})
    public ResponseEntity getIndexPage() {
        return new ResponseEntity<String>("Hello From Spring Boot Web Server!" ,HttpStatus.OK);
    }
}
