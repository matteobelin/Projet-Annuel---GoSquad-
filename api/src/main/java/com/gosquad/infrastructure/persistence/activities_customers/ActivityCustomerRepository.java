package com.gosquad.infrastructure.persistence.activities_customers;

import com.gosquad.core.exceptions.NotFoundException;

import java.sql.SQLException;
import java.util.List;

public interface ActivityCustomerRepository {
    List<ActivityCustomerModel> getActivitiesByGroupIdWhereParticipation(int groupId, boolean participation) throws SQLException;
    List<ActivityCustomerModel> getActivityByCustomerId(int customerId) throws SQLException;
    List<ActivityCustomerModel> getActivityByCustomerWhereParticipation(int customerId, boolean participation,int groupeId) throws SQLException, NotFoundException;
    ActivityCustomerModel getActivityById(int activityId, int customerId, int groupId) throws SQLException, NotFoundException;
    List<ActivityCustomerModel> getActivityByCustomerIdAndGroupIdWhereParticipation(int customerId, int groupId, boolean participation) throws SQLException;
    List<ActivityCustomerModel> getCustomersByActivityIdAndGroupId(int activityId, int groupId) throws SQLException;
    void createActivityCustomer(ActivityCustomerModel activityCustomer) throws SQLException;
    void updateActivityCustomer(ActivityCustomerModel activityCustomer) throws SQLException;
}
