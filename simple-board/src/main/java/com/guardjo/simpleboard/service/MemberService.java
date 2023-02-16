package com.guardjo.simpleboard.service;

import com.guardjo.simpleboard.domain.Member;
import com.guardjo.simpleboard.dto.MemberDto;
import com.guardjo.simpleboard.repository.MemberRepository;
import com.guardjo.simpleboard.util.DtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(@Autowired MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public Optional<MemberDto> searchMember(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);

        return member.map(DtoConverter::from);
    }

    public MemberDto saveMember(String email, String name, String password) {
        Member member = Member.of(email, name, password, email);

        memberRepository.save(member);

        return DtoConverter.from(member);
    }
}
