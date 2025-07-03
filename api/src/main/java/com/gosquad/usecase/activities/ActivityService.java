package com.gosquad.usecase.activities;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.activities.ActivityEntity;

import java.sql.SQLException;
import java.util.List;

public interface ActivityService {
    List<ActivityEntity> getAllActivities() throws Exception;
    List<ActivityEntity> getAllActivitiesByCategory(int categoryId) throws SQLException;
    ActivityEntity getById(int id)throws SQLException, NotFoundException;
    void createActivity(ActivityEntity activity) throws SQLException;
    void updateActivity(ActivityEntity activity) throws SQLException;
    void deleteActivity(int id) throws SQLException;
}
