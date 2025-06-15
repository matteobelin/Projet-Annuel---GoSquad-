package com.gosquad.usecase.customers;

import com.gosquad.presentation.DTO.customers.CustomerRequestDTO;

import org.springframework.web.multipart.MultipartFile;



public interface CustomerPostService {
    void createCustomer(CustomerRequestDTO dto, MultipartFile idCard, MultipartFile passport) throws Exception;
}

