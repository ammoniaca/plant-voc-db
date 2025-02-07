package org.cnr.plantvocdb.controller;

import jakarta.validation.Valid;
import org.cnr.plantvocdb.dto.PlantInfoDTO;
import org.cnr.plantvocdb.dto.RequestPlantVocDTO;
import org.cnr.plantvocdb.dto.ResponsePlantVocDTO;
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
     * GETs
     * */

    @GetMapping(
            value = "/plants",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<PlantInfoDTO> getPlantsVocInfo(){
        return service.getAllPlantsInfo();
    }

    /**
     * Get Plant Voc by ID
     */
    @GetMapping(
            value = "/plants/id/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getPlantById(@PathVariable("id") UUID id){
        Optional<ResponsePlantVocDTO> optionalResponsePlantVocDTO = service.getPlantById(id);
        return optionalResponsePlantVocDTO.map(
                responsePlantVocDTO -> ResponseEntity
                .status(HttpStatus.FOUND)
                .body(responsePlantVocDTO))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get Plant Voc by IPNI (International Plant Names Index) code
     */
    @GetMapping(
            value = "/plants/ipni/{ipni}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getPlantByIpni(@PathVariable("ipni") String ipni){
        Optional<ResponsePlantVocDTO> optionalResponsePlantVocDTO = service.getPlantByIpni(ipni);
        return optionalResponsePlantVocDTO.map(
                        responsePlantVocDTO -> ResponseEntity
                                .status(HttpStatus.FOUND)
                                .body(responsePlantVocDTO))
                .orElse(ResponseEntity.notFound().build());
    }



    /**
     * Get List of Plant always emitters (i.e.,always true)
     */
    @GetMapping(
            value = "/plants/always-emitters",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<PlantInfoDTO> getPlantsAlwaysEmitter(){
        return service.getAlwaysEmitters();
    }

    /**
     * Get List of Plant never emitters (i.e., always false)
     */
    @GetMapping(
            value = "/plants/never-emitters",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<PlantInfoDTO> getPlantsNeverEmitter(){
        return service.getNeverEmitters();
    }

    /**
     * Get List of Plant mixed emitters (i.e., sometimes true and sometimes false)
     */
    @GetMapping(
            value = "/plants/mixed-emitters",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<PlantInfoDTO> getPlantsMixedEmitter(){
        return service.getMixedEmitters();
    }

    /*
    * POSTs
    * */

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
        System.out.println("ciao");
        String name = plantDTO.getName();

        ResponsePlantVocDTO newPlant = service.createPlantVoc(plantDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newPlant);
    }

}
