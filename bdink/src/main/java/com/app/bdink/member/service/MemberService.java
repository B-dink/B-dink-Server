package com.app.bdink.member.service;

import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.global.token.TokenProvider;
import com.app.bdink.member.controller.dto.request.MemberPhoneUpdateRequestDto;
import com.app.bdink.member.controller.dto.request.MemberRequestDto;
import com.app.bdink.member.controller.dto.response.MemberLoginRequestDto;
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
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(
                () -> new NotFoundMemberException("해당 멤버를 찾지 못했습니다.")
        );
    }

    @Transactional(readOnly = true)
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundMemberException("해당 멤버를 찾지 못했습니다.")
        );
    }

    @Transactional(readOnly = true)
    public Member findByRefreshToken(String refreshToken) {
        return memberRepository.findByRefreshToken(refreshToken).orElseThrow(
                () -> new NotFoundMemberException("해당 멤버를 찾지 못했습니다.")
        );
    }

    // 회원가입
    @Transactional
    public Member join(MemberRequestDto memberSaveRequestDto) {
        Optional<Member> existingMember = memberRepository.findByEmail(memberSaveRequestDto.email());

        if (existingMember.isPresent()) { //기존 멤버 존재하는데
            Member existingMemberForUpdate = existingMember.get();
            if (existingMemberForUpdate.getPassword() ==null || existingMemberForUpdate.getPassword().isBlank()) { //패스워드가 비어잇음.
                if (existingMemberForUpdate.getKakaoId() == null &&
                        existingMemberForUpdate.getAppleId() == null) { //카카오 유저나 애플 유저도 아니면 에러
                    log.info("이메일 회원가입 중 반례 발생. : id=" + existingMember.get().getId());
                    throw new CustomException(Error.INTERNAL_SERVER_ERROR, Error.INTERNAL_SERVER_ERROR.getMessage());
                }
                existingMemberForUpdate.updatePassword(passwordEncoder.encode(memberSaveRequestDto.password()));
                return existingMemberForUpdate;
            }

        }

        Member member = Member.builder()
                .name(memberSaveRequestDto.name())
                .email(memberSaveRequestDto.email())
                .password(passwordEncoder.encode(memberSaveRequestDto.password()))
                .role(Role.ROLE_USER)
                .build();

        return memberRepository.save(member);

    }

    // 로그인
    @Transactional
    public Member login(MemberLoginRequestDto memberRequestDto) {
        Member member = findByEmail(memberRequestDto.email());

        if(member.getPassword().isBlank()){
            throw new CustomException(Error.BAD_REQUEST_PROVIDER, Error.BAD_REQUEST_PROVIDER.getMessage());
        }

        if (!passwordEncoder.matches(memberRequestDto.password(), member.getPassword())) {
            throw new InvalidMemberException("비밀번호가 일치하지 않습니다.");
        }

        return member;
    }

    @Transactional
    public boolean passwordDoubleCheck(String origin, String copy){
        if (origin.equals(copy)){
            return true;
        }
        return false;
    }

    @Transactional
    public void updatePhoneNumber(Long id, MemberPhoneUpdateRequestDto memberPhoneUpdateRequestDto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundMemberException("회원 정보를 찾을 수 없습니다."));

        member.updatePhoneNumber(memberPhoneUpdateRequestDto.phoneNumber());
    }

}
