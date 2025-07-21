package com.gosquad.usecase.travel_activity;

import com.gosquad.core.exceptions.InvalidArgumentException;
import com.gosquad.domain.travel_activity.TravelActivityEntity;

import java.sql.SQLException;
import java.util.List;

public interface TravelActivityService {
    List<TravelActivityEntity> getActivitiesByTravelId(int travelId) throws SQLException , InvalidArgumentException;
    void addActivityToTravel(int travelId, int activityId) throws SQLException;
}
