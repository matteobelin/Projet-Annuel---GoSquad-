package com.gosquad.data.advisors;


import com.gosquad.core.exceptions.NotFoundException;

import java.sql.SQLException;
import java.util.List;

public interface AdvisorRepository {
    List<AdvisorModel> getAll() throws SQLException;
    AdvisorModel getById(int id) throws SQLException, NotFoundException;
}
