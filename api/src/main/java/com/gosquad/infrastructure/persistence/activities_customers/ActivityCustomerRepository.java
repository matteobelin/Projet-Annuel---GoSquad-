package com.gosquad.infrastructure.persistence.activities_customers;

import com.gosquad.core.exceptions.NotFoundException;

import java.sql.SQLException;
import java.util.List;

public interface ActivityCustomerRepository {
    List<ActivityCustomerModel> getActivitiesByGroupId(int groupId) throws SQLException;
    List<ActivityCustomerModel> getActivityByCustomerId(int customerId) throws SQLException;
    ActivityCustomerModel getActivityByCustomerWhereParticipation(int customerId, boolean participation) throws SQLException, NotFoundException;
    ActivityCustomerModel getActivityById(int activityId, int customerId, int groupId) throws SQLException, NotFoundException;
    List<ActivityCustomerModel> getActivityByCustomerIdAndGroupId(int customerId, int groupId) throws SQLException;
    void createActivityCustomer(ActivityCustomerModel activityCustomer) throws SQLException;
    void updateActivityCustomer(ActivityCustomerModel activityCustomer) throws SQLException;
}
