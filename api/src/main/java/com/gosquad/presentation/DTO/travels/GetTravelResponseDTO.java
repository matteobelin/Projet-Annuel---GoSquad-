package com.gosquad.presentation.DTO.travels;

import com.gosquad.presentation.DTO.groups.GroupResponseDTO;
import com.gosquad.presentation.DTO.customers.GetCustomerResponseDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
        String companyCode,
        List<GroupResponseDTO> groups,
        List<GetCustomerResponseDTO> participants
) {}