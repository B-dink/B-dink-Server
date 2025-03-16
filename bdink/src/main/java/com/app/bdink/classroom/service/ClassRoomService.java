package com.app.bdink.classroom.service;

import com.app.bdink.classroom.controller.dto.request.ClassRoomDto;
import com.app.bdink.classroom.controller.dto.response.AllClassRoomResponse;
import com.app.bdink.classroom.controller.dto.response.ChapterResponse;
import com.app.bdink.classroom.controller.dto.response.ClassRoomDetailResponse;
import com.app.bdink.classroom.controller.dto.response.ClassRoomResponse;
import com.app.bdink.classroom.controller.dto.response.ClassRoomSummeryDto;
import com.app.bdink.classroom.domain.Career;
import com.app.bdink.classroom.domain.ChapterSummary;
import com.app.bdink.classroom.entity.ClassRoom;
import com.app.bdink.classroom.entity.Instructor;
import com.app.bdink.classroom.repository.ClassRoomRepository;
import com.app.bdink.lecture.repository.ChapterRepository;
import com.app.bdink.lecture.repository.LectureRepository;
import com.app.bdink.lecture.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassRoomService {

    private final ClassRoomRepository classRoomRepository;

    private final LectureService lectureService;
    private final ChapterRepository chapterRepository;
    private final LectureRepository lectureRepository;

    private final BookmarkService bookmarkService;

    public ClassRoom findById(Long id) {
        return classRoomRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("해당 클래스를 찾지 못했습니다.")
        );
    }

    @Transactional(readOnly = true)
    public ClassRoomResponse getClassRoomInfo(final Long id) {
        ClassRoom classRoom = findById(id);

        ClassRoomSummeryDto classRoomSummeryDto = ClassRoomSummeryDto.of(
            chapterRepository.countByClassRoom(classRoom),
            lectureRepository.countByClassRoom(classRoom),
            lectureService.getTotalLectureTime(classRoom)
        );

        return ClassRoomResponse.of(classRoom, classRoomSummeryDto);
    }

    @Transactional(readOnly = true)
    public List<ChapterResponse> getChapterInfo(Long id) {
        ClassRoom classRoom = findById(id);
        return classRoom.getChapters().stream()
            .map(ChapterResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public String createClassRoom(final Instructor instructor,
                                  final String thumbnailKey,
                                  final String mediaKey,
                                  final ClassRoomDto classRoomDto) {

        ClassRoom classRoom = ClassRoom.builder()
                .title(classRoomDto.title())
                .introduction(classRoomDto.introduction())
                .instructor(instructor)
                .thumbnail(thumbnailKey)
                .introLink(mediaKey)
                .priceDetail(classRoomDto.priceDto().toPriceDetail())
                .build();
        Long id = classRoomRepository.save(classRoom).getId();
        return String.valueOf(id);
    }

    @Transactional
    public ClassRoomResponse updateClassRoomInfo(final ClassRoom classRoom,
                                                 final String thumbnailKey,
                                                 final String videoKey,
                                                 final ClassRoomDto classRoomDto) {
        classRoom.modifyClassRoom(classRoomDto, thumbnailKey, videoKey);
        return ClassRoomResponse.builder()
            .id(classRoom.getId())
            .title(classRoom.getTitle())
            .introduction(classRoom.getIntroduction())
            .thumbnail(classRoom.getThumbnail())
            .introLink(classRoom.getIntroLink())
            .priceDetail(classRoom.getPriceDetail())
            .build();
    }

    @Transactional
    public void updateClassRoomCdn(Long id, String assetId) {
        ClassRoom classRoom = findById(id);
        classRoom.updateCDNLink(assetId);
    }

    @Transactional
    public void deleteClassRoom(final ClassRoom classRoom) {
        classRoomRepository.delete(classRoom);
    }

    @Transactional(readOnly = true)
    public List<AllClassRoomResponse> getAllClassRoom() {
        List<ClassRoom> classRooms = classRoomRepository.findAll();
        return classRooms.stream()
                .map(classRoom -> {
                    ChapterSummary chapterSummary = getChapterSummary(classRoom.getId());
                    return AllClassRoomResponse.from(classRoom, chapterSummary);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AllClassRoomResponse> getClassRoomByCareer(Career career) {
        List<ClassRoom> classRooms = classRoomRepository.findAllByInstructorCareer(career);
        return classRooms.stream()
                .map(classRoom -> AllClassRoomResponse.from(classRoom, getChapterSummary(classRoom.getId())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClassRoomDetailResponse getClassRoomDetail(Long id) {
        ClassRoom classRoom = findById(id);
        long bookmarkCount = bookmarkService.getBookmarkCountForClassRoom(classRoom);

        return new ClassRoomDetailResponse(
                classRoom.getTitle(),
                classRoom.getIntroduction(),
                bookmarkCount,
                classRoom.getInstructor().getMember().getName(),
                classRoom.getThumbnail(),
                classRoom.getPriceDetail()
        );
    }

    private ChapterSummary getChapterSummary(Long id) {
        ClassRoom classRoom = findById(id) ;
        int totalLectureCount = lectureService.countLectureByClassRoom(classRoom);
        int totalChapterCount = classRoom.getChapters().size();
        int totalLectureTime = lectureService.getChapterLectureTime(classRoom);

        return new ChapterSummary(totalChapterCount, totalLectureCount, totalLectureTime);
    }
}
