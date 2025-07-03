package com.gosquad.infrastructure.persistence.activities_customers;

import com.gosquad.infrastructure.persistence.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class ActivityCustomerModel extends Model {
    private Integer activityId;
    private Integer customerId;
    private Boolean participation;
    private Date startDate;
    private Date endDate;

    public ActivityCustomerModel(Integer activityId, Integer customerId, Boolean participation, Date startDate, Date endDate) {
        this.activityId = activityId;
        this.customerId = customerId;
        this.participation = participation;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
