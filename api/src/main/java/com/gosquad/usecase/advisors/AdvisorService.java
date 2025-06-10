package com.gosquad.usecase.advisors;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.advisors.AdvisorEntity;

import java.sql.SQLException;
import java.util.List;


public interface AdvisorService {
    List<AdvisorEntity> getAllAdvisors() throws SQLException;
    AdvisorEntity getAdvisorById(int id) throws SQLException, NotFoundException;

}
