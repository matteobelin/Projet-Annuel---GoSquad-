package com.gosquad.usecase.activities;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.presentation.DTO.activities.ActivityResponseDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.util.List;

public interface ActivityGetService {
    List<ActivityResponseDTO> getAllActivities(HttpServletRequest request) throws Exception;
    List<ActivityResponseDTO> getAllActivitiesByCategory(HttpServletRequest request) throws Exception;
    ActivityResponseDTO getActivityById(HttpServletRequest request)throws NotFoundException, Exception;

}
