package com.guardjo.simpleboard.controller;

import com.guardjo.simpleboard.config.SecurityConfig;
import com.guardjo.simpleboard.config.TestSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@WebMvcTest(RootController.class)
public class LoginControllerTest {
    private final MockMvc mockMvc;

    public LoginControllerTest(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @DisplayName("spring security 라이브러리로 추가된 login 페이지 출력 테스트")
    @Test
    void testLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }
}
