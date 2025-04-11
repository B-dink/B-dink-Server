package com.app.bdink.qna.repository;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.member.entity.Member;
import com.app.bdink.qna.entity.Question;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends
    JpaRepository<Question, Long> {
    List<Question> findByClassRoom(ClassRoomEntity classRoomEntity);

    List<Question> findByMember(Member member);

}
