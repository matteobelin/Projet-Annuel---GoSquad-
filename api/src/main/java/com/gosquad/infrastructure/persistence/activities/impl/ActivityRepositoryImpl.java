package com.gosquad.infrastructure.persistence.activities.impl;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.Repository;
import com.gosquad.infrastructure.persistence.activities.ActivityModel;
import com.gosquad.infrastructure.persistence.activities.ActivityRepository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Repository
public class ActivityRepositoryImpl extends Repository<ActivityModel> implements ActivityRepository {
    public static final String TABLE_NAME = "activity";

    public ActivityRepositoryImpl() throws SQLException {
        super(TABLE_NAME);
    }

    @Override
    protected ActivityModel mapResultSetToEntity(java.sql.ResultSet rs) throws SQLException {
        return new ActivityModel(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getInt("address_id"),
                rs.getInt("price_id"),
                rs.getInt("category_id"),
                rs.getInt("company_id")
        );
    }

    public List<ActivityModel> getAllByCompanyId(int companyId) throws Exception{
        return findAllBy("company_id", companyId);
    }

    public List<ActivityModel> getAllActivitiesByCategory(int categoryId) throws SQLException {
            return findAllBy("category_id", categoryId);
    };

    public ActivityModel getByIdAndCompanyId(int id,int companyId)throws SQLException, NotFoundException{
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("id", id);
        conditions.put("company_id", companyId);
        return findByMultiple(conditions);
    }

    public void createActivity(ActivityModel activity) throws SQLException{
        Map<String, Object> values = new HashMap<>();
        values.put("name", activity.getName());
        values.put("description", activity.getDescription());
        values.put("address_id", activity.getAddressId());
        values.put("price_id", activity.getPriceId());
        values.put("category_id", activity.getCategoryId());
        values.put("company_id", activity.getCompanyId());
        activity.setId(insert(values));
    }

    public void updateActivity(ActivityModel activity) throws SQLException {
        Map<String, Object> values = new HashMap<>();
        values.put("name", activity.getName());
        values.put("description", activity.getDescription());
        values.put("address_id", activity.getAddressId());
        values.put("price_id", activity.getPriceId());
        values.put("category_id", activity.getCategoryId());
        values.put("company_id", activity.getCompanyId());
        updateBy("id", activity.getId(), values);
    }

    public void deleteActivity(int id) throws SQLException {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("id", id);
        deleteBy(conditions);
    }




}
