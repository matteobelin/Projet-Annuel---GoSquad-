package com.gosquad.infrastructure.persistence.groups;

import com.gosquad.core.exceptions.NotFoundException;

import java.sql.SQLException;
import java.util.List;

public interface GroupRepository {
    List<GroupModel> getAllGroups(int companyId) throws Exception;
    GroupModel getById(int id) throws NotFoundException, SQLException;
    List<GroupModel> findByIds(List<Integer> ids) throws SQLException;
    GroupModel getByName(String name) throws SQLException, NotFoundException;
    void addGroup(GroupModel group) throws SQLException;
    void updateGroup(GroupModel group) throws SQLException;
    void deleteGroup(int id) throws SQLException;
}