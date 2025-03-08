package com.app.bdink.classroom.repository;

import com.app.bdink.classroom.domain.Review;
import com.app.bdink.classroom.entity.ClassRoom;
import com.app.bdink.member.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    void deleteById(Long id);

    List<Review> findByClassRoom(ClassRoom classRoom);
}
