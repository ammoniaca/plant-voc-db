package org.cnr.plantvocdb.service;

import org.cnr.plantvocdb.dto.PlantEmitterDTO;
import org.cnr.plantvocdb.dto.PlantInfoDTO;
import org.cnr.plantvocdb.dto.RequestPlantVocDTO;
import org.cnr.plantvocdb.dto.ResponsePlantVocDTO;
import org.cnr.plantvocdb.entity.PlantEmitterEntity;
import org.cnr.plantvocdb.entity.PlantVocEntity;
import org.cnr.plantvocdb.repository.PlantsVocRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class PlantsVocService {

    private final PlantsVocRepository repository;
    private final ModelMapper mapper;


    @Autowired
    public PlantsVocService(PlantsVocRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;

    }

    public List<PlantInfoDTO> getInfo() {
        List<PlantVocEntity> plantsEntity = repository
                .findAll()
                .stream()
                .toList();

        return plantsEntity
                .stream()
                .map(it -> mapper.map(it, PlantInfoDTO.class))
                .toList();
    }



    public List<ResponsePlantVocDTO> getAll() {
        // get all plants stored in the DB
        List<PlantVocEntity> plantsEntity = repository
                .findAll()
                .stream()
                .toList();

        return plantsEntity
                .stream()
                .map(it -> mapper.map(it, ResponsePlantVocDTO.class))
                .toList();
    }


    public ResponsePlantVocDTO CreateNewPlantVoc(RequestPlantVocDTO plantDTO){

        // map DTO to Entity
        PlantVocEntity plantEntity = mapper.map(plantDTO, PlantVocEntity.class);

        // set datetime in UTC
        OffsetDateTime odt = OffsetDateTime.now(ZoneOffset.UTC);
        plantEntity.setCreatedDatetimeUTC(odt);
        plantEntity.setUpdatedDatetimeUTC(odt);

        // iterate over the set to add plantEntity
        plantEntity.getEmitter().forEach(it -> it.setPlant(plantEntity));

        // save new plant in DB
        PlantVocEntity savedPlantEntity = repository.save(plantEntity);

        // map Entity to DTO
        return mapper.map(savedPlantEntity, ResponsePlantVocDTO.class);
    }


}
