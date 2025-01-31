package org.cnr.plantvocdb.entity;

import jakarta.persistence.*;

@Entity
@Table(name="emitters")
public class PlantEmitterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean emitter;

    private String doi;

    @ManyToOne
    @JoinColumn(name = "plant_voc_id")
    private PlantVocEntity plantVocEntity;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isEmitter() {
        return emitter;
    }

    public void setEmitter(boolean emitter) {
        this.emitter = emitter;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public PlantVocEntity getPlantVocEntity() {
        return plantVocEntity;
    }

    public void setPlantVocEntity(PlantVocEntity plantVocEntity) {
        this.plantVocEntity = plantVocEntity;
    }
}
