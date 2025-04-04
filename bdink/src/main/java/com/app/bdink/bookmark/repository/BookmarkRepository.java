package com.app.bdink.bookmark.repository;

import com.app.bdink.bookmark.entity.Bookmark;
import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.member.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByMember(Member member);
    int countByClassRoom(ClassRoomEntity classRoomEntity);
    boolean existsByClassRoomAndMember(ClassRoomEntity classRoomEntity, Member member);
}
