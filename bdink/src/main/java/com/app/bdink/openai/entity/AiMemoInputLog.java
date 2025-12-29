package com.app.bdink.openai.entity;

import com.app.bdink.common.entity.BaseTimeEntity;
import com.app.bdink.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "ai_memo_input_log")
public class AiMemoInputLog extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    // 입력 로그를 남긴 사용자 (비로그인 대비 null 허용)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    // 마스킹된 사용자 입력 원문
    @Lob
    @Column(name = "raw_text_masked", columnDefinition = "LONGTEXT")
    private String rawTextMasked;

    // 원문 해시(SHA-256)로 중복/분석용
    @Column(name = "raw_text_hash", length = 64)
    private String rawTextHash;

    // LLM 파싱 결과(JSON)
    @Column(name = "parsed_result_json", columnDefinition = "LONGTEXT")
    private String parsedResultJson;

    // 최종 매칭된 exerciseId 목록(JSON)
    @Column(name = "matched_exercise_ids", columnDefinition = "LONGTEXT")
    private String matchedExerciseIdsJson;

    // 처리 성공 여부
    @Column(name = "success_flag")
    private boolean successFlag;

    // 실패 시 에러 코드(Error enum name)
    @Column(name = "error_code", length = 64)
    private String errorCode;

    @Builder
    public AiMemoInputLog(
            Member member,
            String rawTextMasked,
            String rawTextHash,
            String parsedResultJson,
            String matchedExerciseIdsJson,
            boolean successFlag,
            String errorCode
    ) {
        this.member = member;
        this.rawTextMasked = rawTextMasked;
        this.rawTextHash = rawTextHash;
        this.parsedResultJson = parsedResultJson;
        this.matchedExerciseIdsJson = matchedExerciseIdsJson;
        this.successFlag = successFlag;
        this.errorCode = errorCode;
    }
}
