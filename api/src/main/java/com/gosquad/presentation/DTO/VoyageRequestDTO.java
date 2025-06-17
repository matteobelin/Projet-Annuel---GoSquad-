package com.gosquad.presentation.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class VoyageRequestDTO {
    private String titre;
    private String destination;
    private LocalDate dateDepart;
    private LocalDate dateRetour;
    private Integer nombreParticipants;
    private BigDecimal budget;
    private Integer clientId;
    private String statut;
}