package com.gosquad.infrastructure.persistence.activities_customers.impl;


import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.Repository;
import com.gosquad.infrastructure.persistence.activities_customers.ActivityCustomerModel;
import com.gosquad.infrastructure.persistence.activities_customers.ActivityCustomerRepository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
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
                rs.getTimestamp("start_date").toLocalDateTime(),
                rs.getTimestamp("end_date").toLocalDateTime(),
                rs.getInt("group_id")
        );
    }

    public List<ActivityCustomerModel> getActivityByCustomerId(int customerId) throws SQLException {
        return findAllBy("customer_id", customerId);
    }

    public List<ActivityCustomerModel> getActivitiesByGroupIdWhereParticipation(int groupId,boolean participation) throws SQLException {
        Map<String, Object> values = new HashMap<>();
        values.put("group_id", groupId);
        values.put("participation", participation);
        return findAllBy(values);
    }

    public List<ActivityCustomerModel> getActivityByCustomerWhereParticipation(int customerId, boolean participation,int groupId) throws SQLException, NotFoundException {
        Map<String, Object> values = new HashMap<>();
        values.put("customer_id", customerId);
        values.put("participation", participation);
        values.put("group_id", groupId);
        return findAllBy(values);
    }

    public ActivityCustomerModel getActivityById(int activityId, int customerId,int groupId) throws SQLException, NotFoundException {
        Map<String, Object> values = new HashMap<>();
        values.put("activity_id", activityId);
        values.put("customer_id", customerId);
        values.put("group_id", groupId);
        return findByMultiple(values);
    }

    public List<ActivityCustomerModel> getActivityByCustomerIdAndGroupIdWhereParticipation(int customerId, int groupId,boolean participation) throws SQLException{
        Map<String, Object> values = new HashMap<>();
        values.put("customer_id", customerId);
        values.put("group_id", groupId);
        values.put("participation", participation);
        return findAllBy(values);
    }

    public List<ActivityCustomerModel> getCustomersByActivityIdAndGroupId(int activityId, int groupId) throws SQLException{
        Map<String, Object> values = new HashMap<>();
        values.put("activity_id", activityId);
        values.put("group_id", groupId);
        return findAllBy(values);
    }

    public void createActivityCustomer(ActivityCustomerModel activityCustomer) throws SQLException {
        Map<String, Object> values = new HashMap<>();
        values.put("activity_id", activityCustomer.getActivityId());
        values.put("customer_id", activityCustomer.getCustomerId());
        values.put("participation", activityCustomer.getParticipation());
        values.put("start_date", activityCustomer.getStartDate());
        values.put("end_date", activityCustomer.getEndDate());
        values.put("group_id", activityCustomer.getGroupId());
        insert(values);
    }

    public void updateActivityCustomer(ActivityCustomerModel activityCustomer) throws SQLException {
        Map<String, Object> condition = new HashMap<>();
        condition.put("activity_id", activityCustomer.getActivityId());
        condition.put("customer_id", activityCustomer.getCustomerId());
        condition.put("group_id", activityCustomer.getGroupId());

        Map<String, Object> values = new HashMap<>();
        values.put("participation", activityCustomer.getParticipation());
        values.put("start_date", activityCustomer.getStartDate());
        values.put("end_date", activityCustomer.getEndDate());
        updateByMultiple(condition, values);
    }


}
