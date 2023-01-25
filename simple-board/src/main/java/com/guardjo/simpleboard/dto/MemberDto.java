package com.guardjo.simpleboard.dto;

import com.guardjo.simpleboard.domain.Member;

import java.time.LocalDateTime;

/**
 * A DTO for the {@link com.guardjo.simpleboard.domain.Member} entity
 */
public record MemberDto(String email, String name, String password) {
    public static MemberDto of(String email, String name, String password) {
        return new MemberDto(email, name, password);
    }

    public static MemberDto from(Member member) {
        return MemberDto.of(
                member.getEmail(),
                member.getName(),
                member.getPassword()
        );
    }

    public static Member toEntity(MemberDto memberDto) {
        return Member.of(
                memberDto.email,
                memberDto.name,
                memberDto.password
        );
    }
}