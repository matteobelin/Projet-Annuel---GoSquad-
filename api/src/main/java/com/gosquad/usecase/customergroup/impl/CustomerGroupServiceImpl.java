package com.gosquad.usecase.customergroup.impl;

import com.gosquad.usecase.customergroup.CustomerGroupService;
import com.gosquad.infrastructure.persistence.customergroup.CustomerGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerGroupServiceImpl implements CustomerGroupService {
    private final CustomerGroupRepository customerGroupRepository;

    @Autowired
    public CustomerGroupServiceImpl(CustomerGroupRepository customerGroupRepository) {
        this.customerGroupRepository = customerGroupRepository;
    }

    @Override
    public void addCustomerToGroup(int customerId, int groupId) {
        customerGroupRepository.addCustomerToGroup(customerId, groupId);
    }
}
