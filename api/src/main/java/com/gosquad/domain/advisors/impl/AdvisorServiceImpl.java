package com.gosquad.domain.advisors.impl;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.data.advisors.AdvisorModel;
import com.gosquad.data.advisors.AdvisorRepository;
import com.gosquad.domain.advisors.AdvisorEntity;
import com.gosquad.domain.advisors.AdvisorMapper;
import com.gosquad.domain.advisors.AdvisorService;
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

}
