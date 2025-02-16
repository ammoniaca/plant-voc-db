package org.cnr.plantvocdb.enums;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class LeafHabitusEnumConverter implements  Converter<String, LeafHabitus>{

    @Override
    public LeafHabitus convert(String value) {
        return LeafHabitus.valueOf(value.toUpperCase());
    }

}
