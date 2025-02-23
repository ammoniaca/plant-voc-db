package org.cnr.plantvocdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.cnr.plantvocdb.enums.LeafHabitus;
import org.cnr.plantvocdb.enums.PlantsRanks;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ResponsePlantDTO {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("ipni")
    private String ipni;

    @JsonProperty("fullNameWithAuthors")
    private String fullNamePlain;

    @JsonProperty("fullNameNoAuthors")
    private String fullNameNoAuthorsPlain;

    @JsonProperty("name")
    private String name;

    @JsonProperty("family")
    private String family;

    @JsonProperty("genus")
    private String genus;

    @JsonProperty("species")
    private String species;

    @JsonProperty("validNomenclature")
    private boolean validNomenclature;

    @JsonProperty("rank")
    private PlantsRanks rank;

    @JsonProperty("leafHabitus")
    private LeafHabitus leafHabitus;

    @JsonProperty("synonyms")
    private Set<String> synonyms;

    @JsonProperty("emitter")
    private List<PlantEmitterDTO> emitter;

    @JsonProperty("created")
    private OffsetDateTime createdDatetimeUTC;

    @JsonProperty("updated")
    private OffsetDateTime updatedDatetimeUTC;

}
