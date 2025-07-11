package com.gosquad.domain.travels;

import com.gosquad.domain.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class TravelInformationEntity extends Entity {
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String destination;
    private Double budget;
    private Integer groupId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TravelInformationEntity() {
        super();
    }

    public TravelInformationEntity(Integer id, String title, String description, 
                                  LocalDate startDate, LocalDate endDate, String destination, 
                                  Double budget, Integer groupId, LocalDateTime createdAt, 
                                  LocalDateTime updatedAt) {
        super(id);
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.destination = destination;
        this.budget = budget;
        this.groupId = groupId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}