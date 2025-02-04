package org.cnr.plantvocdb.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="emitters")
@Getter
@Setter
public class PlantEmitterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean emits;

    private String doi;

    @ManyToOne
    @JoinColumn(name = "fk_plant_id")
    private PlantVocEntity plant;

}
