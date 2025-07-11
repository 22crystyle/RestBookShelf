package org.example.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.response.error.RestErrorResponse;
import org.example.dto.response.error.ValidationErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<RestErrorResponse> handleNotFoundException(EntityNotFoundException ex) {
        RestErrorResponse error = new RestErrorResponse(HttpStatus.NOT_FOUND.toString(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        ValidationErrorResponse response = new ValidationErrorResponse(status.toString(), "Validation Error", errors);
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<RestErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        Throwable cause = ex.getCause();
        String message = "Data integrity violation";
        if (cause instanceof org.hibernate.exception.ConstraintViolationException hce) {
            String constraint = hce.getConstraintName();
            message = "Нарушено ограничение: " + constraint;
        }
        RestErrorResponse error = new RestErrorResponse(HttpStatus.CONFLICT.toString(), message);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @Override
    public ResponseEntity<Object> handleExceptionInternal(
            Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request
    ) {
        RestErrorResponse errorResponse;

        if (body instanceof RestErrorResponse) {
            errorResponse = (RestErrorResponse) body;
        } else if (body instanceof ProblemDetail pd) {
            String fullMsg = ex.getMessage();
            String shortMsg = (fullMsg != null && fullMsg.contains(":"))
                    ? fullMsg.substring(0, fullMsg.indexOf(":"))
                    : fullMsg;
            errorResponse = RestErrorResponse.builder()
                    .code(statusCode.toString())
                    .message(shortMsg)
                    .build();
        } else {
            String message = body != null
                    ? body.toString()
                    : Optional.ofNullable(ex.getMessage()).orElse(statusCode.toString());

            errorResponse = RestErrorResponse.builder()
                    .code(statusCode.toString())
                    .message(message)
                    .build();
        }

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestErrorResponse> handleAll(Throwable ex) {
        RestErrorResponse error = new RestErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), ex.getMessage());
        log.error(Arrays.toString(ex.getStackTrace()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
