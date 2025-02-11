package org.cnr.plantvocdb.entity;

import java.time.OffsetDateTime;
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
    @Column(name="id", length = 50, nullable = false, updatable = false)
    private UUID id;

    @Column(name="ipni", length = 50)
    private String ipni;

    @Setter(AccessLevel.NONE)
    @Column(name="full_name_plain", length = 50)
    private String fullNamePlain;

    @Setter(AccessLevel.NONE)
    @Column(name="full_name_no_authors_plain", length = 50)
    private String fullNameNoAuthorsPlain;

    @Setter(AccessLevel.NONE)
    @Column(name="plant_name", length = 30, nullable = false)
    private String name;

    @Setter(AccessLevel.NONE)
    @Column(name="family", length = 30, nullable = false)
    private String family;

    @Setter(AccessLevel.NONE)
    @Column(name="genus", length = 30, nullable = false)
    private String genus;

    @Setter(AccessLevel.NONE)
    @Column(name="species", length = 30, nullable = false)
    private String species;

    @Column(name="valid_nomenclature")
    private boolean validNomenclature;

    @Column(name="rank", length = 20)
    @Enumerated(EnumType.STRING)
    private PlantsRanks rank;

    @Column(name="leaf_habitus", length = 20)
    @Enumerated(EnumType.STRING)
    private LeafHabitus leafHabitus;

    @OneToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            mappedBy = "plant"
    )
    private Set<PlantEmitterEntity> emitter;

    @ElementCollection
    @CollectionTable(
            name = "synonyms", // Nome della tabella intermedia
            joinColumns = @JoinColumn(name = "fk_synonyms_plant_id"),
            foreignKey = @ForeignKey(name = "FK_synonyms_plant")
    )
    @Column(name="synonyms")
    private Set<String> synonyms;

    @Column(name="created_datetime_utc", updatable = false) // creation_datetime_utc
    private OffsetDateTime createdDatetimeUTC;

    @Column(name="updated_datetime_utc")  // last_modified_datetime_utc
    private OffsetDateTime updatedDatetimeUTC;

    public void setName(String name) {

        this.name = StringUtils
                .normalizeSpace(name.toLowerCase());
    }

    public void setFamily(String family) {

        this.family = StringUtils
                .normalizeSpace(StringUtils
                        .capitalize(family.toLowerCase()));
    }

    public void setGenus(String genus) {

        this.genus = StringUtils
                .normalizeSpace(StringUtils
                        .capitalize(genus.toLowerCase()));
    }

    public void setSpecies(String species) {

        this.species = StringUtils
                .normalizeSpace(species.toLowerCase());
    }

    public void setFullNamePlain(String fullNamePlain) {
        this.fullNamePlain = StringUtils
                .normalizeSpace(fullNamePlain);
    }

    public void setFullNameNoAuthorsPlain(String fullNameNoAuthorsPlain) {
        this.fullNameNoAuthorsPlain = StringUtils
                .normalizeSpace(fullNameNoAuthorsPlain);
    }
}
