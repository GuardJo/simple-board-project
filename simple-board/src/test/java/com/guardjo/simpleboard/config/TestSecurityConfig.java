package com.guardjo.simpleboard.config;

import com.guardjo.simpleboard.domain.Member;
import com.guardjo.simpleboard.repository.MemberRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@TestConfiguration
@Import(SecurityConfig.class)
public class TestSecurityConfig {
    @MockBean
    private MemberRepository memberRepository;

    @BeforeTestMethod
    public void testMockMemberRepository() {
        given(memberRepository.findByEmail(anyString())).willReturn(
                Optional.of(
                        Member.of(
                                "test@mail.com",
                                "tester",
                                "pwd"
                        )
                )
        );
    }
}
