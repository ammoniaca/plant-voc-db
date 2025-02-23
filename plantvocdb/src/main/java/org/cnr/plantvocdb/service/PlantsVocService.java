package org.cnr.plantvocdb.service;

import org.apache.commons.lang3.StringUtils;
import org.cnr.plantvocdb.dto.PlantEmitterDTO;
import org.cnr.plantvocdb.dto.PlantInfoDTO;
import org.cnr.plantvocdb.dto.RequestPlantDTO;
import org.cnr.plantvocdb.dto.ResponsePlantDTO;
import org.cnr.plantvocdb.entity.PlantEntity;
import org.cnr.plantvocdb.enums.LeafHabitus;
import org.cnr.plantvocdb.enums.PlantsEmitterType;
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

    public List<PlantInfoDTO> findAllPlantsInfo() {
        List<ResponsePlantDTO> listPlantsDTO = getAllPlants();
        List<PlantInfoDTO> listPlantsInfo = new ArrayList<>();
        for (ResponsePlantDTO plant : listPlantsDTO) {
            OffsetDateTime createdAt = plant.getCreatedDatetimeUTC();
            OffsetDateTime updatedAt = plant.getUpdatedDatetimeUTC();
            boolean dateFlag = createdAt.isEqual(updatedAt);
            PlantsEmitterType emitterType = this.getEmitterType(plant);
            PlantInfoDTO plantInfo = PlantInfoDTO
                    .builder()
                    .id(plant.getId())
                    .fullNameNoAuthors(plant.getFullNameNoAuthorsPlain())
                    .updated(!dateFlag)
                    .emitterType(emitterType)
                    .build();
            listPlantsInfo.add(plantInfo);
        }
        return listPlantsInfo;
    }

    public Optional<ResponsePlantDTO> findPlantById(UUID id) {
        Optional<PlantEntity> optionalPlantEntity = repository.findById(id);
        return optionalPlantEntity.map(it -> mapper.map(it, ResponsePlantDTO.class));
    }

    public Optional<ResponsePlantDTO> findPlantByIpni(String ipni){
        Optional<PlantEntity> optionalPlantEntity = repository.findByIpni(ipni);
        return optionalPlantEntity.map(it -> mapper.map(it, ResponsePlantDTO.class));
    }

    public List<ResponsePlantDTO> findPlantsByName(String name){
        // sanitize name attribute (i.e., normalize space and lower case)
        return repository
                .findByName(StringUtils
                        .normalizeSpace(name.toLowerCase()))
                .stream()
                .map(it -> mapper.map(it, ResponsePlantDTO.class))
                .toList();
    }

    public List<ResponsePlantDTO> findPlantsByFamily(String family){
        // sanitize family attribute (i.e., normalize space and Capitalized case)
        return repository
                .findByFamily(StringUtils
                        .normalizeSpace(StringUtils
                                .capitalize(family
                                        .toLowerCase())))
                .stream()
                .map(it -> mapper.map(it, ResponsePlantDTO.class))
                .toList();
    }

    public List<ResponsePlantDTO> findPlantsByGenus(String genus){
        // sanitize genus attribute (i.e., normalize space and Capitalized case)
        return repository.findByGenus(StringUtils
                        .normalizeSpace(StringUtils
                                .capitalize(genus
                                        .toLowerCase())))
                .stream()
                .map(it -> mapper.map(it, ResponsePlantDTO.class))
                .toList();
    }

    public List<ResponsePlantDTO> findPlantsByRank(PlantsRanks rank){
        return repository.findByRank(rank)
                .stream()
                .map(it -> mapper.map(it, ResponsePlantDTO.class))
                .toList();
    }

    public List<ResponsePlantDTO> findPlantsByLeafHabitus(LeafHabitus leafHabitus){
        return repository
                .findByLeafHabitus(leafHabitus)
                .stream()
                .map(it -> mapper.map(it, ResponsePlantDTO.class))
                .toList();
    }

    public List<ResponsePlantDTO> getAlwaysEmitters(){
        List<ResponsePlantDTO> plants = new ArrayList<>();
        for (ResponsePlantDTO plant : getAllPlants()) {
            if(this.isAlwaysEmitter(plant)){
                plants.add(plant);
            };
        }
        return plants;
    }

    public List<ResponsePlantDTO> getNeverEmitters(){
        List<ResponsePlantDTO> plants = new ArrayList<>();
        for (ResponsePlantDTO plant : getAllPlants()) {
            if(this.isNeverEmitter(plant)){
                plants.add(plant);
            };
        }
        return plants;
    }

    public List<ResponsePlantDTO> getMixedEmitters(){
        List<ResponsePlantDTO> plants = new ArrayList<>();
        for (ResponsePlantDTO plant : getAllPlants()) {
            if(this.isMixedEmitter(plant)){
                plants.add(plant);
            }
        };
        return plants;
    }

    public ResponsePlantDTO createPlantVoc(RequestPlantDTO plant){

        // map DTO to Entity
        PlantEntity plantEntity = mapper.map(plant, PlantEntity.class);

        // set datetime in UTC
        OffsetDateTime odt = OffsetDateTime.now(ZoneOffset.UTC);
        plantEntity.setCreatedDatetimeUTC(odt);
        plantEntity.setUpdatedDatetimeUTC(odt);

        // iterate over the set to add plantEntity
        plantEntity.getEmitter().forEach(it -> it.setPlant(plantEntity));

        // save new plant in DB
        PlantEntity savedPlantEntity = repository.save(plantEntity);

        // map Entity to DTO
        return mapper.map(savedPlantEntity, ResponsePlantDTO.class);
    }

    public void delete(ResponsePlantDTO plant){
        PlantEntity plantEntity = mapper.map(plant, PlantEntity.class);
        repository.deleteById(plantEntity.getId());
    }

    private Boolean isAlwaysEmitter(ResponsePlantDTO plant){
        List<Boolean> boolList = plant
                .getEmitter()
                .stream()
                .map(PlantEmitterDTO::isEmits)
                .toList();
        return boolList.stream().allMatch(b -> b);
    }

    private Boolean isNeverEmitter(ResponsePlantDTO plant){
        List<Boolean> boolList = plant
                .getEmitter()
                .stream()
                .map(PlantEmitterDTO::isEmits)
                .toList();
        return boolList.stream().noneMatch(b -> b);
    }

    private Boolean isMixedEmitter(ResponsePlantDTO plant){
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

    private PlantsEmitterType getEmitterType(ResponsePlantDTO plant){
        PlantsEmitterType result;
        if (this.isAlwaysEmitter(plant)){
            result = PlantsEmitterType.ALWAYS;
        } else if (this.isNeverEmitter(plant)){
            result = PlantsEmitterType.NEVER;
        } else {
            result = PlantsEmitterType.MIXED;
        }
        return result;
    }

    private List<ResponsePlantDTO> getAllPlants(){
        return repository
                .findAll()
                .stream()
                .map(it -> mapper.map(it, ResponsePlantDTO.class))
                .toList();
    }



}
