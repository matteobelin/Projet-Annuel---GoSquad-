package com.gosquad.usecase.company.impl;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.company.CompanyEntity;
import com.gosquad.infrastructure.persistence.company.CompanyModel;
import com.gosquad.infrastructure.persistence.company.CompanyRepository;
import com.gosquad.usecase.company.CompanyMapper;
import com.gosquad.usecase.company.CompanyService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    public CompanyServiceImpl(CompanyRepository companyRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

    @Override
    public CompanyEntity getCompanyById(int id) throws SQLException, NotFoundException{
        CompanyModel companyModel = companyRepository.getById(id);
        return companyMapper.modelToEntity(companyModel);
    }
}
