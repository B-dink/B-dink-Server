package com.app.bdink.member.service;

import com.app.bdink.global.oauth2.domain.TokenDto;
import com.app.bdink.global.token.Token;
import com.app.bdink.global.token.TokenProvider;
import com.app.bdink.member.controller.dto.request.MemberPhoneUpdateRequestDto;
import com.app.bdink.member.controller.dto.request.MemberRequestDto;
import com.app.bdink.member.controller.dto.response.MemberLoginResponseDto;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.entity.Role;
import com.app.bdink.member.exception.InvalidMemberException;
import com.app.bdink.member.exception.NotFoundMemberException;
import com.app.bdink.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional(readOnly = true)
    public Member findById(Long id){
        return memberRepository.findById(id).orElseThrow(
                ()-> new IllegalStateException("해당 멤버를 찾지 못했습니다.")
        );
    }

    @Transactional(readOnly = true)
    public Member findByRefreshToken(String refreshToken){
        return memberRepository.findByRefreshToken(refreshToken).orElseThrow(
                ()-> new IllegalStateException("해당 멤버를 찾지 못했습니다.")
        );
    }

    // 회원가입
    @Transactional
    public void join(MemberRequestDto memberSaveRequestDto) {
        Optional<Member> existingMember = memberRepository.findByEmail(memberSaveRequestDto.email());
        existingMember.ifPresent(member -> {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        });

        Member member = Member.builder()
                .name(memberSaveRequestDto.name())
                .email(memberSaveRequestDto.email())
                .password(passwordEncoder.encode(memberSaveRequestDto.password()))
                .role(Role.ROLE_USER)
                .build();

        memberRepository.save(member);
    }

    // 로그인
    public MemberLoginResponseDto login(MemberRequestDto memberRequestDto) {
        Member member = findById(memberRequestDto.id());
        TokenDto token = tokenProvider.createToken(member);

        if (!passwordEncoder.matches(memberRequestDto.password(), member.getPassword()))
        {
            throw new InvalidMemberException("비밀번호가 일치하지 않습니다.");
        }

        return MemberLoginResponseDto.of(member, token.accessToken());
    }

    @Transactional
    public void updatePhoneNumber(Long id, MemberPhoneUpdateRequestDto memberPhoneUpdateRequestDto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundMemberException("회원 정보를 찾을 수 없습니다."));

        member.updatePhoneNumber(memberPhoneUpdateRequestDto.phoneNumber());
    }

}
