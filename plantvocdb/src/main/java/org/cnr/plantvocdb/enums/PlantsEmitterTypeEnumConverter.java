package org.cnr.plantvocdb.enums;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PlantsEmitterTypeEnumConverter implements Converter<String, PlantsEmitterType> {

    @Override
    public PlantsEmitterType convert(String value) {
        return PlantsEmitterType.valueOf(value);
    }

}
