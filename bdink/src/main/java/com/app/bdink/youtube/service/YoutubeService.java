package com.app.bdink.youtube.service;

import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.instructor.adapter.out.persistence.entity.Instructor;
import com.app.bdink.instructor.repository.InstructorRepository;
import com.app.bdink.youtube.domain.YoutubeInfoDto;
import com.app.bdink.youtube.domain.YoutubeType;
import com.app.bdink.youtube.entity.Youtube;
import com.app.bdink.youtube.repository.YoutubeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class YoutubeService {
    private final YoutubeRepository youtubeRepository;
    private final InstructorRepository instructorRepository;

    public Youtube findById(Long id) {
        return youtubeRepository.findById(id).orElseThrow(
                () -> new CustomException(Error.NOT_FOUND_YOUTUBEVIDEO, Error.NOT_FOUND_YOUTUBEVIDEO.getMessage())
        );
    }

    public List<Youtube> findByYoutubeType(YoutubeType youtubeType) {
        return youtubeRepository.findByYoutubeType(youtubeType);
    }

    @Transactional(readOnly = true)
    public YoutubeInfoDto getYoutubeInfoDto(Long youtubeId) {
        Youtube youtubeVideo = findById(youtubeId);
        Long instructorId = Optional.ofNullable(youtubeVideo.getInstructor())
                .map(Instructor::getId)
                .orElse(null);
        return YoutubeInfoDto.of(youtubeVideo.getYoutubeVideoLink(), youtubeVideo.getYoutubeType(), instructorId);
    }

    @Transactional
    public String saveYoutubeVideo(YoutubeInfoDto youtubeInfoDto) {
        Youtube youtube = Youtube.builder()
                .youtubeVideoLink(youtubeInfoDto.youtubeVideoUrl())
                .youtubeType(youtubeInfoDto.youtubeType())
                .instructor(findInstructorById(youtubeInfoDto.instructorId()))
                .build();
        youtubeRepository.save(youtube);
        return String.valueOf(youtube.getId());
    }

    public void updateYoutubeVideo(Youtube youtube, String youtubeVideoUrl) {
        youtube.updateYoutubeVideoLink(youtubeVideoUrl);
        youtubeRepository.save(youtube);
    }

    public void deleteYoutubeVideo(Youtube youtube) {
        youtubeRepository.delete(youtube);
    }

    private Instructor findInstructorById(Long instructorId) {
        return instructorRepository.findById(instructorId).orElseThrow(
                () -> new CustomException(Error.NOT_FOUND_INSTRUCTOR, Error.NOT_FOUND_INSTRUCTOR.getMessage())
        );
    }
}
