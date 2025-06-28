package com.gosquad.usecase.customers;

import com.gosquad.domain.customers.CustomerEntity;
import com.gosquad.presentation.DTO.customers.CustomerRequestDTO;

import org.springframework.web.multipart.MultipartFile;



public interface CustomerPostService {
    CustomerEntity createCustomer(CustomerRequestDTO dto, MultipartFile idCard, MultipartFile passport) throws Exception;
}

