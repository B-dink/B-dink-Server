package com.app.bdink.sugang.service;

import com.app.bdink.external.aws.lambda.domain.Media;
import com.app.bdink.external.aws.lambda.service.MediaService;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.lecture.entity.Lecture;
import com.app.bdink.member.entity.Member;
import com.app.bdink.sugang.controller.dto.response.SugangInfoDto;
import com.app.bdink.sugang.entity.Sugang;
import com.app.bdink.sugang.repository.SugangRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SugangService {

    private final SugangRepository sugangRepository;

    public Sugang findById(Long id){
        return sugangRepository.findById(id)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_SUGANG, Error.NOT_FOUND_SUGANG.getMessage()));
    }

    public Sugang findByLecture(Lecture lecture){
        return sugangRepository.findByLecture(lecture)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_SUGANG, Error.NOT_FOUND_SUGANG.getMessage()));
    }

    @Transactional(readOnly = true)
    public SugangInfoDto getSugangLecture(Member member, Lecture lecture){
        Sugang sugang = findByLecture(lecture);
        if (!sugang.getMember().equals(member)){
            throw new CustomException(Error.UNAUTHORIZED_ACCESS, Error.UNAUTHORIZED_ACCESS.getMessage());
        }
        return SugangInfoDto.of(sugang.getMedia(), sugang);
    }

    @Transactional
    public SugangInfoDto createSugang(final Media media, final Member member, final Lecture lecture){
        Sugang sugang = Sugang.builder()
                .lecture(lecture)
                .member(member)
                .media(media)
                .build();

        sugang = sugangRepository.save(sugang);
        return SugangInfoDto.of(sugang.getMedia(), sugang);
    }

}
