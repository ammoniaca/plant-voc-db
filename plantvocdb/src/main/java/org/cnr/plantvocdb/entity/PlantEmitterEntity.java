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

    @Column(name="emits")
    private boolean emits;

    @Column(name="doi")
    private String doi;

    @ManyToOne
    @JoinColumn(name = "fk_plant_id", nullable = false, updatable = true, insertable = true)
    private PlantVocEntity plant;

}
