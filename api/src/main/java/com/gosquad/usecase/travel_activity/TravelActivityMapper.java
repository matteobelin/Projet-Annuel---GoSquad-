package com.gosquad.usecase.travel_activity;

import com.gosquad.domain.travel_activity.TravelActivityEntity;
import com.gosquad.infrastructure.persistence.travel_activity.TravelActivityModel;

import java.util.List;

public class TravelActivityMapper {

    public TravelActivityEntity modelToEntity(TravelActivityModel travelActivityModel) {
        return new TravelActivityEntity(
                travelActivityModel.getTravelId(),
                travelActivityModel.getActivityId()
        );
    }

    public TravelActivityModel entityToModel(TravelActivityEntity travelActivityEntity) {
        return new TravelActivityModel(
                travelActivityEntity.getTravelId(),
                travelActivityEntity.getActivityId()
        );
    }

    public List<TravelActivityEntity> modelToEntity(List<TravelActivityModel> travelActivityModels) {
        return travelActivityModels.stream()
                .map(this::modelToEntity)
                .toList();
    }
}
