package com.app.bdink.bookmark.service;

import com.app.bdink.bookmark.adapter.in.controller.dto.response.BookmarkResponse;
import com.app.bdink.bookmark.entity.Bookmark;
import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.bookmark.repository.BookmarkRepository;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.member.entity.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public String saveBookmark(final Member member, final ClassRoomEntity classRoomEntity){
        if (bookmarkRepository.existsByClassRoomAndMember(classRoomEntity, member)) {
            throw new CustomException(Error.EXIST_BOOKMARK, Error.EXIST_BOOKMARK.getMessage());
        }
        Bookmark bookmark = Bookmark.builder()
            .classRoom(classRoomEntity)
            .member(member)
            .build();
        return String.valueOf(bookmarkRepository.save(bookmark).getId());
    }

    public List<BookmarkResponse> getBookmarkClassRoom(final Member member){
        List<Bookmark> bookmarkList = bookmarkRepository.findByMember(member);
        return bookmarkList.stream()
            .map((bookmark) -> BookmarkResponse.from(bookmark.getClassRoom()))
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

    public long getBookmarkCountForClassRoom(ClassRoomEntity classRoomEntity) {
        return bookmarkRepository.countByClassRoom(classRoomEntity);
    }

}
