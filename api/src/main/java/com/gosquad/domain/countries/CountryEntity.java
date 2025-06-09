package com.gosquad.domain.countries;

import com.gosquad.domain.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CountryEntity extends Entity {
    String isoCode;
    String countryName;

    public CountryEntity(Integer id,String isoCode, String countryName) {
        super(id);
        if (isoCode == null || isoCode.length() != 2) {
            throw new IllegalArgumentException("Le code iso doit contenir exactement 2 caractères");
        }
        this.isoCode = isoCode;
        this.countryName = countryName;
    }
}