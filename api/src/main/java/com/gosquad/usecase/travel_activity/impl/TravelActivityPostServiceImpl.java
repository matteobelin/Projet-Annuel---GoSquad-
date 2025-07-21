package com.gosquad.usecase.travel_activity.impl;

import com.gosquad.domain.activities_customers.ActivityCustomerEntity;
import com.gosquad.domain.customers.CustomerEntity;
import com.gosquad.presentation.DTO.travelActivityDTO;
import com.gosquad.usecase.activities_customers.ActivityCustomerService;
import com.gosquad.usecase.customers.CustomerService;
import com.gosquad.usecase.travel_activity.TravelActivityPostService;
import com.gosquad.usecase.travel_activity.TravelActivityService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class TravelActivityPostServiceImpl implements TravelActivityPostService {
    private final CustomerService customerService;
    private final TravelActivityService travelActivityService;
    private final ActivityCustomerService activityCustomerService;

    public TravelActivityPostServiceImpl(CustomerService customerService, TravelActivityService travelActivityService, ActivityCustomerService activityCustomerService) {

        this.customerService = customerService;
        this.travelActivityService = travelActivityService;
        this.activityCustomerService = activityCustomerService;
    }

    public void createCategory(HttpServletRequest request, travelActivityDTO dto){
        try {
                List<ActivityCustomerEntity> activityCustomers = customerService.getCustomersByGroupId(dto.groupId())
                        .stream()
                        .filter(customer -> !"anonymous".equalsIgnoreCase(customer.getFirstname()))
                        .map(customer -> {
                            ActivityCustomerEntity ace = new ActivityCustomerEntity(
                                    dto.activityId(),
                                    customer.getId(),
                                    true,
                                    dto.startDate(),
                                    dto.endDate(),
                                    dto.groupId()
                            );
                            return ace;
                        })
                        .toList();

                for (ActivityCustomerEntity ace : activityCustomers) {
                    activityCustomerService.createActivityCustomer(ace);
                }

                travelActivityService.addActivityToTravel(Integer.parseInt(dto.travelId().substring("TRAVEL".length())),dto.activityId());


        } catch (Exception e) {
            System.err.println("Error fetching customers for group " + dto.groupId() + ": " + e.getMessage());
        }
    }
}






