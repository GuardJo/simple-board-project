package com.guardjo.simpleboard.config;

import com.guardjo.simpleboard.config.props.CorsProperties;
import com.guardjo.simpleboard.domain.Member;
import com.guardjo.simpleboard.service.MemberService;
import com.guardjo.simpleboard.util.DtoConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@EnableConfigurationProperties({CorsProperties.class})
@TestConfiguration
@Import({SecurityConfig.class})
public class TestSecurityConfig {
    @MockBean
    private MemberService memberService;

    @BeforeTestMethod
    public void testMockMemberRepository() {
        given(memberService.searchMember(anyString())).willReturn(Optional.of(
                        DtoConverter.from(
                                Member.of(
                                        "test@mail.com",
                                        "tester",
                                        "pwd"
                                )
                        )
                )
        );
    }
}
