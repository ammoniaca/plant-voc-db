package org.cnr.plantvocdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PlantInfoDTO {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("fullNameNoAuthors")
    private String fullNameNoAuthors;


}
