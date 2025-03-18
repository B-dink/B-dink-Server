package com.app.bdink.classroom.port.in;

import com.app.bdink.classroom.adapter.in.controller.dto.request.ClassRoomDto;
import com.app.bdink.classroom.adapter.in.controller.dto.response.AllClassRoomResponse;
import com.app.bdink.classroom.adapter.in.controller.dto.response.ChapterResponse;
import com.app.bdink.classroom.adapter.in.controller.dto.response.ClassRoomDetailResponse;
import com.app.bdink.classroom.adapter.in.controller.dto.response.ClassRoomResponse;
import com.app.bdink.classroom.domain.Career;
import com.app.bdink.classroom.domain.ChapterSummary;
import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.classroom.service.command.CreateClassRoomCommand;

import java.util.List;

//* useCase에서는 Command를 받아서 서비스에 넘겨주고, 도메인(ResponseDto)을 컨트롤러에 반환한다.
public interface ClassRoomUseCase {

    ClassRoomResponse getClassRoomInfo(final Long id);

    List<ChapterResponse> getChapterInfo(Long id);

    String createClassRoom(CreateClassRoomCommand command);

    ClassRoomResponse updateClassRoomInfo(final ClassRoomEntity classRoomEntity,
                                                 final String thumbnailKey,
                                                 final String videoKey,
                                                 final ClassRoomDto classRoomDto);

    void updateClassRoomCdn(Long id, String assetId);

    void deleteClassRoom(final ClassRoomEntity classRoomEntity);

    List<AllClassRoomResponse> getAllClassRoom();

    List<AllClassRoomResponse> getClassRoomByCareer(Career career);

    ClassRoomDetailResponse getClassRoomDetail(Long id);

    ChapterSummary getChapterSummary(Long id);
}
