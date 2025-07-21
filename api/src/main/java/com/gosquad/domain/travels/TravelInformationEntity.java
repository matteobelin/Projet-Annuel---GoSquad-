package com.gosquad.domain.travels;

import lombok.Data;

@Data
public class TravelInformationEntity {
    private Integer id;
    private String title;
    private String description;
    private java.time.LocalDate startDate;
    private java.time.LocalDate endDate;
    private String destination;
    private Double budget;
    private Integer groupId;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
    private Integer companyId;

    public TravelInformationEntity() {}

    public TravelInformationEntity(Integer id, String title, String description,
                                  java.time.LocalDate startDate, java.time.LocalDate endDate, String destination,
                                  Double budget, Integer groupId, java.time.LocalDateTime createdAt,
                                  java.time.LocalDateTime updatedAt, Integer companyId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.destination = destination;
        this.budget = budget;
        this.groupId = groupId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.companyId = companyId;
    }
}