package com.gosquad.domain.voyages;

import com.gosquad.domain.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class VoyageEntity extends Entity {
    private String titre;
    private String destination;
    private LocalDate dateDepart;
    private LocalDate dateRetour;
    private Integer nombreParticipants;
    private BigDecimal budget;
    private Integer clientId;
    private String statut; // PLANIFIE, EN_COURS, TERMINE

    public VoyageEntity() {
        super();
    }

    public VoyageEntity(Integer id, String titre, String destination, LocalDate dateDepart, 
                       LocalDate dateRetour, Integer nombreParticipants, BigDecimal budget, 
                       Integer clientId, String statut) {
        super(id);
        this.titre = titre;
        this.destination = destination;
        this.dateDepart = dateDepart;
        this.dateRetour = dateRetour;
        this.nombreParticipants = nombreParticipants;
        this.budget = budget;
        this.clientId = clientId;
        this.statut = statut;
    }
}
