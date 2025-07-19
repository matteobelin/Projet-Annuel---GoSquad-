package com.gosquad.usecase.travel_activity;

import com.gosquad.presentation.DTO.travelActivityDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface TravelActivityPostService {
    void createCategory(HttpServletRequest request, travelActivityDTO dto) throws Exception;
}
