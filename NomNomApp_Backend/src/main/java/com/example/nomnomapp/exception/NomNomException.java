package com.example.nomnomapp.exception;

import io.micrometer.common.lang.NonNull;
import org.springframework.http.HttpStatus;

public class NomNomException extends RuntimeException{
    @NonNull
    private HttpStatus status;

    public NomNomException(@NonNull HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    @NonNull
    public HttpStatus getStatus() {
        return status;
    }
}
