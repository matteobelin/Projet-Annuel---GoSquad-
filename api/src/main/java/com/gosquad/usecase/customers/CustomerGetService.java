package com.gosquad.usecase.customers;

import com.gosquad.presentation.DTO.customers.GetAllCustomersResponseDTO;
import com.gosquad.presentation.DTO.customers.GetCustomerResponseDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface CustomerGetService {
    List<GetAllCustomersResponseDTO> getAllCustomers(HttpServletRequest request) throws Exception;
    GetCustomerResponseDTO getCustomer(HttpServletRequest request) throws Exception;
}
