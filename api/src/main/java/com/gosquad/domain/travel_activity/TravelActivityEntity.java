package com.gosquad.domain.travel_activity;

import com.gosquad.domain.Entity;
import lombok.Data;


@Data
public class TravelActivityEntity extends Entity {
    private Integer travelId;
    private Integer activityId;

    public TravelActivityEntity(Integer travelId, Integer activityId) {
        this.travelId = travelId;
        this.activityId = activityId;
    }
}
