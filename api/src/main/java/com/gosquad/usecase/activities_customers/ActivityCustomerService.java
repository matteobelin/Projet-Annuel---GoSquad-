package com.gosquad.usecase.activities_customers;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.activities_customers.ActivityCustomerEntity;
import com.gosquad.infrastructure.persistence.activities_customers.ActivityCustomerModel;

import java.sql.SQLException;
import java.util.List;

public interface ActivityCustomerService {
    List<ActivityCustomerEntity> getActivityByCustomerId(int customerId) throws SQLException;
    List<ActivityCustomerEntity> getActivitiesByGroupId(int groupId) throws SQLException;
    ActivityCustomerEntity getActivityByCustomerWhereParticipation(int customerId, boolean participation) throws SQLException, NotFoundException;
    ActivityCustomerEntity getActivityById(int activityId, int customerId,int groupId) throws SQLException, NotFoundException;
    List<ActivityCustomerModel> getActivityByCustomerIdAndGroupId(int customerId, int groupId) throws SQLException;
    void createActivityCustomer(ActivityCustomerEntity activityCustomer) throws SQLException;
    void updateActivityCustomer(ActivityCustomerEntity activityCustomer) throws SQLException;
}
