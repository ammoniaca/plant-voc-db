package org.cnr.plantvocdb.loader;

import org.cnr.plantvocdb.dto.RequestPlantVocDTO;

import java.util.List;

public record PlantsDTO(List<RequestPlantVocDTO> plants) {
}
