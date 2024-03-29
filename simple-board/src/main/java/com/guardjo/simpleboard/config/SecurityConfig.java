package com.guardjo.simpleboard.config;

import com.guardjo.simpleboard.dto.MemberDto;
import com.guardjo.simpleboard.dto.security.SimpleBoardPrincipal;
import com.guardjo.simpleboard.response.KakaoOAuth2UserResponse;
import com.guardjo.simpleboard.service.MemberService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService) throws Exception {
        httpSecurity.authorizeRequests((auth) ->
                        auth.requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                                .permitAll()
                                .mvcMatchers(
                                        HttpMethod.GET,
                                        "/",
                                        "/article",
                                        "/article/search-hashtag"
                                ).permitAll()
                                .mvcMatchers("/api/**")
                                .permitAll()
                                .anyRequest().authenticated()
                ).formLogin(withDefaults())
                .logout(logOutConfig -> logOutConfig.logoutSuccessUrl("/"))
                .oauth2Login(config -> config.userInfoEndpoint(
                        oAuth2 -> oAuth2.userService(oAuth2UserService)
                ))
                .csrf(configure -> configure.ignoringAntMatchers("/api/**"));

        return httpSecurity.build();
    }

    @Bean
    public UserDetailsService userDetailsService(MemberService memberService) {
        return username ->
                memberService.searchMember(username)
                        .map(SimpleBoardPrincipal::from)
                        .orElseThrow(() -> new UsernameNotFoundException("Not Found Member : " + username));
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService(MemberService memberService, PasswordEncoder passwordEncoder) {
        DefaultOAuth2UserService defaultOAuth2UserService = new DefaultOAuth2UserService();

        return userRequest -> {
            OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(userRequest);
            KakaoOAuth2UserResponse kakaoOAuth2UserResponse = KakaoOAuth2UserResponse.from(oAuth2User.getAttributes());

            MemberDto memberDto = memberService.searchMember(kakaoOAuth2UserResponse.kakaoAccount().email())
                    .orElseGet(() -> memberService.saveMember(
                            generateTemporaryIdAndMailOfKakaoAccount(kakaoOAuth2UserResponse.id()),
                            kakaoOAuth2UserResponse.kakaoAccount().profile().nickname(),
                            passwordEncoder.encode("{bcryp}dummy")
                    ));

            return SimpleBoardPrincipal.from(memberDto);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    private String generateTemporaryIdAndMailOfKakaoAccount(long kakaoRequestId) {
        return "kakao_oauth2_account_" + String.valueOf(kakaoRequestId);
    }
}
