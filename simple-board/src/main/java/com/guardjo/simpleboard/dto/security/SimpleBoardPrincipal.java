package com.guardjo.simpleboard.dto.security;

import com.guardjo.simpleboard.dto.MemberDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public record SimpleBoardPrincipal(String email, String name, String password,
                                   Collection<? extends GrantedAuthority> authorities) implements UserDetails {
    public static SimpleBoardPrincipal of(String email, String name, String password) {
        Set<RoleType> roleTypes = Set.of(RoleType.USER);
        return new SimpleBoardPrincipal(
                email,
                name,
                password,
                roleTypes.stream()
                        .map(RoleType::getName)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toUnmodifiableList()));
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

    public String getNickName() {
        return this.name;
    }
}
