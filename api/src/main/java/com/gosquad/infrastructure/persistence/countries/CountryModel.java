package com.gosquad.infrastructure.persistence.countries;

import com.gosquad.infrastructure.persistence.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CountryModel extends Model {
    String isoCode;
    String countryName;

    public CountryModel(Integer id,String isoCode, String countryName) {
        super(id);
        if (isoCode == null || isoCode.length() != 2) {
            throw new IllegalArgumentException("Le code iso doit contenir exactement 2 caractères");
        }
        this.isoCode = isoCode;
        this.countryName = countryName;
    }
}
