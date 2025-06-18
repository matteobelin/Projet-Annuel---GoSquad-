package com.gosquad.infrastructure.persistence.countries;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;

import java.sql.SQLException;

public interface CountryRepository {
    CountryModel getById(int id) throws SQLException, NotFoundException;
    CountryModel getByIsoCode(String isoCode) throws SQLException, NotFoundException, ConstraintViolationException;
    void addCountry(CountryModel country) throws SQLException;
}
