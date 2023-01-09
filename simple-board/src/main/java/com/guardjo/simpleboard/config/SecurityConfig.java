package com.guardjo.simpleboard.config;

import com.guardjo.simpleboard.dto.MemberDto;
import com.guardjo.simpleboard.dto.security.SimpleBoardPrincipal;
import com.guardjo.simpleboard.repository.MemberRepository;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests((auth) ->
                auth.mvcMatchers(
                                HttpMethod.GET,
                                "/",
                                "/article",
                                "/article/search-hashtag"
                        ).permitAll()
                        .anyRequest().authenticated()
        ).formLogin(Customizer.withDefaults());

        return httpSecurity.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> {
            web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
        };
    }

    @Bean
    public UserDetailsService userDetailsService(MemberRepository memberRepository) {
        return username ->
                memberRepository.findByEmail(username)
                        .map(MemberDto::from)
                        .map(SimpleBoardPrincipal::from)
                        .orElseThrow(() -> new UsernameNotFoundException("Not Found Member : " + username));

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
