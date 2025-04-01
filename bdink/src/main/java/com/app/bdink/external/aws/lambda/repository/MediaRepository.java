package com.app.bdink.external.aws.lambda.repository;

import com.app.bdink.external.aws.lambda.domain.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MediaRepository extends JpaRepository<Media, Long> {

    Optional<Media> findByS3Key(String s3Key);

    Optional<Media> findByLectureId(Long lectureId);
}
