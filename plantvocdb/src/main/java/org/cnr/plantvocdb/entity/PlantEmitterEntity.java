package org.cnr.plantvocdb.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="emitters")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlantEmitterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="emits", length = 50)
    private boolean emits;

    @Column(name="doi")
    private String doi;

    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "fk_emitters_plant_id",
            nullable = false,
            updatable = true,
            insertable = true,
            foreignKey = @ForeignKey(name = "FK_emitters_plant"))
    private PlantEntity plant;

}
