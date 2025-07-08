package com.gosquad.usecase.countries;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.countries.CountryEntity;

import java.sql.SQLException;
import java.util.List;

public interface CountryService {
    CountryEntity getCountryById(int id) throws SQLException, NotFoundException;
    CountryEntity getCountryByIsoCode(String isoCode) throws SQLException, NotFoundException, ConstraintViolationException;
    List<CountryEntity> findByIds(List<Integer> ids) throws SQLException;
    void addCountry(CountryEntity country) throws SQLException;
}
