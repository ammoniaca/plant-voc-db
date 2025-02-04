package org.cnr.plantvocdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.cnr.plantvocdb.enums.LeafHabitus;
import org.cnr.plantvocdb.enums.PlantsRanks;

import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RequestPlantVocDTO {

    @JsonProperty("ipni")
    private String ipni;

    @JsonProperty("fullNameWithAuthors")
    private String fullNamePlain;

    @JsonProperty("fullNameNoAuthors")
    private String fullNameNoAuthorsPlain;

    @NotEmpty(message = "Plant name cannot be empty.")
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
    private Set<PlantEmitterDTO> emitter;


}
