package com.gosquad.presentation.DTO.activities_customers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ActivityCustomerResponseDTO(
        String uniqueCustomerId,
        boolean participation,
        LocalDateTime startDate,
        LocalDateTime endDate,
        int activityId,
        String activityName,
        String description,
        String address,
        String city,
        String country,
        String categoryName,
        BigDecimal netPrice,
        BigDecimal vatRate,
        BigDecimal gross_price
) {
}
