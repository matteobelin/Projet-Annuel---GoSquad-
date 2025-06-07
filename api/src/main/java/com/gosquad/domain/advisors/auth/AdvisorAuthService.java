package com.gosquad.domain.advisors.auth;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;

import java.sql.SQLException;

public interface AdvisorAuthService {
    boolean authentification(String email, String password, String companyCode)throws SQLException, NotFoundException, ConstraintViolationException;
    String generateJwtToken(String email, String companyCode)throws SQLException, ConstraintViolationException,NotFoundException;
}
