package org.cnr.plantvocdb.loader;

import org.cnr.plantvocdb.dto.RequestPlantDTO;

import java.util.List;

public record PlantsDTO(List<RequestPlantDTO> plants) {
}
