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



}
