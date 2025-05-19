package com.charity.charityapp.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }

}
