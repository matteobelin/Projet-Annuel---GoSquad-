package com.gosquad.presentation.controller;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.groups.GroupEntity;
import com.gosquad.presentation.DTO.groups.CreateGroupRequestDTO;
import com.gosquad.presentation.DTO.groups.GroupResponseDTO;
import com.gosquad.usecase.groups.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/groups")
@CrossOrigin(origins = "http://localhost:4200")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody CreateGroupRequestDTO request) {
        try {
            // Créer le groupe
            GroupEntity group = new GroupEntity(null, request.getName());
            group.setVisible(request.getVisible());
            groupService.addGroup(group);

            // Ajouter les participants au groupe
            if (request.getParticipantIds() != null && !request.getParticipantIds().isEmpty()) {
                // TODO: Implémenter l'ajout des customers au groupe quand CustomerService sera disponible
                // for (Integer customerId : request.getParticipantIds()) {
                //     customerService.addCustomerToGroup(customerId, group.getId());
                // }
            }

            GroupResponseDTO response = new GroupResponseDTO(
                group.getId(),
                group.getName(),
                group.getVisible(),
                group.getCreatedAt(),
                group.getUpdatedAt()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Erreur de validation: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erreur serveur: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllGroups() {
        try {
            List<GroupEntity> groups = groupService.getAllGroups();
            List<GroupResponseDTO> response = groups.stream()
                .map(group -> new GroupResponseDTO(
                    group.getId(), 
                    group.getName(), 
                    group.getVisible(),
                    group.getCreatedAt(),
                    group.getUpdatedAt()
                ))
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(response);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erreur serveur: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGroupById(@PathVariable Integer id) {
        try {
            GroupEntity group = groupService.getGroupById(id);
            GroupResponseDTO response = new GroupResponseDTO(
                group.getId(), 
                group.getName(), 
                group.getVisible(),
                group.getCreatedAt(),
                group.getUpdatedAt()
            );
            
            return ResponseEntity.ok(response);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erreur serveur: " + e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Groupe non trouvé");
        }
    }
}
