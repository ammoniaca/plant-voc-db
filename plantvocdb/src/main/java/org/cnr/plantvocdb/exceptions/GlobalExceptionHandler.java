package org.cnr.plantvocdb.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

public class GlobalExceptionHandler {

    @ExceptionHandler(PlantNotFoundException.class)
    public ResponseEntity<?> handlePlantNotFoundException(PlantNotFoundException ex) {
      ErrorResponseDTO plantNotFound = ErrorResponseDTO
              .builder()
              .timestamp(LocalDateTime.now())
              .message(ex.getMessage())
              .details("Plant not found")
              .build();

      return ResponseEntity
              .status(HttpStatus.NOT_FOUND)
              .body(plantNotFound);
    }

}
