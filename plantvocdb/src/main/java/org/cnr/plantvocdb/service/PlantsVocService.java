package org.cnr.plantvocdb.service;

import org.apache.commons.lang3.StringUtils;
import org.cnr.plantvocdb.dto.PlantInfoDTO;
import org.cnr.plantvocdb.dto.RequestPlantVocDTO;
import org.cnr.plantvocdb.dto.ResponsePlantVocDTO;
import org.cnr.plantvocdb.entity.PlantEmitterEntity;
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
    public PlantsVocService(PlantsVocRepository repository, ModelMapper mapper) {
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
