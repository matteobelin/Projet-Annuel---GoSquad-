package com.gosquad.usecase.travels.impl;

import com.gosquad.domain.travels.TravelInformationEntity;
import com.gosquad.domain.groups.GroupEntity;
import com.gosquad.presentation.DTO.travels.VoyageRequestDTO;
import com.gosquad.usecase.travels.TravelCreationService;
import com.gosquad.usecase.travels.TravelService;
import com.gosquad.usecase.groups.GroupService;
import com.gosquad.usecase.customergroup.CustomerGroupService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class TravelCreationServiceImpl implements TravelCreationService {
    private final TravelService travelService;
    private final GroupService groupService;
    private final CustomerGroupService customerGroupService;
    private final com.gosquad.usecase.company.CompanyService companyService;
    private final com.gosquad.infrastructure.jwt.JWTInterceptor jwtInterceptor;

    @Autowired
    public TravelCreationServiceImpl(TravelService travelService, GroupService groupService, CustomerGroupService customerGroupService, com.gosquad.usecase.company.CompanyService companyService, com.gosquad.infrastructure.jwt.JWTInterceptor jwtInterceptor) {
        this.travelService = travelService;
        this.groupService = groupService;
        this.customerGroupService = customerGroupService;
        this.companyService = companyService;
        this.jwtInterceptor = jwtInterceptor;
    }

    @Override
    public TravelInformationEntity createTravel(jakarta.servlet.http.HttpServletRequest request, VoyageRequestDTO voyageRequest) {
        try {
            String authHeader = request.getHeader("Authorization");
            String token = authHeader.substring(7);
            java.util.Map<String, Object> tokenInfo = jwtInterceptor.extractTokenInfo(token);
            String companyCode = tokenInfo.get("companyCode").toString();
            int companyId = companyService.getCompanyByCode(companyCode).getId();
            validateTravelData(voyageRequest);
            Integer groupId = handleGroupLogic(voyageRequest, companyId);
            TravelInformationEntity travel = new TravelInformationEntity(
                null,
                voyageRequest.getTitle(),
                voyageRequest.getDescription(),
                voyageRequest.getStartDate(),
                voyageRequest.getEndDate(),
                voyageRequest.getDestination(),
                voyageRequest.getBudget(),
                groupId,
                null,
                null,
                companyId
            );
            travelService.addTravel(travel);
            return travel;
        } catch (Exception e) {
            return null;
        }
    }
    
    private void validateTravelData(VoyageRequestDTO voyageRequest) {
        if (voyageRequest.getTitle() == null || voyageRequest.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Le title du voyage est obligatoire");
        }
        if (voyageRequest.getDestination() == null || voyageRequest.getDestination().trim().isEmpty()) {
            throw new IllegalArgumentException("La destination est obligatoire");
        }
        if (voyageRequest.getStartDate() == null) {
            throw new IllegalArgumentException("La date de début est obligatoire");
        }
        if (voyageRequest.getEndDate() == null) {
            throw new IllegalArgumentException("La date de fin est obligatoire");
        }
        if (voyageRequest.getStartDate().isAfter(voyageRequest.getEndDate())) {
            throw new IllegalArgumentException("La date de début doit être antérieure à la date de fin");
        }
    }
    
    private Integer handleGroupLogic(VoyageRequestDTO voyageRequest, int companyId) {
        // Gestion stricte selon la spécification métier
        try {
            // Cas 1: Un groupe existant est sélectionné
            if (voyageRequest.getSelectedGroupId() != null) {
                // Ajouter les participants au groupe sélectionné
                if (voyageRequest.getParticipantIds() != null) {
                    for (Long participantId : voyageRequest.getParticipantIds()) {
                        customerGroupService.addCustomerToGroup(participantId.intValue(), voyageRequest.getSelectedGroupId().intValue());
                    }
                }
                return voyageRequest.getSelectedGroupId().intValue();
            }

            // Cas 2: Création d'un nouveau groupe (plusieurs participants ou 1 seul)
            boolean hasMultipleParticipants = voyageRequest.getParticipantIds() != null && voyageRequest.getParticipantIds().size() > 1;
            boolean hasOneParticipant = voyageRequest.getParticipantIds() != null && voyageRequest.getParticipantIds().size() == 1;
            String groupName = (voyageRequest.getGroupName() != null && !voyageRequest.getGroupName().trim().isEmpty()) ? voyageRequest.getGroupName() : "Groupe Voyage";

            if (hasMultipleParticipants || hasOneParticipant) {
                // visible = true si plusieurs participants, false sinon
                boolean visible = hasMultipleParticipants;
                Integer groupId = null;
                try {
                    GroupEntity existingGroup = groupService.getGroupByName(groupName);
                    if (existingGroup != null) {
                        groupId = existingGroup.getId();
                    }
                } catch (Exception e) {
                    // NotFoundException ou autre, on créera le groupe
                }
                if (groupId == null) {
                    GroupEntity newGroup = new GroupEntity(null, groupName, companyId);
                    newGroup.setVisible(visible);
                    groupService.addGroup(newGroup);
                    groupId = newGroup.getId();
                }
                // Ajouter tous les participants au groupe
                for (Long participantId : voyageRequest.getParticipantIds()) {
                    customerGroupService.addCustomerToGroup(participantId.intValue(), groupId);
                }
                return groupId;
            }

            // Cas 3: Pas de participants (erreur métier)
            throw new IllegalArgumentException("Aucun participant fourni pour le voyage");

        } catch (Exception e) {
            // En cas d'erreur dans la gestion de groupe, lever une exception explicite
            throw new RuntimeException("Erreur lors de la gestion du groupe pour le voyage: " + e.getMessage(), e);
        }
    }
}