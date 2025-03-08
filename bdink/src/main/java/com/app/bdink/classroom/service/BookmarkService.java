package com.app.bdink.classroom.service;

import com.app.bdink.classroom.controller.dto.response.BookmarkResponse;
import com.app.bdink.classroom.entity.Bookmark;
import com.app.bdink.classroom.entity.ClassRoom;
import com.app.bdink.classroom.repository.BookmarkRepository;
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
    public String saveBookmark(final Member member, final ClassRoom classRoom){
        // TODO: 이미 북마크를 만들었는지 확인
        Bookmark bookmark = Bookmark.builder()
            .classRoom(classRoom)
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
    public void deleteBookmark(final Member member, Long reviewId){
        // TODO: 로그인한 멤버와 북마크를 만든 멤버가 같은지 확인
        bookmarkRepository.deleteById(reviewId);
    }

}
