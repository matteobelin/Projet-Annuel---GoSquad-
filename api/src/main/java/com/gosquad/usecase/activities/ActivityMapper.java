package com.gosquad.usecase.activities;

import com.gosquad.domain.activities.ActivityEntity;
import com.gosquad.infrastructure.persistence.activities.ActivityModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ActivityMapper {
    public ActivityEntity modelToEntity(ActivityModel activity) {
        return new ActivityEntity(
                activity.getId(),
                activity.getName(),
                activity.getDescription(),
                activity.getAddressId(),
                activity.getPriceId(),
                activity.getCategoryId(),
                activity.getCompanyId()
        );
    }

    public ActivityModel entityToModel(ActivityEntity activity) {
        return new ActivityModel(
                activity.getId(),
                activity.getName(),
                activity.getDescription(),
                activity.getAddressId(),
                activity.getPriceId(),
                activity.getCategoryId(),
                activity.getCompanyId()
        );
    }


    public List<ActivityEntity> modelToEntity(List<ActivityModel> activityModels){;
        return activityModels.stream()
                .map(this::modelToEntity)
                .toList();
    };
}
