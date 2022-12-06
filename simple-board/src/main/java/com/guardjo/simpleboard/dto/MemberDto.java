package com.guardjo.simpleboard.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link com.guardjo.simpleboard.domain.Member} entity
 */
public record MemberDto(String creator, LocalDateTime createTime, String email, String name,
                        String password) implements Serializable {
    public static MemberDto of(String creator, LocalDateTime createTime, String email, String name, String password) {
        return new MemberDto(creator, createTime, email, name, password);
    }
}