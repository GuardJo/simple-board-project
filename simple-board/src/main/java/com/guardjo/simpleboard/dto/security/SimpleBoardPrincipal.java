package com.guardjo.simpleboard.dto.security;

import com.guardjo.simpleboard.dto.MemberDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public record SimpleBoardPrincipal(String email, String name, String password,
                                   Collection<? extends GrantedAuthority> authorities,
                                   Map<String, Object> oAuth2Attributes) implements UserDetails, OAuth2User {
    public static SimpleBoardPrincipal of(String email, String name, String password) {
        Set<RoleType> roleTypes = Set.of(RoleType.USER);
        return new SimpleBoardPrincipal(
                email,
                name,
                password,
                roleTypes.stream()
                        .map(RoleType::getName)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toUnmodifiableList()),
                Map.of());
    }

    public static SimpleBoardPrincipal of(String email, String name, String password, Map<String, Object> oAuth2Attributes) {
        Set<RoleType> roleTypes = Set.of(RoleType.USER);
        return new SimpleBoardPrincipal(
                email,
                name,
                password,
                roleTypes.stream()
                        .map(RoleType::getName)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toUnmodifiableList()),
                oAuth2Attributes);
    }

    public static SimpleBoardPrincipal from(MemberDto memberDto) {
        return SimpleBoardPrincipal.of(
                memberDto.email(),
                memberDto.name(),
                memberDto.password()
        );
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Oauth2 관련 메소드
    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public String getName() {
        return this.email;
    }

    public String getNickName() {
        return this.name;
    }
}
