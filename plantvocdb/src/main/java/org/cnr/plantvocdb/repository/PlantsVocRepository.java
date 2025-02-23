package org.cnr.plantvocdb.repository;

import org.cnr.plantvocdb.entity.PlantEntity;
import org.cnr.plantvocdb.enums.LeafHabitus;
import org.cnr.plantvocdb.enums.PlantsRanks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlantsVocRepository
        extends JpaRepository<PlantEntity, UUID> {

    Optional<PlantEntity> findByIpni(String ipni);

    List<PlantEntity> findByName(String name);

    List<PlantEntity> findByFamily(String family);

    List<PlantEntity> findByGenus(String genus);

    List<PlantEntity> findByRank(PlantsRanks rank);

    List<PlantEntity> findByLeafHabitus(LeafHabitus leafHabitus);

}
