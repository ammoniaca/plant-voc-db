package org.cnr.plantvocdb.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PlantEmitterDTO {

    private boolean emits;
    private String doi;

}
