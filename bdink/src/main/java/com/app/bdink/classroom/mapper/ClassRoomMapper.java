package com.app.bdink.classroom.mapper;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.classroom.domain.ClassRoom;
import com.app.bdink.classroom.domain.PriceDetail;
import com.app.bdink.classroom.adapter.out.persistence.entity.Instructor;
import com.app.bdink.classroom.service.command.CreateClassRoomCommand;
import org.springframework.stereotype.Component;

@Component
public class ClassRoomMapper {
    public ClassRoom commandToDomain(CreateClassRoomCommand command){
        return ClassRoom.builder()
                .title(command.classRoomDto().title())
                .introduction(command.classRoomDto().introduction())
                .thumbnail(command.thumbnailKey())
                .introLink(command.mediaKey())
                .build();
    }

    public ClassRoomEntity toEntity(ClassRoom classRoom, Instructor instructor, PriceDetail priceDetail){
        return ClassRoomEntity.builder()
                .title(classRoom.getTitle())
                .introduction(classRoom.getIntroduction())
                .instructor(instructor)
                .thumbnail(classRoom.getThumbnail())
                .introLink(classRoom.getIntroLink())
                .priceDetail(priceDetail)
                .build();
    }

    public ClassRoom toDomain(ClassRoomEntity classRoomEntity){
        return ClassRoom.builder()
                .id(classRoomEntity.getId())
                .title(classRoomEntity.getTitle())
                .introduction(classRoomEntity.getIntroduction())
                .thumbnail(classRoomEntity.getThumbnail())
                .introLink(classRoomEntity.getIntroLink())
                .build();

    }

}
