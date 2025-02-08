package org.cnr.plantvocdb.repository;

import org.cnr.plantvocdb.entity.PlantVocEntity;
import org.cnr.plantvocdb.enums.LeafHabitus;
import org.cnr.plantvocdb.enums.PlantsRanks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlantsVocRepository
        extends JpaRepository<PlantVocEntity, UUID> {

    Optional<PlantVocEntity> findByIpni(String ipni);

    List<PlantVocEntity> findByName(String name);

    List<PlantVocEntity> findByFamily(String family);

    List<PlantVocEntity> findByGenus(String genus);

    List<PlantVocEntity> findByRank(PlantsRanks rank);

    List<PlantVocEntity> findByLeafHabitus(LeafHabitus leafHabitus);

}
