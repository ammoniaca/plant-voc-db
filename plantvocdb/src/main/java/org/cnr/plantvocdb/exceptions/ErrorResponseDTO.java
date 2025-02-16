package org.cnr.plantvocdb.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;


@Setter
@Getter
@Builder
public class ErrorResponseDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private LocalDateTime timestamp;

    @JsonProperty("status")
    private int status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("details")
    private String details;

   @JsonProperty("instance")
    private String instance;

    public ErrorResponseDTO(LocalDateTime timestamp, int status, String message, String details, String instance) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.details = details;
        this.instance = instance;
    }

}
