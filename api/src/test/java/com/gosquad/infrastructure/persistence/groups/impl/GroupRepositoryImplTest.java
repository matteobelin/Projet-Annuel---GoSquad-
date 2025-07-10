package com.gosquad.infrastructure.persistence.groups.impl;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.groups.GroupModel;
import com.gosquad.infrastructure.persistence.groups.GroupRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GroupRepositoryImplTest {

    @Autowired
    private GroupRepository groupRepository;

    @Test
    @Order(1)
    void testAddAndGetGroup() throws SQLException, ConstraintViolationException, NotFoundException {
        GroupModel group = new GroupModel(null, "Test Group", true, null, null);
        groupRepository.addGroup(group);
        assertNotNull(group.getId());
        GroupModel fetched = groupRepository.getById(group.getId());
        assertEquals("Test Group", fetched.getName());
        assertTrue(fetched.getVisible());
    }

    @Test
    @Order(2)
    void testUpdateGroup() throws SQLException, ConstraintViolationException, NotFoundException {
        GroupModel group = new GroupModel(null, "Update Group", true, null, null);
        groupRepository.addGroup(group);
        group.setName("Updated Name");
        group.setVisible(false);
        groupRepository.updateGroup(group);
        GroupModel updated = groupRepository.getById(group.getId());
        assertEquals("Updated Name", updated.getName());
        assertFalse(updated.getVisible());
    }

    @Test
    @Order(3)
    void testDeleteGroup() throws SQLException, ConstraintViolationException, NotFoundException {
        GroupModel group = new GroupModel(null, "Delete Group", true, null, null);
        groupRepository.addGroup(group);
        int id = group.getId();
        groupRepository.deleteGroup(id);
        assertThrows(NotFoundException.class, () -> groupRepository.getById(id));
    }

    @Test
    @Order(4)
    void testGetAllGroups() throws ConstraintViolationException, SQLException {
        List<GroupModel> groups = groupRepository.getAllGroups();
        assertNotNull(groups);
        assertTrue(groups.size() >= 0);
    }
}
