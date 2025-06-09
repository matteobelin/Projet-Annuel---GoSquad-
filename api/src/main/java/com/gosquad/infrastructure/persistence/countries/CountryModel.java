package com.gosquad.infrastructure.persistence.countries;

import com.gosquad.infrastructure.persistence.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CountryModel extends Model {
    String isoSode;
    String countryName;

    public CountryModel(Integer id,String isoSode, String countryName) {
        super(id);
        if (isoSode == null || isoSode.length() != 2) {
            throw new IllegalArgumentException("Le code doit contenir exactement 2 caractères");
        }
        this.isoSode = isoSode;
        this.countryName = countryName;
    }
}
