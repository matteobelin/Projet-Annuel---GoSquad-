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
    public TravelInformationEntity getTravelById(int id) {
        try {
            return travelMapper.modelToEntity(travelRepository.getById(id));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<TravelInformationEntity> getAllTravels(int companyId) {
        try {
            return travelMapper.modelToEntity(travelRepository.getAllByCompanyId(companyId));
        } catch (Exception e) {
            return List.of();
        }
    }

    @Override
    public void addTravel(TravelInformationEntity travel) {
        try {
            TravelModel travelModel = travelMapper.entityToModel(travel);
            travelRepository.createTravel(travelModel);
            travel.setId(travelModel.getId());
        } catch (Exception ignored) {}
    }

    @Override
    public void updateTravel(TravelInformationEntity travel) {
        try {
            TravelModel travelModel = travelMapper.entityToModel(travel);
            travelRepository.updateTravel(travelModel);
        } catch (Exception ignored) {}
    }

    @Override
    public void deleteTravel(int id) {
        try {
            travelRepository.deleteTravel(id);
        } catch (Exception ignored) {}
    }
}
