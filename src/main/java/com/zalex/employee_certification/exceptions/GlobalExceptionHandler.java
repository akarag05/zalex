package com.zalex.employee_certification.exceptions;

import com.zalex.employee_certification.dtos.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.exc.UnrecognizedPropertyException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDto> handleResponseStatusException(ResponseStatusException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ex.getReason());
        return new ResponseEntity<>(errorResponseDto, ex.getStatusCode());
    }

    @ExceptionHandler(UnrecognizedPropertyException.class)
    public ResponseEntity<ErrorResponseDto> handleUnknownField(UnrecognizedPropertyException ex) {
        String field = ex.getPropertyName();

        String message = "Unknown field in request body: '" + field + "'";
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(message);
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }
}
