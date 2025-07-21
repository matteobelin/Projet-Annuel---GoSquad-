package com.gosquad.domain.activities_customers;

import com.gosquad.domain.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data

public class ActivityCustomerEntity extends Entity {
    private Integer activityId;
    private Integer customerId;
    private Boolean participation;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer groupId;

    public ActivityCustomerEntity(Integer activityId, Integer customerId, Boolean participation, LocalDateTime startDate, LocalDateTime endDate, Integer groupId) {
        this.activityId = activityId;
        this.customerId = customerId;
        this.participation = participation;
        this.startDate = startDate;
        this.endDate = endDate;
        this.groupId = groupId;
    }
}
