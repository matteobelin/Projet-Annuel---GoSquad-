package com.gosquad.data.advisors;

import java.sql.SQLException;
import java.util.List;

public interface AdvisorRepository {
    List<AdvisorModel> getAll() throws SQLException;
}
