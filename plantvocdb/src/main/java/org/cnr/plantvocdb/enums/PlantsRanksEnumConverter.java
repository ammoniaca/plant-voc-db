package org.cnr.plantvocdb.enums;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PlantsRanksEnumConverter implements Converter<String, PlantsRanks> {

    @Override
    public PlantsRanks convert(String value) {
        return PlantsRanks.valueOf(value.toUpperCase());
    }
}
