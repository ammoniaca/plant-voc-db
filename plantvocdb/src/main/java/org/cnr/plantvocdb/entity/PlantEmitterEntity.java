package org.cnr.plantvocdb.entity;

import jakarta.persistence.*;

@Entity
@Table(name="emitters")
public class PlantEmitterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean emits;

    private String doi;

    @ManyToOne
    @JoinColumn(name = "fk_plant_id")
    // @CollectionTable(name = "emitter", joinColumns = @JoinColumn(name = "fk_plant_id"))
    private PlantVocEntity plantVocEntity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getEmits() {
        return emits;
    }

    public void setEmits(boolean emitter) {
        this.emits = emitter;
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
