package org.cnr.plantvocdb.repository;

import org.cnr.plantvocdb.entity.PlantVocEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlantsVocRepository
        extends JpaRepository<PlantVocEntity, UUID> {


    //PlantVocEntity save(PlantVocEntity plant);


}
