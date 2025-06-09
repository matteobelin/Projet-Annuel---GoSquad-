package com.gosquad.usecase.advisors;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.advisors.AdvisorEntity;

import java.sql.SQLException;
import java.util.List;


public interface AdvisorService {
    List<AdvisorEntity> getAllAdvisors() throws SQLException;
    AdvisorEntity getAdvisorById(int id) throws SQLException, NotFoundException;
    AdvisorEntity getAdvisorByEmail(String email) throws SQLException, NotFoundException, ConstraintViolationException;
    boolean validateAdvisorPassword(String email, String password) throws SQLException, NotFoundException, ConstraintViolationException;

}
