package com.yunha.shopeasy.global.error;

public record ErrorResponse(String code, String message, int status) {

    public static ErrorResponse of(CustomError error) {
        return new ErrorResponse(
                error.getCode(),
                error.getMessage(),
                error.getStatus().value()
        );
    }
}