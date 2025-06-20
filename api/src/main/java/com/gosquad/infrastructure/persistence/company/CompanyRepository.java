package com.gosquad.infrastructure.persistence.company;
import com.gosquad.core.exceptions.NotFoundException;


import java.sql.SQLException;

public interface CompanyRepository {
    CompanyModel getById(int id) throws SQLException, NotFoundException;
    CompanyModel getByCode(String name) throws SQLException, NotFoundException;
}
