package com.gosquad.usecase.travels.impl;

import com.gosquad.domain.travels.TravelInformationEntity;
import com.gosquad.domain.groups.GroupEntity;
import com.gosquad.presentation.DTO.travels.VoyageRequestDTO;
import com.gosquad.usecase.travels.TravelCreationService;
import com.gosquad.usecase.travels.TravelService;
import com.gosquad.usecase.groups.GroupService;
import org.springframework.stereotype.Service;

@Service
public class TravelCreationServiceImpl implements TravelCreationService {
    
    private final TravelService travelService;
    private final GroupService groupService;

    public TravelCreationServiceImpl(TravelService travelService, GroupService groupService) {
        this.travelService = travelService;
        this.groupService = groupService;
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
            throw new IllegalArgumentException("Le titre du voyage est obligatoire");
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
        // Logic pour gérer les groupes lors de la création de voyage
        
        try {
            // Cas 1: Un groupe existant est sélectionné
            if (voyageRequest.getSelectedGroupId() != null) {
                return voyageRequest.getSelectedGroupId().intValue();
            }
            
            // Cas 2: Création d'un nouveau groupe si plusieurs participants
            if (voyageRequest.getParticipantIds() != null && 
                voyageRequest.getParticipantIds().size() > 1 && 
                voyageRequest.getGroupName() != null && 
                !voyageRequest.getGroupName().trim().isEmpty()) {
                
                // Créer un nouveau groupe pour ce voyage
                GroupEntity newGroup = new GroupEntity(null, voyageRequest.getGroupName());
                newGroup.setVisible(true);
                groupService.addGroup(newGroup);
                
                // TODO: Ajouter les participants au groupe une fois que la logique customer-group sera implémentée
                // for (Long participantId : voyageRequest.getParticipantIds()) {
                //     customerService.addCustomerToGroup(participantId.intValue(), newGroup.getId());
                // }
                
                return newGroup.getId();
            }
            
            // Cas 3: Pas de groupe (voyage individuel ou sans logique de groupe)
            return null;
            
        } catch (Exception e) {
            // En cas d'erreur dans la gestion de groupe, on continue sans groupe
            // plutôt que de faire échouer toute la création de voyage
            System.err.println("Erreur lors de la gestion de groupe: " + e.getMessage());
            return null;
        }
    }
}