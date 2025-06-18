package com.gosquad.domain.cities;

import com.gosquad.domain.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CityEntity extends Entity {
    String cityName;
    String postalCode;
    Integer countryId;

    public CityEntity(Integer id,String cityName, String postalCode, Integer countryId) {
        super(id);
        this.cityName = cityName;
        this.postalCode = postalCode;
        this.countryId = countryId;
    }
}
