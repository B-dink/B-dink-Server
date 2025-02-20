package com.app.bdink.news.controller;

import com.app.bdink.news.controller.dto.request.NewsReqDto;
import com.app.bdink.news.service.NewsService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    @PostMapping
    public ResponseEntity<?> createNews(@RequestBody NewsReqDto newsReqDto) {
        String id = newsService.createNews(newsReqDto);
        return ResponseEntity.created(
            URI.create(id))
            .build();
    }

    @GetMapping
    public ResponseEntity<?> getAllNews() {
        return ResponseEntity.ok().body(newsService.getAllNews());
    }

    @PatchMapping
    public ResponseEntity<?> updateNews(@RequestBody NewsReqDto newsReqDto) {
        newsService.updateNews(newsReqDto.toEntity().getId(), newsReqDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteNews(@RequestBody NewsReqDto newsReqDto) {
        newsService.deleteNews(newsReqDto.toEntity().getId());
        return ResponseEntity.ok().build();
    }
}
