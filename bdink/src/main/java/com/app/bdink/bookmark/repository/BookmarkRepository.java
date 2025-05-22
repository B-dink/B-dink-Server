package com.app.bdink.bookmark.repository;

import com.app.bdink.bookmark.entity.Bookmark;
import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.member.entity.Member;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByMember(Member member);
    int countByClassRoom(ClassRoomEntity classRoomEntity);
    boolean existsByClassRoomAndMember(ClassRoomEntity classRoomEntity, Member member);
    Optional<Bookmark> findByClassRoomAndMember(ClassRoomEntity classRoomEntity, Member member);

    // 또는 ID로 비교 (더 안전)
    @Query("SELECT COUNT(b) > 0 FROM Bookmark b WHERE b.classRoom.id = :classRoomId AND b.member.id = :memberId")
    boolean existsByClassRoomIdAndMemberId(@Param("classRoomId") Long classRoomId,
                                           @Param("memberId") Long memberId);
}
