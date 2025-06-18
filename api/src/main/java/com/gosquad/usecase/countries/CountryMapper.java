package com.gosquad.usecase.countries;

import com.gosquad.domain.countries.CountryEntity;
import com.gosquad.infrastructure.persistence.countries.CountryModel;
import org.springframework.stereotype.Component;

@Component
public class CountryMapper {
    public CountryEntity modelToEntity(CountryModel countryModel) {
        return new CountryEntity(
                countryModel.getId(),
                countryModel.getIsoCode(),
                countryModel.getCountryName()
        );
    }

    public CountryModel entityToModel(CountryEntity countryEntity) {
        return new CountryModel(
                countryEntity.getId(),
                countryEntity.getIsoCode(),
                countryEntity.getCountryName()
        );
    }
}
