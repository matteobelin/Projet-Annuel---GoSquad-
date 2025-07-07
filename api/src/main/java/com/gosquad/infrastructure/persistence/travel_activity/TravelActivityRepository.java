package com.gosquad.infrastructure.persistence.travel_activity;

import java.sql.SQLException;
import java.util.List;

public interface TravelActivityRepository {
    List<TravelActivityModel> getActivitiesByTravelId(int travelId) throws SQLException;
    void addActivityToTravel(int travelId, int activityId) throws SQLException;
}
