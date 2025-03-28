package com.app.bdink.external.aws.lambda.service;

import com.app.bdink.external.aws.lambda.domain.Media;
import com.app.bdink.external.aws.lambda.repository.MediaRepository;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final MediaRepository mediaRepository;

    @Value("${aws-property.cdn-name}")
    private String CDN_URL;

    @Transactional
    public void createMedia(Long classRoomId, String videoKey){
        Media media = Media.builder()
                .classRoomId(classRoomId)
                .s3Key(videoKey)
                .build();
        mediaRepository.save(media);
    }

    public String generateCdn360Link(String assetId, String s3Key){
        String updateS3Path = s3Key.replace(".mp4", "_360.m3u8");
        return CDN_URL+assetId+"/HLS/"+s3Key;
    }

    public Media findByKey(String videoKey){
        return mediaRepository.findByS3Key(videoKey)
                .orElseThrow(()-> new CustomException(Error.NOT_FOUND_FILE, Error.NOT_FOUND_FILE.getMessage()));
    }
}
