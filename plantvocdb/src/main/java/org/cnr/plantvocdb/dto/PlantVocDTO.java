package org.cnr.plantvocdb.dto;

import org.cnr.plantvocdb.enums.LeafHabitus;
import org.cnr.plantvocdb.enums.PlantsRanks;

import java.time.OffsetDateTime;
import java.util.Set;

public class PlantVocDTO {

    private String IpniCode;
    private String FullNamePlain;
    private String FullNameNoAuthorsPlain;
    private String Name;
    private String Family;
    private String Genus;
    private String Species;
    private boolean validNomenclature;
    private PlantsRanks Rank;
    private LeafHabitus leafHabitus;
    private Set<String> synonyms;
    private Set<PlantEmitterDTO> emitter;
    private OffsetDateTime createdDatetimeUTC;
    private OffsetDateTime updatedDatetimeUTC;



}
