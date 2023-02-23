package com.guardjo.simpleboard.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Test APIs")
public class ExternalController {
    @GetMapping("/test")
    @Operation(summary = "test api")
    public String test() {
        return "Test Successes";
    }
}
