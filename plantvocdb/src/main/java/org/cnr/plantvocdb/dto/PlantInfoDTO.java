package org.cnr.plantvocdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.cnr.plantvocdb.enums.PlantsEmitterType;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PlantInfoDTO {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("name")
    private String fullNameNoAuthors;

    @JsonProperty("updated")
    private boolean updated;

    @JsonProperty("emitter")
    private PlantsEmitterType emitterType;

}
