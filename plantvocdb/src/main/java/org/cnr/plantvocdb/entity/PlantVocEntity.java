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
@Table(name = "plants_voc")
@Setter(AccessLevel.NONE)
public class PlantVocEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id", length = 36, nullable = false, updatable = false)
    private UUID id;

    @Column(name="ipni", length = 50)
    private String ipni;

    // @Column(name="full_name_plain", length = 50, nullable = false)
    private String fullNamePlain;

    @Column(name="full_name_no_authors_plain", length = 20)
    private String fullNameNoAuthorsPlain;

    @Column(name="name", length = 20)
    private String name;

    @Column(name="family", length = 20)
    private String family;

    @Column(name="genus", length = 20)
    private String genus;

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

    @OneToMany(mappedBy = "plantVocEntity")
    private Set<PlantEmitterEntity> emitter;

    @ElementCollection
    @Column(name="synonyms")
    private Set<String> synonyms;

    @Column(name="created_datetime_utc", updatable = false) // creation_datetime_utc
    private OffsetDateTime createdDatetimeUTC;

    @Column(name="updated_datetime_utc")  // last_modified_datetime_utc
    private OffsetDateTime updatedDatetimeUTC;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getIpni() {
        return ipni;
    }

    public void setIpni(String ipniCode) {
        this.ipni = ipniCode;
    }

    public String getFullNamePlain() {
        return fullNamePlain;
    }

    public void setFullNamePlain(String fullNamePlain) {
        this.fullNamePlain = fullNamePlain;
    }

    public String getFullNameNoAuthorsPlain() {
        return fullNameNoAuthorsPlain;
    }

    public void setFullNameNoAuthorsPlain(String fullNameNoAuthorsPlain) {
        this.fullNameNoAuthorsPlain = fullNameNoAuthorsPlain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = StringUtils.capitalize(family.toLowerCase());
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = StringUtils.capitalize(genus.toLowerCase());
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species.toLowerCase();
    }

    public boolean isValidNomenclature() {
        return validNomenclature;
    }

    public void setValidNomenclature(boolean validNomenclature) {
        this.validNomenclature = validNomenclature;
    }

    public PlantsRanks getRank() {
        return rank;
    }

    public void setRank(PlantsRanks rank) {
        this.rank = rank;
    }

    public LeafHabitus getLeafHabitus() {
        return leafHabitus;
    }

    public void setLeafHabitus(LeafHabitus leafHabitus) {
        this.leafHabitus = leafHabitus;
    }

    public Set<PlantEmitterEntity> getEmitter() {
        return emitter;
    }

    public void setEmitter(Set<PlantEmitterEntity> emitter) {
        this.emitter = emitter;
    }

    public Set<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(Set<String> synonyms) {
        this.synonyms = synonyms;
    }

    public OffsetDateTime getCreatedDatetimeUTC() {
        return createdDatetimeUTC;
    }

    public void setCreatedDatetimeUTC(OffsetDateTime createdDatetimeUTC) {
        this.createdDatetimeUTC = createdDatetimeUTC;
    }

    public OffsetDateTime getUpdatedDatetimeUTC() {
        return updatedDatetimeUTC;
    }

    public void setUpdatedDatetimeUTC(OffsetDateTime updatedDatetimeUTC) {
        this.updatedDatetimeUTC = updatedDatetimeUTC;
    }
}
