package org.cnr.plantvocdb.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PlantEmitterDTO {

    private boolean emitter;
    private String doi;

}
