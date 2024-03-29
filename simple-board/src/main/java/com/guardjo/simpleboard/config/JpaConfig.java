package com.guardjo.simpleboard.config;

import com.guardjo.simpleboard.dto.security.SimpleBoardPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class JpaConfig {
    @Bean
    public AuditorAware<String> auditorAware() {
        // TODO : 차후 닉네임을 통해 creator를 저장하면서 수정 및 삭제 권한 이슈도 해결할 예정
        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(SimpleBoardPrincipal.class::cast)
                .map(SimpleBoardPrincipal::getNickName);
    }
}
