package com.gosquad.data.company.impl;

import com.gosquad.data.Repository;
import com.gosquad.data.company.CompanyModel;
import com.gosquad.data.company.CompanyRepository;
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

}
