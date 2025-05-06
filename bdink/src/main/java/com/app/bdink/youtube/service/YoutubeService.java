package com.app.bdink.youtube.service;

import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.youtube.domain.YoutubeInfoDto;
import com.app.bdink.youtube.domain.YoutubeType;
import com.app.bdink.youtube.entity.Youtube;
import com.app.bdink.youtube.repository.YoutubeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class YoutubeService {
    private final YoutubeRepository youtubeRepository;

    public Youtube findById(Long id) {
        return youtubeRepository.findById(id).orElseThrow(
                () -> new CustomException(Error.NOT_FOUND_YOUTUBEVIDEO, Error.NOT_FOUND_YOUTUBEVIDEO.getMessage())
        );
    }

    public List<Youtube> findByYoutubeType(YoutubeType youtubeType) {
        return youtubeRepository.findByYoutubeType(youtubeType);
    }

    public YoutubeInfoDto getYoutubeInfoDto(Long youtubeId) {
        Youtube youtubeVideo = findById(youtubeId);
        return YoutubeInfoDto.of(youtubeVideo.getYoutubeVideoLink(), youtubeVideo.getYoutubeType());
    }

    public String saveYoutubeVideo(YoutubeInfoDto youtubeInfoDto) {
        Youtube youtube = Youtube.builder()
                .youtubeVideoLink(youtubeInfoDto.youtubeVideoUrl())
                .youtubeType(youtubeInfoDto.youtubeType())
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
}
