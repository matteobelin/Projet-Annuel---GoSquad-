package com.gosquad.usecase.travels.impl;

import com.gosquad.domain.travels.TravelInformationEntity;
import com.gosquad.domain.groups.GroupEntity;
import com.gosquad.presentation.DTO.travels.VoyageRequestDTO;
import com.gosquad.usecase.travels.TravelCreationService;
import com.gosquad.usecase.travels.TravelService;
import com.gosquad.usecase.groups.GroupService;
import com.gosquad.usecase.customergroup.CustomerGroupService;
import org.springframework.stereotype.Service;

@Service
public class TravelCreationServiceImpl implements TravelCreationService {
    private final TravelService travelService;
    private final GroupService groupService;
    private final CustomerGroupService customerGroupService;

    public TravelCreationServiceImpl(TravelService travelService, GroupService groupService, CustomerGroupService customerGroupService) {
        this.travelService = travelService;
        this.groupService = groupService;
        this.customerGroupService = customerGroupService;
    }

    @Override
    public TravelInformationEntity createTravel(VoyageRequestDTO voyageRequest) {
        try {
            // 1. Valider les données du voyage
            validateTravelData(voyageRequest);
            
            // 2. Gérer la logique des groupes (TODO: implémenter plus tard)
            Integer groupId = handleGroupLogic(voyageRequest);
            
            // 3. Créer l'entité voyage
            TravelInformationEntity travel = new TravelInformationEntity(
                null, // ID sera généré
                voyageRequest.getTitle(),
                voyageRequest.getDescription(),
                voyageRequest.getStartDate(),
                voyageRequest.getEndDate(),
                voyageRequest.getDestination(),
                voyageRequest.getBudget(),
                groupId,
                null, // createdAt géré par la DB
                null  // updatedAt géré par la DB
            );
            
            // 4. Sauvegarder le voyage
            travelService.addTravel(travel);
            
            return travel;
            
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création du voyage: " + e.getMessage(), e);
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
    
    private Integer handleGroupLogic(VoyageRequestDTO voyageRequest) {
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
                GroupEntity newGroup = new GroupEntity(null, groupName);
                newGroup.setVisible(visible);
                groupService.addGroup(newGroup);
                // Ajouter tous les participants au groupe
                for (Long participantId : voyageRequest.getParticipantIds()) {
                    customerGroupService.addCustomerToGroup(participantId.intValue(), newGroup.getId());
                }
                return newGroup.getId();
            }

            // Cas 3: Pas de participants (erreur métier)
            throw new IllegalArgumentException("Aucun participant fourni pour le voyage");

        } catch (Exception e) {
            // En cas d'erreur dans la gestion de groupe, lever une exception explicite
            throw new RuntimeException("Erreur lors de la gestion du groupe pour le voyage: " + e.getMessage(), e);
        }
    }
}