package com.gosquad.usecase.activities_customers;

import com.gosquad.presentation.DTO.activities_customers.ActivityCustomerResponseDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface ActivityCustomerGetService {
    List<ActivityCustomerResponseDTO> getActivitiesByGroupIdWhereParticipation(HttpServletRequest request) throws Exception;
    List<ActivityCustomerResponseDTO> getActivityByCustomerId(HttpServletRequest request) throws Exception;
    List<ActivityCustomerResponseDTO> getActivityByCustomerWhereParticipation(HttpServletRequest request) throws Exception;
    ActivityCustomerResponseDTO getActivityById(HttpServletRequest request) throws Exception;
    List<ActivityCustomerResponseDTO> getActivityByCustomerIdAndGroupIdWhereParticipation(HttpServletRequest request) throws Exception;
    List<ActivityCustomerResponseDTO> getCustomersByActivityIdAndGroupId(HttpServletRequest request) throws Exception;
}
