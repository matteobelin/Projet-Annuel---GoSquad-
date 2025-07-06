package com.gosquad.usecase.activities_customers;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.activities_customers.ActivityCustomerEntity;

import java.sql.SQLException;
import java.util.List;

public interface ActivityCustomerService {
    List<ActivityCustomerEntity> getActivitiesByGroupIdWhereParticipation(int groupId, boolean participation) throws SQLException;
    List<ActivityCustomerEntity> getActivityByCustomerId(int customerId) throws SQLException;
    List<ActivityCustomerEntity> getActivityByCustomerWhereParticipation(int customerId, boolean participation, int groupeId) throws SQLException, NotFoundException;
    ActivityCustomerEntity getActivityById(int activityId, int customerId, int groupId) throws SQLException, NotFoundException;
    List<ActivityCustomerEntity> getActivityByCustomerIdAndGroupIdWhereParticipation(int customerId, int groupId, boolean participation) throws SQLException;
    List<ActivityCustomerEntity> getCustomersByActivityIdAndGroupId(int activityId, int groupId) throws SQLException;
    void createActivityCustomer(ActivityCustomerEntity activityCustomer) throws SQLException;
    void updateActivityCustomer(ActivityCustomerEntity activityCustomer) throws SQLException;
}
