package com.gosquad.presentation.DTO.groups;

import lombok.Data;
import java.util.List;

@Data
public class CreateGroupRequestDTO {
    
    private String name;
    
    private Boolean visible = true; // Default to visible
    
    private List<Integer> participantIds; // IDs of customers to add to the group
    
    public CreateGroupRequestDTO() {
    }
    
    public CreateGroupRequestDTO(String name) {
        this.name = name;
        this.visible = true;
    }
    
    public CreateGroupRequestDTO(String name, Boolean visible) {
        this.name = name;
        this.visible = visible;
    }
    
    public CreateGroupRequestDTO(String name, Boolean visible, List<Integer> participantIds) {
        this.name = name;
        this.visible = visible;
        this.participantIds = participantIds;
    }
}