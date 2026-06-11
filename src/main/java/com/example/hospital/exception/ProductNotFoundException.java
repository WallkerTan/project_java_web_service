package com.example.hospital.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);// truyền tb lỗi lên runtimeexxception
    }

}
