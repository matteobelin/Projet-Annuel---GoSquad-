package com.gosquad.usecase.customers;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.presentation.DTO.customers.CustomerUpdateDTO;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;

public interface CustomerUpdateService {
    void updateCustomer(CustomerUpdateDTO dto, String companyCode) throws SQLException, NotFoundException, ConstraintViolationException;
    void updateCustomerPassport(CustomerUpdateDTO dto, MultipartFile passport, String companyCode) throws Exception;
    void updateCustomerIdCard(CustomerUpdateDTO dto, MultipartFile idCard, String companyCode) throws Exception;
    void anonymizeCustomer(CustomerUpdateDTO dto, String companyCode) throws SQLException, NotFoundException, ConstraintViolationException;
}
