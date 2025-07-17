package com.gosquad.infrastructure.persistence.groups.impl;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.Repository;
import com.gosquad.infrastructure.persistence.groups.GroupModel;
import com.gosquad.infrastructure.persistence.groups.GroupRepository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Repository
public class GroupRepositoryImpl extends Repository<GroupModel> implements GroupRepository {

    public static final String TABLE_NAME = "groups";

    public GroupRepositoryImpl() throws SQLException {
        super(TABLE_NAME);
    }

    @Override
    protected GroupModel mapResultSetToEntity(java.sql.ResultSet rs) throws SQLException {
        return new GroupModel(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getBoolean("visible"),
            rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
            rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null,
            rs.getInt("company_id")
        );
    }



    public List<GroupModel> getAllGroups(int companyId) throws Exception {
        try {
            Map<String, Object> conditions = new HashMap<>();
            conditions.put("company_id", companyId);
            return findAllBy(conditions);
        } catch (Exception e) {
            throw new com.gosquad.core.exceptions.ConstraintViolationException(e.getMessage());
        }
    }

    public GroupModel getByName(String name) throws SQLException, NotFoundException {
        return findBy("name", name);
    }

    public void addGroup(GroupModel group) throws SQLException {
        Map<String, Object> values = new HashMap<>();
        values.put("name", group.getName());
        values.put("visible", group.getVisible());
        values.put("company_id", group.getCompanyId());
        group.setId(insert(values));
    }

    public void updateGroup(GroupModel group) throws SQLException {
        Map<String, Object> values = new HashMap<>();
        values.put("name", group.getName());
        values.put("visible", group.getVisible());
        updateBy("id", group.getId(), values);
    }

    public void deleteGroup(int id) throws SQLException {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("id", id);
        deleteBy(conditions);
    }
}