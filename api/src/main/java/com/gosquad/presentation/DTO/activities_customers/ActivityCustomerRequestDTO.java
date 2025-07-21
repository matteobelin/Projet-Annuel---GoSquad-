package com.gosquad.presentation.DTO.activities_customers;

import java.time.LocalDateTime;
public record ActivityCustomerRequestDTO(
    int activityId,
    String uniqueCustomerId,
    boolean participation,
    LocalDateTime startDate,
    LocalDateTime endDate,
    int groupId
) {
}
