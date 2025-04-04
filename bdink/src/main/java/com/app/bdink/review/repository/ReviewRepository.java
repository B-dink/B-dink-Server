package com.app.bdink.review.repository;

import com.app.bdink.review.domain.Review;
import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;

import com.app.bdink.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    void deleteById(Long id);
    Page<Review> findAllByClassRoom(ClassRoomEntity classRoomEntity, Pageable pageable);
  
    int countByClassRoom(ClassRoomEntity classRoomEntity);
  
    boolean existsByClassRoomAndMember(ClassRoomEntity classRoom, Member member);
}
