package com.example.hospital.exception;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        log.warn("401 Unauthorized: {}", authException.getMessage());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("code", "401");
        errorResponse.put("error", "Unauthorized");
        errorResponse.put("message", "Quyền hạn không đủ để truy cập, vui lòng đăng nhập lại");
        errorResponse.put("path", request.getRequestURI());

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
