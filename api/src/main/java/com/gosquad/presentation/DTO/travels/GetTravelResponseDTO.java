package com.gosquad.presentation.DTO.travels;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record GetTravelResponseDTO(
        String uniqueTravelId,  // companyCode + travelId
        String title,
        String description,
        LocalDate startDate,
        LocalDate endDate,
        String destination,
        Double budget,
        Integer groupId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String companyCode
) {}