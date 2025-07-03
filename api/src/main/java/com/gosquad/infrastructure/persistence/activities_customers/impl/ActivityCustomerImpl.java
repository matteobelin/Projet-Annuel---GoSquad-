package com.gosquad.infrastructure.persistence.activities_customers.impl;


import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.Repository;
import com.gosquad.infrastructure.persistence.activities_customers.ActivityCustomerModel;
import com.gosquad.infrastructure.persistence.activities_customers.ActivityCustomerRepository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@org.springframework.stereotype.Repository
public class ActivityCustomerImpl extends Repository<ActivityCustomerModel> implements ActivityCustomerRepository {

    public static final String TABLE_NAME = "activity_customer";

    public ActivityCustomerImpl() throws SQLException {
        super(TABLE_NAME);
    }

    @Override
    protected ActivityCustomerModel mapResultSetToEntity(java.sql.ResultSet rs) throws SQLException {
        return new ActivityCustomerModel(
                rs.getInt("activity_id"),
                rs.getInt("customer_id"),
                rs.getBoolean("participation"),
                rs.getDate("start_date"),
                rs.getDate("end_date")
        );
    }

    public ActivityCustomerModel getActivityByCustomerId(int customerId) throws SQLException, NotFoundException {
        return findBy("customer_id", customerId);
    }

    public ActivityCustomerModel getActivityByCustomerWhereParticipation(int customerId, boolean participation) throws SQLException, NotFoundException {
        Map<String, Object> values = new HashMap<>();
        values.put("customer_id", customerId);
        values.put("participation", participation);
        return findByMultiple(values);
    }

    public ActivityCustomerModel getActivityById(int activityId, int customerId) throws SQLException, NotFoundException {
        Map<String, Object> values = new HashMap<>();
        values.put("activity_id", activityId);
        values.put("customer_id", customerId);
        return findByMultiple(values);
    }

    public void createActivityCustomer(ActivityCustomerModel activityCustomer) throws SQLException {
        Map<String, Object> values = new HashMap<>();
        values.put("activity_id", activityCustomer.getActivityId());
        values.put("customer_id", activityCustomer.getCustomerId());
        values.put("participation", activityCustomer.getParticipation());
        values.put("start_date", activityCustomer.getStartDate());
        values.put("end_date", activityCustomer.getEndDate());
        insert(values);
    }

    public void updateActivityCustomer(ActivityCustomerModel activityCustomer) throws SQLException {
        Map<String, Object> condition = new HashMap<>();
        condition.put("activity_id", activityCustomer.getActivityId());
        condition.put("customer_id", activityCustomer.getCustomerId());

        Map<String, Object> values = new HashMap<>();
        values.put("participation", activityCustomer.getParticipation());
        values.put("start_date", activityCustomer.getStartDate());
        values.put("end_date", activityCustomer.getEndDate());
        updateByMultiple(condition, values);
    }


}
