package com.gosquad.usecase.activities_customers;

import com.gosquad.presentation.DTO.activities_customers.ActivityCustomerRequestDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface ActivityCustomerUpdateService {
    void updateActivityCustomer(HttpServletRequest request, ActivityCustomerRequestDTO dto) throws Exception;
}
