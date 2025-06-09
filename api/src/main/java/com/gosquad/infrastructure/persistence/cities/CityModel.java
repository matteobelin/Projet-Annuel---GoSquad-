package com.gosquad.infrastructure.persistence.cities;

import com.gosquad.infrastructure.persistence.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CityModel extends Model {
    String cityName;
    String postalCode;
    Integer countryId;

    public CityModel(Integer id,String cityName, String postalCode, Integer countryId) {
        super(id);
        this.cityName = cityName;
        this.postalCode = postalCode;
        this.countryId = countryId;
    }
}
