package com.gosquad.usecase.cities;

import com.gosquad.domain.cities.CityEntity;
import com.gosquad.infrastructure.persistence.cities.CityModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CityMapper {
    public CityEntity modelToEntity(CityModel cityModel){
        return new CityEntity(
                cityModel.getId(),
                cityModel.getCityName(),
                cityModel.getPostalCode(),
                cityModel.getCountryId()
        );
    }

    public CityModel entityToModel(CityEntity cityEntity){
        return new CityModel(
                cityEntity.getId(),
                cityEntity.getCityName(),
                cityEntity.getPostalCode(),
                cityEntity.getCountryId()
        );
    }

    public List<CityEntity> modelsToEntities(List<CityModel> cityModels) {
        return cityModels.stream()
                .map(this::modelToEntity)
                .toList();
    }
}
