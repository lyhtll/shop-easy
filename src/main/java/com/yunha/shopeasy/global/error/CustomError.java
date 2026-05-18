package com.yunha.shopeasy.global.error;

import org.springframework.http.HttpStatus;

public interface CustomError {
    String getCode();
    String getMessage();
    HttpStatus getStatus();
}