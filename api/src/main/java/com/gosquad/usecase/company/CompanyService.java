package com.gosquad.usecase.company;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.company.CompanyEntity;

import java.sql.SQLException;

public interface CompanyService {
    CompanyEntity getCompanyById(int id) throws SQLException, NotFoundException;
}
