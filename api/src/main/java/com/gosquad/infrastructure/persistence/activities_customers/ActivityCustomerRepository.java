package com.gosquad.infrastructure.persistence.activities_customers;

import com.gosquad.core.exceptions.NotFoundException;

import java.sql.SQLException;

public interface ActivityCustomerRepository {
    ActivityCustomerModel getActivityByCustomerId(int customerId) throws NotFoundException, SQLException;
    ActivityCustomerModel getActivityByCustomerWhereParticipation(int customerId, boolean participation) throws SQLException, NotFoundException;
    ActivityCustomerModel getActivityById(int activityId, int customerId) throws SQLException, NotFoundException;
    void createActivityCustomer(ActivityCustomerModel activityCustomer) throws SQLException;
    void updateActivityCustomer(ActivityCustomerModel activityCustomer) throws SQLException;
}
