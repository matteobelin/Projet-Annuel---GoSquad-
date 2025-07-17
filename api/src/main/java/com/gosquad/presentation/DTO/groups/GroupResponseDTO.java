package com.gosquad.presentation.DTO.groups;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record GroupResponseDTO(
    Integer id,
    String name,
    Boolean visible,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime createdAt,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime updatedAt
) {
}