package com.app.bdink.classroom.service;

import com.app.bdink.classroom.adapter.in.controller.dto.request.ClassRoomDto;
import com.app.bdink.classroom.adapter.in.controller.dto.response.AllClassRoomResponse;
import com.app.bdink.classroom.adapter.in.controller.dto.response.ChapterResponse;
import com.app.bdink.classroom.adapter.in.controller.dto.response.ClassRoomDetailResponse;
import com.app.bdink.classroom.adapter.in.controller.dto.response.ClassRoomResponse;
import com.app.bdink.classroom.adapter.in.controller.dto.response.ClassRoomSummeryDto;
import com.app.bdink.classroom.adapter.out.persistence.ClassRoomRepositoryAdapter;
import com.app.bdink.classroom.domain.*;
import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.classroom.adapter.out.persistence.entity.Instructor;
import com.app.bdink.classroom.mapper.ClassRoomMapper;
import com.app.bdink.classroom.mapper.InstructorMapper;
import com.app.bdink.classroom.mapper.PriceDetailMapper;
import com.app.bdink.classroom.port.in.ClassRoomUseCase;
import com.app.bdink.classroom.repository.ClassRoomRepository;
import com.app.bdink.classroom.service.command.CreateClassRoomCommand;
import com.app.bdink.lecture.repository.ChapterRepository;
import com.app.bdink.lecture.repository.LectureRepository;
import com.app.bdink.lecture.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassRoomService implements ClassRoomUseCase {

    private final ClassRoomRepository classRoomRepository;

    private final LectureService lectureService;
    private final ChapterRepository chapterRepository;
    private final LectureRepository lectureRepository;
    private final BookmarkService bookmarkService;
    private final ClassRoomMapper classRoomMapper;
    private final PriceDetailMapper priceDetailMapper;
    private final InstructorMapper instructorMapper;
    private final ClassRoomRepositoryAdapter classRoomRepositoryAdapter;

    public ClassRoomEntity findById(Long id) {
        return classRoomRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("해당 클래스를 찾지 못했습니다.")
        );
    }

    @Transactional(readOnly = true)
    public ClassRoomResponse getClassRoomInfo(final Long id) {
        ClassRoomEntity classRoomEntity = findById(id);

        ClassRoomSummeryDto classRoomSummeryDto = ClassRoomSummeryDto.of(
            chapterRepository.countByClassRoom(classRoomEntity),
            lectureRepository.countByClassRoom(classRoomEntity),
            lectureService.getTotalLectureTime(classRoomEntity)
        );

        return ClassRoomResponse.of(classRoomEntity, classRoomSummeryDto);
    }

    @Transactional(readOnly = true)
    public List<ChapterResponse> getChapterInfo(Long id) {
        ClassRoomEntity classRoomEntity = findById(id);
        return classRoomEntity.getChapters().stream()
            .map(ChapterResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public String createClassRoom(CreateClassRoomCommand command) {

        ClassRoom classRoom = classRoomMapper.commandToDomain(command); //mapper에서 커맨드를 -> 도메인으로 바꾸고
        PriceDetail priceDetail = priceDetailMapper.commandToDomain(command);
        Instructor instructor = instructorMapper.commandToEntity(command); //TODO: 나중에 도메인으로 바꾸든가하기.
        ClassRoom savedClassRoom = classRoomRepositoryAdapter.createClassRoom(classRoom, priceDetail, instructor); //나중에 member빼기
        return String.valueOf(savedClassRoom.getId());
    }

    @Transactional
    public ClassRoomResponse updateClassRoomInfo(final ClassRoomEntity classRoomEntity,
                                                 final String thumbnailKey,
                                                 final String videoKey,
                                                 final ClassRoomDto classRoomDto) {
        classRoomEntity.modifyClassRoom(classRoomDto, thumbnailKey, videoKey);
        return ClassRoomResponse.builder()
            .id(classRoomEntity.getId())
            .title(classRoomEntity.getTitle())
            .introduction(classRoomEntity.getIntroduction())
            .thumbnail(classRoomEntity.getThumbnail())
            .introLink(classRoomEntity.getIntroLink())
            .priceDetail(classRoomEntity.getPriceDetail())
            .build();
    }

    @Transactional
    public void updateClassRoomCdn(Long id, String assetId) {
        ClassRoomEntity classRoomEntity = findById(id);
        classRoomEntity.updateCDNLink(assetId);
    }

    @Transactional
    public void deleteClassRoom(final ClassRoomEntity classRoomEntity) {
        classRoomRepository.delete(classRoomEntity);
    }

    @Transactional(readOnly = true)
    public List<AllClassRoomResponse> getAllClassRoom() {
        List<ClassRoomEntity> classRoomEntities = classRoomRepository.findAll();
        return classRoomEntities.stream()
                .map(classRoom -> {
                    ChapterSummary chapterSummary = getChapterSummary(classRoom.getId());
                    return AllClassRoomResponse.from(classRoom, chapterSummary);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AllClassRoomResponse> getClassRoomByCareer(Career career) {
        List<ClassRoomEntity> classRoomEntities = classRoomRepository.findAllByInstructorCareer(career);
        return classRoomEntities.stream()
                .map(classRoom -> AllClassRoomResponse.from(classRoom, getChapterSummary(classRoom.getId())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClassRoomDetailResponse getClassRoomDetail(Long id) {
        ClassRoomEntity classRoomEntity = findById(id);
        long bookmarkCount = bookmarkService.getBookmarkCountForClassRoom(classRoomEntity);

        return new ClassRoomDetailResponse(
                classRoomEntity.getTitle(),
                classRoomEntity.getIntroduction(),
                bookmarkCount,
                classRoomEntity.getInstructor().getMember().getName(),
                classRoomEntity.getThumbnail(),
                classRoomEntity.getPriceDetail()
        );
    }

    public ChapterSummary getChapterSummary(Long id) {
        ClassRoomEntity classRoomEntity = findById(id) ;
        int totalLectureCount = lectureService.countLectureByClassRoom(classRoomEntity);
        int totalChapterCount = classRoomEntity.getChapters().size();
        int totalLectureTime = lectureService.getChapterLectureTime(classRoomEntity);

        return new ChapterSummary(totalChapterCount, totalLectureCount, totalLectureTime);
    }
}
