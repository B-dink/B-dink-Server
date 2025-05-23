package com.app.bdink.bookmark.service;

import com.app.bdink.bookmark.adapter.in.controller.dto.response.BookmarkResponse;
import com.app.bdink.bookmark.entity.Bookmark;
import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.bookmark.repository.BookmarkRepository;
import com.app.bdink.classroom.service.ClassRoomService;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.lecture.service.LectureService;
import com.app.bdink.member.entity.Member;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final LectureService lectureService;

    @Transactional
    public String saveBookmark(final Member member, final ClassRoomEntity classRoomEntity){
        if (bookmarkRepository.existsByClassRoomIdAndMemberId(
                classRoomEntity.getId(), member.getId())) {
            throw new CustomException(Error.EXIST_BOOKMARK, Error.EXIST_BOOKMARK.getMessage());
        }
        Bookmark bookmark = Bookmark.builder()
            .classRoom(classRoomEntity)
            .member(member)
            .build();
        return String.valueOf(bookmarkRepository.save(bookmark).getId());
    }

    @Transactional(readOnly = true)
    public List<BookmarkResponse> getBookmarkClassRoomWithLectureCount(final Member member) {
        List<Bookmark> bookmarks = bookmarkRepository.findByMember(member);

        return bookmarks.stream()
                .map(bookmark -> {
                    ClassRoomEntity classRoom = bookmark.getClassRoom();
                    int lectureCount = lectureService.countLectureByClassRoom(classRoom);
                    return BookmarkResponse.from(bookmark, lectureCount);
                })
                .toList();
    }

    @Transactional
    public void deleteBookmark(final Member member, Long bookmarkId){
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId).orElseThrow(
            () -> new CustomException(Error.NOT_FOUND_BOOKMARK, Error.NOT_FOUND_BOOKMARK.getMessage())
        );
        if (!bookmark.getMember().equals(member)) {
            throw new CustomException(Error.INVALID_USER_ACCESS, Error.INVALID_USER_ACCESS.getMessage());
        }
        bookmarkRepository.deleteById(bookmarkId);
    }

    @Transactional
    public void deleteBookmarkByClassRoomId(Member member, ClassRoomEntity classRoomEntity) {
        Optional<Bookmark> bookmark = bookmarkRepository.findByClassRoomAndMember(classRoomEntity, member);
        if (bookmark.isEmpty()) {
            throw new CustomException(Error.NOT_FOUND_BOOKMARK, Error.NOT_FOUND_BOOKMARK.getMessage());
        } else if (!bookmark.get().getMember().equals(member)) {
            throw new CustomException(Error.INVALID_USER_ACCESS, Error.INVALID_USER_ACCESS.getMessage());
        }
        bookmark.ifPresent(value -> bookmarkRepository.deleteById(value.getId()));
    }

    public long getBookmarkCountForClassRoom(ClassRoomEntity classRoomEntity) {
        return bookmarkRepository.countByClassRoom(classRoomEntity);
    }
}
