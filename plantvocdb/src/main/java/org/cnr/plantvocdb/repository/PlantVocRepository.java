package org.cnr.plantvocdb.repository;

import org.cnr.plantvocdb.entity.PlantVocEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlantVocRepository
        extends JpaRepository<PlantVocEntity, UUID> {
}
