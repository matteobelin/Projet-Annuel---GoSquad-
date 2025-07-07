package com.gosquad.usecase.activities.impl;

import com.gosquad.presentation.DTO.activities.ActivityRequestDTO;
import com.gosquad.usecase.activities.ActivityDeleteService;
import com.gosquad.usecase.activities.ActivityService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class ActivityDeleteServiceImpl implements ActivityDeleteService {
    private final ActivityService activityService;

    public ActivityDeleteServiceImpl(ActivityService activityService) {
        this.activityService = activityService;
    }

    public void deleteActivity(HttpServletRequest request, ActivityRequestDTO dto) throws Exception {
        activityService.deleteActivity(dto.id());
    }

}
