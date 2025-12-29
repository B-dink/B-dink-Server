package com.app.bdink.openai.repository;

import com.app.bdink.openai.entity.AiMemoInputLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiMemoInputLogRepository extends JpaRepository<AiMemoInputLog, Long> {
    // 입력 로그 저장소
}
