package org.cnr.plantvocdb.entity;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import jakarta.persistence.*;

import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.cnr.plantvocdb.enums.LeafHabitus;
import org.cnr.plantvocdb.enums.PlantsRanks;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "plants_voc")
public class PlantVocEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id", length = 36, nullable = false, updatable = false)
    private UUID id;

    @Column(name="ipni", length = 50)
    private String ipni;

    @Column(name="full_name_plain", length = 50)
    private String fullNamePlain;

    @Column(name="full_name_no_authors_plain", length = 50)
    private String fullNameNoAuthorsPlain;

    @Setter(AccessLevel.NONE)
    @Column(name="name", length = 30, nullable = false)
    private String name;

    @Setter(AccessLevel.NONE)
    @Column(name="family", length = 30, nullable = false)
    private String family;

    @Setter(AccessLevel.NONE)
    @Column(name="genus", length = 20, nullable = false)
    private String genus;

    @Setter(AccessLevel.NONE)
    @Column(name="species", length = 20)
    private String species;

    @Column(name="valid_nomenclature")
    private boolean validNomenclature;

    @Column(name="rank")
    @Enumerated(EnumType.STRING)
    private PlantsRanks rank;

    @Column(name="leaf_habitus")
    @Enumerated(EnumType.STRING)
    private LeafHabitus leafHabitus;

    @OneToMany(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.ALL},
            mappedBy = "plant"
    )
    private Set<PlantEmitterEntity> emitter;

    @ElementCollection
    @Column(name="synonyms")
    private Set<String> synonyms;

    @Column(name="created_datetime_utc", updatable = false) // creation_datetime_utc
    private OffsetDateTime createdDatetimeUTC;

    @Column(name="updated_datetime_utc")  // last_modified_datetime_utc
    private OffsetDateTime updatedDatetimeUTC;

    public void setName(String name) {

        this.name = name.toLowerCase();
    }

    public void setFamily(String family) {

        this.family = StringUtils.capitalize(family.toLowerCase());
    }

    public void setGenus(String genus) {

        this.genus = StringUtils.capitalize(genus.toLowerCase());
    }

    public void setSpecies(String species) {

        this.species = species.toLowerCase();
    }

}
