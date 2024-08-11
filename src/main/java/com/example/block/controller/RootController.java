package com.example.block.controller;

import com.example.block.ApiResponse;
import com.google.protobuf.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/health")
    public String healthCheck(){
        return "hi";
    }
}
