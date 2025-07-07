package com.gosquad.usecase.activities_customers.impl;

import com.gosquad.domain.activities_customers.ActivityCustomerEntity;
import com.gosquad.domain.company.CompanyEntity;
import com.gosquad.presentation.DTO.activities_customers.ActivityCustomerRequestDTO;
import com.gosquad.usecase.activities_customers.ActivityCustomerPostService;
import com.gosquad.usecase.activities_customers.ActivityCustomerService;
import com.gosquad.usecase.company.utils.GetCompany;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;


@Service
public class ActivityCustomerPostServiceImpl implements ActivityCustomerPostService {

    private final ActivityCustomerService activityCustomerService;
    private final GetCompany getCompany;


    public ActivityCustomerPostServiceImpl(ActivityCustomerService activityCustomerService, GetCompany getCompany) {
        this.activityCustomerService = activityCustomerService;
        this.getCompany = getCompany;
    }

    public void createActivityCustomer(HttpServletRequest request, ActivityCustomerRequestDTO dto) throws Exception{
        CompanyEntity company = getCompany.getCompanyFromRequest(request);
        int customerId = getCompany.GetCustomerIdByCompany(dto.uniqueCustomerId(), company);
        ActivityCustomerEntity activityCustomerEntity = new ActivityCustomerEntity(
                dto.activityId(),
                customerId,
                dto.participation(),
                dto.startDate(),
                dto.endDate(),
                dto.groupId()
        );

        activityCustomerService.createActivityCustomer(activityCustomerEntity);
    }
}
