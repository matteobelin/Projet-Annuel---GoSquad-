package com.gosquad.usecase.travels;

import com.gosquad.presentation.DTO.travels.GetAllTravelsResponseDTO;
import com.gosquad.presentation.DTO.travels.GetTravelResponseDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface TravelGetService {
    List<GetAllTravelsResponseDTO> getAllTravels(HttpServletRequest request) throws Exception;
    GetTravelResponseDTO getTravel(HttpServletRequest request) throws Exception;
}