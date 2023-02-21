package com.guardjo.simpleboard.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guardjo.simpleboard.dto.security.oauth2.kakao.KakaoAccount;
import com.guardjo.simpleboard.dto.security.oauth2.kakao.Profile;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class KakaoOAuth2UserResponseTest {
    @DisplayName("카카오로부터 전달 받은 응답 값에 대한 객체 변환 테스트")
    @Test
    void testConvertAttributeToResponse() throws JsonProcessingException {
        Map<String, Object> testAttributies = generateTestKakaoResponseData();
        KakaoOAuth2UserResponse kakaoOAuth2UserResponse = KakaoOAuth2UserResponse.from(testAttributies);

        assertThat(kakaoOAuth2UserResponse)
                .hasFieldOrPropertyWithValue("id", 123456789L)
                .hasFieldOrPropertyWithValue("connectedAt", LocalDateTime.parse("2022-04-11T01:45:28Z", DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault())))
                .hasFieldOrPropertyWithValue("kakaoAccount.profile.nickname", "홍길동")
                .hasFieldOrPropertyWithValue("kakaoAccount.profile.thumbnailImageUrl", "http://yyy.kakao.com/.../img_110x110.jpg")
                .hasFieldOrPropertyWithValue("kakaoAccount.profile.profileImageUrl", "http://yyy.kakao.com/dn/.../img_640x640.jpg")
                .hasFieldOrPropertyWithValue("kakaoAccount.profile.isDefaultImage", false)
                .hasFieldOrPropertyWithValue("kakaoAccount.name", "홍길동")
                .hasFieldOrPropertyWithValue("kakaoAccount.email", "sample@sample.com");
    }

    private Map<String, Object> generateTestKakaoResponseData() {
        String testDataFileName = "kakao-response-test.json";
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream = new ClassPathResource(testDataFileName).getInputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            bufferedReader.lines().forEach(stringBuilder::append);

            return new ObjectMapper().readValue(stringBuilder.toString(), Map.class);
        } catch (IOException e) {
            return null;
        }
    }
}