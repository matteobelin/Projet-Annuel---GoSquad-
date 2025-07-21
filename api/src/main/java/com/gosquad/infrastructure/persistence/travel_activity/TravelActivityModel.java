package com.gosquad.infrastructure.persistence.travel_activity;

import com.gosquad.infrastructure.persistence.Model;
import lombok.Data;

@Data
public class TravelActivityModel extends Model {
    private Integer travelId;
    private Integer activityId;

    public TravelActivityModel(Integer travelId, Integer activityId) {
        this.travelId = travelId;
        this.activityId = activityId;
    }
}
