package com.app.bdink.global.exception;

import com.app.bdink.global.exception.Error;
import com.app.bdink.global.template.RspTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomEntryPoint implements AuthenticationEntryPoint {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8"); // 문자 인코딩 설정
        response.getWriter().write(
                objectMapper.writeValueAsString(RspTemplate.error(Error.UNAUTHORIZED_ACCESS, Error.UNAUTHORIZED_ACCESS.getMessage())));
    }
}
