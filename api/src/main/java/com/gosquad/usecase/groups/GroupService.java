package com.gosquad.usecase.groups;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.groups.GroupEntity;

import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

public interface GroupService {
    GroupEntity getGroupById(int id) throws SQLException, NotFoundException;
    GroupEntity getGroupByName(String name) throws SQLException, NotFoundException;
    List<GroupEntity> getAllGroups(HttpServletRequest request) throws ConstraintViolationException;
    void addGroup(GroupEntity group) throws SQLException, ConstraintViolationException;
    void updateGroup(GroupEntity group) throws SQLException, ConstraintViolationException;
    void deleteGroup(int id) throws SQLException;
}
