package com.gosquad.infrastructure.persistence.company.impl;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.Repository;
import com.gosquad.infrastructure.persistence.company.CompanyModel;
import com.gosquad.infrastructure.persistence.company.CompanyRepository;
import java.sql.ResultSet;
import java.sql.SQLException;


@org.springframework.stereotype.Repository
public class CompanyRepositoryImpl extends Repository<CompanyModel> implements CompanyRepository {

    public static final String TABLE_NAME = "company";

    public CompanyRepositoryImpl() throws SQLException {
        super(TABLE_NAME);
    }

    @Override
    protected CompanyModel mapResultSetToEntity(ResultSet rs) throws SQLException{
        return new CompanyModel(rs.getInt("id"), rs.getString("code"), rs.getString("name"));
    }
    public CompanyModel getByCode(String code) throws SQLException, NotFoundException {
        return findBy("code",code,"id","code","name");
    }

}
