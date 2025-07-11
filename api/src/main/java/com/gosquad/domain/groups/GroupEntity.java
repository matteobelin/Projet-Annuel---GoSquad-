package com.gosquad.domain.groups;

import com.gosquad.domain.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class GroupEntity extends Entity {
    private String name;
    private Boolean visible = true; // Default to visible
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public GroupEntity() {
        super();
    }

    public GroupEntity(Integer id, String name) {
        super(id);
        this.name = name;
        this.visible = true;
    }

    public GroupEntity(Integer id, String name, Boolean visible, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(id);
        this.name = name;
        this.visible = visible;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
