package com.app.bdink.classroom.adapter.in.controller.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record ClassRoomImagesDto(
        MultipartFile thumbnail,
        MultipartFile promotionThumbnail,
        MultipartFile detailThumbnail
) {
    public static ClassRoomImagesDto of(MultipartFile thumbnail, MultipartFile promotionThumbnail, MultipartFile detailThumbnail){
        return new ClassRoomImagesDto(thumbnail, promotionThumbnail, detailThumbnail);
    }
}