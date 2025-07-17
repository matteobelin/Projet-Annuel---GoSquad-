package com.gosquad.usecase.travels;

import com.gosquad.domain.travels.TravelInformationEntity;
import com.gosquad.infrastructure.persistence.travels.TravelModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TravelMapper {
    public TravelInformationEntity modelToEntity(TravelModel model) {
        if (model == null) return null;
        TravelInformationEntity entity = new TravelInformationEntity();
        entity.setId(model.getId());
        entity.setTitle(model.getTitle());
        entity.setDescription(model.getDescription());
        entity.setStartDate(model.getStartDate() != null ? model.getStartDate().toLocalDate() : null);
        entity.setEndDate(model.getEndDate() != null ? model.getEndDate().toLocalDate() : null);
        entity.setDestination(model.getDestination());
        entity.setBudget(model.getBudget());
        entity.setGroupId(model.getGroupId());
        return entity;
    }

    public TravelModel entityToModel(TravelInformationEntity entity) {
        return entity == null ? null : new TravelModel(
                entity.getId(),
                entity.getTitle(),
                entity.getDestination(),
                entity.getStartDate() != null ? java.sql.Date.valueOf(entity.getStartDate()) : null,
                entity.getEndDate() != null ? java.sql.Date.valueOf(entity.getEndDate()) : null,
                entity.getBudget(),
                entity.getDescription(),
                entity.getGroupId(),
                entity.getCompanyId()
        );
    }

    public List<TravelInformationEntity> modelToEntity(List<TravelModel> models) {
        return models == null ? List.of() : models.stream().map(this::modelToEntity).toList();
    }

    public List<TravelModel> entityToModel(List<TravelInformationEntity> entities) {
        return entities == null ? List.of() : entities.stream().map(this::entityToModel).toList();
    }
}