package com.gosquad.usecase.cities;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.cities.CityEntity;

import java.sql.SQLException;
import java.util.List;

public interface CityService {
    CityEntity getCityById(int id) throws SQLException, NotFoundException;
    CityEntity getCityByNameByPostalCodeByCountry(String name,String postalCode,int countryId) throws SQLException, NotFoundException, ConstraintViolationException;
    List<CityEntity> findByIds(List<Integer> ids) throws SQLException;
    void addCity(CityEntity city) throws SQLException;
    void updateCity(CityEntity city) throws ConstraintViolationException;
    CityEntity getOrCreateCity(String cityName, String postalCode, Integer countryId) throws SQLException, NotFoundException, ConstraintViolationException;
}
