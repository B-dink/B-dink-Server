package com.app.bdink.global.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    @Qualifier("appleRestTemplate")
    public RestTemplate appleRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);    // 연결 타임아웃: 5초
        factory.setReadTimeout(10000);      // 읽기 타임아웃: 10초

        return new RestTemplate(factory);
    }
}