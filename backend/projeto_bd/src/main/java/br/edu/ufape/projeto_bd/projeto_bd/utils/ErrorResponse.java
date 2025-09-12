package br.edu.ufape.projeto_bd.projeto_bd.utils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private Map<String, List<String>> errors;

    public ErrorResponse(LocalDateTime timestamp, int status, String error,
            String message, Map<String, List<String>> errors) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.errors = errors;
    }

}
