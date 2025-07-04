package com.gosquad.usecase.travel_activity.impl;

import com.gosquad.core.exceptions.InvalidArgumentException;
import com.gosquad.domain.travel_activity.TravelActivityEntity;
import com.gosquad.infrastructure.persistence.travel_activity.TravelActivityModel;
import com.gosquad.infrastructure.persistence.travel_activity.TravelActivityRepository;
import com.gosquad.usecase.travel_activity.TravelActivityMapper;
import com.gosquad.usecase.travel_activity.TravelActivityService;

import java.sql.SQLException;
import java.util.List;

public class TravelActivityServiceImpl implements TravelActivityService {

    private final TravelActivityMapper travelActivityMapper;
    private final TravelActivityRepository travelActivityRepository;

    public TravelActivityServiceImpl(TravelActivityMapper travelActivityMapper, TravelActivityRepository travelActivityRepository) {
        this.travelActivityMapper = travelActivityMapper;
        this.travelActivityRepository = travelActivityRepository;
    }

    public List<TravelActivityEntity> getActivitiesByTravelId(int travelId) throws InvalidArgumentException,SQLException{
        List<TravelActivityEntity> travelActivities = travelActivityMapper.modelToEntity(
                travelActivityRepository.getActivitiesByTravelId(travelId)
        );
        if (travelActivities.isEmpty()) {
            throw new InvalidArgumentException("No activities found for travel ID: " + travelId);
        }
        return travelActivities;
    }

    public void addActivityToTravel(int travelId, int activityId) {
        TravelActivityModel travelActivityModel = new TravelActivityModel(travelId, activityId);
        travelActivityMapper.modelToEntity(travelActivityModel);
    }
}
