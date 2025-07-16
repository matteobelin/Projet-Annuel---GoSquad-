package com.gosquad.infrastructure.persistence.groups;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;

import java.sql.SQLException;
import java.util.List;

public interface GroupRepository {
    GroupModel getById(int id) throws SQLException, NotFoundException;
    List<GroupModel> getAllGroups() throws ConstraintViolationException;
    void addGroup(GroupModel group) throws SQLException, ConstraintViolationException;
    GroupModel getByName(String name) throws SQLException, NotFoundException;
    void updateGroup(GroupModel group) throws SQLException, ConstraintViolationException;
    void deleteGroup(int id) throws SQLException;
}