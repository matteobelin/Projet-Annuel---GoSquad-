package com.gosquad.presentation.DTO.travels;

import java.time.LocalDate;

public record GetAllTravelsResponseDTO(
        String uniqueTravelId, // companyCode + travelId
        String title,
        String destination,
        LocalDate startDate,
        LocalDate endDate,
        Double budget
) {}