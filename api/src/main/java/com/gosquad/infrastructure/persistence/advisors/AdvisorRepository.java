package com.gosquad.infrastructure.persistence.advisors;


import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;

import java.sql.SQLException;
import java.util.List;

public interface AdvisorRepository {
    List<AdvisorModel> getAll() throws SQLException;
    AdvisorModel getById(int id) throws SQLException, NotFoundException;
    AdvisorModel getByEmail(String email) throws SQLException, NotFoundException, ConstraintViolationException;
}
