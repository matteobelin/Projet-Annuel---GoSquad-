package com.gosquad.presentation.DTO;

import java.time.LocalDateTime;

public record travelActivityDTO (
        String travelId,
        int activityId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        int groupId
){
}
