package org.cnr.plantvocdb.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Arrays;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PlantNotFoundException.class)
    public ResponseEntity<?> handlePlantNotFoundException(
            PlantNotFoundException ex,
            WebRequest request
    ) {
        ErrorResponseDTO plantNotFound = ErrorResponseDTO
                .builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .details(ex.getMessage())
                .message(HttpStatus.NOT_FOUND.name())
                .instance(getPath(request))
                .build();

      return ResponseEntity
              .status(HttpStatus.NOT_FOUND)
              .body(plantNotFound);
    }

    @ExceptionHandler(DoiNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleDoiNotFoundException(
            DoiNotValidException ex,
            WebRequest request
    ) {
        ErrorResponseDTO doiNotValid = ErrorResponseDTO
                .builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .details(ex.getMessage())
                .message(HttpStatus.BAD_REQUEST.name())
                .instance(getPath(request))
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(doiNotValid);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(
            HttpMessageNotReadableException exception)
    {
        String errorDetails = "";

        if (exception.getCause() instanceof InvalidFormatException) {
            InvalidFormatException ifx = (InvalidFormatException) exception.getCause();
            if (ifx.getTargetType()!=null && ifx.getTargetType().isEnum()) {
                errorDetails = String.format("Invalid enum value: '%s' for the field: '%s'. The value must be one of: %s.",
                        ifx.getValue(), ifx.getPath().get(ifx.getPath().size()-1).getFieldName(), Arrays.toString(ifx.getTargetType().getEnumConstants()));
            }
        }
        ErrorResponseDTO errorResponse = ErrorResponseDTO
                .builder()
                .timestamp(LocalDateTime.now())
                .details(errorDetails)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private String getPath(WebRequest request){
        return request
                .getDescription(false)
                .replace("uri=", "");
    }

}
