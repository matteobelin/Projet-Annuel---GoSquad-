package com.gosquad.usecase.voyages;

import com.gosquad.domain.voyages.VoyageEntity;
import com.gosquad.infrastructure.persistence.voyages.VoyageModel;

import java.sql.Date;
import java.time.LocalDate;

public class VoyageMapper {

    public static VoyageEntity toEntity(VoyageModel model) {
        if (model == null) {
            return null;
        }

        return new VoyageEntity(
            model.getId(),
            model.getTitre(),
            model.getDestination(),
            model.getDateDepart() != null ? model.getDateDepart().toLocalDate() : null,
            model.getDateRetour() != null ? model.getDateRetour().toLocalDate() : null,
            model.getNombreParticipants(),
            model.getBudget(),
            model.getClientId(),
            model.getStatut()
        );
    }

    public static VoyageModel toModel(VoyageEntity entity) {
        if (entity == null) {
            return null;
        }

        return new VoyageModel(
            entity.getId(),
            entity.getTitre(),
            entity.getDestination(),
            entity.getDateDepart() != null ? Date.valueOf(entity.getDateDepart()) : null,
            entity.getDateRetour() != null ? Date.valueOf(entity.getDateRetour()) : null,
            entity.getNombreParticipants(),
            entity.getBudget(),
            entity.getClientId(),
            entity.getStatut()
        );
    }
}