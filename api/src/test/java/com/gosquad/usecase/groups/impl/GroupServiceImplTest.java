package com.gosquad.usecase.groups.impl;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.groups.GroupEntity;
import com.gosquad.infrastructure.persistence.groups.GroupModel;
import com.gosquad.infrastructure.persistence.groups.GroupRepository;
import com.gosquad.usecase.groups.GroupMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GroupServiceImplTest {
    private GroupRepository groupRepository;
    private GroupMapper groupMapper;
    private GroupServiceImpl groupService;

    @BeforeEach
    void setUp() {
        groupRepository = mock(GroupRepository.class);
        groupMapper = new GroupMapper();
        groupService = new GroupServiceImpl(groupRepository, groupMapper);
    }

    @Test
    void testGetGroupByIdSuccess() throws SQLException, NotFoundException {
        GroupModel model = new GroupModel(1, "Group1", true, LocalDateTime.now(), LocalDateTime.now());
        when(groupRepository.getById(1)).thenReturn(model);
        GroupEntity entity = groupService.getGroupById(1);
        assertNotNull(entity);
        assertEquals(1, entity.getId());
        assertEquals("Group1", entity.getName());
        assertTrue(entity.getVisible());
    }

    @Test
    void testGetGroupByIdNotFound() throws SQLException, NotFoundException {
        when(groupRepository.getById(99)).thenThrow(new NotFoundException("Not found"));
        assertThrows(NotFoundException.class, () -> groupService.getGroupById(99));
    }

    @Test
    void testGetAllGroupsSuccess() throws ConstraintViolationException {
        List<GroupModel> models = Arrays.asList(
                new GroupModel(1, "Group1", true, LocalDateTime.now(), LocalDateTime.now()),
                new GroupModel(2, "Group2", false, LocalDateTime.now(), LocalDateTime.now())
        );
        when(groupRepository.getAllGroups()).thenReturn(models);
        List<GroupEntity> entities = groupService.getAllGroups();
        assertEquals(2, entities.size());
        assertEquals("Group1", entities.get(0).getName());
        assertEquals("Group2", entities.get(1).getName());
    }

    @Test
    void testAddGroup() throws SQLException, ConstraintViolationException {
        GroupEntity entity = new GroupEntity(null, "GroupNew", true, LocalDateTime.now(), LocalDateTime.now());
        GroupModel model = groupMapper.entityToModel(entity);
        // Simuler que le repo met à jour l'id et les timestamps
        doAnswer(invocation -> {
            GroupModel arg = invocation.getArgument(0);
            arg.setId(10);
            arg.setCreatedAt(LocalDateTime.of(2024,1,1,0,0));
            arg.setUpdatedAt(LocalDateTime.of(2024,1,1,0,0));
            return null;
        }).when(groupRepository).addGroup(any(GroupModel.class));
        groupService.addGroup(entity);
        assertEquals(10, entity.getId());
        assertEquals(LocalDateTime.of(2024,1,1,0,0), entity.getCreatedAt());
        assertEquals(LocalDateTime.of(2024,1,1,0,0), entity.getUpdatedAt());
    }

    @Test
    void testUpdateGroup() throws SQLException, ConstraintViolationException {
        GroupEntity entity = new GroupEntity(1, "Group1", true, LocalDateTime.now(), LocalDateTime.now());
        GroupModel model = groupMapper.entityToModel(entity);
        doAnswer(invocation -> {
            GroupModel arg = invocation.getArgument(0);
            arg.setUpdatedAt(LocalDateTime.of(2025,1,1,0,0));
            return null;
        }).when(groupRepository).updateGroup(any(GroupModel.class));
        groupService.updateGroup(entity);
        assertEquals(LocalDateTime.of(2025,1,1,0,0), entity.getUpdatedAt());
    }

    @Test
    void testDeleteGroup() throws SQLException {
        doNothing().when(groupRepository).deleteGroup(1);
        groupService.deleteGroup(1);
        verify(groupRepository, times(1)).deleteGroup(1);
    }
}
