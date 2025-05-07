package com.app.bdink.classroom.service;

import com.app.bdink.chapter.domain.ChapterSummary;
import com.app.bdink.chapter.repository.ChapterRepository;
import com.app.bdink.classroom.adapter.in.controller.dto.request.ClassRoomDto;
import com.app.bdink.classroom.adapter.in.controller.dto.response.*;
import com.app.bdink.classroom.adapter.out.persistence.ClassRoomRepositoryAdapter;
import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomDetailImage;
import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.classroom.domain.Career;
import com.app.bdink.classroom.domain.ClassRoom;
import com.app.bdink.classroom.mapper.ClassRoomMapper;
import com.app.bdink.classroom.port.in.ClassRoomUseCase;
import com.app.bdink.classroom.repository.ClassRoomDetailImageRepository;
import com.app.bdink.classroom.repository.ClassRoomRepository;
import com.app.bdink.classroom.service.command.CreateClassRoomCommand;
import com.app.bdink.external.kollus.entity.KollusMediaLink;
import com.app.bdink.external.kollus.repository.KollusMediaLinkRepository;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.instructor.adapter.out.persistence.entity.Instructor;
import com.app.bdink.instructor.mapper.InstructorMapper;
import com.app.bdink.lecture.entity.Lecture;
import com.app.bdink.lecture.repository.LectureRepository;
import com.app.bdink.lecture.service.LectureService;
import com.app.bdink.member.entity.Member;
import com.app.bdink.price.domain.PriceDetail;
import com.app.bdink.price.mapper.PriceDetailMapper;
import com.app.bdink.review.service.ReviewService;
import com.app.bdink.sugang.entity.Sugang;
import com.app.bdink.sugang.repository.SugangRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassRoomService implements ClassRoomUseCase {

    private final ClassRoomRepository classRoomRepository;

    private final LectureService lectureService;
    private final ChapterRepository chapterRepository;
    private final LectureRepository lectureRepository;
    private final ClassRoomMapper classRoomMapper;
    private final PriceDetailMapper priceDetailMapper;
    private final InstructorMapper instructorMapper;
    private final ClassRoomRepositoryAdapter classRoomRepositoryAdapter;
    private final ReviewService reviewService;
    private final ClassRoomDetailImageRepository classRoomDetailImageRepository;
    private final SugangRepository sugangRepository;
    private final KollusMediaLinkRepository kollusMediaLinkRepository;

    public ClassRoomEntity findById(Long id) {
        return classRoomRepository.findById(id).orElseThrow(
                () -> new CustomException(Error.NOT_FOUND_CLASSROOM, Error.NOT_FOUND_CLASSROOM.getMessage())
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
    public CareerListDto getAllClassRoom() {
        List<ClassRoomEntity> promotions = classRoomRepository.findAllByCareer(Career.PROMOTION);
        List<PromotionDto> dtos = promotions.stream()
                .map(PromotionDto::from)
                .toList();
        List<CategorizedClassroomDto> result = new ArrayList<>();
        for (Career career : Career.values()) {
            if (career.equals(Career.PROMOTION)) {
                continue;
            }
            List<CareerClassroomDto> classroomsByCareer = getClassRoomByCareer(career);
            result.add(CategorizedClassroomDto.from(classroomsByCareer));
        }
        return CareerListDto.of(dtos, result);
    }

    @Transactional(readOnly = true)
    public List<CareerClassroomDto> getClassRoomByCareer(Career career) {
        List<ClassRoomEntity> classRoomEntities = classRoomRepository.findAllByCareer(career);

        List<CareerClassroomDto> careerClassroomDtos = classRoomEntities.stream()
                .map(classRoom -> CareerClassroomDto.of(classRoom, getChapterSummary(classRoom.getId()), reviewService.countReview(classRoom)))
                .toList();
        return careerClassroomDtos;
    }

    @Transactional(readOnly = true)
    public List<ClassRoomEntity> getClassRoomEntityByInstructor(final Instructor instructor) {
        return classRoomRepository.findAllByInstructor(instructor);
    }

    @Transactional(readOnly = true)
    public List<CareerClassroomDto> getFilteredClassroomByInstructor(final Instructor instructor) {
        return classRoomRepository.findAllByInstructor(instructor).stream()
                .map(classRoom -> CareerClassroomDto.of(classRoom, getChapterSummary(classRoom.getId()), reviewService.countReview(classRoom)))
                .toList();
    }


    @Transactional(readOnly = true)
    public ClassRoomDetailResponse getClassRoomDetail(Long id, long bookmarkCount) {
        ClassRoomEntity classRoomEntity = findById(id);

        //클래스룸 엔티티로 디테일이미지 리스트를 가져옴.
        List<ClassRoomDetailImage> byClassRoom = classRoomDetailImageRepository.findByClassRoomOrderBySortOrderAsc(classRoomEntity);

        List<String> imageUrls = byClassRoom.stream()
                .map(ClassRoomDetailImage::getImageUrl)
                .toList();

        return new ClassRoomDetailResponse(
                classRoomEntity.getTitle(),
                classRoomEntity.getIntroduction(),
                bookmarkCount,
                classRoomEntity.getInstructor().getMember().getName(),
                classRoomEntity.getInstructor().getMember().getPictureUrl(),
                classRoomEntity.getThumbnail(),
                classRoomEntity.getPriceDetail(),
                classRoomEntity.getLevel(),
                imageUrls
        );
    }

    public ChapterSummary getChapterSummary(Long id) {
        ClassRoomEntity classRoomEntity = findById(id);
        int totalLectureCount = lectureService.countLectureByClassRoom(classRoomEntity);
        int totalChapterCount = classRoomEntity.getChapters().size();
        int totalLectureTime = lectureService.getChapterLectureTime(classRoomEntity);

        return new ChapterSummary(totalChapterCount, totalLectureCount, totalLectureTime);
    }

//    @Transactional(readOnly = true)
//    public List<ClassRoomProgressResponse> getLectureProgress(Member member, Long classRoomId) {
//        ClassRoomEntity classRoom = findById(classRoomId);
//        int progress = lectureService.lectureProgress(member, classRoom);
//
//        return classRoom.getChapters().stream()
//                .flatMap(chapter -> chapter.getLectures().stream())
//                .map(lecture -> new ClassRoomProgressResponse(
//                        lecture.getTitle(),
//                        classRoom.getInstructor().getMember().getName(),
//                        progress + "%"
//                ))
//                .collect(Collectors.toList());
//    }

    @Transactional(readOnly = true)
    public List<ClassRoomProgressResponse> getLectureProgress(Member member, Long classRoomId) {

        ClassRoomEntity classRoom = findById(classRoomId);

        // Step 1: 이 사용자가 이 클래스룸을 수강중인지 확인
        Sugang sugang = sugangRepository.findByMemberAndClassRoomEntity(member, classRoom).orElseThrow(
                () -> new CustomException(Error.NOT_FOUND_SUGANG, Error.NOT_FOUND_SUGANG.getMessage())
        );

        // Step 2: 클래스룸의 모든 Lecture 조회
        List<Lecture> lectures = lectureRepository.findAllByClassRoom(classRoom);

        // Step 3: 각 Lecture에 대해 진행률 확인
        List<ClassRoomProgressResponse> progressList = new ArrayList<>();

        for (Lecture lecture : lectures) {
            // 이 lecture에 대한 KollusMediaLink 찾기
            Optional<KollusMediaLink> linkOpt = kollusMediaLinkRepository
                    .findByMemberIdAndLectureId(member.getId(), lecture.getId());

            String status;
            if (linkOpt.isPresent() && linkOpt.get().isCompleted()) {
                status = "완강";
            } else if (linkOpt.isPresent() && linkOpt.get().getWatchProgress() > 0) {
                status = linkOpt.get().getPlaytimePercent() + "%";
            } else {
                status = "0";
            }

            progressList.add(new ClassRoomProgressResponse(
                    lecture.getTitle(),
                    classRoom.getInstructor().getMember().getName(),
                    status
            ));
        }

        return progressList;
    }
}
