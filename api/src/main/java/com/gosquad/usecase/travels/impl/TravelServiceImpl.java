package com.gosquad.usecase.travels.impl;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.travels.TravelInformationEntity;
import com.gosquad.infrastructure.persistence.travels.TravelRepository;
import com.gosquad.infrastructure.persistence.travels.TravelModel;
import com.gosquad.usecase.travels.TravelService;
import com.gosquad.usecase.travels.TravelMapper;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class TravelServiceImpl implements TravelService {
    
    private final TravelRepository travelRepository;
    private final TravelMapper travelMapper;

    public TravelServiceImpl(TravelRepository travelRepository, TravelMapper travelMapper) {
        this.travelRepository = travelRepository;
        this.travelMapper = travelMapper;
    }

    @Override
    public TravelInformationEntity getTravelById(int id) throws SQLException, ConstraintViolationException, NotFoundException {
        TravelModel travelModel = travelRepository.getById(id);
        return travelMapper.modelToEntity(travelModel);
    }

    @Override
    public List<TravelInformationEntity> getAllTravels() throws ConstraintViolationException {
        List<TravelModel> travelModels = travelRepository.getAllTravels();
        return travelMapper.modelToEntity(travelModels);
    }

    @Override
    public void addTravel(TravelInformationEntity travel) throws SQLException, ConstraintViolationException {
        TravelModel travelModel = travelMapper.entityToModel(travel);
        travelRepository.addTravel(travelModel);
        travel.setId(travelModel.getId());
    }

    @Override
    public void updateTravel(TravelInformationEntity travel) throws SQLException, ConstraintViolationException {
        TravelModel travelModel = travelMapper.entityToModel(travel);
        travelRepository.updateTravel(travelModel);
    }

    @Override
    public void deleteTravel(int id) throws SQLException {
        travelRepository.deleteTravel(id);
    }
}
