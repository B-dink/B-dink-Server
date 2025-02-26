package com.app.bdink.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .components(new Components())
            .info(info())
            .servers(servers());
    }

    private Info info() {
        return new Info()
            .title("Bdink API")
            .description("버딩크 서버")
            .version("1.0.0");
    }

    private List<Server> servers() {
        return List.of(new Server()
            .url("http://localhost:8080")
            .description("Configured Server")
        );
    }
}
