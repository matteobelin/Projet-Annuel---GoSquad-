package com.gosquad.infrastructure.persistence.groups.impl;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.groups.GroupModel;
import com.gosquad.infrastructure.persistence.groups.GroupRepository;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class GroupRepositoryImpl implements GroupRepository {

    private final ConcurrentHashMap<Integer, GroupModel> groups = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    public GroupRepositoryImpl() {
        // Ajouter quelques groupes par défaut pour les tests
        GroupModel defaultGroup = new GroupModel(1, "Groupe par défaut", true, LocalDateTime.now(), LocalDateTime.now());
        groups.put(1, defaultGroup);
        idGenerator.set(2);
    }

    @Override
    public GroupModel getById(int id) throws SQLException, NotFoundException {
        GroupModel group = groups.get(id);
        if (group == null) {
            throw new NotFoundException("Group not found with ID: " + id);
        }
        return group;
    }

    @Override
    public List<GroupModel> getAllGroups() throws ConstraintViolationException {
        return new ArrayList<>(groups.values());
    }

    @Override
    public void addGroup(GroupModel group) throws SQLException, ConstraintViolationException {
        if (group.getId() == null) {
            group.setId(idGenerator.getAndIncrement());
        }
        if (group.getCreatedAt() == null) {
            group.setCreatedAt(LocalDateTime.now());
        }
        group.setUpdatedAt(LocalDateTime.now());
        groups.put(group.getId(), group);
    }

    @Override
    public void updateGroup(GroupModel group) throws SQLException, ConstraintViolationException {
        if (group.getId() == null) {
            throw new ConstraintViolationException("Cannot update group without ID");
        }
        if (!groups.containsKey(group.getId())) {
            throw new ConstraintViolationException("Failed to update group with id: " + group.getId());
        }
        group.setUpdatedAt(LocalDateTime.now());
        groups.put(group.getId(), group);
    }

    @Override
    public void deleteGroup(int id) throws SQLException {
        groups.remove(id);
    }
}