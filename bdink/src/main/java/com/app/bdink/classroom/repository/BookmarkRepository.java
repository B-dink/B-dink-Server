package com.app.bdink.classroom.repository;

import com.app.bdink.classroom.entity.Bookmark;
import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.member.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByMember(Member member);
    int countByClassRoom(ClassRoomEntity classRoomEntity);
}
