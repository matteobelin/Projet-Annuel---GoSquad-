package com.gosquad.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.group.GroupEntity;
import com.gosquad.presentation.DTO.group.CreateGroupRequestDTO;
import com.gosquad.presentation.DTO.group.GroupResponseDTO;
import com.gosquad.usecase.group.GroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GroupControllerTest {

    private MockMvc mockMvc;
    private GroupService groupService;
    private GroupController groupController;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        groupService = mock(GroupService.class);
        groupController = new GroupController(groupService);
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();
        objectMapper = new ObjectMapper();
    }

    private GroupEntity createSampleGroupEntity() {
        return new GroupEntity(
                1,
                "Groupe Test",
                true,
                LocalDateTime.of(2024, 1, 1, 10, 0, 0),
                LocalDateTime.of(2024, 1, 2, 10, 0, 0)
        );
    }

    @Test
    void testCreateGroup_Success() throws Exception {
        CreateGroupRequestDTO request = new CreateGroupRequestDTO("Nouveau Groupe", true);
        GroupEntity createdGroup = createSampleGroupEntity();
        createdGroup.setName("Nouveau Groupe");

        doNothing().when(groupService).addGroup(any(GroupEntity.class));
        doAnswer(invocation -> {
            GroupEntity group = invocation.getArgument(0);
            group.setId(1);
            group.setCreatedAt(LocalDateTime.of(2024, 1, 1, 10, 0, 0));
            group.setUpdatedAt(LocalDateTime.of(2024, 1, 2, 10, 0, 0));
            return null;
        }).when(groupService).addGroup(any(GroupEntity.class));

        mockMvc.perform(post("/group")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Nouveau Groupe"))
                .andExpect(jsonPath("$.visible").value(true));

        verify(groupService, times(1)).addGroup(any(GroupEntity.class));
    }

    @Test
    void testCreateGroup_WithParticipants() throws Exception {
        CreateGroupRequestDTO request = new CreateGroupRequestDTO(
                "Groupe avec participants", 
                true, 
                List.of(1, 2, 3)
        );

        doNothing().when(groupService).addGroup(any(GroupEntity.class));
        doAnswer(invocation -> {
            GroupEntity group = invocation.getArgument(0);
            group.setId(1);
            group.setCreatedAt(LocalDateTime.of(2024, 1, 1, 10, 0, 0));
            group.setUpdatedAt(LocalDateTime.of(2024, 1, 2, 10, 0, 0));
            return null;
        }).when(groupService).addGroup(any(GroupEntity.class));

        mockMvc.perform(post("/group")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Groupe avec participants"))
                .andExpect(jsonPath("$.visible").value(true));

        verify(groupService, times(1)).addGroup(any(GroupEntity.class));
    }

    @Test
    void testCreateGroup_ConstraintViolationException() throws Exception {
        CreateGroupRequestDTO request = new CreateGroupRequestDTO("", true);

        doThrow(new ConstraintViolationException("Le nom du groupe est requis"))
                .when(groupService).addGroup(any(GroupEntity.class));

        mockMvc.perform(post("/group")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erreur de validation: Le nom du groupe est requis"));

        verify(groupService, times(1)).addGroup(any(GroupEntity.class));
    }

    @Test
    void testCreateGroup_InternalServerError() throws Exception {
        CreateGroupRequestDTO request = new CreateGroupRequestDTO("Groupe Test", true);

        doThrow(new RuntimeException("Erreur base de données"))
                .when(groupService).addGroup(any(GroupEntity.class));

        mockMvc.perform(post("/group")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Erreur serveur: Erreur base de données"));

        verify(groupService, times(1)).addGroup(any(GroupEntity.class));
    }

    @Test
    void testGetAllGroups_Success() throws Exception {
        GroupEntity group1 = createSampleGroupEntity();
        GroupEntity group2 = new GroupEntity(
                2,
                "Groupe Test 2",
                false,
                LocalDateTime.of(2024, 2, 1, 10, 0, 0),
                LocalDateTime.of(2024, 2, 2, 10, 0, 0)
        );

        when(groupService.getAllGroups()).thenReturn(List.of(group1, group2));

        mockMvc.perform(get("/group"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Groupe Test"))
                .andExpect(jsonPath("$[0].visible").value(true))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Groupe Test 2"))
                .andExpect(jsonPath("$[1].visible").value(false));

        verify(groupService, times(1)).getAllGroups();
    }

    @Test
    void testGetAllGroups_EmptyList() throws Exception {
        when(groupService.getAllGroups()).thenReturn(List.of());

        mockMvc.perform(get("/group"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(groupService, times(1)).getAllGroups();
    }

    @Test
    void testGetAllGroups_ConstraintViolationException() throws Exception {
        when(groupService.getAllGroups()).thenThrow(new ConstraintViolationException("Erreur de contrainte"));

        mockMvc.perform(get("/group"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Erreur serveur: Erreur de contrainte"));

        verify(groupService, times(1)).getAllGroups();
    }

    @Test
    void testGetGroupById_Success() throws Exception {
        GroupEntity group = createSampleGroupEntity();
        when(groupService.getGroupById(1)).thenReturn(group);

        mockMvc.perform(get("/group/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Groupe Test"))
                .andExpect(jsonPath("$.visible").value(true));

        verify(groupService, times(1)).getGroupById(1);
    }

    @Test
    void testGetGroupById_NotFound() throws Exception {
        when(groupService.getGroupById(999)).thenThrow(new NotFoundException("Groupe non trouvé"));

        mockMvc.perform(get("/group/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Groupe non trouvé"));

        verify(groupService, times(1)).getGroupById(999);
    }

    @Test
    void testGetGroupById_SQLException() throws Exception {
        when(groupService.getGroupById(1)).thenThrow(new SQLException("Erreur SQL"));

        mockMvc.perform(get("/group/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Erreur serveur: Erreur SQL"));

        verify(groupService, times(1)).getGroupById(1);
    }

    @Test
    void testCreateGroup_WithNullVisible() throws Exception {
        CreateGroupRequestDTO request = new CreateGroupRequestDTO("Groupe sans visible", null);

        doNothing().when(groupService).addGroup(any(GroupEntity.class));
        doAnswer(invocation -> {
            GroupEntity group = invocation.getArgument(0);
            group.setId(1);
            group.setVisible(true); // Default value
            group.setCreatedAt(LocalDateTime.of(2024, 1, 1, 10, 0, 0));
            group.setUpdatedAt(LocalDateTime.of(2024, 1, 2, 10, 0, 0));
            return null;
        }).when(groupService).addGroup(any(GroupEntity.class));

        mockMvc.perform(post("/group")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Groupe sans visible"))
                .andExpect(jsonPath("$.visible").value(true));

        verify(groupService, times(1)).addGroup(any(GroupEntity.class));
    }

    @Test
    void testCreateGroup_WithInvisibleGroup() throws Exception {
        CreateGroupRequestDTO request = new CreateGroupRequestDTO("Groupe invisible", false);

        doNothing().when(groupService).addGroup(any(GroupEntity.class));
        doAnswer(invocation -> {
            GroupEntity group = invocation.getArgument(0);
            group.setId(1);
            group.setCreatedAt(LocalDateTime.of(2024, 1, 1, 10, 0, 0));
            group.setUpdatedAt(LocalDateTime.of(2024, 1, 2, 10, 0, 0));
            return null;
        }).when(groupService).addGroup(any(GroupEntity.class));

        mockMvc.perform(post("/group")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Groupe invisible"))
                .andExpect(jsonPath("$.visible").value(false));

        verify(groupService, times(1)).addGroup(any(GroupEntity.class));
    }
}