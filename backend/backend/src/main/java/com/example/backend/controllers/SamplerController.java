package com.example.backend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class SamplerController {
    @GetMapping("/title")
    public String getTitle() {
        System.out.println("debug");
        return "<title>Hello from Back-end</title>";
    }
}
