package com.guardjo.simpleboard.response;

import com.guardjo.simpleboard.dto.security.oauth2.kakao.KakaoAccount;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * 카카오 로그인 응답 객체
 * 카카오 공식 문서 참조
 * @param id 회원번호
 * @param connectedAt 서비스에 연결 완료된 시각, UTC
 * @param kakaoAccount 카카오 계정 정보
 */
public record KakaoOAuth2UserResponse(
        Long id,
        LocalDateTime connectedAt,
        KakaoAccount kakaoAccount
) {
    public static KakaoOAuth2UserResponse of(Long id, LocalDateTime connectedAt, KakaoAccount kakaoAccount) {
        return new KakaoOAuth2UserResponse(id, connectedAt, kakaoAccount);
    }

    public static KakaoOAuth2UserResponse from(Map<String, Object> attributies) {
        return KakaoOAuth2UserResponse.of(
                Long.valueOf(String.valueOf(attributies.get("id"))),
                LocalDateTime.parse(
                        String.valueOf(attributies.get("connected_at")),
                        DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault())
                ),
                KakaoAccount.from((Map<String, Object>) attributies.get("kakao_account"))
        );
    }
}
