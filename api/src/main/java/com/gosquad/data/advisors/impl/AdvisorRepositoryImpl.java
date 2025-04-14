package com.gosquad.data.advisors.impl;

import com.gosquad.data.Repository;
import com.gosquad.data.advisors.AdvisorModel;
import com.gosquad.data.advisors.AdvisorRepository;
import java.sql.ResultSet;
import java.sql.SQLException;

@org.springframework.stereotype.Repository
public class AdvisorRepositoryImpl extends Repository<AdvisorModel> implements AdvisorRepository {

    public static final String TABLE_NAME = "advisor";

    public AdvisorRepositoryImpl() throws SQLException {
        super(TABLE_NAME);
    }

    @Override
    protected AdvisorModel mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new AdvisorModel(
                rs.getInt("id"),
                rs.getString("firstname"),
                rs.getString("lastname"),
                rs.getString("email"),
                rs.getString("phone_number")
        );

    }
}
