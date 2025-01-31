package org.cnr.plantvocdb.service;

import org.cnr.plantvocdb.dto.RequestPlantVocDTO;
import org.cnr.plantvocdb.dto.ResponsePlantVocDTO;
import org.cnr.plantvocdb.entity.PlantVocEntity;
import org.cnr.plantvocdb.repository.PlantsVocRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
public class PlantsVocService {

    private final PlantsVocRepository repository;
    private final ModelMapper mapper;


    @Autowired
    public PlantsVocService(PlantsVocRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;

    }

    public ResponsePlantVocDTO CreateNewPlantVoc(RequestPlantVocDTO plant){

        // map DTO to Entity
        PlantVocEntity plantEntity = this.mapper.map(plant, PlantVocEntity.class);

        // set datetime in UTC
        OffsetDateTime odt = OffsetDateTime.now(ZoneOffset.UTC);
        plantEntity.setCreatedDatetimeUTC(odt);
        plantEntity.setUpdatedDatetimeUTC(odt);

        //

        return null;
    }


}
