package com.app.bdink.member.service;

import com.app.bdink.member.entity.Member;
import com.app.bdink.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    public Member findById(Long id){
        return memberRepository.findById(id).orElseThrow(
                ()-> new IllegalStateException("해당 멤버를 찾지 못했습니다.")
        );
    }

}
