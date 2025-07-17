package com.gosquad.usecase.groups;

import com.gosquad.domain.groups.GroupEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GroupServiceImplTest {

    private GroupService groupService;

    @BeforeEach
    void setUp() {
        groupService = mock(GroupService.class);
    }

    @Test
    void testAddGroup_success() throws Exception {
        GroupEntity group = mock(GroupEntity.class);
        doNothing().when(groupService).addGroup(group);

        groupService.addGroup(group);

        verify(groupService).addGroup(group);
    }

    @Test
    void testAddGroup_serviceThrowsException() throws Exception {
        GroupEntity group = mock(GroupEntity.class);
        doThrow(new RuntimeException("Erreur création groupe")).when(groupService).addGroup(group);

        assertThrows(RuntimeException.class, () -> groupService.addGroup(group));
        verify(groupService).addGroup(group);
    }
}
