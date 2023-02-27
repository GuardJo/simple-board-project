package com.guardjo.simpleboard.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.lang.Nullable;
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

    /**
     * openapi-javadoc 라이브러리를 통해 javadoc 주석을 swagger 문서화
     * @param 테스트용 parameter (nullable)
     * @return 테스트용
     */
    @GetMapping("/javadoc-test")
    public String test2(@Nullable String param) {
        return "Openapi javadoc test Successes " + param ;
    }
}
