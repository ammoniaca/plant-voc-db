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

@RestController
@RequestMapping("/api/v1/voc")
public class PlantsVocController {


    private final PlantsVocService service;

    @Autowired
    public PlantsVocController(PlantsVocService service) {
        this.service = service;
    }


    @GetMapping(
            value = "/hello"
    )
    public String getHello(){
        return "Hello World";

    }

    @GetMapping(
            value = "/plants",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<PlantInfoDTO> getPlantsVocInfo(){
        return service.getAllPlantsInfo();
    }

    @GetMapping(
            value = "/plants/always-emitters",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<PlantInfoDTO> getPlantsAlwaysEmitter(){
        return service.getAlwaysEmitters();
    }

    @GetMapping(
            value = "/plants/never-emitters",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<PlantInfoDTO> getPlantsNeverEmitter(){
        return service.getNeverEmitters();
    }

    @GetMapping(
            value = "/plants/mixed-emitters",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<PlantInfoDTO> getPlantsMixedEmitter(){
        return service.getMixedEmitters();
    }


    @PostMapping(
            value = "/plants",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createPlant(
            @RequestBody
            @Valid
            RequestPlantVocDTO plantDTO
    ){
        System.out.println("ciao");
        String name = plantDTO.getName();

        ResponsePlantVocDTO newPlant = service.CreateNewPlantVoc(plantDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newPlant);
    }

}
