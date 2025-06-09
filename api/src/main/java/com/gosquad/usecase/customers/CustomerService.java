package com.gosquad.usecase.customers;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.domain.customers.CustomerEntity;

import java.sql.SQLException;
import java.util.List;

public interface CustomerService {
    CustomerEntity getCustomerByCustomerNumber(String customerNumber, int companyId) throws SQLException, ConstraintViolationException;
    List<CustomerEntity> getAllCustomers(int companyId) throws ConstraintViolationException;
    void addCustomer(CustomerEntity customer) throws SQLException, ConstraintViolationException;
    void updateCustomerPassport(CustomerEntity customer) throws ConstraintViolationException;
    void updateCustomerIdCard(CustomerEntity customer) throws ConstraintViolationException;
    void updateEmail(CustomerEntity customer) throws ConstraintViolationException;
    void updatePhone(CustomerEntity customer) throws ConstraintViolationException;
}
