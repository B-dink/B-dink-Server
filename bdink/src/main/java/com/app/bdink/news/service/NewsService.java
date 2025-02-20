package com.app.bdink.news.service;

import com.app.bdink.news.controller.dto.request.NewsReqDto;
import com.app.bdink.news.controller.dto.response.NewsResDto;
import com.app.bdink.news.entity.News;
import com.app.bdink.news.repository.NewsRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

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

    public void updateNews(Long newsId, NewsReqDto newsReqDto) {
        News news = newsRepository.findById(newsId).orElseThrow(
            () -> new IllegalArgumentException("해당 뉴스를 찾지 못했습니다.")
        );

        news.update(newsReqDto);
    }

    public void deleteNews(Long newsId) {
        News news = newsRepository.findById(newsId).orElseThrow(
            () -> new IllegalArgumentException("해당 뉴스를 찾지 못했습니다.")
        );

        newsRepository.delete(news);
    }

}
