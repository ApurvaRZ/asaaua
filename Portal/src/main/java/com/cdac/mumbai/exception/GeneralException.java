package com.cdac.mumbai.exception;


import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {
    private final String Error;

    public GeneralException(String emailError) {
        this.Error = emailError;
    }
}
