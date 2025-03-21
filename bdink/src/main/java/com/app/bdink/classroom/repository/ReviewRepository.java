package com.app.bdink.classroom.repository;

import com.app.bdink.classroom.domain.Review;
import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    void deleteById(Long id);
    Page<Review> findAllByClassRoom(ClassRoomEntity classRoomEntity, Pageable pageable);
}
