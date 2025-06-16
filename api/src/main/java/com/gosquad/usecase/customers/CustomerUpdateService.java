package com.gosquad.usecase.customers;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.presentation.DTO.customers.CustomerRequestDTO;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;

public interface CustomerUpdateService {
    void updateCustomer(CustomerRequestDTO dto, String companyCode) throws SQLException, NotFoundException, ConstraintViolationException;
    void updateCustomerPassport(CustomerRequestDTO dto, MultipartFile passport, String companyCode) throws Exception;
    void updateCustomerIdCard(CustomerRequestDTO dto, MultipartFile idCard, String companyCode) throws Exception;
    void anonymizeCustomer(CustomerRequestDTO dto, String companyCode) throws SQLException, NotFoundException, ConstraintViolationException;
}
