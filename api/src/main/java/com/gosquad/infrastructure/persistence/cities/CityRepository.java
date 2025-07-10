package com.gosquad.infrastructure.persistence.cities;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;

import java.sql.SQLException;
import java.util.List;

public interface CityRepository {
    CityModel getById(int id) throws SQLException, NotFoundException;
    CityModel getByNameByPostalCodeByCountry(String name,String postalCode,int countryId) throws SQLException, NotFoundException, ConstraintViolationException;
    List<CityModel> findByIds(List<Integer> ids) throws SQLException;
    void addCity(CityModel city) throws SQLException;
    void updateCity(CityModel city) throws ConstraintViolationException;
}
