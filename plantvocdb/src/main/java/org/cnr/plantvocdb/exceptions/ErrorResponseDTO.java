package org.cnr.plantvocdb.exceptions;

import lombok.*;

import java.time.LocalDateTime;


@Setter
@Getter
@Builder
public class ErrorResponseDTO {

    private LocalDateTime timestamp;
    private int status;
    private String message;
    private String details;

    public ErrorResponseDTO(LocalDateTime timestamp, int status, String message, String details) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.details = details;
    }

}
