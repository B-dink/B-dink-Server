package com.app.bdink.classroom.mapper;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.classroom.domain.ClassRoom;
import com.app.bdink.price.domain.PriceDetail;
import com.app.bdink.instructor.adapter.out.persistence.entity.Instructor;
import com.app.bdink.classroom.service.command.CreateClassRoomCommand;
import org.springframework.stereotype.Component;

@Component
public class ClassRoomMapper {
    public ClassRoom commandToDomain(CreateClassRoomCommand command){
        return ClassRoom.builder()
                .title(command.classRoomDto().title())
                .introduction(command.classRoomDto().introduction())
                .thumbnail(command.thumbnailKey())
                .promotionThumbnail(command.promotionThumbnailKey())
                .detailThumbnail(command.detailThumbnailKey())
                .otLink(command.classRoomDto().otLink())
                .introLink(command.mediaKey())
                .level(command.classRoomDto().level())
                .build();
    }

    public ClassRoomEntity toEntity(ClassRoom classRoom, Instructor instructor, PriceDetail priceDetail){
        return ClassRoomEntity.builder()
                .title(classRoom.getTitle())
                .introduction(classRoom.getIntroduction())
                .instructor(instructor)
                .thumbnail(classRoom.getThumbnail())
                .promotionThumbnail(classRoom.getPromotionThumbnail())
                .detailThumbnail(classRoom.getDetailThumbnail())
                .otLink(classRoom.getOtLink())
                .introLink(classRoom.getIntroLink())
                .priceDetail(priceDetail)
                .level(classRoom.getLevel())
                .build();
    }

    public ClassRoom toDomain(ClassRoomEntity classRoomEntity){
        return ClassRoom.builder()
                .id(classRoomEntity.getId())
                .title(classRoomEntity.getTitle())
                .introduction(classRoomEntity.getIntroduction())
                .thumbnail(classRoomEntity.getThumbnail())
                .promotionThumbnail(classRoomEntity.getPromotionThumbnail())
                .detailThumbnail(classRoomEntity.getDetailThumbnail())
                .otLink(classRoomEntity.getOtLink())
                .introLink(classRoomEntity.getIntroLink())
                .level(classRoomEntity.getLevel())
                .build();

    }

}
