package com.guardjo.simpleboard.dto.security.oauth2.kakao;

import java.util.Map;

public record KakaoAccount(Profile profile, String name, String email) {
    public static KakaoAccount of(Profile profile, String name, String email) {
        return new KakaoAccount(profile, name, email);
    }

    public static KakaoAccount from(Map<String, Object> attributies) {
        return KakaoAccount.of(
                Profile.from((Map<String, Object>) attributies.get("profile")),
                String.valueOf(attributies.get("name")),
                String.valueOf(attributies.get("email"))
        );
    }
}
