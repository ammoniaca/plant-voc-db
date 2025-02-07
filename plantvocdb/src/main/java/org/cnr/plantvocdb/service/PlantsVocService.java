package org.cnr.plantvocdb.service;

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
import java.util.Optional;
import java.util.UUID;

@Service
public class PlantsVocService {

    private final PlantsVocRepository repository;
    private final ModelMapper mapper;


    @Autowired
    public PlantsVocService(PlantsVocRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;

    }

    public List<PlantInfoDTO> getAllPlantsInfo() {
        List<PlantVocEntity> plantsEntity = repository
                .findAll()
                .stream()
                .toList();

        return plantsEntity
                .stream()
                .map(it -> mapper.map(it, PlantInfoDTO.class))
                .toList();
    }

    public Optional<ResponsePlantVocDTO> getPlantById(UUID id) {
        Optional<PlantVocEntity> optionalPlantEntity = repository.findById(id);
        if(optionalPlantEntity.isPresent()) {
            ResponsePlantVocDTO responsePlantDTO = mapper.map(optionalPlantEntity.get(), ResponsePlantVocDTO.class);
            return Optional.of(responsePlantDTO);
        }
        return Optional.empty();
    }

    public Optional<ResponsePlantVocDTO> getPlantByIpni(String ipni){
        Optional<PlantVocEntity> optionalPlantEntity = repository.findByIpni(ipni);
        return optionalPlantEntity.map(it -> mapper.map(it, ResponsePlantVocDTO.class));

//        if(optionalPlantEntity.isPresent()) {
//            ResponsePlantVocDTO responsePlantVocDTO = mapper.map(optionalPlantEntity.get(), ResponsePlantVocDTO.class);
//            return Optional.of(responsePlantVocDTO);
//        }
//        return Optional.empty();
    }

    public List<PlantInfoDTO> getAlwaysEmitters(){
        List<PlantInfoDTO> PlantInfoDTOList = new ArrayList<>();
        for (PlantVocEntity plant : getAllEntity()) {
            if(this.isAlwaysEmitter(plant)){
                PlantInfoDTOList.add(PlantInfoDTO
                        .builder()
                        .id(plant.getId())
                        .fullNameNoAuthors(plant.getFullNameNoAuthorsPlain())
                        .build());
            };
        }
        return PlantInfoDTOList;

    }

    public List<PlantInfoDTO> getNeverEmitters(){
        List<PlantInfoDTO> PlantInfoDTOList = new ArrayList<>();
        for (PlantVocEntity plant : getAllEntity()) {
            if(this.isNeverEmitter(plant)){
                PlantInfoDTOList.add(PlantInfoDTO
                        .builder()
                        .id(plant.getId())
                        .fullNameNoAuthors(plant.getFullNameNoAuthorsPlain())
                        .build());
            };
        }
        return PlantInfoDTOList;
    }

    public List<PlantInfoDTO> getMixedEmitters(){
        List<PlantInfoDTO> PlantInfoDTOList = new ArrayList<>();
        for (PlantVocEntity plant : getAllEntity()) {
            if(this.isMixedEmitter(plant)){
                PlantInfoDTOList.add(PlantInfoDTO
                        .builder()
                        .id(plant.getId())
                        .fullNameNoAuthors(plant.getFullNameNoAuthorsPlain())
                        .build());
            }
        };
        return PlantInfoDTOList;
    }

    public List<ResponsePlantVocDTO> getAll() {
        // get all Entity stored in the DB and map DTO
        return getAllEntity().stream()
                .map(it -> mapper.map(it, ResponsePlantVocDTO.class))
                .toList();
    }

    public ResponsePlantVocDTO createPlantVoc(RequestPlantVocDTO plantDTO){

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

    private Boolean isAlwaysEmitter(PlantVocEntity plantVocEntity){

        List<Boolean> boolList = plantVocEntity
                .getEmitter()
                .stream()
                .map(PlantEmitterEntity::isEmits)
                .toList();
        return boolList.stream().allMatch(b -> b);

    }

    private Boolean isNeverEmitter(PlantVocEntity plantVocEntity){

        List<Boolean> boolList = plantVocEntity
                .getEmitter()
                .stream()
                .map(PlantEmitterEntity::isEmits)
                .toList();
        return boolList.stream().noneMatch(b -> b);

    }

    private Boolean isMixedEmitter(PlantVocEntity plantVocEntity){
        List<Boolean> boolList = plantVocEntity
                .getEmitter()
                .stream()
                .map(PlantEmitterEntity::isEmits)
                .toList();
        boolean flag = false;
        if(boolList.size() != 1) {
            flag = boolList.stream().distinct().count() == 1;
        }
        return flag;

    }

    private List<PlantVocEntity> getAllEntity(){
        return repository
                .findAll()
                .stream()
                .toList();
    }



}
