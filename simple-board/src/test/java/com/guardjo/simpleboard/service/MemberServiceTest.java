package com.guardjo.simpleboard.service;

import com.guardjo.simpleboard.domain.Member;
import com.guardjo.simpleboard.dto.MemberDto;
import com.guardjo.simpleboard.generator.TestDataGenerator;
import com.guardjo.simpleboard.repository.MemberRepository;
import com.guardjo.simpleboard.util.DtoConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    private TestDataGenerator testDataGenerator = new TestDataGenerator();

    @DisplayName("특정 회원 조회 테스트")
    @Test
    void findMemberTest() {
        Member tester = testDataGenerator.generateMember();
        MemberDto testerDto = DtoConverter.from(tester);
        String testMail = tester.getEmail();

        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(tester));

        MemberDto memberDto = memberService.searchMember(testMail).orElseThrow();

        assertThat(memberDto).isEqualTo(testerDto);
    }

    @DisplayName("특정 회원 조회 실패 테스트")
    @Test
    void NotFoundMemberTest() {
        Member tester = testDataGenerator.generateMember();
        MemberDto testerDto = DtoConverter.from(tester);

        given(memberRepository.findByEmail(anyString())).willReturn(Optional.empty());

        Optional<MemberDto> memberDto = memberService.searchMember(testerDto.email());

        assertThat(memberDto.isEmpty()).isTrue();
    }

    @DisplayName("회원 저장 테스트")
    @Test
    void saveMemberTest() {
        String name = "SaveTester";
        String email = "save@mail.com";
        String password = "1234";

        given(memberRepository.save(any(Member.class))).willReturn(Member.of(email, name, password, email));

        MemberDto memberDto = memberService.saveMember(email, name, password);

        assertThat(memberDto).hasFieldOrPropertyWithValue("name", name)
                .hasFieldOrPropertyWithValue("email", email)
                .hasFieldOrPropertyWithValue("password", password);
    }
}