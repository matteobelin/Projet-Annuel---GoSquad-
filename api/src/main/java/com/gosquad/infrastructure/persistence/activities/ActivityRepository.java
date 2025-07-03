package com.gosquad.infrastructure.persistence.activities;

import com.gosquad.core.exceptions.NotFoundException;

import java.sql.SQLException;
import java.util.List;

public interface ActivityRepository {
    List<ActivityModel> getAll() throws Exception;
    List<ActivityModel> getAllActivitiesByCategory(int categoryId) throws SQLException;
    ActivityModel getById(int id)throws SQLException, NotFoundException;
    void createActivity(ActivityModel activity) throws SQLException;
    void updateActivity(ActivityModel activity) throws SQLException;
    void deleteActivity(int id) throws SQLException;
}
