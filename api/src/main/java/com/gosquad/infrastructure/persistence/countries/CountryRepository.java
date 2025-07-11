package com.gosquad.infrastructure.persistence.countries;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;

import java.sql.SQLException;
import java.util.List;

public interface CountryRepository {
    CountryModel getById(int id) throws SQLException, NotFoundException;
    CountryModel getByIsoCode(String isoCode) throws SQLException, NotFoundException, ConstraintViolationException;
    List<CountryModel> findByIds(List<Integer> ids) throws SQLException;
    void addCountry(CountryModel country) throws SQLException;
}
