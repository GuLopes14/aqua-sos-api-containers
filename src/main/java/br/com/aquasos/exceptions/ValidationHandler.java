package br.com.aquasos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ValidationHandler {

    record ValidationError(String campo, String mensagem) {
        public ValidationError(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ValidationError> handleValidation(MethodArgumentNotValidException e) {
        return e.getFieldErrors()
                .stream()
                .map(ValidationError::new)
                .toList();
    }

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus
    public Map<String, Object> handleResponseStatus(ResponseStatusException ex) {
        return Map.of(
                "status", ex.getStatusCode().value(),
                "erro", ex.getReason()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleGeneric(Exception ex) {
        return Map.of(
                "status", 500,
                "erro", "Erro interno no servidor",
                "detalhes", ex.getMessage()
        );
    }
}
