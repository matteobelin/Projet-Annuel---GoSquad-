package com.gosquad.infrastructure.persistence.travels.impl;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.Repository;
import com.gosquad.infrastructure.persistence.travels.TravelModel;
import com.gosquad.infrastructure.persistence.travels.TravelRepository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@org.springframework.stereotype.Repository
public class TravelRepositoryImpl extends Repository<TravelModel> implements TravelRepository {

    public static final String TABLE_NAME = "travel";

    public TravelRepositoryImpl() throws SQLException {
        super(TABLE_NAME);
    }

    @Override
    protected TravelModel mapResultSetToEntity(java.sql.ResultSet rs) throws SQLException {
        return new TravelModel(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("destination"),
                rs.getDate("start_date"),
                rs.getDate("end_date"),
                rs.getDouble("budget"),
                rs.getString("description"),
                rs.getInt("group_id"),
                rs.getInt("company_id")
        );
    }

    public List<TravelModel> getAllByCompanyId(int companyId) throws Exception {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("company_id", companyId);
        return findAllBy(conditions);
    }

    public TravelModel getTravelByTitleAndCompanyId(String title, int companyId) throws SQLException, NotFoundException {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("title", title);
        conditions.put("company_id", companyId);
        return findByMultiple(conditions);
    }

    public void createTravel(TravelModel travel) throws SQLException {
        Map<String, Object> values = new HashMap<>();
        values.put("title", travel.getTitle());
        values.put("destination", travel.getDestination());
        values.put("start_date", travel.getStartDate());
        values.put("end_date", travel.getEndDate());
        values.put("budget", travel.getBudget());
        values.put("description", travel.getDescription());
        values.put("group_id", travel.getGroupId());
        values.put("company_id", travel.getCompanyId());
        travel.setId(insert(values));
    }

    public void updateTravel(TravelModel travel) throws SQLException {
        Map<String, Object> values = new HashMap<>();
        values.put("title", travel.getTitle());
        values.put("destination", travel.getDestination());
        values.put("start_date", travel.getStartDate());
        values.put("end_date", travel.getEndDate());
        values.put("budget", travel.getBudget());
        values.put("description", travel.getDescription());
        values.put("group_id", travel.getGroupId());
        values.put("company_id", travel.getCompanyId());
        updateBy("id", travel.getId(), values);
    }

    public void deleteTravel(int id) throws SQLException {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("id", id);
        deleteBy(conditions);
    }
}
