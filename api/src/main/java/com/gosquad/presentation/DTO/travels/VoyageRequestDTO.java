package com.gosquad.presentation.DTO.travels;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class VoyageRequestDTO {
    
    // Informations générales du voyage (étape 1)
    private String title;
    
    private String description;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    
    private String destination;
    
    private Double budget;
    
    // Participants (étape 2)
    private List<Long> participantIds;
    
    // Gestion des groupes (étape 2)
    private String groupName; // Obligatoire si 2+ participants et pas de groupe existant sélectionné
    private Long selectedGroupId;// ID du groupe existant sélectionné (optionnel)


    // Constructeurs
    public VoyageRequestDTO() {}
    
    public VoyageRequestDTO(String title, String description, LocalDate startDate, 
                           LocalDate endDate, String destination, Double budget,
                           List<Long> participantIds, String groupName, Long selectedGroupId) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.destination = destination;
        this.budget = budget;
        this.participantIds = participantIds;
        this.groupName = groupName;
        this.selectedGroupId = selectedGroupId;
    }
}