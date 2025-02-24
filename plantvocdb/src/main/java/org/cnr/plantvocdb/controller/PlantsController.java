package org.cnr.plantvocdb.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.cnr.plantvocdb.dto.PlantInfoDTO;
import org.cnr.plantvocdb.dto.RequestPlantDTO;
import org.cnr.plantvocdb.dto.ResponsePlantDTO;
import org.cnr.plantvocdb.enums.LeafHabitus;
import org.cnr.plantvocdb.enums.PlantsEmitterType;
import org.cnr.plantvocdb.enums.PlantsRanks;
import org.cnr.plantvocdb.exceptions.ErrorResponseDTO;
import org.cnr.plantvocdb.exceptions.PlantNotFoundException;
import org.cnr.plantvocdb.service.PlantsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;


import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/isoprene")
@Tag(name = "Plant isoprene APIs")
public class PlantsController {

    private final PlantsService service;

    @Autowired
    public PlantsController(PlantsService service) {

        this.service = service;
    }


    /*
     *
     * Endpoints GET
     *
     * */

    /**
     * Get a list of all plants isoprene as infos
     */
    @Operation(
            summary = "Search all the information about the plant collection in the database."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @GetMapping(
            value = "/plants",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<PlantInfoDTO> getPlantsVocInfo(){
        List<PlantInfoDTO> plantInfos = service.findAllPlantsInfo();
        if(plantInfos.isEmpty()){
            throw new PlantNotFoundException("Plants not found.");
        }
        return plantInfos;
    }

    /**
     * Get a single plant isoprene by ID
     */
    @Operation(
            summary = "Search plant by ID.",
            description = "This endpoint allows users to search for a specific plant using the unique ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @GetMapping(
            value = "/plants/id/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponsePlantDTO> getPlantById(
            @Valid
            @PathVariable("id") UUID plantId
    ){
        Optional<ResponsePlantDTO> optionalResponsePlantDTO = service.findPlantById(plantId);
        return optionalResponsePlantDTO.map(
                responsePlantVocDTO -> ResponseEntity
                .status(HttpStatus.OK)
                .body(responsePlantVocDTO))
                .orElseThrow(() -> new PlantNotFoundException(
                        MessageFormat.format("Plant not found with id: {0}.", plantId.toString()))
                );
    }

    /**
     * Get a single plant isoprene by IPNI (International Plant Names Index) code.
     */
    @Operation(
            summary = "Search plant by IPNI (International Plant Names Index) code.",
            description = "This endpoint allows users to search for a specific plant using the unique IPNI " +
                    "(International Plant Names Index) code. "
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @GetMapping(
            value = "/plants/ipni/{ipni}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponsePlantDTO> getPlantByIpni(
            @PathVariable("ipni") String ipni
    ){
        Optional<ResponsePlantDTO> optionalResponsePlantDTO = service.findPlantByIpni(ipni);
        return optionalResponsePlantDTO.map(
                        responsePlantVocDTO -> ResponseEntity
                                .status(HttpStatus.OK)
                                .body(responsePlantVocDTO))
                .orElseThrow(() -> new PlantNotFoundException(
                        MessageFormat.format("Plant not found with ipni code: {0}.", ipni))
                );
    }


    /**
     * Get a plant genus and species attributes.
     */
    @Operation(
            summary = "Search plant by genus and species attributes.",
            description = "This endpoint allows users to search for a specific plant using " +
                    "genus and species attributes."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @GetMapping(
            value = "/plants/species",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponsePlantDTO> getPlantBySpecies(
            @RequestParam(value = "genus") String genus,
            @RequestParam(value = "species") String species)
    {
        String genusSanitized = StringUtils.normalizeSpace(StringUtils.capitalize(genus.toLowerCase()));
        String speciesSanitized = StringUtils.normalizeSpace(species.toLowerCase());
        Optional<ResponsePlantDTO> optionalResponsePlantDTO = service
                .findPlantBySpecies(
                        genusSanitized,
                        speciesSanitized
                );
        return optionalResponsePlantDTO.map(
                        responsePlantVocDTO -> ResponseEntity
                                .status(HttpStatus.OK)
                                .body(responsePlantVocDTO))
                .orElseThrow(() -> new PlantNotFoundException(
                        MessageFormat.format("Plant {0} {1} not found.",
                                genusSanitized,
                                speciesSanitized))
                );
    }

    /**
     * Get a list of plants isoprene by name attribute
     */
    @Operation(
            summary = "Search plants by name.",
            description = "This endpoint allows users to search plants by using the name attribute name."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @GetMapping(
            value = "/plants/names/{name}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ResponsePlantDTO> getPlantsByName(
            @Valid
            @PathVariable("name") String name
    ){
        List<ResponsePlantDTO> plants = service.findPlantsByName(name);
        if(plants.isEmpty()){
            String errorMessage = MessageFormat.format("Plants not found with name: {0}.",
                    StringUtils.normalizeSpace(name.toLowerCase()));
            throw new PlantNotFoundException(errorMessage);
        }
        return plants;
    }

    /**
     * Get a list of plants isoprene by family attribute
     */
    @Operation(
            summary = "Search plants by the name of the botanical family.",
            description = "This endpoint allows users to search for plants in the database using the botanical " +
                    "family as a search key. By providing the name of a botanical family, users can retrieve a list " +
                    "of plants that belong to that family, along with relevant details such as scientific name, " +
                    "common name, leaf habitus, and whether the plant emits isoprene."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @GetMapping(
            value = "/plants/families/{family}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ResponsePlantDTO> getPlantsByFamily(
            @Valid
            @PathVariable("family") String family
    ){
        String familySanitized = StringUtils
                .normalizeSpace(StringUtils
                        .capitalize(family.toLowerCase()));
        List<ResponsePlantDTO> plants = service.findPlantsByFamily(familySanitized);
        if(plants.isEmpty()){
            String errorMessage = MessageFormat
                    .format("Plants not found with family: {0}.", familySanitized);
            throw new PlantNotFoundException(errorMessage);
        }
        return plants;

    }

    /**
     * Get a list of plants isoprene by genus attribute
     */
    @Operation(
            summary = "Search plants by the name of the botanical genus.",
            description = "This endpoint allows users to search for plants in the database using the botanical " +
                    "genus as a search key. By providing the name of a botanical genus, users can retrieve a list " +
                    "of plants that belong to that family, along with relevant details such as scientific name, " +
                    "common name, leaf habitus, and whether the plant emits isoprene."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @GetMapping(
            value = "/plants/genera/{genus}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ResponsePlantDTO> getPlantsByGenus(
            @Valid
            @PathVariable("genus") String genus
    ){
        String genusSanitized = StringUtils
                .normalizeSpace(StringUtils.capitalize(genus.toLowerCase()));
        List<ResponsePlantDTO> plants = service.findPlantsByGenus(genusSanitized);
        if(plants.isEmpty()){
            String errorMessage = MessageFormat
                    .format("Plants not found with genus: {0}.", genusSanitized);
            throw new PlantNotFoundException(errorMessage);
        }
        return plants;
    }

    /**
     * Get a list of plants isoprene by rank attribute
     */
    @Operation(
            summary = "Search plants by botanical rank.",
            description = "This endpoint allows users to search for plants in the database using the botanical " +
                    "rank (i.e., species, subspecies, variety, hybrid, and form) as a search key. By providing a " +
                    "botanical rank, users can retrieve a list of plants that belong to that family, along " +
                    "with relevant details such as scientific name, common name, leaf habitus, and whether " +
                    "the plant emits isoprene."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )}
    )
    @GetMapping(
            value = "/plants/ranks/{rank}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ResponsePlantDTO> getPlantsByRank(
            @Valid
            @PathVariable("rank") PlantsRanks rank
    ){
        List<ResponsePlantDTO> plants = service.findPlantsByRank(rank);
        if (plants.isEmpty()){
            String errorMessage = MessageFormat.format(
                    "Plants not found with rank: {0}.",
                    rank.name().toLowerCase());
            throw new PlantNotFoundException(errorMessage);
        }
        return plants;
    }

    /**
     * Get a List of plants isoprene by habitus attribute
     */
    @Operation(
            summary = "Search plants by leaf habitus.",
            description = "This endpoint allows users to search for plants in the database using the leaf " +
                    "habitus (i.e.,  annual, evergreen, and deciduous) as a search key. By providing a leaf " +
                    "habitus, users can retrieve a list of plants that belong to that family, along with relevant " +
                    "details such as scientific name, common name, leaf habitus, and whether the plant " +
                    "emits isoprene."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )}
    )
    @GetMapping(
            value = "/plants/leaf-habitus/{leaf-habitus}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ResponsePlantDTO> getPlantsByLeafHabitus(
            @Valid
            @PathVariable("leaf-habitus") LeafHabitus leafHabitus
    ){
        List<ResponsePlantDTO> plants = service.findPlantsByLeafHabitus(leafHabitus);
        if(plants.isEmpty()){
            String errorMessage = MessageFormat.format(
                    "Plants not found with leaf habitus: {0}.",
                    leafHabitus.name().toLowerCase());
            throw new PlantNotFoundException(errorMessage);
        }
        return plants;
    }

    /**
     * Get list of plants isoprene always emitters (i.e.,always true)
     */
    @Operation(
            summary = "Search plants that have consistently emitted isoprene in experiments.",
            description = "This endpoint allows users to search for plants in the database that have consistently " +
                    "reported emitting isoprene in experiments.",
            hidden = true
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )}
    )
    @GetMapping(
            value = "/plants/always-emitters",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ResponsePlantDTO> getPlantsAlwaysEmitter(){
        List<ResponsePlantDTO> plants = service.findAllPlantsAlwaysEmitters();
        if(plants.isEmpty()){
            throw new PlantNotFoundException("Plants always emitter not found.");
        }
        return plants;
    }

    /**
     * Get list of plants isoprene never emitters (i.e., always false)
     */
    @Operation(
            summary = "Search plants that have never emitted isoprene in experiments.",
            description = "This endpoint allows users to search for plants that have never reported emitting " +
                    "isoprene in experiments.",
            hidden = true
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )}
    )
    @GetMapping(
            value = "/plants/never-emitters",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ResponsePlantDTO> getPlantsNeverEmitter(){
        List<ResponsePlantDTO> plants = service.findAllPlantsNeverEmitters();
        if(plants.isEmpty()){
            throw new PlantNotFoundException("Plants never emitter not found.");
        }
        return plants;
    }

    /**
     * Get list of plants isoprene mixed emitters (i.e., sometimes true and sometimes false)
     */
    @Operation(
            summary = "Search plants that have been shown to emmit isoprene in some experiments and not in others.",
            description = "This endpoint allows users to search for plants that have been shown to emit " +
                    "isoprene in some experiments and not in others.",
            hidden = true
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )}
    )
    @GetMapping(
            value = "/plants/mixed-emitters",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ResponsePlantDTO> getPlantsMixedEmitter(){
        List<ResponsePlantDTO> plants = service.findAllPlantsMixedEmitters();
        if(plants.isEmpty()){
            throw new PlantNotFoundException("Plants mixed emitter not found.");
        }
        return plants;
    }

    /**
     * Get list of plants isoprene emitters (always, never, mixed)
     */
    @Operation(
            summary = "Search plants always, never, or mixed isoprene emitter.",
            description = "This endpoint allows users to search for plants always emitter (plants that have " +
                    "consistently reported emitting isoprene in experiments), never emitter " +
                    "(plants that have never reported emitting isoprene in experiments), or " +
                    "mixed emitter (i.e., plants that have been shown to emit isoprene in some experiments " +
                    "and not in others)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )}
    )
    @GetMapping(
            value = "/plants/emitters",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ResponsePlantDTO> getEmitters(
            @RequestParam(value="emitter") PlantsEmitterType emitter
    ){
        List<ResponsePlantDTO> plants = service.findAllPlantsEmitters(emitter);
        if(plants.isEmpty()){
            String errorMessage = MessageFormat
                    .format("Plants {0} emitter not found.", emitter.name().toLowerCase());
            throw new PlantNotFoundException(errorMessage);
        }
        return plants;
    }

    /**
     * Get list of plants isoprene emitters (always, never, mixed) by leaf-habitus and family
     */
    @Operation(
            summary = "Search plants always, never, or mixed isoprene emitter by leaf-habitus and family.",
            description = "This endpoint allows users to search for plants always emitter (plants that have " +
                    "consistently reported emitting isoprene in experiments), never emitter " +
                    "(plants that have never reported emitting isoprene in experiments), or " +
                    "mixed emitter (i.e., plants that have been shown to emit isoprene in some experiments " +
                    "and not in others) by leaf-habitus and family."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )}
    )
    @GetMapping(
            value = "/plants/emitters/leaf-habitus/family",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ResponsePlantDTO> getPlantsEmitterTypeByFamilyAndLeafHabitus(
            @RequestParam(value="family") String family,
            @RequestParam(value="leafHabitus") LeafHabitus leafHabitus,
            @RequestParam(value="emitter") PlantsEmitterType emitter)
    {
        String familySanitized = StringUtils
                .normalizeSpace(StringUtils
                        .capitalize(family.toLowerCase()));
        List<ResponsePlantDTO> plants = service.findAllPlantsEmitterTypeByFamilyAndLeafHabitus(
                familySanitized,
                leafHabitus,
                emitter);
        if (plants.isEmpty()){
            String errorMessage = MessageFormat.format(
                    "Plants not found with family: {0}, leaf habitus: {1}, and emitter type: {2}",
                    familySanitized,
                    leafHabitus.name().toLowerCase(),
                    emitter.name().toLowerCase());
            throw new PlantNotFoundException(errorMessage);
        }
        return plants;
    }

    /**
     * Get list of plants isoprene emitters (always, never, mixed) by leaf-habitus and genus
     */
    @Operation(
            summary = "Search plants always, never, or mixed isoprene emitter by leaf-habitus and genus.",
            description = "This endpoint allows users to search for plants always emitter (plants that have " +
                    "consistently reported emitting isoprene in experiments), never emitter " +
                    "(plants that have never reported emitting isoprene in experiments), or " +
                    "mixed emitter (i.e., plants that have been shown to emit isoprene in some experiments " +
                    "and not in others) by leaf-habitus and genus."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )}
    )
    @GetMapping(
            value = "/plants/emitters/leaf-habitus/genus",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ResponsePlantDTO> getPlantsEmitterTypeByGenusAndLeafHabitus(
            @RequestParam(value="genus") String genus,
            @RequestParam(value = "leafHabitus") LeafHabitus leafHabitus,
            @RequestParam(value="emitter") PlantsEmitterType emitter)
    {
        String genusSanitized = StringUtils
                .normalizeSpace(StringUtils.capitalize(genus.toLowerCase()));
        List<ResponsePlantDTO> plants = service.findAllPlantsEmitterTypeByGenusAndLeafHabitus(
                genusSanitized,
                leafHabitus,
                emitter);
        if(plants.isEmpty()){
            String errorMessage = MessageFormat.format(
                    "Plants not found with family: {0}, leaf habitus: {1}, and emitter type: {2}",
                    genusSanitized,
                    leafHabitus.name().toLowerCase(),
                    emitter.name().toLowerCase());
            throw new PlantNotFoundException(errorMessage);
        }
        return plants;
    }



    /*
    *
    * Endpoints POST
    *
    * */

    /**
     * Post a single new plants isoprene
     */
    @PostMapping(
            value = "/plants",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> postNewPlant(
            @RequestBody
            @Valid
            RequestPlantDTO plantDTO
    ){
        ResponsePlantDTO newPlant = service.createPlantVoc(plantDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newPlant);
    }

    /*
     *
     * Endpoints DELETE
     *
     * */

    /**
     * Delete plant by ID
     */
    @Operation(
            summary = "Delete plant by ID.",
            description = "This endpoint allows users to delete a plant by ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )}
    )
    @DeleteMapping("/plants/id/{id}")
    public ResponseEntity<ResponsePlantDTO> deleteById(
            @PathVariable("id") UUID id
    ){
        Optional<ResponsePlantDTO> optionalResponsePlantDTO = service.findPlantById(id);
        if(optionalResponsePlantDTO.isPresent()){
            service.deleteById(optionalResponsePlantDTO.get());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(optionalResponsePlantDTO.get());
        } else {
            throw new PlantNotFoundException(
                    MessageFormat.format("Plant not found with id: {0}.", id.toString())
            );
        }
    }

    /**
     * Delete plant by genius and species attributes.
     */
    @Operation(
            summary = "Delete plant by genius and species attributes.",
            description = "This endpoint allows users to delete a plant by genius and species attributes."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )}
    )
    @DeleteMapping("/plants/species")
    public ResponseEntity<ResponsePlantDTO> deleteBySpecies(
            @RequestParam(value = "genus") String genus,
            @RequestParam(value = "species") String species)
    {
        String genusSanitized = StringUtils.normalizeSpace(StringUtils.capitalize(genus.toLowerCase()));
        String speciesSanitized = StringUtils.normalizeSpace(species.toLowerCase());
        Optional<ResponsePlantDTO> optionalResponsePlantDTO = service
                .findPlantBySpecies(genusSanitized, speciesSanitized);
        if(optionalResponsePlantDTO.isPresent()){
            ResponsePlantDTO plant = optionalResponsePlantDTO.get();
            service.deleteById(plant);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(plant);
        } else {
            throw new PlantNotFoundException(
                    MessageFormat.format("Plant {0} {1} not found.",
                            genusSanitized,
                            speciesSanitized));
        }
    }
}
