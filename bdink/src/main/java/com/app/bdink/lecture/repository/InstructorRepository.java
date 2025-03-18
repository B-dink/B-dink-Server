package com.app.bdink.lecture.repository;

import com.app.bdink.classroom.adapter.out.persistence.entity.Instructor;
import com.app.bdink.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    Instructor findByMember(final Member member);
}
