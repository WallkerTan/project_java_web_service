package com.example.hospital.exception.handler;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.hospital.exception.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalException {

    // Loại 1 — bắt lỗi nghiệp vụ cụ thể (tự định nghĩa)
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlerProductNotFoundException(
            ProductNotFoundException e) {

        log.warn("khong tim thay du lieu", e.getMessage());

        Map<String, String> res = new HashMap<>();
        res.put("error", "Not_found");
        res.put("message", e.getMessage());
        return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    }

    // Loại 2 — bắt lỗi validator
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException e) {
        log.warn("validator false");
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // Loại 3 — bắt lỗi Spring Security (role không đủ quyền)
    // @ExceptionHandler(AccessDeniedException.class)
    // public ResponseEntity<Map<String, String>> handleAccessDenied(AccessDeniedException e) {
    //     log.warn("Access denied: {}", e.getMessage());

    //     Map<String, String> res = new HashMap<>();
    //     res.put("error", "Forbidden");
    //     res.put("message", "Quyền hạn không đủ để truy cập, vui lòng đăng nhập lại");

    //     return new ResponseEntity<>(res, HttpStatus.FORBIDDEN); // 403
    // }

    // Loại 4 — lưới cuối, bắt TẤT CẢ còn lại
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handlerGlobleException(Exception e) {
        log.error("loi he thong", e);

        Map<String, String> res = new HashMap<>();
        res.put("error", "Internal Server Error");
        res.put("message", "Hệ thống đang có lỗi, vui lòng thử lại sau");

        return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
