package com.app.bdink.news.controller;

import com.app.bdink.news.controller.dto.request.NewsReqDto;
import com.app.bdink.news.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/news")
@Tag(name = "News API", description = "뉴스와 관련된 API들입니다. 뉴스는 관리자만 생성할 수 있습니다.")
public class NewsController {

    private final NewsService newsService;

    @Operation(method = "POST", description = "뉴스를 생성합니다.")
    @PostMapping
    public ResponseEntity<?> createNews(@RequestBody NewsReqDto newsReqDto) {
        String id = newsService.createNews(newsReqDto);
        return ResponseEntity.created(
            URI.create(id))
            .build();
    }

    @Operation(method = "GET", description = "모든 뉴스를 조회합니다.")
    @GetMapping
    public ResponseEntity<?> getAllNews() {
        return ResponseEntity.ok().body(newsService.getAllNews());
    }

    @Operation(method = "PATCH", description = "뉴스를 수정합니다.")
    @PatchMapping
    public ResponseEntity<?> updateNews(@RequestParam Long id, @RequestBody NewsReqDto newsReqDto) {
        newsService.updateNews(id, newsReqDto);
        return ResponseEntity.ok().build();
    }

    @Operation(method = "DELETE", description = "뉴스를 삭제합니다.")
    @DeleteMapping
    public ResponseEntity<?> deleteNews(@RequestParam Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.ok().build();
    }
}
