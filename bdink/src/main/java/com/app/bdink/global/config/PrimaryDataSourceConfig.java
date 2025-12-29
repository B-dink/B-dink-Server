package com.app.bdink.global.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class PrimaryDataSourceConfig {
    // 기존 spring.datasource.* 설정을 기본 DataSource로 사용
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSourceProperties primaryDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource primaryDataSource(DataSourceProperties primaryDataSourceProperties) {
        // spring.datasource.* 로드된 값으로 DataSource 생성
        return primaryDataSourceProperties.initializeDataSourceBuilder().build();
    }
}
