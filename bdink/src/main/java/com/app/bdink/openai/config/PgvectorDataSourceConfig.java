package com.app.bdink.openai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class PgvectorDataSourceConfig {
    // 로컬 Docker 기본값을 제공하고, 환경 변수로 재정의 가능
    @Value("${pgvector.datasource.url}")
    private String url;

    @Value("${pgvector.datasource.username}")
    private String username;

    @Value("${pgvector.datasource.password}")
    private String password;

    @Bean(name = "pgvectorDataSource")
    public DataSource pgvectorDataSource() {
        // pgvector 전용 데이터소스 (기본값은 로컬 Docker 기준)
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .build();
    }

    @Bean(name = "pgvectorJdbcTemplate")
    public JdbcTemplate pgvectorJdbcTemplate(DataSource pgvectorDataSource) {
        return new JdbcTemplate(pgvectorDataSource);
    }
}
