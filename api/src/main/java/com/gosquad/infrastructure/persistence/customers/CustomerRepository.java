package com.gosquad.infrastructure.persistence.customers;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;


import java.sql.SQLException;
import java.util.List;

public interface CustomerRepository {
    CustomerModel getByIdAndCompanyId(int id,int companyId) throws SQLException, ConstraintViolationException, NotFoundException;
    List<CustomerModel> getAllCustomers(int companyId) throws ConstraintViolationException;
    List<CustomerModel> getCustomersByGroupId(int groupId) throws ConstraintViolationException;
    void addCustomer(CustomerModel customer) throws SQLException, ConstraintViolationException;
    void updateCustomerPassport(CustomerModel customer) throws ConstraintViolationException;
    void updateCustomerIdCard(CustomerModel customer) throws ConstraintViolationException;
    void updateCustomer(CustomerModel customer) throws ConstraintViolationException;
    List<CustomerModel> findAllById(List<Long> ids) throws SQLException, ConstraintViolationException;
}
