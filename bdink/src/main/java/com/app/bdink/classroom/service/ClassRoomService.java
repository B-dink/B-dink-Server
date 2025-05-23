package com.app.bdink.classroom.service;

import com.app.bdink.bookmark.repository.BookmarkRepository;
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
import com.app.bdink.instructor.adapter.in.controller.dto.response.InstructorClassroomDto;
import com.app.bdink.instructor.adapter.out.persistence.entity.Instructor;
import com.app.bdink.instructor.mapper.InstructorMapper;
import com.app.bdink.lecture.entity.Lecture;
import com.app.bdink.lecture.repository.LectureRepository;
import com.app.bdink.lecture.service.LectureService;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import com.app.bdink.member.util.MemberUtilService;
import com.app.bdink.price.domain.PriceDetail;
import com.app.bdink.price.mapper.PriceDetailMapper;
import com.app.bdink.review.service.ReviewService;
import com.app.bdink.sugang.controller.dto.SugangStatus;
import com.app.bdink.sugang.entity.Sugang;
import com.app.bdink.sugang.repository.SugangRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
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
    private final BookmarkRepository bookmarkRepository;
    private final MemberService memberService;
    private final MemberUtilService memberUtilService;

    public ClassRoomEntity findById(Long id) {
        return classRoomRepository.findById(id).orElseThrow(
                () -> new CustomException(Error.NOT_FOUND_CLASSROOM, Error.NOT_FOUND_CLASSROOM.getMessage())
        );
    }

    @Transactional(readOnly = true)
    public ClassRoomResponse getClassRoomInfo(final Member member, final Long classRoomId) {
        ClassRoomEntity classRoomEntity = findById(classRoomId);

        ClassRoomSummeryDto classRoomSummeryDto = ClassRoomSummeryDto.of(
                chapterRepository.countByClassRoom(classRoomEntity),
                lectureRepository.countByClassRoom(classRoomEntity),
                lectureService.getTotalLectureTime(classRoomEntity)
        );

        Boolean isBookmarked = isClassRoomBookmarked(member, classRoomEntity);

        return ClassRoomResponse.of(classRoomEntity, classRoomSummeryDto, isBookmarked);
    }

    @Transactional(readOnly = true)
    public ChapterListResponse getChapterInfo(Long id, Principal principal) {
        Member member = memberService.findById(memberUtilService.getMemberId(principal));
        ClassRoomEntity classRoomEntity = findById(id);

        List<KollusMediaLink> mediaLinks = kollusMediaLinkRepository
                .findAllByMemberAndLectureClassRoom(member, classRoomEntity);

        Map<Long, KollusMediaLink> mediaLinkMap = mediaLinks.stream()
                .collect(Collectors.toMap(link -> link.getLecture().getId(), link -> link));

        List<ChapterResponse> chapters = classRoomEntity.getChapters().stream()
                .map(chapter -> ChapterResponse.of(chapter, mediaLinkMap))
                .collect(Collectors.toList());


        int totalLectures = lectureRepository.countByClassRoom(classRoomEntity);

        int completedLectures = kollusMediaLinkRepository.countByMemberAndLectureClassRoomAndCompleted(member, classRoomEntity, true);

        double progressRatio = totalLectures == 0 ? 0.0 :
                ((double) completedLectures / totalLectures) * 100;

        double totalProgress = BigDecimal.valueOf(progressRatio)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();

        Optional<Sugang> sugangOPT = sugangRepository.findByMemberAndClassRoomEntity(member, classRoomEntity);

        LocalDate expiredDate = sugangOPT.get().getExpiredDate();

        return ChapterListResponse.of(totalProgress, totalLectures, completedLectures, expiredDate, chapters);
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
    public CareerListDto getAllClassRoom(Member member) {
        List<ClassRoomEntity> promotions = classRoomRepository.findAllByCareer(Career.PROMOTION);
        List<PromotionDto> dtos = promotions.stream()
                .map(PromotionDto::from)
                .toList();
        List<CategorizedClassroomDto> result = new ArrayList<>();
        for (Career career : Career.values()) {
            if (career.equals(Career.PROMOTION)) {
                continue;
            }
            List<AllCareerClassRoomResponse> classroomsByCareer = getClassRoomByCareerWithIsBookmarked(career, member);
            result.add(CategorizedClassroomDto.from(classroomsByCareer));
        }
        return CareerListDto.of(dtos, result);
    }

    @Transactional(readOnly = true)
    public List<AllCareerClassRoomResponse> getClassRoomByCareerWithIsBookmarked(Career career, Member member) {
        List<ClassRoomEntity> classRoomEntities = classRoomRepository.findAllByCareer(career);
        return classRoomEntities.stream()
                .map(classRoom -> AllCareerClassRoomResponse.of(
                        classRoom,
                        getChapterSummary(classRoom.getId()),
                        isClassRoomBookmarked(member, classRoom),
                        reviewService.countReview(classRoom)))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CareerClassroomDto> getClassRoomByCareer(Career career) {
        List<ClassRoomEntity> classRoomEntities = classRoomRepository.findAllByCareer(career);

        return classRoomEntities.stream()
                .map(classRoom -> CareerClassroomDto.of(classRoom, getChapterSummary(classRoom.getId()), reviewService.countReview(classRoom)))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ClassRoomEntity> getClassRoomEntityByInstructor(final Instructor instructor) {
        return classRoomRepository.findAllByInstructor(instructor);
    }

    @Transactional(readOnly = true)
    public List<InstructorClassroomDto> getFilteredClassroomByInstructor(Member member, final Instructor instructor) {
        return classRoomRepository.findAllByInstructor(instructor).stream()
                .map(classRoom -> InstructorClassroomDto.of(classRoom, member, getChapterSummary(classRoom.getId()), reviewService.countReview(classRoom)))
                .toList();
    }


    @Transactional(readOnly = true)
    public ClassRoomDetailResponse getClassRoomDetail(Long id, long bookmarkCount, Member member, Boolean isBookmarked) {
        ClassRoomEntity classRoomEntity = findById(id);

        Optional<Sugang> sugangOpt = sugangRepository.findByMemberAndClassRoomEntity(member, classRoomEntity);

        Boolean payment = false;

        if(sugangOpt.isPresent()){
            Sugang sugang = sugangOpt.get();
            if (sugang.getSugangStatus() == SugangStatus.PAYMENT_COMPLETED) {
                log.info("해당 클래스에 대해 결제 완료된 유저입니다.");
                payment = true;
            }
        }


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
                payment,
                classRoomEntity.getPriceDetail(),
                classRoomEntity.getLevel(),
                isBookmarked,
                id,
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

    @Transactional(readOnly = true)
    public Boolean isClassRoomBookmarked(Member member, ClassRoomEntity classRoom) {
        return bookmarkRepository.existsByClassRoomAndMember(classRoom, member);
    }
}
