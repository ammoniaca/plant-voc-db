package org.cnr.plantvocdb.controller;

import jakarta.validation.Valid;
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
        return service.retrieveAllPlantsInfo();
    }

    /**
     * Get a single plant VOC by ID
     */
    @GetMapping(
            value = "/plants/id/{plantId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getPlantById(@PathVariable("plantId") UUID plantId){
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
    public ResponseEntity<?> getPlantByIpni(@PathVariable("ipni") String ipni){
        Optional<ResponsePlantVocDTO> optionalResponsePlantVocDTO = service.retrievePlantByIpni(ipni);
        return optionalResponsePlantVocDTO.map(
                        responsePlantVocDTO -> ResponseEntity
                                .status(HttpStatus.FOUND)
                                .body(responsePlantVocDTO))
                .orElse(ResponseEntity.notFound().build());
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

        return service.retrievePlantsByFamily(family);

    }

    /**
     * Get a list of plants VOC by genus attribute
     */
    @GetMapping(
            value = "/plants/genera/{genus}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ResponsePlantVocDTO> getPlantsByGenus(@PathVariable("genus") String genus){

        return service.retrievePlantsByGenus(genus);

    }

    /**
     * Get a list of plants VOC by rank attribute
     */
    @GetMapping(
            value = "/plants/ranks/{rank}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ResponsePlantVocDTO> getPlantsByRank(@PathVariable("rank") PlantsRanks rank){

        return service.retrieveByRank(rank);

    }

    /**
     * Get a List of plants VOC by habitus attribute
     */
    @GetMapping(
            value = "/plants/leaf-habitus/{leaf-habitus}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ResponsePlantVocDTO> getPlantsByLeafHabitus(@PathVariable("leaf-habitus") LeafHabitus leafHabitus){

        return service.retrievePlantsByLeafHabitus(leafHabitus);
    }

    /**
     * Get list of plants VOC always emitters (i.e.,always true)
     */
    @GetMapping(
            value = "/plants/always-emitters",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ResponsePlantVocDTO> getPlantsAlwaysEmitter(){

        return service.getAlwaysEmitters();
    }

    /**
     * Get list of plants VOC never emitters (i.e., always false)
     */
    @GetMapping(
            value = "/plants/never-emitters",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ResponsePlantVocDTO> getPlantsNeverEmitter(){

        return service.getNeverEmitters();
    }

    /**
     * Get list of plants VOC mixed emitters (i.e., sometimes true and sometimes false)
     */
    @GetMapping(
            value = "/plants/mixed-emitters",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ResponsePlantVocDTO> getPlantsMixedEmitter(){

        return service.getMixedEmitters();
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
