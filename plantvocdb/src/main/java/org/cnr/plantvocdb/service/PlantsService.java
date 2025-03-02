package org.cnr.plantvocdb.service;

import org.apache.commons.lang3.StringUtils;
import org.cnr.plantvocdb.dto.PlantEmitterDTO;
import org.cnr.plantvocdb.dto.PlantInfoDTO;
import org.cnr.plantvocdb.dto.RequestPlantDTO;
import org.cnr.plantvocdb.dto.ResponsePlantDTO;
import org.cnr.plantvocdb.entity.PlantEmitterEntity;
import org.cnr.plantvocdb.entity.PlantEntity;
import org.cnr.plantvocdb.enums.LeafHabitus;
import org.cnr.plantvocdb.enums.PlantsEmitterType;
import org.cnr.plantvocdb.enums.PlantsRanks;
import org.cnr.plantvocdb.exceptions.PlantNotFoundException;
import org.cnr.plantvocdb.repository.PlantsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlantsService {

    private final PlantsRepository repository;
    private final ModelMapper mapper;


    @Autowired
    public PlantsService(PlantsRepository repository,
                         ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;

    }

    // GET
    public List<PlantInfoDTO> findAllPlantsInfo() {
        List<ResponsePlantDTO> listPlantsDTO = findAllPlants();
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

    // GET
    public ResponsePlantDTO findPlantById(UUID id) {
        Optional<PlantEntity> optionalPlantEntity = repository.findById(id);
        if (optionalPlantEntity.isEmpty()) {
            String errorMessage = MessageFormat.format("Plant with id {0} not found.", id.toString());
            throw new PlantNotFoundException(errorMessage);
        }
        PlantEntity plantEntity = optionalPlantEntity.get();
        return mapper.map(plantEntity, ResponsePlantDTO.class);
    }

    // GET
    public ResponsePlantDTO findPlantByIpni(String ipni){
        Optional<PlantEntity> optionalPlantEntity = repository.findByIpni(ipni);
        if (optionalPlantEntity.isEmpty()) {
            String errorMessage = MessageFormat.format("Plant with ipni {0} not found.", ipni);
            throw new PlantNotFoundException(errorMessage);
        }
        PlantEntity plantEntity = optionalPlantEntity.get();
        return mapper.map(plantEntity, ResponsePlantDTO.class);
  }

    // GET
    public ResponsePlantDTO findPlantBySpecies(String genus, String species) {
        String genusSanitized = StringUtils.normalizeSpace(StringUtils.capitalize(genus.toLowerCase()));
        String speciesSanitized = StringUtils.normalizeSpace(species.toLowerCase());
        Optional<PlantEntity> optionalPlantEntity = repository.findByGenusAndSpecies(
                genusSanitized,
                speciesSanitized
        );
        if(optionalPlantEntity.isEmpty()){
            String errorMessage = MessageFormat.format("Plant {0} {1} not found.",
                    genusSanitized,
                    speciesSanitized
            );
            throw new PlantNotFoundException(errorMessage);
        }
        PlantEntity plantEntity = optionalPlantEntity.get();
        return mapper.map(plantEntity, ResponsePlantDTO.class);
    }

    // GET
    public List<ResponsePlantDTO> findPlantsByName(String name){
        // sanitize name attribute (i.e., normalize space and lower case)
        String nameSanitized = StringUtils.normalizeSpace(name.toLowerCase());
        List<PlantEntity> plantEntities = repository.findByName(nameSanitized);
        // check if list is empty
        if(plantEntities.isEmpty()){
            String errorMessage = MessageFormat.format("Plants not found with name: {0}.", nameSanitized);
            throw new PlantNotFoundException(errorMessage);
        }
        return plantEntities
                .stream()
                .map(it -> mapper.map(it, ResponsePlantDTO.class))
                .toList();
    }

    // GET
    public List<ResponsePlantDTO> findPlantsByFamily(String family){
        // sanitize family attribute (i.e., normalize space and Capitalized case)
        String familySanitized = StringUtils.normalizeSpace(StringUtils.capitalize(family.toLowerCase()));
        List<PlantEntity> plantEntities = repository.findByFamily(familySanitized);
        if(plantEntities.isEmpty()){
            String errorMessage = MessageFormat
                    .format("Plants not found with family: {0}.", familySanitized);
            throw new PlantNotFoundException(errorMessage);
        }
        return plantEntities
                .stream()
                .map(it -> mapper.map(it, ResponsePlantDTO.class))
                .toList();
    }

    // GET
    public List<ResponsePlantDTO> findPlantsByGenus(String genus){
        // sanitize genus attribute (i.e., normalize space and Capitalized case)
        String genusSanitized = StringUtils.normalizeSpace(StringUtils.capitalize(genus.toLowerCase()));
        List<PlantEntity> plantEntities = repository.findByGenus(genusSanitized);
        if(plantEntities.isEmpty()){
            String errorMessage = MessageFormat.format("Plants not found with genus: {0}.", genusSanitized);
            throw new PlantNotFoundException(errorMessage);
        }
        return plantEntities.stream()
                .map(it -> mapper.map(it, ResponsePlantDTO.class))
                .toList();
    }

    // GET
    public List<ResponsePlantDTO> findPlantsByRank(PlantsRanks rank){
        List<PlantEntity> plantEntities = repository.findByRank(rank);
        if(plantEntities.isEmpty()){
            String errorMessage = MessageFormat.format(
                    "Plants not found with rank: {0}.",
                    rank.name().toLowerCase());
            throw new PlantNotFoundException(errorMessage);
        }
        return plantEntities
                .stream()
                .map(it -> mapper.map(it, ResponsePlantDTO.class))
                .toList();
    }

    // GET
    public List<ResponsePlantDTO> findPlantsByLeafHabitus(LeafHabitus leafHabitus){
        List<PlantEntity> plantEntities = repository.findByLeafHabitus(leafHabitus);
        if(plantEntities.isEmpty()){
            String errorMessage = MessageFormat.format(
                    "Plants not found with leaf habitus: {0}.",
                    leafHabitus.name().toLowerCase());
            throw new PlantNotFoundException(errorMessage);
        }
        return plantEntities
                .stream()
                .map(it -> mapper.map(it, ResponsePlantDTO.class))
                .toList();
    }

    // GET
    public List<ResponsePlantDTO> findAllPlantsAlwaysEmitters(){
        List<ResponsePlantDTO> plants = new ArrayList<>();
        for (ResponsePlantDTO plant : findAllPlants()) {
            if(this.isAlwaysEmitter(plant)){
                plants.add(plant);
            };
        }
        if (plants.isEmpty()) {
            throw new PlantNotFoundException("Always emitter plants not found.");
        }
        return plants;
    }

    // GET
    public List<ResponsePlantDTO> findAllPlantsNeverEmitters(){
        List<ResponsePlantDTO> plants = new ArrayList<>();
        for (ResponsePlantDTO plant : findAllPlants()) {
            if(this.isNeverEmitter(plant)){
                plants.add(plant);
            };
        }
        if(plants.isEmpty()) {
            throw new PlantNotFoundException("Never emitter plants not found.");
        }
        return plants;
    }

    // GET
    public List<ResponsePlantDTO> findAllPlantsMixedEmitters(){
        List<ResponsePlantDTO> plants = new ArrayList<>();
        for (ResponsePlantDTO plant : findAllPlants()) {
            if(this.isMixedEmitter(plant)){
                plants.add(plant);
            }
        };
        if(plants.isEmpty()) {
            throw new PlantNotFoundException("Mixed emitter plants not found.");
        }
        return plants;
    }

    // GET
    public List<ResponsePlantDTO> findAllPlantsEmitters(PlantsEmitterType emitterFlag){
        List<PlantEntity> plantEntities = repository.findAll();
        List<ResponsePlantDTO> plants = getPlantsByEmitterTyper(emitterFlag, plantEntities);
        if(plants.isEmpty()){
            String errorMessage = MessageFormat
                    .format("Plants {0} emitter not found.", emitterFlag.name().toLowerCase());
            throw new PlantNotFoundException(errorMessage);
        }
        return plants;
    }

    // GET
    public List<ResponsePlantDTO> findAllPlantsEmitterTypeByFamilyAndLeafHabitus(
            String family,
            LeafHabitus leafHabitus,
            PlantsEmitterType emitterFlag
    ){
        String familySanitized = StringUtils.normalizeSpace(StringUtils.capitalize(family.toLowerCase()));
        List<PlantEntity> plantEntities = repository.findAllByFamilyIsAndLeafHabitus(familySanitized, leafHabitus);
        List<ResponsePlantDTO> plants = getPlantsByEmitterTyper(emitterFlag, plantEntities);
        if(plants.isEmpty()){
            String errorMessage = MessageFormat.format(
                    "Plants not found with family: {0}, leaf habitus: {1}, and emitter type: {2}.",
                    familySanitized,
                    leafHabitus.name().toLowerCase(),
                    emitterFlag.name().toLowerCase());
            throw new PlantNotFoundException(errorMessage);
        }
        return plants;
    }

    // GET
    public List<ResponsePlantDTO> findAllPlantsEmitterTypeByGenusAndLeafHabitus(
            String genus,
            LeafHabitus leafHabitus,
            PlantsEmitterType emitterFlag)
    {
        String genusSanitized = StringUtils.normalizeSpace(StringUtils.capitalize(genus.toLowerCase()));
        List<PlantEntity> plantEntities = repository.findAllByGenusAndLeafHabitus(genusSanitized, leafHabitus);
        List<ResponsePlantDTO> plants = getPlantsByEmitterTyper(emitterFlag, plantEntities);
        if(plants.isEmpty()){
            String errorMessage = MessageFormat.format(
                    "Plants not found with family: {0}, leaf habitus: {1}, and emitter type: {2}.",
                    genusSanitized,
                    leafHabitus.name().toLowerCase(),
                    emitterFlag.name().toLowerCase());
            throw new PlantNotFoundException(errorMessage);
        }
        return plants;
    }

    // POST
    public ResponsePlantDTO createPlant(RequestPlantDTO plant){

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

    // DELETE
    public void deleteById(ResponsePlantDTO plant){
        PlantEntity plantEntity = mapper.map(plant, PlantEntity.class);
        repository.deleteById(plantEntity.getId());
    }

    // PUT
    public ResponsePlantDTO addEmitter(
            UUID id,
            boolean emits,
            String doi
    ){
        Optional<PlantEntity> optionalPlant = repository.findById(id);
        if (optionalPlant.isEmpty()) {
            String errorMessage = MessageFormat.format("Plant with id {0} not found.", id.toString());
            throw new PlantNotFoundException(errorMessage);
        }
        PlantEntity plantEntity = optionalPlant.get();
        // update date
        plantEntity.setUpdatedDatetimeUTC(OffsetDateTime.now(ZoneOffset.UTC));
        // Add the new emitter to the list
        plantEntity.getEmitter().add(PlantEmitterEntity
                .builder()
                .emits(emits)
                .plant(plantEntity) // Associate it with the existing plant
                .doi(doi)
                .build()
        );
        PlantEntity updatedPlantEntity = repository.save(plantEntity);
        // Convert back to DTO and return
        return mapper.map(updatedPlantEntity, ResponsePlantDTO.class);
    }

    // PRIVATE METHODS
    private List<ResponsePlantDTO> getPlantsByEmitterTyper(
            PlantsEmitterType emitterFlag,
            List<PlantEntity> listPlantsEntity
    ){
        List<ResponsePlantDTO> listPlantsDTO = new ArrayList<>();
        if(!listPlantsEntity.isEmpty()){
            for (PlantEntity plantEntity : listPlantsEntity) {
                ResponsePlantDTO plantDTO = mapper.map(plantEntity, ResponsePlantDTO.class);
                PlantsEmitterType emitterType = getEmitterType(plantDTO);
                if(emitterType.equals(emitterFlag)){
                    listPlantsDTO.add(plantDTO);
                }
            }
        }
        return listPlantsDTO;
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

    private List<ResponsePlantDTO> findAllPlants(){
        return repository
                .findAll()
                .stream()
                .map(it -> mapper.map(it, ResponsePlantDTO.class))
                .toList();
    }



}
