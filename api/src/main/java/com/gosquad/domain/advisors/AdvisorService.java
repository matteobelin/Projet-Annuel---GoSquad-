package com.gosquad.domain.advisors;


import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;

import java.sql.SQLException;
import java.util.List;


public interface AdvisorService {
    List<AdvisorEntity> getAllAdvisors() throws SQLException;
    AdvisorEntity getAdvisorById(int id) throws SQLException, NotFoundException;

}
