package com.gosquad.usecase.groups.impl;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.groups.GroupEntity;
import com.gosquad.infrastructure.persistence.groups.GroupModel;
import com.gosquad.infrastructure.persistence.groups.GroupRepository;
import com.gosquad.usecase.groups.GroupMapper;
import com.gosquad.usecase.groups.GroupService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;

    public GroupServiceImpl(GroupRepository groupRepository, GroupMapper groupMapper) {
        this.groupRepository = groupRepository;
        this.groupMapper = groupMapper;
    }

    @Override
    public GroupEntity getGroupByName(String name) throws SQLException, NotFoundException {
        GroupModel groupModel = groupRepository.getByName(name);
        return groupMapper.modelToEntity(groupModel);
    }

    @Override
    public GroupEntity getGroupById(int id) throws SQLException, NotFoundException {
        GroupModel groupModel = groupRepository.getById(id);
        return groupMapper.modelToEntity(groupModel);
    }

    @Override
    public List<GroupEntity> getAllGroups() throws ConstraintViolationException {
        List<GroupModel> groupModels = groupRepository.getAllGroups();
        return groupMapper.modelToEntity(groupModels);
    }

    @Override
    public void addGroup(GroupEntity group) throws SQLException, ConstraintViolationException {
        GroupModel groupModel = groupMapper.entityToModel(group);
        groupRepository.addGroup(groupModel);
        group.setId(groupModel.getId());
        group.setCreatedAt(groupModel.getCreatedAt());
        group.setUpdatedAt(groupModel.getUpdatedAt());
    }

    @Override
    public void updateGroup(GroupEntity group) throws SQLException, ConstraintViolationException {
        GroupModel groupModel = groupMapper.entityToModel(group);
        groupRepository.updateGroup(groupModel);
        group.setUpdatedAt(groupModel.getUpdatedAt());
    }

    @Override
    public void deleteGroup(int id) throws SQLException {
        groupRepository.deleteGroup(id);
    }
}
