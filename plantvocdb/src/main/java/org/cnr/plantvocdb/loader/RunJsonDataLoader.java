package org.cnr.plantvocdb.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cnr.plantvocdb.dto.RequestPlantDTO;
import org.cnr.plantvocdb.entity.PlantEntity;
import org.cnr.plantvocdb.repository.PlantsRepository;
import org.springframework.boot.CommandLineRunner;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;


@Component
public class RunJsonDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(RunJsonDataLoader.class);
    private final PlantsRepository repository;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    public RunJsonDataLoader(PlantsRepository repository, ObjectMapper mapper, ModelMapper modelMapper){
        this.repository = repository;
        this.objectMapper = mapper;
        this.modelMapper = modelMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        if(repository.count() == 0){
            try(InputStream inputStream = TypeReference.class.getResourceAsStream("/data/plants.json")){
                PlantsDTO allPlants = objectMapper.readValue(inputStream, PlantsDTO.class);
                log.info("Reading {} runs from JSON data and saving to in-memory collection.", allPlants.plants().size());
                OffsetDateTime odt = OffsetDateTime.now(ZoneOffset.UTC);
                List<PlantEntity> plantList = new ArrayList<>();
                for (RequestPlantDTO plant : allPlants.plants()) {
                    PlantEntity plantEntity = modelMapper.map(plant, PlantEntity.class);
                    plantEntity.setCreatedDatetimeUTC(odt);
                    plantEntity.setUpdatedDatetimeUTC(odt);
                    plantEntity.getEmitter().forEach(it -> it.setPlant(plantEntity));
                    plantList.add(plantEntity);
                }
                repository.saveAll(plantList);
            } catch (IOException e) {
                throw new RuntimeException("Failed to read JSON data", e);
            }
        } else {
            log.info("Not loading Runs from JSON data because the collection contains data.");
        }
    }
}
