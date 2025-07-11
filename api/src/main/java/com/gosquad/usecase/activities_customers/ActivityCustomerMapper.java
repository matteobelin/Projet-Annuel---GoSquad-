package com.gosquad.usecase.activities_customers;

import com.gosquad.domain.activities_customers.ActivityCustomerEntity;
import com.gosquad.infrastructure.persistence.activities_customers.ActivityCustomerModel;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ActivityCustomerMapper {
    public ActivityCustomerEntity modelToEntity(ActivityCustomerModel activityCustomer) {
        return new ActivityCustomerEntity(
            activityCustomer.getActivityId(),
            activityCustomer.getCustomerId(),
            activityCustomer.getParticipation(),
            activityCustomer.getStartDate(),
            activityCustomer.getEndDate(),
            activityCustomer.getGroupId()
        );
    }

    public ActivityCustomerModel entityToModel(ActivityCustomerEntity activityCustomer) {
        return new ActivityCustomerModel(
            activityCustomer.getActivityId(),
            activityCustomer.getCustomerId(),
            activityCustomer.getParticipation(),
            activityCustomer.getStartDate(),
            activityCustomer.getEndDate(),
            activityCustomer.getGroupId()
        );
    }

    public List<ActivityCustomerEntity> modelToEntity(List<ActivityCustomerModel> activityCustomers) {
        return activityCustomers.stream()
                .map(this::modelToEntity)
                .toList();
    }
}
