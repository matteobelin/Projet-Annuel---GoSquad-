package com.gosquad.usecase.travels;

import com.gosquad.domain.travels.TravelInformationEntity;
import com.gosquad.infrastructure.persistence.travels.TravelModel;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TravelMapper {

    public TravelInformationEntity modelToEntity(TravelModel travelModel) {
        if (travelModel == null) {
            return null;
        }
        
        return new TravelInformationEntity(
                travelModel.getId(),
                travelModel.getTitle(),
                travelModel.getDescription(),
                travelModel.getStartDate() != null ? travelModel.getStartDate().toLocalDate() : null,
                travelModel.getEndDate() != null ? travelModel.getEndDate().toLocalDate() : null,
                travelModel.getDestination(),
                travelModel.getBudget(),
                travelModel.getGroupId(),
                null, // createdAt - managed by database
                null  // updatedAt - managed by database
        );
    }

    public TravelModel entityToModel(TravelInformationEntity travelEntity) {
        if (travelEntity == null) {
            return null;
        }
        
        return new TravelModel(
                travelEntity.getId(),
                travelEntity.getTitle(),
                travelEntity.getDestination(),
                travelEntity.getStartDate() != null ? Date.valueOf(travelEntity.getStartDate()) : null,
                travelEntity.getEndDate() != null ? Date.valueOf(travelEntity.getEndDate()) : null,
                travelEntity.getBudget(),
                travelEntity.getDescription(),
                travelEntity.getGroupId(),
                null // companyId will be set by the repository or service layer
        );
    }

    public List<TravelInformationEntity> modelToEntity(List<TravelModel> travelModels) {
        if (travelModels == null) {
            return null;
        }
        
        return travelModels.stream()
                .map(this::modelToEntity)
                .collect(Collectors.toList());
    }

    public List<TravelModel> entityToModel(List<TravelInformationEntity> travelEntities) {
        if (travelEntities == null) {
            return null;
        }
        
        return travelEntities.stream()
                .map(this::entityToModel)
                .collect(Collectors.toList());
    }
}