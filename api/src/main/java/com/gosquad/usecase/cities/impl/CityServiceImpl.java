package com.gosquad.usecase.cities.impl;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.cities.CityEntity;
import com.gosquad.infrastructure.persistence.cities.CityModel;
import com.gosquad.infrastructure.persistence.cities.CityRepository;
import com.gosquad.usecase.cities.CityMapper;
import com.gosquad.usecase.cities.CityService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class CityServiceImpl implements CityService {

    private CityRepository cityRepository;
    private CityMapper cityMapper;

    public CityServiceImpl(CityRepository cityRepository, CityMapper cityMapper) {
        this.cityRepository = cityRepository;
        this.cityMapper = cityMapper;
    }

    public CityEntity getCityById(int id) throws SQLException, NotFoundException{
        CityModel cityModel = cityRepository.getById(id);
        return cityMapper.modelToEntity(cityModel);
    };

    public CityEntity getCityByNameByPostalCodeByCountry(String name,String postalCode,int countryId) throws SQLException, NotFoundException, ConstraintViolationException{
        CityModel cityModel = cityRepository.getByNameByPostalCodeByCountry(name, postalCode, countryId);
        return cityMapper.modelToEntity(cityModel);
    };

    public void addCity(CityEntity city) throws SQLException{
        CityModel cityModel = cityMapper.entityToModel(city);
        cityRepository.addCity(cityModel);
        city.setId(cityModel.getId());
    };

    public void updateCity(CityEntity city) throws ConstraintViolationException{
        CityModel cityModel = cityMapper.entityToModel(city);
        cityRepository.updateCity(cityModel);
    };

    public CityEntity getOrCreateCity(String cityName, String postalCode, Integer countryId) throws SQLException, NotFoundException, ConstraintViolationException {
        try {
            return getCityByNameByPostalCodeByCountry(cityName, postalCode, countryId);
        } catch (NotFoundException | ConstraintViolationException e) {
            CityEntity newCity = new CityEntity(null, cityName, postalCode, countryId);
            addCity(newCity);
            return getCityByNameByPostalCodeByCountry(cityName, postalCode, countryId);
        }
    }


}
