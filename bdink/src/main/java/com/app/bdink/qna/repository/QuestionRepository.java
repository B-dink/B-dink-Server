package com.app.bdink.qna.repository;

import com.app.bdink.classroom.entity.ClassRoom;
import com.app.bdink.qna.entity.Question;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends
    JpaRepository<Question, Long> {
    List<Question> findByClassRoom(ClassRoom classRoom);

}
