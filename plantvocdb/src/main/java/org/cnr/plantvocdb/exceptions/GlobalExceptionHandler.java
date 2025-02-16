package org.cnr.plantvocdb.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PlantNotFoundException.class)
    public ResponseEntity<?> handlePlantNotFoundException(PlantNotFoundException ex) {
      ErrorResponseDTO plantNotFound = ErrorResponseDTO
              .builder().timestamp(LocalDateTime.now())
              .status(HttpStatus.NOT_FOUND.value())
              .details(ex.getMessage())
              .message(HttpStatus.NOT_FOUND.name())
              .build();

      return ResponseEntity
              .status(HttpStatus.NOT_FOUND)
              .body(plantNotFound);
    }

}
