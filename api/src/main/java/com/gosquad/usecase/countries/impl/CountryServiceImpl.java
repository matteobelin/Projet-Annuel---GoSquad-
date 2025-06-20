package com.gosquad.usecase.countries.impl;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.countries.CountryEntity;
import com.gosquad.infrastructure.persistence.countries.CountryModel;
import com.gosquad.infrastructure.persistence.countries.CountryRepository;
import com.gosquad.usecase.countries.CountryMapper;
import com.gosquad.usecase.countries.CountryService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    public CountryServiceImpl(CountryRepository countryRepository, CountryMapper countryMapper) {
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
    }

    public CountryEntity getCountryById(int id) throws SQLException, NotFoundException{
        CountryModel countryModel = countryRepository.getById(id);
        return countryMapper.modelToEntity(countryModel);
    };
    public CountryEntity getCountryByIsoCode(String isoCode) throws SQLException, NotFoundException, ConstraintViolationException{
        CountryModel countryModel = countryRepository.getByIsoCode(isoCode);
        return countryMapper.modelToEntity(countryModel);
    };
    public void addCountry(CountryEntity country) throws SQLException{
        CountryModel countryModel = countryMapper.entityToModel(country);
        countryRepository.addCountry(countryModel);
        country.setId(countryModel.getId());
    };

}
