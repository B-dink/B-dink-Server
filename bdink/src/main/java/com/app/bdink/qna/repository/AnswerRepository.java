package com.app.bdink.qna.repository;

import com.app.bdink.qna.entity.Answer;
import com.app.bdink.qna.entity.Question;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestion(Question question);

}
