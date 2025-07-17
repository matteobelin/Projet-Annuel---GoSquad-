package com.gosquad.presentation.controller;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.groups.GroupEntity;
import com.gosquad.presentation.DTO.groups.CreateGroupRequestDTO;
import com.gosquad.presentation.DTO.groups.GroupResponseDTO;
import com.gosquad.usecase.groups.GroupService;
import com.gosquad.usecase.company.CompanyService;
import com.gosquad.infrastructure.jwt.JWTInterceptor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/groups")
@CrossOrigin(origins = "http://localhost:4200")
public class GroupController {

    private final GroupService groupService;
    private final com.gosquad.usecase.customergroup.CustomerGroupService customerGroupService;
    private final CompanyService companyService;
    private final JWTInterceptor jwtInterceptor;

    public GroupController(GroupService groupService, com.gosquad.usecase.customergroup.CustomerGroupService customerGroupService, CompanyService companyService, JWTInterceptor jwtInterceptor) {
        this.groupService = groupService;
        this.customerGroupService = customerGroupService;
        this.companyService = companyService;
        this.jwtInterceptor = jwtInterceptor;
    }

    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody CreateGroupRequestDTO request, HttpServletRequest httpRequest) {
        try {
            // Extraire le companyId depuis le JWT
            String authHeader = httpRequest.getHeader("Authorization");
            String token = authHeader.substring(7);
            java.util.Map<String, Object> tokenInfo = jwtInterceptor.extractTokenInfo(token);
            String companyCode = tokenInfo.get("companyCode").toString();
            int companyId = companyService.getCompanyByCode(companyCode).getId();

            // Créer le groupe avec le companyId
            GroupEntity group = new GroupEntity(null, request.getName(), companyId);
            group.setVisible(request.getVisible());
            groupService.addGroup(group);

            // Ajouter les participants au groupe
            if (request.getParticipantIds() != null && !request.getParticipantIds().isEmpty()) {
                for (Integer customerId : request.getParticipantIds()) {
                    customerGroupService.addCustomerToGroup(customerId, group.getId());
                }
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
    public ResponseEntity<?> getAllGroups(HttpServletRequest request) {
        try {
            List<GroupEntity> groups = groupService.getAllGroups(request);
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
