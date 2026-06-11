package com.example.hospital.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GlobalFuntion {

    // Hàm lấy ngày giờ hiện tại
    public static LocalDateTime getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        // return LocalDateTime.now().format(formatter);
        return LocalDateTime.now();
    }
}
