package com.gosquad.usecase.activities.impl;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.activities.ActivityEntity;
import com.gosquad.infrastructure.persistence.activities.ActivityModel;
import com.gosquad.infrastructure.persistence.activities.ActivityRepository;
import com.gosquad.usecase.activities.ActivityMapper;
import com.gosquad.usecase.activities.ActivityService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;


@Service
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;

    public ActivityServiceImpl(ActivityRepository activityRepository, ActivityMapper activityMapper) {
        this.activityRepository = activityRepository;
        this.activityMapper = activityMapper;
    }

    public List<ActivityEntity> getAllActivities() throws Exception{
        List<ActivityModel> activityModel = activityRepository.getAll();
        return activityMapper.modelToEntity(activityModel);

    }

    public List<ActivityEntity> getAllActivitiesByCategory(int categoryId) throws SQLException{
        List<ActivityModel> activityModels = activityRepository.getAllActivitiesByCategory(categoryId);
        return activityMapper.modelToEntity(activityModels);
    }

    public ActivityEntity getById(int id)throws SQLException, NotFoundException{
        ActivityModel activityModel = activityRepository.getById(id);
        return activityMapper.modelToEntity(activityModel);
    }

    public void createActivity(ActivityEntity activity) throws SQLException{
        ActivityModel activityModel = activityMapper.entityToModel(activity);
        activityRepository.createActivity(activityModel);
        activity.setId(activityModel.getId());
    }

    public void updateActivity(ActivityEntity activity) throws SQLException{
        ActivityModel activityModel = activityMapper.entityToModel(activity);
        activityRepository.updateActivity(activityModel);
    }

    public void deleteActivity(int id) throws SQLException{
        activityRepository.deleteActivity(id);
    }

}
