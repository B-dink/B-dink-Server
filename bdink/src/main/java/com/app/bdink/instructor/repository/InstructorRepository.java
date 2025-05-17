package com.app.bdink.instructor.repository;

import com.app.bdink.instructor.adapter.out.persistence.entity.Instructor;
import com.app.bdink.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    Instructor findByMember(final Member member);
    Optional<Instructor> findByMemberId(Long memberId);
}
