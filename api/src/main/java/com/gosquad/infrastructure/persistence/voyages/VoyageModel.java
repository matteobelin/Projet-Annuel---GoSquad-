package com.gosquad.infrastructure.persistence.voyages;

import com.gosquad.infrastructure.persistence.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.sql.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class VoyageModel extends Model {
    private String titre;
    private String destination;
    private Date dateDepart;
    private Date dateRetour;
    private Integer nombreParticipants;
    private BigDecimal budget;
    private Integer clientId;
    private String statut;

    public VoyageModel() {
        super();
    }

    public VoyageModel(Integer id, String titre, String destination, Date dateDepart, 
                      Date dateRetour, Integer nombreParticipants, BigDecimal budget, 
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