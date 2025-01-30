package org.cnr.plantvocdb.entity;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;
import jakarta.persistence.*;
import lombok.*;
import org.cnr.plantvocdb.enums.LeafHabitus;
import org.cnr.plantvocdb.enums.PlantsRanks;

@Entity
@Table(name = "plants_voc")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PlantVocEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id", length = 36, nullable = false, updatable = false)
    private UUID id;

    @Column(name="ipni", length = 50)
    private String IpniCode;

    @Column(name="full_name_plain", length = 50, nullable = false)
    private String FullNamePlain;

    @Column(name="full_name_no_authors_plain", length = 20, nullable = false)
    private String FullNameNoAuthorsPlain;

    @Column(name="name", length = 20, nullable = false)
    private String Name;

    @Column(name="family", length = 20, nullable = false)
    private String Family;

    @Column(name="genus", length = 20, nullable = false)
    private String Genus;

    @Column(name="species", length = 20, nullable = false)
    private String Species;

    @Column(name="valid_nomenclature", nullable = false)
    private boolean validNomenclature;

    @Column(name="rank", nullable = false)
    @Enumerated(EnumType.STRING)
    private PlantsRanks Rank;

    @Column(name="leaf_habitus", nullable = false)
    @Enumerated(EnumType.STRING)
    private LeafHabitus leafHabitus;

    @OneToMany(mappedBy = "plantVocEntity")
    private Set<PlantEmitterEntity> emitter;

    @ElementCollection
    @Column(name="synonyms")
    private Set<String> synonyms;

    @Column(name="created_datetime_utc", nullable = false, updatable = false)
    private OffsetDateTime createdDatetimeUTC;

    @Column(name="updated_datetime_utc", nullable = false)
    private OffsetDateTime updatedDatetimeUTC;




}
