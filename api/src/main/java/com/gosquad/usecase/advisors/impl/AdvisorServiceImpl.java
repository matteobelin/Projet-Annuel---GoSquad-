package com.gosquad.usecase.advisors.impl;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.advisors.AdvisorModel;
import com.gosquad.infrastructure.persistence.advisors.AdvisorRepository;
import com.gosquad.domain.advisors.AdvisorEntity;
import com.gosquad.usecase.advisors.AdvisorMapper;
import com.gosquad.usecase.advisors.AdvisorService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;


@Service
public class AdvisorServiceImpl implements AdvisorService {

    private final AdvisorRepository advisorRepository;
    private final AdvisorMapper advisorMapper;

    public AdvisorServiceImpl(AdvisorRepository advisorRepository, AdvisorMapper advisorMapper) {
        this.advisorRepository = advisorRepository;
        this.advisorMapper = advisorMapper;
    }

    @Override
    public List<AdvisorEntity> getAllAdvisors() throws SQLException {
        List<AdvisorModel> advisorModels = advisorRepository.getAll();
        return advisorMapper.modelToEntity(advisorModels);
    }

    @Override
    public AdvisorEntity getAdvisorById(int id) throws SQLException,NotFoundException {
        AdvisorModel advisorModel = advisorRepository.getById(id);
        return advisorMapper.modelToEntity(advisorModel);
    }

    @Override
    public AdvisorEntity getAdvisorByEmail(String email) throws SQLException, NotFoundException, ConstraintViolationException {
        AdvisorModel advisorModel = advisorRepository.getByEmail(email);
        return advisorMapper.modelToEntity(advisorModel);
    }

    @Override
    public boolean validateAdvisorPassword(String email, String password) throws SQLException, NotFoundException, ConstraintViolationException {
        AdvisorModel advisorModel = advisorRepository.getByEmail(email);
        return BCrypt.checkpw(password, advisorModel.getPassword());
    }

}
