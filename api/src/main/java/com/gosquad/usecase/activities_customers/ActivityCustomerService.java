package com.gosquad.usecase.activities_customers;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.activities_customers.ActivityCustomerEntity;

import java.sql.SQLException;

public interface ActivityCustomerService {
    ActivityCustomerEntity getActivityByCustomerId(int customerId) throws NotFoundException, SQLException;
    ActivityCustomerEntity getActivityByCustomerWhereParticipation(int customerId, boolean participation) throws SQLException, NotFoundException;
    ActivityCustomerEntity getActivityById(int activityId, int customerId) throws SQLException, NotFoundException;
    void createActivityCustomer(ActivityCustomerEntity activityCustomer) throws SQLException;
    void updateActivityCustomer(ActivityCustomerEntity activityCustomer) throws SQLException;
}
