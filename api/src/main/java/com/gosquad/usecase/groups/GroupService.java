package com.gosquad.usecase.groups;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.groups.GroupEntity;

import java.sql.SQLException;
import java.util.List;

public interface GroupService {
    GroupEntity getGroupById(int id) throws SQLException, NotFoundException;
    List<GroupEntity> getAllGroups() throws ConstraintViolationException;
    void addGroup(GroupEntity group) throws SQLException, ConstraintViolationException;
    void updateGroup(GroupEntity group) throws SQLException, ConstraintViolationException;
    void deleteGroup(int id) throws SQLException;
}
