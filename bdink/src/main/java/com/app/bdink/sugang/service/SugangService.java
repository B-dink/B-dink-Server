package com.app.bdink.sugang.service;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.external.kollus.repository.KollusMediaLinkRepository;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.lecture.repository.LectureRepository;
import com.app.bdink.member.entity.Member;
import com.app.bdink.sugang.controller.dto.SugangStatus;
import com.app.bdink.sugang.controller.dto.response.SugangClassRoomInfo;
import com.app.bdink.sugang.controller.dto.response.SugangInfoDto;
import com.app.bdink.sugang.entity.Sugang;
import com.app.bdink.sugang.repository.SugangRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SugangService {

    private final SugangRepository sugangRepository;
    private final LectureRepository lectureRepository;
    private final KollusMediaLinkRepository kollusMediaLinkRepository;

    public Sugang findById(Long id){
        return sugangRepository.findById(id)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_SUGANG, Error.NOT_FOUND_SUGANG.getMessage()));
    }

    public List<Sugang> findAllByMember(Member member){
        return sugangRepository.findAllByMember(member);
    }

    public Sugang findByMemberAndClassRoomEntity(Member member, ClassRoomEntity classRoomEntity){
        return sugangRepository.findByMemberAndClassRoomEntity(member, classRoomEntity)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_SUGANG, Error.NOT_FOUND_SUGANG.getMessage()));
    }

    @Transactional(readOnly = true)
    public List<SugangInfoDto> getSugangLecture(Member member){
        //todo: 환불일경우 status complete만 다시 필터하는 기능
        List<Sugang> sugangs = findAllByMember(member);
        return sugangs.stream()
                .map(SugangInfoDto::of)
                .toList();
    }

    @Transactional
    public SugangInfoDto createSugang(ClassRoomEntity classRoomEntity, Member member, SugangStatus sugangStatus){
        log.info("수강 스테이터스 : {}", sugangStatus);
        Sugang sugang = Sugang.builder()
                .classRoomEntity(classRoomEntity)
                .member(member)
                .sugangStatus(sugangStatus)
                .build();

        sugang = sugangRepository.save(sugang);
        return SugangInfoDto.of(sugang);
    }

    @Transactional
    public List<SugangClassRoomInfo> getSugangClassRoomInfo(Member member){
        updateAllSugangProgress(member);
        List<Sugang> sugangs = sugangRepository.findByMemberAndSugangStatus(member, SugangStatus.PAYMENT_COMPLETED);

        return sugangs.stream()
                .map(SugangClassRoomInfo::of)
                .toList();
    }
    @Transactional
    public void updateAllSugangProgress(Member member){
        List<Sugang> sugangs = sugangRepository.findByMemberAndSugangStatus(member, SugangStatus.PAYMENT_COMPLETED);

        for(Sugang sugang : sugangs){
            updateSugangProgress(sugang);
        }
    }
    @Transactional
    public void updateSugangProgress(Sugang sugang){
        Member member = sugang.getMember();
        ClassRoomEntity classRoomEntity = sugang.getClassRoomEntity();

        int totalLectureCount = lectureRepository.countByClassRoom(classRoomEntity);
        log.info("totalLectureCount : {}", totalLectureCount);
        if(totalLectureCount == 0) {
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

}
