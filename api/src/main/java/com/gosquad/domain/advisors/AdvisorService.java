package com.gosquad.domain.advisors;


import java.sql.SQLException;
import java.util.List;


public interface AdvisorService {
    List<AdvisorEntity> getAllAdvisors() throws SQLException;
}
