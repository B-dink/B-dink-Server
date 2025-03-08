package com.app.bdink.external.aws.lambda.controller;

import com.app.bdink.classroom.service.ClassRoomService;
import com.app.bdink.external.aws.lambda.controller.request.CdnLinkUrlDto;
import com.app.bdink.external.aws.lambda.domain.Media;
import com.app.bdink.external.aws.lambda.service.MediaService;
import com.app.bdink.external.aws.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/receiver")
public class LambdaController {

    private final ClassRoomService classRoomService;
    private final MediaService mediaService;

    @PostMapping("/vod")
    public ResponseEntity<?> updateCdnLink(@RequestBody CdnLinkUrlDto dto){
        Media media = mediaService.findByKey(dto.s3Key());

        classRoomService.updateClassRoomCdn(
                media.getClassRoomId(),
                mediaService.generateCdn360Link(
                        dto.assetID(),
                        dto.s3Key())
        );
        return ResponseEntity.ok().build();
    }

}
