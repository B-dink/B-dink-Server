package com.app.bdink.youtube.repository;

import com.app.bdink.youtube.domain.YoutubeType;
import com.app.bdink.youtube.entity.Youtube;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface YoutubeRepository extends JpaRepository<Youtube, Long> {
    List<Youtube> findByYoutubeType(YoutubeType youtubeType);
}
