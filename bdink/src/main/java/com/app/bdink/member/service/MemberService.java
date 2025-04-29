package com.app.bdink.member.service;

import com.app.bdink.external.kollus.repository.UserKeyRepository;
import com.app.bdink.global.exception.Error;
import com.app.bdink.member.controller.dto.request.MemberMarketingDto;
import com.app.bdink.member.controller.dto.request.MemberPhoneUpdateRequestDto;
import com.app.bdink.member.controller.dto.request.MemberSocialRequestDto;
import com.app.bdink.member.controller.dto.request.MemberUpdateNameDto;
import com.app.bdink.member.controller.dto.response.NameCheckDto;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.entity.Role;
import com.app.bdink.member.exception.NotFoundMemberException;
import com.app.bdink.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final UserKeyRepository userKeyRepository;

    @Transactional(readOnly = true)
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(
                () -> new NotFoundMemberException(Error.NOT_FOUND_USER_EXCEPTION, "해당 멤버를 찾지 못했습니다.")
        );
    }

    @Transactional(readOnly = true)
    public Member findByPhone(String phone) {
        return memberRepository.findByPhoneNumber(phone).orElseThrow(
                () -> new NotFoundMemberException(Error.NOT_FOUND_USER_EXCEPTION, "해당 멤버를 찾지 못했습니다.")
        );
    }

    @Transactional(readOnly = true)
    public Member findByRefreshToken(String refreshToken) {
        return memberRepository.findByRefreshToken(refreshToken).orElseThrow(
                () -> new NotFoundMemberException(Error.NOT_FOUND_USER_EXCEPTION, "해당 멤버를 찾지 못했습니다.")
        );
    }

    // 회원가입
//    @Transactional
//    @Deprecated
//    public Member join(MemberRequestDto memberSaveRequestDto) {
//
//        Optional<Member> existingMember = memberRepository.findByEmail(memberSaveRequestDto.email()); //이메일로 있는지 확인
//
////        if (existingMember.isPresent()) { //기존 멤버 존재하는데
////            Member existingMemberForUpdate = existingMember.get();
////            if (existingMemberForUpdate.getPassword() ==null || existingMemberForUpdate.getPassword().isBlank()) { //패스워드가 비어잇음.
////                if (existingMemberForUpdate.getKakaoId() == null &&
////                        existingMemberForUpdate.getAppleId() == null) { //카카오 유저나 애플 유저도 아니면 에러
////                    log.info("이메일 회원가입 중 반례 발생. : id=" + existingMember.get().getId());
////                    throw new CustomException(Error.INTERNAL_SERVER_ERROR, Error.INTERNAL_SERVER_ERROR.getMessage());
////                }
////                existingMemberForUpdate.updatePassword(passwordEncoder.encode(memberSaveRequestDto.password()));
////                return existingMemberForUpdate;
////            }
////        }
//        if (existingMember.isPresent()) {
//            Member member = existingMember.get();
//            if (member.getSocialType().equals(SocialType.APPLE) || member.getSocialType().equals(SocialType.KAKAO)) {
//                throw new CustomException(Error.BAD_REQUEST_PROVIDER, Error.BAD_REQUEST_PROVIDER.getMessage());
//            }
//            throw new CustomException(Error.EXIST_USER, Error.EXIST_USER.getMessage());
//        }
//
//        Member member = Member.builder()
//                .name(memberSaveRequestDto.name())
//                .email(memberSaveRequestDto.email())
//                .password(passwordEncoder.encode(memberSaveRequestDto.password()))
//                .phoneNumber(memberSaveRequestDto.phone())
//                .socialType(SocialType.INTERNAL)
//                .role(Role.ROLE_USER)
//                .build();
//
//        return memberRepository.save(member);
//
//    }

    @Transactional
    public Member socialJoin(final Member member, MemberSocialRequestDto memberSaveRequestDto, String image) {
        if (member.getRole().equals(Role.SIGNUP_PROGRESS)) {
            member.modifyingInSocialSignUp(memberSaveRequestDto.name(), image, memberSaveRequestDto.phone());
            /**
             * 여기가 회원가입 로직의 마지막일경우 사용자키와 사용자 매핑
             * 근데이거 여유있는 사용자 키가 없으면 가입이 안됨. -> 로그만 찍고 넘어가야할수도. 
             * 로그를 사용자이름하고 찍어놓으면 서버에서 확인은 가능
             */
//            UserKey userkey = userKeyRepository
//                    .findFirstByMemberIsNull()
//                    .orElseThrow(() -> new CustomException(Error.NOT_EXIST_USERKEY, Error.NOT_EXIST_USERKEY.getMessage()));
//
//            userkey.updateMember(member);
//            userKeyRepository.save(userkey);

        }
        return member;
    }

    // 로그인
//    @Transactional
//    public Member login(MemberLoginRequestDto memberRequestDto) {
//        Member member = findByEmail(memberRequestDto.email());
//
//        if (member.getPassword().isBlank() || !passwordEncoder.matches(memberRequestDto.password(), member.getPassword())) {
//            throw new InvalidMemberException(Error.BAD_REQUEST_PASSWORD, Error.BAD_REQUEST_PASSWORD.getMessage());
//        }
//
//        return member;
//    }


//    @Transactional(readOnly = true)
//    public int passwordCheck(PasswordValidDto passwordDto) {
//
//        String password = passwordDto.password();
//
//        return PasswordValidator.validatePassword(password);
//    }


//    @Transactional(readOnly = true)
//    public DoubleCheckResponse passwordDoubleCheck(String origin, String copy) {
//        if (origin.equals(copy)) {
//            return DoubleCheckResponse.from(true);
//        }
//        return DoubleCheckResponse.from(false);
//    }

    @Transactional
    public void updatePhoneNumber(Long id, MemberPhoneUpdateRequestDto memberPhoneUpdateRequestDto) {
        Member member = memberRepository.findById(id).orElseThrow(
                    () -> new NotFoundMemberException(Error.NOT_FOUND_USER_EXCEPTION, "해당 멤버를 찾지 못했습니다.")
                );

        member.updatePhoneNumber(memberPhoneUpdateRequestDto.phoneNumber());
    }

    @Transactional
    public void deleteMember(final Member member) {
        member.delete();
    }

    @Transactional(readOnly = true)
    public NameCheckDto checkName(String name) {

        boolean isExist = memberRepository.existsByName(name); //존재하지않아야지만 통과.

        return NameCheckDto.from(!isExist);

    }

    @Transactional
    public void updateMarketing(final Member member, MemberMarketingDto memberMarketingDto){
        member.updateEventAgree(memberMarketingDto.isAgree());
    }

//    @Transactional
//    public void updatePassword(final Member member, PasswordValidDto passwordValidDto){
//        member.updatePassword(passwordEncoder.encode(passwordValidDto.password()));
//    }

    @Transactional
    public void updateName(final Member member, MemberUpdateNameDto memberUpdateDto) {
        member.updateName(memberUpdateDto.name());
    }

    @Transactional
    public void updateProfile(final Member member, String pictureUrl) {
        member.updatePictureUrl(pictureUrl);
    }

}
