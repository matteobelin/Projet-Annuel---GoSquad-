package com.gosquad.usecase.customers;

import com.gosquad.domain.customers.CustomerEntity;
import com.gosquad.presentation.DTO.customers.CustomerRequestDTO;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;



public interface CustomerPostService {
    String createCustomer(HttpServletRequest request,CustomerRequestDTO dto, MultipartFile idCard, MultipartFile passport) throws Exception;
}

