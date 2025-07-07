package com.gosquad.infrastructure.persistence.travel_activity.impl;

import com.gosquad.infrastructure.persistence.Repository;
import com.gosquad.infrastructure.persistence.travel_activity.TravelActivityModel;
import com.gosquad.infrastructure.persistence.travel_activity.TravelActivityRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Repository
public class TravelActivityRepositoryImpl extends Repository<TravelActivityModel> implements TravelActivityRepository {

    public static final String TABLE_NAME = "travel_activity";

    public TravelActivityRepositoryImpl() throws SQLException {
        super(TABLE_NAME);
    }

    protected TravelActivityModel mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new TravelActivityModel(
                rs.getInt("travel_id"),
                rs.getInt("activity_id")
        );
    }

    public List<TravelActivityModel> getActivitiesByTravelId(int travelId) throws SQLException{
        return findAllBy("travel_id", travelId);
    }


    public void addActivityToTravel(int travelId, int activityId) throws SQLException{
        Map<String, Object> values = new HashMap<>();
        values.put("travel_id", travelId);
        values.put("activity_id", activityId);
        insert(values);
    }

}
