package com.gosquad.usecase.customers;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.customers.CustomerEntity;

import java.sql.SQLException;
import java.util.List;

public interface CustomerService {
    CustomerEntity getCustomerByIdAndCompanyId(int id, int companyId) throws SQLException, ConstraintViolationException, NotFoundException;
    List<CustomerEntity> getAllCustomers(int companyId) throws ConstraintViolationException;
    List<CustomerEntity> getCustomersByGroupId(int groupId) throws ConstraintViolationException;
    void addCustomer(CustomerEntity customer) throws SQLException, ConstraintViolationException;
    void updateCustomerPassport(CustomerEntity customer) throws ConstraintViolationException;
    void updateCustomerIdCard(CustomerEntity customer) throws ConstraintViolationException;
    void updateCustomer(CustomerEntity customer) throws ConstraintViolationException;
}
