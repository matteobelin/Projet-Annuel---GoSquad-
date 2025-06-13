package com.gosquad.usecase.customers.impl;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.domain.customers.CustomerEntity;
import com.gosquad.infrastructure.persistence.customers.CustomerModel;
import com.gosquad.infrastructure.persistence.customers.CustomerRepository;
import com.gosquad.usecase.customers.CustomerMapper;
import com.gosquad.usecase.customers.CustomerService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    public CustomerEntity getCustomerByIdAndCompanyId(int id, int companyId) throws SQLException, ConstraintViolationException{
        CustomerModel customerModel = customerRepository.getByIdAndCompanyId(id, companyId);
        return customerMapper.modelToEntity(customerModel);
    };

    public List<CustomerEntity> getAllCustomers(int companyId) throws ConstraintViolationException{
        List<CustomerModel> customerModels = customerRepository.getAllCustomers(companyId);
        return customerMapper.modelToEntity(customerModels);
    };


    public void addCustomer(CustomerEntity customer) throws SQLException, ConstraintViolationException{
        CustomerModel customerModel = customerMapper.entityToModel(customer);
        customerRepository.addCustomer(customerModel);
        customer.setId(customerModel.getId());
    };

    public void updateCustomerPassport(CustomerEntity customer) throws ConstraintViolationException {
        CustomerModel customerModel = customerMapper.entityToModel(customer);
        customerRepository.updateCustomerPassport(customerModel);
    };

    public void updateCustomerIdCard(CustomerEntity customer) throws ConstraintViolationException{
        CustomerModel customerModel = customerMapper.entityToModel(customer);
        customerRepository.updateCustomerIdCard(customerModel);
    };

    public void updateCustomer(CustomerEntity customer) throws ConstraintViolationException{
        CustomerModel customerModel = customerMapper.entityToModel(customer);
        customerRepository.updateCustomer(customerModel);
    };

}
