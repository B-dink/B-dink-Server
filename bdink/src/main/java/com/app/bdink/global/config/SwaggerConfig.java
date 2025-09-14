package com.app.bdink.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${prod-server-url}")
    private String prodServerUrl;

    @Value("${dev-server-url}")
    private String devServerUrl;

    @Bean
    public OpenAPI openAPI() {
        SecurityScheme apiKey = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("Bearer Token");

        return new OpenAPI()
            .components(new Components().addSecuritySchemes("Bearer Token", apiKey))
            .addSecurityItem(securityRequirement)
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
        Server prodServer = new Server()
                .url(prodServerUrl)
                .description("Configured Server");

        Server devServer = new Server()
                .url(devServerUrl)
                .description("Development Server");

        return List.of(prodServer, devServer);
    }
}
