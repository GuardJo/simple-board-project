package com.guardjo.simpleboard.dto.security.oauth2.kakao;

import java.util.Map;

public record Profile(
        String nickname,
        String thumbnailImageUrl,
        String profileImageUrl,
        boolean isDefaultImage
) {
    public static Profile of(String nickname, String thumbnailImageUrl, String profileImageUrl, boolean isDefaultImage) {
        return new Profile(nickname, thumbnailImageUrl, profileImageUrl, isDefaultImage);
    }

    public static Profile from(Map<String, Object> attributies) {
        return Profile.of(
                String.valueOf(attributies.get("nickname")),
                String.valueOf(attributies.get("thumbnail_image_url")),
                String.valueOf(attributies.get("profile_image_url")),
                Boolean.valueOf(String.valueOf(attributies.get("is_default_image")))
        );
    }
}
