package com.gosquad.domain.groups;

import lombok.Data;

@Data
public class GroupEntity {
    private Integer id;
    private String name;
    private Boolean visible = true;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
    private Integer companyId;

    public GroupEntity() {}

    public GroupEntity(Integer id, String name, Integer companyId) {
        this.id = id;
        this.name = name;
        this.visible = true;
        this.companyId = companyId;
    }
}
