package com.gosquad.usecase.activities_customers.impl;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.activities_customers.ActivityCustomerEntity;
import com.gosquad.infrastructure.persistence.activities_customers.ActivityCustomerModel;
import com.gosquad.infrastructure.persistence.activities_customers.ActivityCustomerRepository;
import com.gosquad.usecase.activities_customers.ActivityCustomerMapper;
import com.gosquad.usecase.activities_customers.ActivityCustomerService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class ActivityCustomerServiceImpl implements ActivityCustomerService {

    private final ActivityCustomerRepository activityCustomerRepository;
    private final ActivityCustomerMapper activityCustomerMapper;

    public ActivityCustomerServiceImpl(ActivityCustomerRepository activityCustomerRepository, ActivityCustomerMapper activityCustomerMapper) {
        this.activityCustomerRepository = activityCustomerRepository;
        this.activityCustomerMapper = activityCustomerMapper;
    }

    public ActivityCustomerEntity getActivityByCustomerId(int customerId) throws NotFoundException, SQLException{
        ActivityCustomerModel activityCustomerModel = activityCustomerRepository.getActivityByCustomerId(customerId);
        return activityCustomerMapper.modelToEntity(activityCustomerModel);
    }

    public ActivityCustomerEntity getActivityByCustomerWhereParticipation(int customerId, boolean participation) throws SQLException, NotFoundException{
        ActivityCustomerModel activityCustomerModel = activityCustomerRepository.getActivityByCustomerWhereParticipation(customerId, participation);
        return activityCustomerMapper.modelToEntity(activityCustomerModel);
    }

    public ActivityCustomerEntity getActivityById(int activityId, int customerId) throws SQLException, NotFoundException{
        ActivityCustomerModel activityCustomerModel = activityCustomerRepository.getActivityById(activityId, customerId);
        return activityCustomerMapper.modelToEntity(activityCustomerModel);
    }

    public void createActivityCustomer(ActivityCustomerEntity activityCustomer) throws SQLException{
        ActivityCustomerModel activityCustomerModel = activityCustomerMapper.entityToModel(activityCustomer);
        activityCustomerRepository.createActivityCustomer(activityCustomerModel);
    }

    public void updateActivityCustomer(ActivityCustomerEntity activityCustomer) throws SQLException{
        ActivityCustomerModel activityCustomerModel = activityCustomerMapper.entityToModel(activityCustomer);
        activityCustomerRepository.updateActivityCustomer(activityCustomerModel);
    }


}
