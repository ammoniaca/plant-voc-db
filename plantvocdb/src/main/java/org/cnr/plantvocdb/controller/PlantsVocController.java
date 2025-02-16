package org.cnr.plantvocdb.controller;

import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.cnr.plantvocdb.dto.PlantInfoDTO;
import org.cnr.plantvocdb.dto.RequestPlantVocDTO;
import org.cnr.plantvocdb.dto.ResponsePlantVocDTO;
import org.cnr.plantvocdb.enums.LeafHabitus;
import org.cnr.plantvocdb.enums.PlantsRanks;
import org.cnr.plantvocdb.exceptions.PlantNotFoundException;
import org.cnr.plantvocdb.service.PlantsVocService;
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
@RequestMapping("/api/v1/voc")
public class PlantsVocController {

    private final PlantsVocService service;

    @Autowired
    public PlantsVocController(PlantsVocService service) {

        this.service = service;
    }


    /*
     *
     * Endpoints GET
     *
     * */

    /**
     * Get a list of all plants VOC as infos
     */
    @GetMapping(
            value = "/plants",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<PlantInfoDTO> getPlantsVocInfo(){
        List<PlantInfoDTO> plantInfos = service.retrieveAllPlantsInfo();
        if(plantInfos.isEmpty()){
            throw new PlantNotFoundException("Plants not found.");
        }
        return plantInfos;
    }

    /**
     * Get a single plant VOC by ID
     */
    @GetMapping(
            value = "/plants/id/{plantId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponsePlantVocDTO> getPlantById(@PathVariable("plantId") UUID plantId){
        Optional<ResponsePlantVocDTO> optionalResponsePlantVocDTO = service.retrievePlantById(plantId);
        return optionalResponsePlantVocDTO.map(
                responsePlantVocDTO -> ResponseEntity
                .status(HttpStatus.FOUND)
                .body(responsePlantVocDTO))
                .orElseThrow(() -> new PlantNotFoundException("Plant not found with id: " + plantId.toString()));
    }

    /**
     * Get a single plant VOC by IPNI (International Plant Names Index) code
     */
    @GetMapping(
            value = "/plants/ipni/{ipni}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponsePlantVocDTO> getPlantByIpni(@PathVariable("ipni") String ipni){
        Optional<ResponsePlantVocDTO> optionalResponsePlantVocDTO = service.retrievePlantByIpni(ipni);
        return optionalResponsePlantVocDTO.map(
                        responsePlantVocDTO -> ResponseEntity
                                .status(HttpStatus.FOUND)
                                .body(responsePlantVocDTO))
                .orElseThrow(() -> new PlantNotFoundException(
                        MessageFormat.format("Plant not found with ipni code: {0}.", ipni))
                );
    }

    /**
     * Get a list of plants VOC by name attribute
     */
    @GetMapping(
            value = "/plants/names/{name}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ResponsePlantVocDTO> getPlantsByName(
            @PathVariable("name") String name
    ){
        List<ResponsePlantVocDTO> plants = service.retrievePlantsByName(name);
        if(plants.isEmpty()){
            String errorMessage = MessageFormat.format("Plants not found with name: {0}.",
                    StringUtils.normalizeSpace(name.toLowerCase()));
            throw new PlantNotFoundException(errorMessage);
        }
        return plants;
    }

    /**
     * Get a list of plants VOC by family attribute
     */
    @GetMapping(
            value = "/plants/families/{family}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ResponsePlantVocDTO> getPlantsByFamily(@PathVariable("family") String family){
        List<ResponsePlantVocDTO> plants = service.retrievePlantsByFamily(family);
        if(plants.isEmpty()){
            String errorMessage = MessageFormat.format(
                    "Plants not found with family: {0}.",
                    StringUtils.normalizeSpace(StringUtils.capitalize(family.toLowerCase())));
            throw new PlantNotFoundException(errorMessage);
        }
        return plants;

    }

    /**
     * Get a list of plants VOC by genus attribute
     */
    @GetMapping(
            value = "/plants/genera/{genus}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ResponsePlantVocDTO> getPlantsByGenus(
            @PathVariable("genus") String genus
    ){
        List<ResponsePlantVocDTO> plants = service.retrievePlantsByGenus(genus);
        if(plants.isEmpty()){
            String errorMessage = MessageFormat.format(
                    "Plants not found with genus: {0}.",
                    StringUtils.normalizeSpace(StringUtils.capitalize(genus.toLowerCase())));
            throw new PlantNotFoundException(errorMessage);
        }
        return plants;
    }

    /**
     * Get a list of plants VOC by rank attribute
     */
    @GetMapping(
            value = "/plants/ranks/{rank}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ResponsePlantVocDTO> getPlantsByRank(
            @Valid
            @PathVariable("rank") PlantsRanks rank
    ){
        List<ResponsePlantVocDTO> plants = service.retrievePlantsByRank(rank);
        if (plants.isEmpty()){
            String errorMessage = MessageFormat.format(
                    "Plants not found with rank: {0}.",
                    rank.name().toLowerCase());
            throw new PlantNotFoundException(errorMessage);
        }
        return plants;
    }

    /**
     * Get a List of plants VOC by habitus attribute
     */
    @GetMapping(
            value = "/plants/leaf-habitus/{leaf-habitus}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ResponsePlantVocDTO> getPlantsByLeafHabitus(
            @PathVariable("leaf-habitus") LeafHabitus leafHabitus
    ){
        List<ResponsePlantVocDTO> plants = service.retrievePlantsByLeafHabitus(leafHabitus);
        if(plants.isEmpty()){
            String errorMessage = MessageFormat.format(
                    "Plants not found with leaf habitus: {0}.",
                    leafHabitus.name().toLowerCase());
            throw new PlantNotFoundException(errorMessage);
        }
        return plants;
    }

    /**
     * Get list of plants VOC always emitters (i.e.,always true)
     */
    @GetMapping(
            value = "/plants/always-emitters",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ResponsePlantVocDTO> getPlantsAlwaysEmitter(){
        List<ResponsePlantVocDTO> plants = service.getAlwaysEmitters();
        if(plants.isEmpty()){
            throw new PlantNotFoundException("Plants always emitter not found.");
        }
        return plants;
    }

    /**
     * Get list of plants VOC never emitters (i.e., always false)
     */
    @GetMapping(
            value = "/plants/never-emitters",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ResponsePlantVocDTO> getPlantsNeverEmitter(){
        List<ResponsePlantVocDTO> plants = service.getNeverEmitters();
        if(plants.isEmpty()){
            throw new PlantNotFoundException("Plants never emitter not found.");
        }
        return plants;
    }

    /**
     * Get list of plants VOC mixed emitters (i.e., sometimes true and sometimes false)
     */
    @GetMapping(
            value = "/plants/mixed-emitters",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ResponsePlantVocDTO> getPlantsMixedEmitter(){
        List<ResponsePlantVocDTO> plants = service.getMixedEmitters();
        if(plants.isEmpty()){
            throw new PlantNotFoundException("Plants mixed emitter not found.");
        }
        return plants;
    }

    /*
    *
    * Endpoints POST
    *
    * */

    /**
     * Post a single new plants VOC
     */
    @PostMapping(
            value = "/plants",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> postNewPlant(
            @RequestBody
            @Valid
            RequestPlantVocDTO plantDTO
    ){
        ResponsePlantVocDTO newPlant = service.createPlantVoc(plantDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newPlant);
    }

}
