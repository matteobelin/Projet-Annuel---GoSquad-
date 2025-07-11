package com.gosquad.usecase.activities_customers.impl;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.activities_customers.ActivityCustomerEntity;
import com.gosquad.infrastructure.persistence.activities_customers.ActivityCustomerModel;
import com.gosquad.infrastructure.persistence.activities_customers.ActivityCustomerRepository;
import com.gosquad.usecase.activities_customers.ActivityCustomerMapper;
import com.gosquad.usecase.activities_customers.ActivityCustomerService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class ActivityCustomerServiceImpl implements ActivityCustomerService {

    private final ActivityCustomerRepository activityCustomerRepository;
    private final ActivityCustomerMapper activityCustomerMapper;

    public ActivityCustomerServiceImpl(ActivityCustomerRepository activityCustomerRepository, ActivityCustomerMapper activityCustomerMapper) {
        this.activityCustomerRepository = activityCustomerRepository;
        this.activityCustomerMapper = activityCustomerMapper;
    }

    public List<ActivityCustomerEntity> getActivitiesByGroupIdWhereParticipation(int customerId,boolean participation) throws  SQLException{
        List<ActivityCustomerModel> activityCustomerModel = activityCustomerRepository.getActivitiesByGroupIdWhereParticipation(customerId,participation);
        return activityCustomerMapper.modelToEntity(activityCustomerModel);
    }

    public List<ActivityCustomerEntity> getActivityByCustomerId(int customerId) throws SQLException{
        List<ActivityCustomerModel> activityCustomerModel = activityCustomerRepository.getActivityByCustomerId(customerId);
        return activityCustomerMapper.modelToEntity(activityCustomerModel);
    }

    public List<ActivityCustomerEntity> getActivityByCustomerWhereParticipation(int customerId, boolean participation, int groupeId) throws SQLException, NotFoundException{
        List<ActivityCustomerModel> activityCustomerModel = activityCustomerRepository.getActivityByCustomerWhereParticipation(customerId, participation,groupeId);
        return activityCustomerMapper.modelToEntity(activityCustomerModel);
    }

    public ActivityCustomerEntity getActivityById(int activityId, int customerId,int groupId) throws SQLException, NotFoundException{
        ActivityCustomerModel activityCustomerModel = activityCustomerRepository.getActivityById(activityId, customerId,groupId);
        return activityCustomerMapper.modelToEntity(activityCustomerModel);
    }

    public List<ActivityCustomerEntity> getActivityByCustomerIdAndGroupIdWhereParticipation(int customerId, int groupId,boolean participation) throws SQLException{
        List<ActivityCustomerModel> activityCustomerModel = activityCustomerRepository.getActivityByCustomerIdAndGroupIdWhereParticipation(customerId, groupId,participation);
        return activityCustomerMapper.modelToEntity(activityCustomerModel);
    }

    public  List<ActivityCustomerEntity> getCustomersByActivityIdAndGroupId(int activityId, int groupId) throws SQLException{
        List<ActivityCustomerModel> activityCustomerModel = activityCustomerRepository.getCustomersByActivityIdAndGroupId(activityId, groupId);
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
