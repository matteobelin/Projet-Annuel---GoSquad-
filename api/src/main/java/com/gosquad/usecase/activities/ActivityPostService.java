package com.gosquad.usecase.activities;

import com.gosquad.presentation.DTO.activities.ActivityRequestDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface ActivityPostService {
    void createActivity(HttpServletRequest request, ActivityRequestDTO dto) throws Exception;
}
