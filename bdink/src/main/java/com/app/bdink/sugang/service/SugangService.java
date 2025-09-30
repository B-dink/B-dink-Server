package com.app.bdink.sugang.service;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.external.kollus.entity.KollusMediaLink;
import com.app.bdink.external.kollus.repository.KollusMediaLinkRepository;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.lecture.entity.Lecture;
import com.app.bdink.lecture.repository.LectureRepository;
import com.app.bdink.member.entity.Member;
import com.app.bdink.message.controller.dto.AlimTalkText;
import com.app.bdink.message.service.KakaoAlimtalkService;
import com.app.bdink.sugang.controller.dto.SugangStatus;
import com.app.bdink.sugang.controller.dto.response.SugangClassRoomInfo;
import com.app.bdink.sugang.controller.dto.response.SugangClassRoomListInfo;
import com.app.bdink.sugang.controller.dto.response.SugangInfoDto;
import com.app.bdink.sugang.entity.Sugang;
import com.app.bdink.sugang.repository.SugangRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SugangService {

    private final KakaoAlimtalkService kakaoAlimtalkService;

    private final SugangRepository sugangRepository;
    private final LectureRepository lectureRepository;
    private final KollusMediaLinkRepository kollusMediaLinkRepository;

    public Sugang findById(Long id) {
        return sugangRepository.findById(id)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_SUGANG, Error.NOT_FOUND_SUGANG.getMessage()));
    }

    public List<Sugang> findAllByMember(Member member) {
        return sugangRepository.findAllByMember(member);
    }

    @Transactional(readOnly = true)
    public List<SugangInfoDto> getSugangLecture(Member member) {
        //todo: 환불일경우 status complete만 다시 필터하는 기능
        List<Sugang> sugangs = findAllByMember(member);
        return sugangs.stream()
                .map(SugangInfoDto::of)
                .toList();
    }

    @Transactional
    public SugangInfoDto createSugang(ClassRoomEntity classRoomEntity, Member member, SugangStatus sugangStatus) {

        //수강 중복처리
        verifyDuplicateSugang(member, classRoomEntity);

        Sugang sugang = Sugang.builder()
                .classRoomEntity(classRoomEntity)
                .member(member)
                .sugangStatus(sugangStatus)
                .build();

        sugang = sugangRepository.save(sugang);

        // 알림톡 발송
        Integer price = classRoomEntity.getPriceDetail().getOriginPrice();
        String instructorName = classRoomEntity.getInstructor().getMember().getName();
        String className = classRoomEntity.getTitle();

        String sugangDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        long currentSugangCount = sugangRepository.countByClassRoomEntity(classRoomEntity);
        String count = String.valueOf(currentSugangCount);

        String phoneNumber = formatPhoneNumber(classRoomEntity.getInstructor().getMember().getPhoneNumber()); // 강사 번호로 수정
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                log.info("트랜잭션 커밋 완료. 강사에게 알림톡 발송을 시작합니다.");
                if (price == 0) {
                    return;
                }
                sendAlimtalkToInstructor(instructorName, className, sugangDate, count, phoneNumber)
                        .subscribe(
                                response -> log.info("알림톡 발송 요청 성공: {}", response.getMessage()),
                                error -> log.error("알림톡 발송 요청 실패", error)
                        );
            }
        });

        return SugangInfoDto.of(sugang);
    }

    private String formatPhoneNumber(String localNumber) {
        if (localNumber == null || localNumber.trim().isEmpty()) {
            log.warn("전화번호가 null이거나 비어있습니다.");
            return "";
        }
        if (!localNumber.startsWith("0")) {
            log.warn("유효하지 않은 전화번호 형식입니다: {}", localNumber);
            return localNumber;
        }
        String cleanedNumber = localNumber.replaceAll("-", "");
        return "82-" + cleanedNumber.substring(1);
    }

    private Mono<RspTemplate<String>> sendAlimtalkToInstructor(
            String instructorName, String className, String sugangDate, String count, String phoneNumber
    ) {
        AlimTalkText alimTalkText = new AlimTalkText(instructorName, className, sugangDate, count);

        return kakaoAlimtalkService.sendAlimTalk(phoneNumber, alimTalkText)
                .doOnSuccess(response -> log.info(">>> 알림톡 발송 WebClient 요청 성공: {}", response))
                .doOnError(error -> log.error(">>> 알림톡 발송 WebClient 요청 실패: {}", error.getMessage()));
    }

    @Transactional
    public List<SugangClassRoomInfo> getSugangClassRoomInfo(Member member) {
        updateAllSugangProgress(member);
        List<Sugang> sugangs = sugangRepository.findByMemberAndSugangStatus(member, SugangStatus.PAYMENT_COMPLETED);

        List<SugangClassRoomInfo> sugangClassRoomInfos = sugangs.stream()
                .flatMap(sugang -> {
                    List<Lecture> lectures = lectureRepository.findAllByClassRoom(sugang.getClassRoomEntity());
                    return lectures.stream()
                            .map(lecture -> {
                                KollusMediaLink kollusMediaLink = kollusMediaLinkRepository
                                        .findByMemberIdAndLectureId(member.getId(), lecture.getId())
                                        .orElse(null);
                                //todo: 이쪽 문제 터질수도? 나중에 확인한번 더하기
                                return SugangClassRoomInfo.of(sugang, lecture, kollusMediaLink);
                            });
                })
                .toList();
        return sugangClassRoomInfos;
    }

    @Transactional
    public List<SugangClassRoomListInfo> getSugangClassRoomListInfo(Member member) {
        updateAllSugangProgress(member);
        List<Sugang> sugangs = sugangRepository.findByMemberAndSugangStatus(member, SugangStatus.PAYMENT_COMPLETED);
        return sugangs.stream()
                .map(SugangClassRoomListInfo::of)
                .toList();
    }

    @Transactional
    public void updateAllSugangProgress(Member member) {
        List<Sugang> sugangs = sugangRepository.findByMemberAndSugangStatus(member, SugangStatus.PAYMENT_COMPLETED);

        for (Sugang sugang : sugangs) {
            updateSugangProgress(sugang);
        }
    }

    @Transactional
    public void updateSugangProgress(Sugang sugang) {
        Member member = sugang.getMember();
        ClassRoomEntity classRoomEntity = sugang.getClassRoomEntity();

        int totalLectureCount = lectureRepository.countByClassRoom(classRoomEntity);
        log.info("totalLectureCount : {}", totalLectureCount);
        if (totalLectureCount == 0) {
            sugang.updateProgressPercent(0);
            sugangRepository.save(sugang);
            return;
        }

        int completedLectureCount = kollusMediaLinkRepository.countByMemberAndLecture_ClassRoomAndPlaytimePercentGreaterThanEqual(member,
                classRoomEntity, 90);

        double progress = ((double) completedLectureCount / totalLectureCount) * 100;
        log.info("progress1 : {}", progress);
        sugang.updateProgressPercent(progress);
        log.info("progress2 : {}", sugang.getProgressPercent());
        sugangRepository.save(sugang);
        log.info("progress3 : {}", sugang.getProgressPercent());
    }

    // 사용자가 이미 수강을 했는지 검사하는 메서드
    public void verifyDuplicateSugang(Member member, ClassRoomEntity classRoomEntity) {
        // member와 classRoomEntity로 이미 수강 정보가 존재하는지 확인합니다.
        sugangRepository.findByMemberAndClassRoomEntity(member, classRoomEntity)
                .ifPresent(sugang -> {
                    // 수강 정보가 존재하면 CustomException을 던져 중복 처리합니다.
                    throw new CustomException(Error.EXIST_SUGANG, Error.EXIST_SUGANG.getMessage());
                });
    }
}
