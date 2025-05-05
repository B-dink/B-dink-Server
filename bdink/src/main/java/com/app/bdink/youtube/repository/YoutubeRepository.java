package com.app.bdink.youtube.repository;

import com.app.bdink.youtube.entity.Youtube;
import org.springframework.data.jpa.repository.JpaRepository;

public interface YoutubeRepository extends JpaRepository<Youtube, Long> {
}
