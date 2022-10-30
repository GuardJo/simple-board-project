package com.guardjo.simpleboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class JpaConfig {
    @Bean
    public AuditorAware<String> auditorAware() {
        /**
         * TODO 향후 사용자 관련 서비스가 추가되면 변경할 예정
         * 현재는 의미 없는 임의의 {UserName}으로 지정
         */
        return () -> Optional.of("UserName"); 
    }
}
