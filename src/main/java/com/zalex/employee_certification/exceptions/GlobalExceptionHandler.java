package com.zalex.employee_certification.exceptions;

import com.zalex.employee_certification.dtos.ErrorResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDto> handleResponseStatusException(ResponseStatusException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ex.getReason());
        return new ResponseEntity<>(errorResponseDto, ex.getStatusCode());
    }
}
