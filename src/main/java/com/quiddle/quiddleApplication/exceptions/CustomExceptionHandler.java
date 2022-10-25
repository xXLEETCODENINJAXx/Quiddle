package com.quiddle.quiddleApplication.exceptions;

import com.quiddle.quiddleApplication.responses.ApiResponse;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request)
    {
        return buildResponse(ex, "A critical error occurred, kindly contact support for more info", Optional.of(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> handleAllExceptions(
            AccessDeniedException ex,
            WebRequest request
    ) {
        return buildResponse(ex, "Ensure you have the right role and/or permission", Optional.of(HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(ApplicationException.class)
    protected ResponseEntity<Object> handleAllApplicationExceptions(
            ApplicationException ex,
            WebRequest request
    ) {
        return buildResponse(ex, ex.getMessage(), Optional.of(HttpStatus.UNPROCESSABLE_ENTITY));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                                                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                                .collect(Collectors.joining( "," ));

        return buildResponse(ex, message, Optional.of(HttpStatus.BAD_REQUEST));
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponse(ex, ex.getMessage(), Optional.of(HttpStatus.BAD_REQUEST));
    }

    private ResponseEntity<Object> buildResponse(Exception exception, String message,  Optional<HttpStatus> statusOptional) {
        ApiResponse errorResponse = new ApiResponse(null, message, ApiResponse.ERROR);

        return new ResponseEntity<>(errorResponse, statusOptional.orElse(HttpStatus.UNPROCESSABLE_ENTITY));
    }
}
