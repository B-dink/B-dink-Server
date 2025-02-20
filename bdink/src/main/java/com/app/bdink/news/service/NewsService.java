package com.app.bdink.news.service;

import com.app.bdink.news.controller.dto.request.NewsReqDto;
import com.app.bdink.news.controller.dto.response.NewsResDto;
import com.app.bdink.news.entity.News;
import com.app.bdink.news.repository.NewsRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    @Transactional
    public String createNews(NewsReqDto newsReqDto) {
        Long id = newsRepository.save(
            newsReqDto.toEntity())
            .getId();
        return String.valueOf(id);
    }

    public List<NewsResDto> getAllNews() {
        List<News> newsList = newsRepository.findAll();
        return newsList.stream()
            .map(NewsResDto::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public void updateNews(Long newsId, NewsReqDto newsReqDto) {
        News news = getById(newsId);

        news.update(newsReqDto.title(), newsReqDto.content(), newsReqDto.url(), newsReqDto.thumbnailUrl());
    }

    @Transactional
    public void deleteNews(Long newsId) {
        News news = getById(newsId);

        newsRepository.delete(news);
    }

    public News getById(Long newsId) {
        return newsRepository.findById(newsId).orElseThrow(
            () -> new IllegalArgumentException("해당 뉴스를 찾지 못했습니다.")
        );
    }
}
