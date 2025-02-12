package org.cnr.plantvocdb.service;

import org.apache.commons.lang3.StringUtils;
import org.cnr.plantvocdb.dto.PlantEmitterDTO;
import org.cnr.plantvocdb.dto.PlantInfoDTO;
import org.cnr.plantvocdb.dto.RequestPlantVocDTO;
import org.cnr.plantvocdb.dto.ResponsePlantVocDTO;
import org.cnr.plantvocdb.entity.PlantVocEntity;
import org.cnr.plantvocdb.enums.LeafHabitus;
import org.cnr.plantvocdb.enums.PlantsRanks;
import org.cnr.plantvocdb.repository.PlantsVocRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlantsVocService {

    private final PlantsVocRepository repository;
    private final ModelMapper mapper;


    @Autowired
    public PlantsVocService(PlantsVocRepository repository,
                            ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;

    }

    public List<PlantInfoDTO> retrieveAllPlantsInfo() {
        return repository
                .findAll()
                .stream()
                .map(it -> mapper.map(it, PlantInfoDTO.class))
                .toList();
    }

    public Optional<ResponsePlantVocDTO> retrievePlantById(UUID id) {
        Optional<PlantVocEntity> optionalPlantEntity = repository.findById(id);
        return optionalPlantEntity.map(it -> mapper.map(it, ResponsePlantVocDTO.class));
    }

    public Optional<ResponsePlantVocDTO> retrievePlantByIpni(String ipni){
        Optional<PlantVocEntity> optionalPlantEntity = repository.findByIpni(ipni);
        return optionalPlantEntity.map(it -> mapper.map(it, ResponsePlantVocDTO.class));
    }

    public List<ResponsePlantVocDTO> retrievePlantsByName(String name){
        // sanitize name attribute (i.e., normalize space and lower case)
        return repository
                .findByName(StringUtils
                        .normalizeSpace(name.toLowerCase()))
                .stream()
                .map(it -> mapper.map(it, ResponsePlantVocDTO.class))
                .toList();
    }

    public List<ResponsePlantVocDTO> retrievePlantsByFamily(String family){
        // sanitize family attribute (i.e., normalize space and Capitalized case)
        return repository
                .findByFamily(StringUtils
                        .normalizeSpace(StringUtils
                                .capitalize(family
                                        .toLowerCase())))
                .stream()
                .map(it -> mapper.map(it, ResponsePlantVocDTO.class))
                .toList();
    }

    public List<ResponsePlantVocDTO> retrievePlantsByGenus(String genus){
        // sanitize genus attribute (i.e., normalize space and Capitalized case)
        return repository.findByGenus(StringUtils
                        .normalizeSpace(StringUtils
                                .capitalize(genus
                                        .toLowerCase())))
                .stream()
                .map(it -> mapper.map(it, ResponsePlantVocDTO.class))
                .toList();
    }

    public List<ResponsePlantVocDTO> retrieveByRank(PlantsRanks rank){
        return repository.findByRank(rank)
                .stream()
                .map(it -> mapper.map(it, ResponsePlantVocDTO.class))
                .toList();
    }

    public List<ResponsePlantVocDTO> retrievePlantsByLeafHabitus(LeafHabitus leafHabitus){
        return repository
                .findByLeafHabitus(leafHabitus)
                .stream()
                .map(it -> mapper.map(it, ResponsePlantVocDTO.class))
                .toList();
    }

    public List<ResponsePlantVocDTO> getAlwaysEmitters(){
        List<ResponsePlantVocDTO> plants = new ArrayList<>();
        for (ResponsePlantVocDTO plant : getAllPlants()) {
            if(this.isAlwaysEmitter(plant)){
                plants.add(plant);
            };
        }
        return plants;
    }

    public List<ResponsePlantVocDTO> getNeverEmitters(){
        List<ResponsePlantVocDTO> plants = new ArrayList<>();
        for (ResponsePlantVocDTO plant : getAllPlants()) {
            if(this.isNeverEmitter(plant)){
                plants.add(plant);
            };
        }
        return plants;
    }

    public List<ResponsePlantVocDTO> getMixedEmitters(){
        List<ResponsePlantVocDTO> plants = new ArrayList<>();
        for (ResponsePlantVocDTO plant : getAllPlants()) {
            if(this.isMixedEmitter(plant)){
                plants.add(plant);
            }
        };
        return plants;
    }

    public ResponsePlantVocDTO createPlantVoc(RequestPlantVocDTO plant){

        // map DTO to Entity
        PlantVocEntity plantEntity = mapper.map(plant, PlantVocEntity.class);

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

    private Boolean isAlwaysEmitter(ResponsePlantVocDTO plant){
        List<Boolean> boolList = plant
                .getEmitter()
                .stream()
                .map(PlantEmitterDTO::isEmits)
                .toList();
        return boolList.stream().allMatch(b -> b);
    }

    private Boolean isNeverEmitter(ResponsePlantVocDTO plant){
        List<Boolean> boolList = plant
                .getEmitter()
                .stream()
                .map(PlantEmitterDTO::isEmits)
                .toList();
        return boolList.stream().noneMatch(b -> b);
    }

    // TODO: not works bug!!!!!
    private Boolean isMixedEmitter(ResponsePlantVocDTO plant){
        List<Boolean> boolList = plant
                .getEmitter()
                .stream()
                .map(PlantEmitterDTO::isEmits)
                .toList();
        boolean flag = false;
        if(boolList.size() != 1) {
            flag = boolList.stream().distinct().count() == 1;
        }
        return flag;
    }

    private List<ResponsePlantVocDTO> getAllPlants(){
        return repository
                .findAll()
                .stream()
                .map(it -> mapper.map(it, ResponsePlantVocDTO.class))
                .toList();
    }



}
