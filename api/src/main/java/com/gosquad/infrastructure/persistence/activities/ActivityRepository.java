package com.gosquad.infrastructure.persistence.activities;

import com.gosquad.core.exceptions.NotFoundException;

import java.sql.SQLException;
import java.util.List;

public interface ActivityRepository {
    List<ActivityModel> getAllByCompanyId(int companyId) throws Exception;
    List<ActivityModel> getAllActivitiesByCategory(int categoryId) throws SQLException;
    ActivityModel getByIdAndCompanyId(int id,int companyId)throws SQLException, NotFoundException;
    void createActivity(ActivityModel activity) throws SQLException;
    void updateActivity(ActivityModel activity) throws SQLException;
    void deleteActivity(int id) throws SQLException;
}
