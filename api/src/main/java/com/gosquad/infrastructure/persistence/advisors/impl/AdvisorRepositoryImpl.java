package com.gosquad.infrastructure.persistence.advisors.impl;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.advisors.AdvisorModel;
import com.gosquad.infrastructure.persistence.Repository;
import com.gosquad.infrastructure.persistence.advisors.AdvisorRepository;
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
        try {
            return new AdvisorModel(
                    rs.getInt("id"),
                    rs.getString("firstname"),
                    rs.getString("lastname"),
                    rs.getString("email"),
                    rs.getString("phone_number"),
                    rs.getInt("compagny_id"),
                    rs.getString("password"),
                    rs.getString("role")
            );
        } catch (SQLException e) {
            return new AdvisorModel(
                    rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getInt("compagny_id"),
                    rs.getString("role")
            );
        }
    }

    @Override
    public AdvisorModel getByEmail(String email) throws SQLException, ConstraintViolationException {
        try {
            return findBy("email", email, "id", "email", "password", "compagny_id","role"
            );
        } catch (NotFoundException e) {
            throw new ConstraintViolationException(e);
        }
    }
}
